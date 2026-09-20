package dev.nitron.wayfinder.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.nitron.wayfinder.Wayfinder;
import dev.nitron.wayfinder.item.SignalTweakerItem;
import dev.nitron.wayfinder.item.SignalscopeItem;
import dev.nitron.wayfinder.item.component.SignalTweakerComponent;
import dev.nitron.wayfinder.item.component.SignalscopeComponent;
import dev.nitron.wayfinder.registry.WayfinderDataComponents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Vector2ic;

import java.util.List;

public final class AdvancedTooltipOverlay {
    private static final ResourceLocation SIGNALSCOPE_TOOLTIP =
            ResourceLocation.fromNamespaceAndPath(
                    Wayfinder.MOD_ID,
                    "textures/gui/sprites/hud/signalscope_tooltip.png");

    private static final ResourceLocation SIGNAL_TWEAKER_TOOLTIP =
            ResourceLocation.fromNamespaceAndPath(
                    Wayfinder.MOD_ID,
                    "textures/gui/sprites/hud/signal_tweaker_tooltip.png");

    private static final ResourceLocation BARS =
            ResourceLocation.fromNamespaceAndPath(
                    Wayfinder.MOD_ID,
                    "hud/bars");

    private static final ResourceLocation BARS_SELECTED =
            ResourceLocation.fromNamespaceAndPath(
                    Wayfinder.MOD_ID,
                    "hud/bars_selected");

    public static void render(
            GuiGraphics graphics,
            Font font,
            ItemStack stack,
            int mouseX,
            int mouseY,
            List<Component> baseTooltip) {
        List<ClientTooltipComponent> components =
                ClientHooks.gatherTooltipComponents(
                        stack,
                        baseTooltip,
                        stack.getTooltipImage(),
                        mouseX,
                        graphics.guiWidth(),
                        graphics.guiHeight(),
                        font);

        if (components.isEmpty()) return;

        int tooltipWidth = 0;
        int tooltipHeight = components.size() == 1 ? -2 : 0;

        for (ClientTooltipComponent component : components) {
            tooltipWidth = Math.max(
                    tooltipWidth,
                    component.getWidth(font));
            tooltipHeight += component.getHeight();
        }

        Vector2ic position =
                DefaultTooltipPositioner.INSTANCE.positionTooltip(
                        graphics.guiWidth(),
                        graphics.guiHeight(),
                        mouseX,
                        mouseY,
                        tooltipWidth,
                        tooltipHeight);

        if (stack.getItem() instanceof SignalscopeItem) {
            renderSignalscope(
                    graphics,
                    stack,
                    position.x(),
                    position.y());
        } else if (stack.getItem() instanceof SignalTweakerItem) {
            renderTweaker(
                    graphics,
                    stack,
                    position.x(),
                    position.y());
        }
    }

    private static void renderSignalscope(
            GuiGraphics graphics,
            ItemStack stack,
            int tooltipX,
            int tooltipY) {
        SignalscopeComponent component =
                stack.get(WayfinderDataComponents.SIGNALSCOPE.get());

        if (component == null) return;

        int x = tooltipX - 12;
        int y = tooltipY + 12;

        graphics.blit(
                SIGNALSCOPE_TOOLTIP,
                x + 15,
                y,
                0,
                0,
                64,
                26,
                64,
                64);

        if (!component.lensItemstack().isEmpty()) {
            graphics.renderItem(
                    component.lensItemstack(),
                    x + 20,
                    y + 5);
        }

        if (!component.upgradeItemstack().isEmpty()) {
            graphics.renderItem(
                    component.upgradeItemstack(),
                    x + 52,
                    y + 5);
        }

        if (component.selected() != 0) {
            graphics.blit(
                    SIGNALSCOPE_TOOLTIP,
                    x + 18
                            + (component.selected() == 2 ? 32 : 0),
                    y + 3,
                    0,
                    26,
                    64,
                    26,
                    64,
                    64);
        }
    }

