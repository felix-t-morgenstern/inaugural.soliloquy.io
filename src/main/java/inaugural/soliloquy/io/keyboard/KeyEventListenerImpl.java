package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.input.keyboard.entities.KeyBinding;
import soliloquy.specs.io.input.keyboard.entities.KeyBindingContext;
import soliloquy.specs.io.input.keyboard.infrastructure.KeyEventListener;
import soliloquy.specs.ui.definitions.keyboard.KeyEventInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.ui.definitions.keyboard.KeyEventInfo.keyEventInfo;

public class KeyEventListenerImpl implements KeyEventListener {
    private final Map<Integer, List<KeyBindingContext>> CONTEXTS;
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final Map<KeyBindingContext, Integer> PRIORITIES_BY_CONTEXTS;

    public KeyEventListenerImpl(TimestampValidator timestampValidator) {
        CONTEXTS = mapOf();
        TIMESTAMP_VALIDATOR = timestampValidator;
        PRIORITIES_BY_CONTEXTS = mapOf();
    }

    @Override
    public void addContext(KeyBindingContext keyBindingContext, int priority)
            throws IllegalArgumentException {
        Check.ifNull(keyBindingContext, "keyBindingContext");
        removeContext(keyBindingContext);
        if (!CONTEXTS.containsKey(priority)) {
            List<KeyBindingContext> contextsAtPriority = listOf();
            contextsAtPriority.add(keyBindingContext);
            CONTEXTS.put(priority, contextsAtPriority);
        }
        else {
            CONTEXTS.get(priority).add(keyBindingContext);
        }
        PRIORITIES_BY_CONTEXTS.put(keyBindingContext, priority);
    }

    @Override
    public void removeContext(KeyBindingContext keyBindingContext)
            throws IllegalArgumentException {
        Check.ifNull(keyBindingContext, "keyBindingContext");
        if (PRIORITIES_BY_CONTEXTS.containsKey(keyBindingContext)) {
            var priority = PRIORITIES_BY_CONTEXTS.get(keyBindingContext);
            var contextsAtPriority = CONTEXTS.get(priority);
            contextsAtPriority.remove(keyBindingContext);
            if (contextsAtPriority.isEmpty()) {
                CONTEXTS.remove(priority);
            }
            PRIORITIES_BY_CONTEXTS.remove(keyBindingContext);
        }
    }

    @Override
    public List<Character> activeKeysRepresentation() {
        List<Character> representation = listOf();
        loopOverBindings(null, null, c -> {
            if (!representation.contains(c)) {
                representation.add(c);
            }
        });
        return representation;
    }

    @Override
    public void press(char key, long timestamp) {
        runKeyAction(key, timestamp, b -> b.ON_PRESS);
    }

    @Override
    public void release(char key, long timestamp) {
        runKeyAction(key, timestamp, b -> b.ON_RELEASE);
    }

    private void runKeyAction(char key, long timestamp,
                              Function<KeyBinding, Action<KeyEventInfo>> getKeyAction) {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);
        handleKeyEvent(key, binding -> {
            var keyAction = getKeyAction.apply(binding);
            if (keyAction != null) {
                keyAction.accept(
                        keyEventInfo()
                                .withKey(key)
                                .withTimestamp(timestamp));
            }
        });
    }

    // TODO: Ensure that the values in CONTEXT are also deeply cloned in KeyEventListenerImpl
    //  .contextsRepresentation
    @Override
    public Map<Integer, List<KeyBindingContext>> contextsRepresentation() {
        return mapOf(CONTEXTS);
    }

    private void handleKeyEvent(char c, Consumer<KeyBinding> onEvent) {
        loopOverBindings(c, onEvent, null);
    }

    private void loopOverBindings(Character pressedKey,
                                  Consumer<KeyBinding> handleEvent,
                                  Consumer<Character> handleKey) {
        var orderedPriorities = CONTEXTS.keySet().stream().sorted((i1, i2) -> i2 - i1).toList();
        for (var priority : orderedPriorities) {
            var contexts = CONTEXTS.get(priority);
            for (var context : contexts) {
                for (var binding : context.BINDINGS) {
                    for (var boundKey : binding.BOUND_KEYS) {
                        if (handleKey != null) {
                            handleKey.accept(boundKey);
                        }
                        if (handleEvent != null && boundKey == pressedKey) {
                            handleEvent.accept(binding);
                        }
                    }
                }
                if (context.BLOCKS_LOWER_BINDINGS) {
                    return;
                }
            }
        }
    }
}
