package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.input.keyboard.KeyBinding;
import soliloquy.specs.io.input.keyboard.KeyEventListener;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.ui.EventInputs.eventInputs;

public class KeyEventListenerImpl implements KeyEventListener {
    private final Map<Integer, Set<Component>> COMPONENTS;
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final Map<Component, Integer> PRIORITIES_BY_CONTEXTS;

    public KeyEventListenerImpl(TimestampValidator timestampValidator) {
        COMPONENTS = mapOf();
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        PRIORITIES_BY_CONTEXTS = mapOf();
    }

    @Override
    public void addComponent(Component component, int priority)
            throws IllegalArgumentException {
        Check.ifNull(component, "component");
        removeComponent(component);
        if (!COMPONENTS.containsKey(priority)) {
            var contextsAtPriority = Collections.<Component>setOf();
            contextsAtPriority.add(component);
            COMPONENTS.put(priority, contextsAtPriority);
        }
        else {
            COMPONENTS.get(priority).add(component);
        }
        PRIORITIES_BY_CONTEXTS.put(component, priority);
    }

    @Override
    public void removeComponent(Component component) throws IllegalArgumentException {
        Check.ifNull(component, "component");
        if (PRIORITIES_BY_CONTEXTS.containsKey(component)) {
            var priority = PRIORITIES_BY_CONTEXTS.get(component);
            var contextsAtPriority = COMPONENTS.get(priority);
            contextsAtPriority.remove(component);
            if (contextsAtPriority.isEmpty()) {
                COMPONENTS.remove(priority);
            }
            PRIORITIES_BY_CONTEXTS.remove(component);
        }
    }

    @Override
    public Set<Character> activeKeysRepresentation() {
        var representation = Collections.<Character>setOf();
        loopOverBindings(null, null, (b, _) -> representation.add(b));
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
                              Function<KeyBinding, Action<EventInputs>> getKeyAction) {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);
        handleKeyEvent(key, (binding, component) -> {
            var keyAction = getKeyAction.apply(binding);
            if (keyAction != null) {
                keyAction.accept(
                        eventInputs(timestamp)
                                .withKeyEvent(key, component)
                );
            }
        });
    }

    @Override
    public Map<Integer, Set<Component>> componentsRepresentation() {
        return mapOf(COMPONENTS);
    }

    private void handleKeyEvent(char c, BiConsumer<KeyBinding, Component> onEvent) {
        loopOverBindings(c, onEvent, null);
    }

    private void loopOverBindings(Character pressedKey,
                                  BiConsumer<KeyBinding, Component> handleEvent,
                                  BiConsumer<Character, Component> handleKey) {
        var orderedPriorities = COMPONENTS.keySet().stream().sorted((i1, i2) -> i2 - i1).toList();
        for (var priority : orderedPriorities) {
            var components = COMPONENTS.get(priority);
            for (var component : components) {
                for (var binding : component.keyBindings()) {
                    for (var boundKey : binding.BOUND_KEYS) {
                        if (handleKey != null) {
                            handleKey.accept(boundKey, component);
                        }
                        if (handleEvent != null && boundKey == pressedKey) {
                            handleEvent.accept(binding, component);
                        }
                    }
                }
                if (component.blocksLowerKeyBindings()) {
                    return;
                }
            }
        }
    }
}