    private static void renderTweaker(
            GuiGraphics graphics,
            ItemStack stack,
            int tooltipX,
            int tooltipY) {
        SignalTweakerComponent component =
                stack.get(WayfinderDataComponents.SIGNAL_TWEAKER.get());

        if (component == null) return;

        boolean advanced =
                net.minecraft.client.Minecraft.getInstance()
                        .options.advancedItemTooltips;

        int advancedOffset = advanced ? 20 : 0;

        int x = tooltipX - 12;
        int y = tooltipY - 12 + advancedOffset;

        int baseWidth = 29;
        int baseHeight = 29;

        for (int i = 0; i < 3; i++) {
            ResourceLocation sprite =
                    component.selected() == i
                            ? BARS_SELECTED
                            : BARS;

            graphics.blitSprite(
                    sprite,
                    x + 8 + i * 26,
                    y + 31,
                    baseWidth + 12,
                    baseHeight + 4);
        }

        graphics.blitSprite(
                BARS,
                x + 88,
                y + 31,
                baseWidth + 5,
                baseHeight + 5);

        int rgb =
                0xFF000000
                        | (component.color().getX() << 16)
                        | (component.color().getY() << 8)
                        | component.color().getZ();

        blitTinted(
                graphics,
                SIGNAL_TWEAKER_TOOLTIP,
                tooltipX + 86,
                tooltipY + 29 + advancedOffset,
                16,
                15,
                14,
                14,
                128,
                128,
                rgb);

        String network =
                component.privateNetwork()
                        ? "Private"
                        : "Public";

        graphics.blit(
                SIGNAL_TWEAKER_TOOLTIP,
                tooltipX
                        + net.minecraft.client.Minecraft.getInstance()
                                .font.width(network)
                        + 60,
                tooltipY + 70 + advancedOffset,
                40 - (component.privateNetwork() ? 0 : 9),
                0,
                9,
                11,
                128,
                128);

        ResourceLocation networkBar =
                component.selected() == 3
                        ? BARS_SELECTED
                        : BARS;

        graphics.blitSprite(
                networkBar,
                x + 53,
                y + 71,
                baseWidth
                        + net.minecraft.client.Minecraft.getInstance()
                                .font.width(network)
                        - 6,
                baseHeight + 5);

        ResourceLocation frequencyBar =
                component.selected() == 4
                        ? BARS_SELECTED
                        : BARS;

        String frequency =
                Integer.toString(component.freq());

        graphics.blitSprite(
                frequencyBar,
                x + 82,
                y + 102,
                baseWidth
                        + net.minecraft.client.Minecraft.getInstance()
                                .font.width(frequency)
                        - 6,
                baseHeight + 2);

        ResourceLocation typeBar =
                component.selected() == 5
                        ? BARS_SELECTED
                        : BARS;

        String type =
                component.type() == 1
                        ? "Alternate"
                        : component.type() == 2
                        ? "Final"
                        : "Default";

        graphics.blitSprite(
                typeBar,
                x + 138 + (component.freq() == 12 ? 6 : 0),
                y + 101,
                baseWidth
                        + net.minecraft.client.Minecraft.getInstance()
                                .font.width(type)
                        - 6,
                baseHeight + 5);
    }

    private static void blitTinted(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x,
            int y,
            int u,
            int v,
            int width,
            int height,
            int textureWidth,
            int textureHeight,
            int argb) {
        float alpha = ((argb >>> 24) & 255) / 255.0F;
        float red = ((argb >>> 16) & 255) / 255.0F;
        float green = ((argb >>> 8) & 255) / 255.0F;
        float blue = (argb & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(
                red,
                green,
                blue,
                alpha);

        graphics.blit(
                texture,
                x,
                y,
                u,
                v,
                width,
                height,
                textureWidth,
                textureHeight);

        RenderSystem.setShaderColor(
                1.0F,
                1.0F,
                1.0F,
                1.0F);
    }

    private AdvancedTooltipOverlay() {}
}
