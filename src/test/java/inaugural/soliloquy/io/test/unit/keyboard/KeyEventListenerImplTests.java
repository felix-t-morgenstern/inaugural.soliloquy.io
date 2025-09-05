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
import soliloquy.specs.io.input.keyboard.entities.KeyBindingContext;
import soliloquy.specs.io.input.keyboard.infrastructure.KeyEventListener;
import soliloquy.specs.ui.definitions.keyboard.KeyEventInfo;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.io.input.keyboard.entities.KeyBinding.keyBinding;
import static soliloquy.specs.io.input.keyboard.entities.KeyBindingContext.bindingContext;

@ExtendWith(MockitoExtension.class)
public class KeyEventListenerImplTests {
    private final int priority1 = randomIntWithInclusiveCeiling(Integer.MAX_VALUE - 2);
    private final int priority2 = randomIntWithInclusiveFloor(priority1 + 1);
    private final Long MOST_RECENT_TIMESTAMP = randomLong();
    private final char KEY = randomChar();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private KeyBindingContext mockKeyBindingContext1;
    @Mock private KeyBindingContext mockKeyBindingContext2;
    @Mock private KeyBindingContext mockKeyBindingContext3;

    private KeyEventListener keyEventListener;

    @BeforeEach
    public void setUp() {
        keyEventListener = new KeyEventListenerImpl(mockTimestampValidator);
    }

    @Test
    public void testAddAndRemoveContextAndContextsRepresentation() {
        keyEventListener.addContext(mockKeyBindingContext1, priority1);
        keyEventListener.addContext(mockKeyBindingContext2, priority2);
        keyEventListener.addContext(mockKeyBindingContext3, priority1);

        var contextsRepresentation = keyEventListener.contextsRepresentation();
        var contextsRepresentation2 = keyEventListener.contextsRepresentation();

        assertNotNull(contextsRepresentation);
        assertNotSame(contextsRepresentation, contextsRepresentation2);
        assertEquals(2, contextsRepresentation.size());
        assertEquals(2, contextsRepresentation.get(priority1).size());
        assertTrue(contextsRepresentation.get(priority1).contains(mockKeyBindingContext1));
        assertTrue(contextsRepresentation.get(priority1).contains(mockKeyBindingContext3));
        assertEquals(1, contextsRepresentation.get(priority2).size());
        assertTrue(contextsRepresentation.get(priority2).contains(mockKeyBindingContext2));

        keyEventListener.removeContext(mockKeyBindingContext2);
        var contextsRepresentationUpdated = keyEventListener.contextsRepresentation();

        assertEquals(1, contextsRepresentationUpdated.size());
        assertEquals(2, contextsRepresentationUpdated.get(priority1).size());
        assertTrue(contextsRepresentationUpdated.get(priority1).contains(mockKeyBindingContext1));
        assertTrue(contextsRepresentationUpdated.get(priority1).contains(mockKeyBindingContext3));
    }

    @Test
    public void testAddContextUpdatesPriority() {
        keyEventListener.addContext(mockKeyBindingContext1, priority1);
        keyEventListener.addContext(mockKeyBindingContext2, priority2);
        keyEventListener.addContext(mockKeyBindingContext3, priority1);

        var contextsRepresentation = keyEventListener.contextsRepresentation();
        var contextsRepresentation2 = keyEventListener.contextsRepresentation();

        assertNotNull(contextsRepresentation);
        assertNotSame(contextsRepresentation, contextsRepresentation2);
        assertEquals(2, contextsRepresentation.size());
        assertEquals(2, contextsRepresentation.get(priority1).size());
        assertTrue(contextsRepresentation.get(priority1).contains(mockKeyBindingContext1));
        assertTrue(contextsRepresentation.get(priority1).contains(mockKeyBindingContext3));
        assertEquals(1, contextsRepresentation.get(priority2).size());
        assertTrue(contextsRepresentation.get(priority2).contains(mockKeyBindingContext2));

        keyEventListener.addContext(mockKeyBindingContext3, priority2);
        var contextsRepresentationUpdated = keyEventListener.contextsRepresentation();

        assertEquals(2, contextsRepresentationUpdated.size());
        assertEquals(1, contextsRepresentationUpdated.get(priority1).size());
        assertTrue(contextsRepresentationUpdated.get(priority1).contains(mockKeyBindingContext1));
        assertEquals(2, contextsRepresentationUpdated.get(priority2).size());
        assertTrue(contextsRepresentationUpdated.get(priority2).contains(mockKeyBindingContext2));
        assertTrue(contextsRepresentationUpdated.get(priority2).contains(mockKeyBindingContext3));
    }

