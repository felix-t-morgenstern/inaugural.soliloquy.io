package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.keyboard.KeyBinding;
import soliloquy.specs.io.keyboard.KeyBindingContext;
import soliloquy.specs.io.keyboard.KeyEventListener;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class KeyEventListenerImpl implements KeyEventListener {
    private final Map<Integer, List<KeyBindingContext>> CONTEXTS;
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final Map<KeyBindingContext, Integer> PRIORITIES_BY_CONTEXTS;

    public KeyEventListenerImpl(Long mostRecentTimestamp) {
        CONTEXTS = mapOf();
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
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
    public void press(char c, long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        handleKeyEvent(c, binding -> binding.press(timestamp));
    }

    @Override
    public void release(char c, long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        handleKeyEvent(c, binding -> binding.release(timestamp));
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

    private void loopOverBindings(Character c, Consumer<KeyBinding> handleEvent,
                                  Consumer<Character> handleCharacter) {
        var orderedPriorities = CONTEXTS.keySet().stream().sorted((i1, i2) -> i2 - i1).toList();
        for (var priority : orderedPriorities) {
            var contexts = CONTEXTS.get(priority);
            for (var context : contexts) {
                var bindings = context.bindings();
                for (var binding : bindings) {
                    if (handleCharacter != null) {
                        binding.boundCharacters().forEach(handleCharacter);
                    }
                    if (handleEvent != null) {
                        if (binding.boundCharacters().contains(c)) {
                            handleEvent.accept(binding);
                        }
                    }
                    if (binding.getBlocksLowerBindings()) {
                        break;
                    }
                }
                if (context.getBlocksAllLowerBindings()) {
                    return;
                }
            }
        }
    }
}
