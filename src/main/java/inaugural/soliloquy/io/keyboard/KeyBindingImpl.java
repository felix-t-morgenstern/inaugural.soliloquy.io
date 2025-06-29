package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.keyboard.KeyBinding;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class KeyBindingImpl implements KeyBinding {
    private final List<Character> BOUND_CHARACTERS;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    private Action<Long> onPress;
    private Action<Long> onRelease;
    private boolean blocksLowerBindings;

    public KeyBindingImpl(char[] chars) {
        Check.ifNull(chars, "chars");
        BOUND_CHARACTERS = listOf();
        for (var c : chars) {
            BOUND_CHARACTERS.add(c);
        }
        // TODO: Consider having this accept a most recent timestamp on creation
        TIMESTAMP_VALIDATOR = new TimestampValidator(null);
    }

    // TODO: Ensure that this is a clone
    @Override
    public List<Character> boundCharacters() {
        return listOf(BOUND_CHARACTERS);
    }

    @Override
    public void press(long timestamp) throws IllegalArgumentException {
        runAction(timestamp, onPress);
    }

    @Override
    public String onPressActionId() {
        return onPress == null ? null : onPress.id();
    }

    @Override
    public void release(long timestamp) throws IllegalArgumentException {
        runAction(timestamp, onRelease);
    }

    @Override
    public String onReleaseActionId() {
        return onRelease == null ? null : onRelease.id();
    }

    private void runAction(long timestamp, Action<Long> action) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (action != null) {
            action.run(timestamp);
        }
    }

    @Override
    public void setOnPress(Action<Long> onPress) {
        this.onPress = onPress;
    }

    @Override
    public void setOnRelease(Action<Long> onRelease) {
        this.onRelease = onRelease;
    }

    @Override
    public boolean getBlocksLowerBindings() {
        return blocksLowerBindings;
    }

    @Override
    public void setBlocksLowerBindings(boolean blocksLowerBindings) {
        this.blocksLowerBindings = blocksLowerBindings;
    }
}