    @Test
    public void testAddAndRemoveContextWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> keyEventListener.addContext(null, 0));
        assertThrows(IllegalArgumentException.class, () -> keyEventListener.removeContext(null));
    }

    @Test
    public void testActiveKeysRepresentation() {
        var context1Binding1 = keyBinding(arrayChars('a', 'b'), null, null);

        var context1Binding2 = keyBinding(arrayChars('c'), null, null);

        var keyBindingContext1 = bindingContext(listOf(context1Binding1, context1Binding2), false);

        var context2Binding1 = keyBinding(arrayChars('d'), null, null);

        var context2Binding2 = keyBinding(arrayChars('e'), null, null);

        var keyBindingContext2 = bindingContext(listOf(context2Binding1, context2Binding2), true);

        var context3Binding1 = keyBinding(arrayChars('f'), null, null);
        var keyBindingContext3 = bindingContext(listOf(context3Binding1), false);

        keyEventListener.addContext(keyBindingContext1, 1);
        keyEventListener.addContext(keyBindingContext2, 2);
        keyEventListener.addContext(keyBindingContext3, 3);

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
        @SuppressWarnings("unchecked") var onPress = (Action<KeyEventInfo>) mock(Action.class);
        var binding = keyBinding(arrayChars(KEY), onPress, null);

        var keyBindingContext = bindingContext(listOf(binding), false);

        keyEventListener.addContext(keyBindingContext, 0);

        keyEventListener.press(KEY, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(KeyEventInfo.class);
        verify(onPress, once()).run(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY, eventInfoProvided.key);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.timestamp);
    }

    @Test
    public void testKeyReleased() {
        @SuppressWarnings("unchecked") var onRelease = (Action<KeyEventInfo>) mock(Action.class);
        var binding = keyBinding(arrayChars(KEY), null, onRelease);

        var keyBindingContext = bindingContext(listOf(binding), false);

        keyEventListener.addContext(keyBindingContext, 0);

        keyEventListener.release(KEY, MOST_RECENT_TIMESTAMP);

        var keyEventInfoCaptor = ArgumentCaptor.forClass(KeyEventInfo.class);
        verify(onRelease, once()).run(keyEventInfoCaptor.capture());
        var eventInfoProvided = keyEventInfoCaptor.getValue();
        assertEquals(KEY, eventInfoProvided.key);
        assertEquals(MOST_RECENT_TIMESTAMP, eventInfoProvided.timestamp);
    }

    @Test
    public void testContextCanBlockLowerContexts() {
        @SuppressWarnings("unchecked") var lowerBindingOnPress =
                (Action<KeyEventInfo>) mock(Action.class);
        var lowerBinding = keyBinding(arrayChars('a'), lowerBindingOnPress, null);

        var lowerBindingContext = bindingContext(listOf(lowerBinding), false);

        @SuppressWarnings("unchecked") var upperBindingOnPress =
                (Action<KeyEventInfo>) mock(Action.class);
        var upperBinding = keyBinding(arrayChars(KEY), upperBindingOnPress, null);

        var upperBindingContext = bindingContext(listOf(upperBinding), true);

        keyEventListener.addContext(upperBindingContext, 1);
        keyEventListener.addContext(lowerBindingContext, 0);

        keyEventListener.press(KEY, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).run(any());
        verify(lowerBindingOnPress, never()).run(any());

        keyEventListener.release(KEY, MOST_RECENT_TIMESTAMP);

        verify(upperBindingOnPress, once()).run(any());
        verify(lowerBindingOnPress, never()).run(any());
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
}
