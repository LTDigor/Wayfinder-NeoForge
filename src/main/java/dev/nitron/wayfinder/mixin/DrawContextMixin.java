package dev.nitron.wayfinder.mixin;

import dev.nitron.wayfinder.client.AdvancedTooltipOverlay;
import dev.nitron.wayfinder.item.SignalTweakerItem;
import dev.nitron.wayfinder.item.SignalscopeItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class DrawContextMixin<T extends AbstractContainerMenu>
        extends Screen {
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Shadow
    @Final
    protected T menu;

    @Shadow
    protected abstract List<Component>
    getTooltipFromContainerItem(ItemStack stack);

    protected DrawContextMixin(Component title) {
        super(title);
    }

    @Inject(
            method = "renderTooltip",
            at = @At("TAIL")
    )
    private void wayfinder$renderAdvancedOverlay(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            CallbackInfo ci) {
        if (!menu.getCarried().isEmpty()
                || hoveredSlot == null
                || !hoveredSlot.hasItem()) {
            return;
        }

        ItemStack stack = hoveredSlot.getItem();

        if (!(stack.getItem() instanceof SignalscopeItem)
                && !(stack.getItem() instanceof SignalTweakerItem)) {
            return;
        }

        AdvancedTooltipOverlay.render(
                graphics,
                this.font,
                stack,
                mouseX,
                mouseY,
                getTooltipFromContainerItem(stack));
    }
}
