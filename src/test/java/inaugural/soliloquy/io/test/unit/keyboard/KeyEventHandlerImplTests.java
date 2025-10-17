package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyEventHandlerImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.input.keyboard.KeyBinding;
import soliloquy.specs.io.input.keyboard.KeyEventHandler;
import soliloquy.specs.ui.EventInputs;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.io.input.keyboard.KeyBinding.keyBinding;

@ExtendWith(MockitoExtension.class)
public class KeyEventHandlerImplTests {
    private final int PRIORITY_1 = randomIntWithInclusiveCeiling(Integer.MAX_VALUE - 2);
    private final int PRIORITY_2 = randomIntWithInclusiveFloor(PRIORITY_1 + 1);
    private final Long MOST_RECENT_TIMESTAMP = randomLong();
    private final int KEY_CODEPOINT = randomInt();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Component mockComponent1;
    @Mock private Component mockComponent2;
    @Mock private Component mockComponent3;

    private KeyEventHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new KeyEventHandlerImpl(mockTimestampValidator);
    }

    @Test
    public void testAddAndRemoveComponentAndComponentsRepresentation() {
        handler.addComponent(mockComponent1, PRIORITY_1);
        handler.addComponent(mockComponent2, PRIORITY_2);
        handler.addComponent(mockComponent3, PRIORITY_1);

        var representation = handler.componentsRepresentation();
        var representation2 = handler.componentsRepresentation();

        assertNotNull(representation);
        assertNotSame(representation, representation2);
        assertEquals(2, representation.size());
        assertEquals(2, representation.get(PRIORITY_1).size());
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent3));
        assertEquals(1, representation.get(PRIORITY_2).size());
        assertTrue(representation.get(PRIORITY_2).contains(mockComponent2));

        handler.removeComponent(mockComponent2);
        var representationUpdated = handler.componentsRepresentation();

        assertEquals(1, representationUpdated.size());
        assertEquals(2, representationUpdated.get(PRIORITY_1).size());
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent3));
    }

    @Test
    public void testAddComponentUpdatesPriority() {
        handler.addComponent(mockComponent1, PRIORITY_1);
        handler.addComponent(mockComponent2, PRIORITY_2);
        handler.addComponent(mockComponent3, PRIORITY_1);

        var representation = handler.componentsRepresentation();
        var representation2 = handler.componentsRepresentation();

        assertNotNull(representation);
        assertNotSame(representation, representation2);
        assertEquals(2, representation.size());
        assertEquals(2, representation.get(PRIORITY_1).size());
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent3));
        assertEquals(1, representation.get(PRIORITY_2).size());
        assertTrue(representation.get(PRIORITY_2).contains(mockComponent2));

        handler.addComponent(mockComponent3, PRIORITY_2);
        var representationUpdated = handler.componentsRepresentation();

        assertEquals(2, representationUpdated.size());
        assertEquals(1, representationUpdated.get(PRIORITY_1).size());
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent1));
        assertEquals(2, representationUpdated.get(PRIORITY_2).size());
        assertTrue(representationUpdated.get(PRIORITY_2).contains(mockComponent2));
        assertTrue(representationUpdated.get(PRIORITY_2).contains(mockComponent3));
    }

    @Test
    public void testAddAndRemoveComponentWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.addComponent(null, 0));
        assertThrows(IllegalArgumentException.class, () -> handler.removeComponent(null));
    }

    @Test
    public void testActiveKeysRepresentation() {
        var keyCodepoint1 = randomInt();
        var keyCodepoint2 = randomInt();
        var keyCodepoint3 = randomInt();
        var keyCodepoint4 = randomInt();
        var keyCodepoint5 = randomInt();
        var keyCodepoint6 = randomInt();

        var component1Binding1 = keyBinding(arrayInts(keyCodepoint1, keyCodepoint2), null, null);

        var component1Binding2 = keyBinding(arrayInts(keyCodepoint3), null, null);

        var component1 = makeComponent(setOf(component1Binding1, component1Binding2), false);

        var component2Binding1 = keyBinding(arrayInts(keyCodepoint4), null, null);

        var component2Binding2 = keyBinding(arrayInts(keyCodepoint5), null, null);

        var component2 = makeComponent(setOf(component2Binding1, component2Binding2), true);

        var component3Binding1 = keyBinding(arrayInts(keyCodepoint6), null, null);
        var component3 = makeComponent(setOf(component3Binding1), false);

        handler.addComponent(component1, 1);
        handler.addComponent(component2, 2);
        handler.addComponent(component3, 3);

        var activeKeysRepresentation = handler.activeKeysRepresentation();
        var activeKeysRepresentation2 = handler.activeKeysRepresentation();

        assertNotNull(activeKeysRepresentation);
        assertNotSame(activeKeysRepresentation, activeKeysRepresentation2);
        assertEquals(3, activeKeysRepresentation.size());
        assertTrue(activeKeysRepresentation.contains(keyCodepoint6));
        assertTrue(activeKeysRepresentation.contains(keyCodepoint4));
        assertTrue(activeKeysRepresentation.contains(keyCodepoint5));
    }

    @Test
    public void testKeyPressed() {
        @SuppressWarnings("unchecked") var onPress = (Action<EventInputs>) mock(Action.class);
        var binding = keyBinding(arrayInts(KEY_CODEPOINT), onPress, null);

        var component = makeComponent(setOf(binding), false);

        handler.addComponent(component, 0);

        handler.press(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(EventInputs.class);
        verify(onPress, once()).accept(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY_CODEPOINT, eventInfoProvided.keyCodepoint);
        assertSame(component, eventInfoProvided.component);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.TIMESTAMP);
    }

    @Test
    public void testKeyReleased() {
        @SuppressWarnings("unchecked") var onRelease = (Action<EventInputs>) mock(Action.class);
        var binding = keyBinding(arrayInts(KEY_CODEPOINT), null, onRelease);

        var component = makeComponent(setOf(binding), false);

        handler.addComponent(component, 0);

        handler.release(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(EventInputs.class);
        verify(onRelease, once()).accept(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY_CODEPOINT, eventInfoProvided.keyCodepoint);
        assertSame(component, eventInfoProvided.component);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.TIMESTAMP);
    }

    @Test
    public void testComponentCanBlockLowerComponent() {
        @SuppressWarnings("unchecked") var lowerBindingOnPress =
                (Action<EventInputs>) mock(Action.class);
        var lowerBinding = keyBinding(arrayInts(randomInt()), lowerBindingOnPress, null);

        var lowerComponent = makeComponent(setOf(lowerBinding), false);

        @SuppressWarnings("unchecked") var upperBindingOnPress =
                (Action<EventInputs>) mock(Action.class);
        var upperBinding = keyBinding(arrayInts(KEY_CODEPOINT), upperBindingOnPress, null);

        var upperComponent = makeComponent(setOf(upperBinding), true);

        handler.addComponent(upperComponent, 1);
        handler.addComponent(lowerComponent, 0);

        handler.press(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).accept(any());
        verify(lowerBindingOnPress, never()).accept(any());

        handler.release(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).accept(any());
        verify(lowerBindingOnPress, never()).accept(any());
    }

    @Test
    public void testPressValidatesTimestamp() {
        handler.press(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        verify(mockTimestampValidator, once())
                .validateTimestamp(handler.getClass().getCanonicalName(),
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testReleaseValidatesTimestamp() {
        handler.release(KEY_CODEPOINT, MOST_RECENT_TIMESTAMP);

        verify(mockTimestampValidator, once())
                .validateTimestamp(handler.getClass().getCanonicalName(),
                        MOST_RECENT_TIMESTAMP);
    }

    private Component makeComponent(Set<KeyBinding> bindings, boolean overrides) {
        var mockComponent = mock(Component.class);
        lenient().when(mockComponent.keyBindings()).thenReturn(bindings);
        lenient().when(mockComponent.blocksLowerKeyBindings()).thenReturn(overrides);
        return mockComponent;
    }
}
