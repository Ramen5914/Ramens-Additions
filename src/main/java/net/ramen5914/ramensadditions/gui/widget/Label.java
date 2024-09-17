package net.ramen5914.ramensadditions.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class Label extends AbstractWidget {
    public static final int HEADER_RGB = 0x5391E1;
    public static final int HINT_RGB = 0x96B7E0;

    protected float z;
    protected boolean wasHovered = false;
    protected List<Component> toolTip = new LinkedList<>();
    protected BiConsumer<Integer, Integer> onClick = (_$, _$$) -> {};

    public int lockedTooltipX = -1;
    public int lockedTooltipY = -1;
    public Component text;
    public String suffix;
    protected boolean hasShadow;
    protected int color;
    protected Font font;

    public Label(int x, int y, Component text) {
        this(x, y, Minecraft.getInstance().font.width(text), 10);
        font = Minecraft.getInstance().font;
        this.text = Component.literal("Label");
        color = 0xFFFFFF;
        hasShadow = false;
        suffix = "";
    }

    public Label(int x, int y, int width, int height) {
        this(x, y, width, height, Component.empty());
    }

    public Label(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public Label colored(int color) {
        this.color = color;
        return this;
    }

    public Label withShadow() {
        this.hasShadow = true;
        return this;
    }

    public Label withSuffix(String s) {
        suffix = s;
        return this;
    }

    public void setTextAndTrim(Component newText, boolean trimFront, int maxWidthPx) {
        Font fontRenderer = Minecraft.getInstance().font;

        if (fontRenderer.width(newText) <= maxWidthPx) {
            text = newText;
            return;
        }

        String trim = "...";
        int trimWidth = fontRenderer.width(trim);

        String raw = newText.getString();
        StringBuilder builder = new StringBuilder(raw);
        int startIndex = trimFront ? 0 : raw.length() - 1;
        int endIndex = !trimFront ? 0 : raw.length() - 1;
        int step = (int) Math.signum(endIndex - startIndex);

        for (int i = startIndex; i != endIndex; i += step) {
            String sub = builder.substring(trimFront ? i : startIndex, trimFront ? endIndex + 1 : i + 1);
            if (fontRenderer.width(Component.literal(sub).setStyle(newText.getStyle())) + trimWidth <= maxWidthPx) {
                text = Component.literal(trimFront ? trim + sub : sub + trim).setStyle(newText.getStyle());
                return;
            }
        }
    }

    protected void doRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (text == null || text.getString().isEmpty())
            return;

        RenderSystem.setShaderColor(1, 1, 1, 1);
        MutableComponent copy = text.plainCopy();
        if (suffix != null && !suffix.isEmpty())
            copy.append(suffix);

        graphics.drawString(font, copy, getX(), getY(), color, hasShadow);
    }

    public <T extends Label> T withCallback(BiConsumer<Integer, Integer> cb) {
        this.onClick = cb;
        return (T) this;
    }

    public <T extends Label> T withCallback(Runnable cb) {
        return withCallback((_$, _$$) -> cb.run());
    }

    public <T extends Label> T atZLevel(float z) {
        this.z = z;
        return (T) this;
    }

    public List<Component> getToolTip() {
        return toolTip;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        beforeRender(graphics, mouseX, mouseY, partialTicks);
        doRender(graphics, mouseX, mouseY, partialTicks);
        afterRender(graphics, mouseX, mouseY, partialTicks);
        wasHovered = isHoveredOrFocused();
    }

    protected void beforeRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
    }

    protected void afterRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().popPose();
    }

    public void runCallback(double mouseX, double mouseY) {
        onClick.accept((int) mouseX, (int) mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        runCallback(mouseX, mouseY);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }
}
