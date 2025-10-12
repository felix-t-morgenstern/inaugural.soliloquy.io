package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
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
import soliloquy.specs.io.input.keyboard.KeyEventListener;
import soliloquy.specs.ui.EventInputs;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.io.input.keyboard.KeyBinding.keyBinding;

@ExtendWith(MockitoExtension.class)
public class KeyEventListenerImplTests {
    private final int PRIORITY_1 = randomIntWithInclusiveCeiling(Integer.MAX_VALUE - 2);
    private final int PRIORITY_2 = randomIntWithInclusiveFloor(PRIORITY_1 + 1);
    private final Long MOST_RECENT_TIMESTAMP = randomLong();
    private final char KEY = randomChar();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Component mockComponent1;
    @Mock private Component mockComponent2;
    @Mock private Component mockComponent3;

    private KeyEventListener keyEventListener;

    @BeforeEach
    public void setUp() {
        keyEventListener = new KeyEventListenerImpl(mockTimestampValidator);
    }

    @Test
    public void testAddAndRemoveComponentAndComponentsRepresentation() {
        keyEventListener.addComponent(mockComponent1, PRIORITY_1);
        keyEventListener.addComponent(mockComponent2, PRIORITY_2);
        keyEventListener.addComponent(mockComponent3, PRIORITY_1);

        var representation = keyEventListener.componentsRepresentation();
        var representation2 = keyEventListener.componentsRepresentation();

        assertNotNull(representation);
        assertNotSame(representation, representation2);
        assertEquals(2, representation.size());
        assertEquals(2, representation.get(PRIORITY_1).size());
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent3));
        assertEquals(1, representation.get(PRIORITY_2).size());
        assertTrue(representation.get(PRIORITY_2).contains(mockComponent2));

        keyEventListener.removeComponent(mockComponent2);
        var representationUpdated = keyEventListener.componentsRepresentation();

        assertEquals(1, representationUpdated.size());
        assertEquals(2, representationUpdated.get(PRIORITY_1).size());
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent3));
    }

    @Test
    public void testAddComponentUpdatesPriority() {
        keyEventListener.addComponent(mockComponent1, PRIORITY_1);
        keyEventListener.addComponent(mockComponent2, PRIORITY_2);
        keyEventListener.addComponent(mockComponent3, PRIORITY_1);

        var representation = keyEventListener.componentsRepresentation();
        var representation2 = keyEventListener.componentsRepresentation();

        assertNotNull(representation);
        assertNotSame(representation, representation2);
        assertEquals(2, representation.size());
        assertEquals(2, representation.get(PRIORITY_1).size());
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent1));
        assertTrue(representation.get(PRIORITY_1).contains(mockComponent3));
        assertEquals(1, representation.get(PRIORITY_2).size());
        assertTrue(representation.get(PRIORITY_2).contains(mockComponent2));

        keyEventListener.addComponent(mockComponent3, PRIORITY_2);
        var representationUpdated = keyEventListener.componentsRepresentation();

        assertEquals(2, representationUpdated.size());
        assertEquals(1, representationUpdated.get(PRIORITY_1).size());
        assertTrue(representationUpdated.get(PRIORITY_1).contains(mockComponent1));
        assertEquals(2, representationUpdated.get(PRIORITY_2).size());
        assertTrue(representationUpdated.get(PRIORITY_2).contains(mockComponent2));
        assertTrue(representationUpdated.get(PRIORITY_2).contains(mockComponent3));
    }

    @Test
    public void testAddAndRemoveComponentWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> keyEventListener.addComponent(null, 0));
        assertThrows(IllegalArgumentException.class, () -> keyEventListener.removeComponent(null));
    }

    @Test
    public void testActiveKeysRepresentation() {
        var component1Binding1 = keyBinding(arrayChars('a', 'b'), null, null);

        var component1Binding2 = keyBinding(arrayChars('c'), null, null);

        var component1 = makeComponent(setOf(component1Binding1, component1Binding2), false);

        var component2Binding1 = keyBinding(arrayChars('d'), null, null);

        var component2Binding2 = keyBinding(arrayChars('e'), null, null);

        var component2 = makeComponent(setOf(component2Binding1, component2Binding2), true);

        var component3Binding1 = keyBinding(arrayChars('f'), null, null);
        var component3 = makeComponent(setOf(component3Binding1), false);

        keyEventListener.addComponent(component1, 1);
        keyEventListener.addComponent(component2, 2);
        keyEventListener.addComponent(component3, 3);

        var activeKeysRepresentation = keyEventListener.activeKeysRepresentation();
        var activeKeysRepresentation2 = keyEventListener.activeKeysRepresentation();

        assertNotNull(activeKeysRepresentation);
        assertNotSame(activeKeysRepresentation, activeKeysRepresentation2);
        assertEquals(3, activeKeysRepresentation.size());
        assertTrue(activeKeysRepresentation.contains('f'));
        assertTrue(activeKeysRepresentation.contains('d'));
        assertTrue(activeKeysRepresentation.contains('e'));
    }

    @Test
    public void testKeyPressed() {
        @SuppressWarnings("unchecked") var onPress = (Action<EventInputs>) mock(Action.class);
        var binding = keyBinding(arrayChars(KEY), onPress, null);

        var component = makeComponent(setOf(binding), false);

        keyEventListener.addComponent(component, 0);

        keyEventListener.press(KEY, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(EventInputs.class);
        verify(onPress, once()).accept(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY, eventInfoProvided.key);
        assertSame(component, eventInfoProvided.component);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.TIMESTAMP);
    }

    @Test
    public void testKeyReleased() {
        @SuppressWarnings("unchecked") var onRelease = (Action<EventInputs>) mock(Action.class);
        var binding = keyBinding(arrayChars(KEY), null, onRelease);

        var component = makeComponent(setOf(binding), false);

        keyEventListener.addComponent(component, 0);

        keyEventListener.release(KEY, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(EventInputs.class);
        verify(onRelease, once()).accept(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY, eventInfoProvided.key);
        assertSame(component, eventInfoProvided.component);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.TIMESTAMP);
    }

    @Test
    public void testComponentCanBlockLowerComponent() {
        @SuppressWarnings("unchecked") var lowerBindingOnPress =
                (Action<EventInputs>) mock(Action.class);
        var lowerBinding = keyBinding(arrayChars('a'), lowerBindingOnPress, null);

        var lowerComponent = makeComponent(setOf(lowerBinding), false);

        @SuppressWarnings("unchecked") var upperBindingOnPress =
                (Action<EventInputs>) mock(Action.class);
        var upperBinding = keyBinding(arrayChars(KEY), upperBindingOnPress, null);

        var upperComponent = makeComponent(setOf(upperBinding), true);

        keyEventListener.addComponent(upperComponent, 1);
        keyEventListener.addComponent(lowerComponent, 0);

        keyEventListener.press(KEY, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).accept(any());
        verify(lowerBindingOnPress, never()).accept(any());

        keyEventListener.release(KEY, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).accept(any());
        verify(lowerBindingOnPress, never()).accept(any());
    }

    @Test
    public void testPressValidatesTimestamp() {
        keyEventListener.press(KEY, MOST_RECENT_TIMESTAMP);

        verify(mockTimestampValidator, once())
                .validateTimestamp(keyEventListener.getClass().getCanonicalName(),
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testReleaseValidatesTimestamp() {
        keyEventListener.release(KEY, MOST_RECENT_TIMESTAMP);

        verify(mockTimestampValidator, once())
                .validateTimestamp(keyEventListener.getClass().getCanonicalName(),
                        MOST_RECENT_TIMESTAMP);
    }

    private Component makeComponent(Set<KeyBinding> bindings, boolean overrides) {
        var mockComponent = mock(Component.class);
        lenient().when(mockComponent.keyBindings()).thenReturn(bindings);
        lenient().when(mockComponent.blocksLowerKeyBindings()).thenReturn(overrides);
        return mockComponent;
    }
}
