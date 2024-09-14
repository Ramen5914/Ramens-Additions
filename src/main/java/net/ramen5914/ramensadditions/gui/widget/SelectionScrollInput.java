package net.ramen5914.ramensadditions.gui.widget;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectionScrollInput extends AbstractWidget {
    public static final int HEADER_RGB = 0x5391E1;
    public static final int HINT_RGB = 0x96B7E0;
    protected final Component scrollToModify = Component.literal("Scroll to Modify");
    protected final Component shiftScrollsFaster = Component.literal("Shift to Scroll Faster");
    private final MutableComponent scrollToSelect = Component.literal("Scroll to Select");

    protected float z;
    protected boolean wasHovered = false;
    protected List<Component> toolTip = new LinkedList<>();

    public int lockedTooltipX = -1;
    public int lockedTooltipY = -1;
    protected Consumer<Integer> onScroll;
    protected int state;
    protected Component title = Component.literal("Choose an Option:");
    protected Component hint = null;
    protected Label displayLabel;
    protected boolean soundPlayed;
    protected Function<Integer, Pair<Component, Boolean>> formatter;
    protected int min;
    protected int max;
    protected List<Pair<Component, Boolean>> options;

    protected SelectionScrollInput(int x, int y) {
        this(x, y, 16, 16);
    }

    public SelectionScrollInput(int x, int y, int width, int height) {
        this(x, y, width, height, Component.empty());
        state = 0;
        formatter = i -> this.options.get(i);
        soundPlayed = false;
    }

    protected SelectionScrollInput(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        beforeRender(graphics, mouseX, mouseY, partialTicks);

        wasHovered = isHoveredOrFocused();
        if (wasHovered && visible) {
            doRender(graphics, mouseX, mouseY, partialTicks);
        }

        afterRender(graphics, mouseX, mouseY, partialTicks);
    }

    protected void beforeRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().pushPose();
    }

    protected void doRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

        graphics.renderTooltip(this.displayLabel.font, toolTip, Optional.empty(), mouseX, mouseY);
    }

    protected void afterRender(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.pose().popPose();
    }

    public <T extends SelectionScrollInput> T atZLevel(float z) {
        this.z = z;
        //noinspection unchecked
        return (T) this;
    }

    public List<Component> getToolTip() {
        return toolTip;
    }

    public SelectionScrollInput withRange(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public SelectionScrollInput calling(Consumer<Integer> onScroll) {
        this.onScroll = onScroll;
        return this;
    }

    public SelectionScrollInput format(Function<Integer, Pair<Component, Boolean>> formatter) {
        this.formatter = formatter;
        return this;
    }

    public SelectionScrollInput removeCallback() {
        this.onScroll = null;
        return this;
    }

    public SelectionScrollInput titled(MutableComponent title) {
        this.title = title;
        updateTooltip();
        return this;
    }

    public SelectionScrollInput addHint(MutableComponent hint) {
        this.hint = hint;
        updateTooltip();
        return this;
    }

    public SelectionScrollInput writingTo(Label label) {
        this.displayLabel = label;
        if (label != null)
            writeToLabel();
        return this;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }

    public int getState() {
        return state;
    }

    public SelectionScrollInput setState(int state) {
        this.state = state;
        clampState();
        updateTooltip();
        if (displayLabel != null)
            writeToLabel();
        return this;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int priorState = state;

        if (scrollY > 0) {
            state += 1;
        } else if (scrollY < 0) {
            state -= 1;
        }

        clampState();

        if (priorState != state) {
            if (!soundPlayed)
                Minecraft.getInstance()
                        .getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK,
                                1.5f + 0.1f * (state - min) / (max - min)));
            soundPlayed = true;
            onChanged();
        }

        return priorState != state;
    }


    protected void clampState() {
        if (state >= max)
            state = max - 1;
        if (state < min)
            state = min;
    }

    public void onChanged() {
        if (displayLabel != null)
            writeToLabel();
        if (onScroll != null)
            onScroll.accept(state);
        updateTooltip();
    }

    protected void writeToLabel() {
        Pair<Component, Boolean> componentBooleanPair = formatter.apply(state);

        displayLabel.text = componentBooleanPair.getA();
        if (componentBooleanPair.getB()) {
            displayLabel.colored(0xff5555);
        }
    }

    public SelectionScrollInput forOptions(List<Pair<Component, Boolean>> options) {
        this.options = options;
        this.max = options.size();

        format(options::get);
        updateTooltip();
        return this;
    }

    protected void updateTooltip() {
        toolTip.clear();
        if (title == null)
            return;
        toolTip.add(title.plainCopy()
                .withStyle(s -> s.withColor(HEADER_RGB)));
        int min = Math.min(this.max - 16, state - 7);
        int max = Math.max(this.min + 16, state + 8);
        min = Math.max(min, this.min);
        max = Math.min(max, this.max);
        if (this.min + 1 == min)
            min--;
        if (min > this.min)
            toolTip.add(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));
        if (this.max - 1 == max)
            max++;
        for (int i = min; i < max; i++) {
            if (i == state)
                if (options.get(i).getB()) {
                    toolTip.add(Component.empty()
                            .append("-> ")
                            .append(options.get(i).getA())
                            .withStyle(ChatFormatting.RED));
                } else {
                    toolTip.add(Component.empty()
                            .append("-> ")
                            .append(options.get(i).getA())
                            .withStyle(ChatFormatting.WHITE));
                }
            else
                if (options.get(i).getB()) {
                    toolTip.add(Component.empty()
                            .append("> ")
                            .append(options.get(i).getA())
                            .withStyle(ChatFormatting.DARK_RED));
                } else {
                    toolTip.add(Component.empty()
                            .append("> ")
                            .append(options.get(i).getA())
                            .withStyle(ChatFormatting.GRAY));
                }
        }
        if (max < this.max)
            toolTip.add(Component.literal("> ...")
                    .withStyle(ChatFormatting.GRAY));

        if (hint != null)
            toolTip.add(hint.plainCopy()
                    .withStyle(s -> s.withColor(HINT_RGB)));
        toolTip.add(scrollToSelect.plainCopy()
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
    }
}
