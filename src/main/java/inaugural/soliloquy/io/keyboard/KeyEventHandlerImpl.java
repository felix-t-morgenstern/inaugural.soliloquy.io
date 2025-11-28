package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.input.keyboard.KeyBinding;
import soliloquy.specs.io.input.keyboard.KeyEventHandler;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.ui.EventInputs.eventInputs;

public class KeyEventHandlerImpl implements KeyEventHandler {
    private final Map<Integer, Set<Component>> COMPONENTS;
    private final Map<Component, Integer> PRIORITIES_BY_CONTEXTS;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public KeyEventHandlerImpl(TimestampValidator timestampValidator) {
        COMPONENTS = mapOf();
        PRIORITIES_BY_CONTEXTS = mapOf();
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
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
    public Integer getPriority(Component component) throws IllegalArgumentException {
        return PRIORITIES_BY_CONTEXTS.get(Check.ifNull(component, "component"));
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
    public Set<Integer> activeKeysRepresentation() {
        var representation = Collections.<Integer>setOf();
        loopOverBindings(null, null, (b, _) -> representation.add(b));
        return representation;
    }

    @Override
    public void press(int pressedKeyCodepoint, long timestamp) {
        runKeyConsumer(pressedKeyCodepoint, timestamp, b -> b.ON_PRESS);
    }

    @Override
    public void release(int releasedKeyCodepoint, long timestamp) {
        runKeyConsumer(releasedKeyCodepoint, timestamp, b -> b.ON_RELEASE);
    }

    private void runKeyConsumer(int pressedKeyCodepoint, long timestamp,
                              Function<KeyBinding, Consumer<EventInputs>> getKeyConsumer) {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);
        handleKeyEvent(pressedKeyCodepoint, (binding, component) -> {
            var keyConsumer = getKeyConsumer.apply(binding);
            if (keyConsumer != null) {
                keyConsumer.accept(
                        eventInputs(timestamp)
                                .withKeyEvent(pressedKeyCodepoint, component)
                );
            }
        });
    }

    @Override
    public Map<Integer, Set<Component>> componentsRepresentation() {
        return mapOf(COMPONENTS);
    }

    private void handleKeyEvent(int c, BiConsumer<KeyBinding, Component> onEvent) {
        loopOverBindings(c, onEvent, null);
    }

    private void loopOverBindings(Integer pressedKeyCodepoint,
                                  BiConsumer<KeyBinding, Component> handleEvent,
                                  BiConsumer<Integer, Component> handleKeyCodepoint) {
        var orderedPriorities = COMPONENTS.keySet().stream().sorted((i1, i2) -> i2 - i1).toList();
        for (var priority : orderedPriorities) {
            var components = COMPONENTS.get(priority);
            for (var component : components) {
                for (var binding : component.keyBindings()) {
                    for (var boundKey : binding.BOUND_CODEPOINTS) {
                        if (handleKeyCodepoint != null) {
                            handleKeyCodepoint.accept(boundKey, component);
                        }
                        if (handleEvent != null && boundKey == pressedKeyCodepoint) {
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
