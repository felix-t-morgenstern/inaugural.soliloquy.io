package inaugural.soliloquy.io.keyboard;

import soliloquy.specs.io.keyboard.KeyBinding;
import soliloquy.specs.io.keyboard.KeyBindingContext;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class KeyBindingContextImpl implements KeyBindingContext {
    private final List<KeyBinding> BINDINGS;

    private boolean blocksAllLowerBindings;

    public KeyBindingContextImpl() {
        BINDINGS = listOf();
    }

    // TODO: Ensure that this is a clone
    @Override
    public List<KeyBinding> bindings() {
        return listOf(BINDINGS);
    }

    @Override
    public boolean getBlocksAllLowerBindings() {
        return blocksAllLowerBindings;
    }

    @Override
    public void setBlocksAllLowerBindings(boolean blocksAllLowerBindings) {
        this.blocksAllLowerBindings = blocksAllLowerBindings;
    }
}
