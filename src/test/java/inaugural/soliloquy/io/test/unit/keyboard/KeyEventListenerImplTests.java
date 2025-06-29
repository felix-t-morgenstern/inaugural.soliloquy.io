package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.keyboard.KeyBinding;
import soliloquy.specs.io.keyboard.KeyBindingContext;
import soliloquy.specs.io.keyboard.KeyEventListener;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeyEventListenerImplTests {
    private final int priority1 = randomIntWithInclusiveCeiling(Integer.MAX_VALUE - 2);
    private final int priority2 = randomIntWithInclusiveFloor(priority1 + 1);
    private final Long MOST_RECENT_TIMESTAMP = randomLong();
    private final char CHAR = randomChar();

    @Mock private KeyBindingContext mockKeyBindingContext1;
    @Mock private KeyBindingContext mockKeyBindingContext2;
    @Mock private KeyBindingContext mockKeyBindingContext3;

    private KeyEventListener keyEventListener;

    @BeforeEach
    public void setUp() {
        keyEventListener = new KeyEventListenerImpl(MOST_RECENT_TIMESTAMP);
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
        var mockKeyBindingContext1Binding1 = mock(KeyBinding.class);
        lenient().when(mockKeyBindingContext1Binding1.boundCharacters()).thenReturn(listOf(('a')));
        lenient().when(mockKeyBindingContext1Binding1.boundCharacters()).thenReturn(listOf(('b')));
        lenient().when(mockKeyBindingContext1Binding1.getBlocksLowerBindings()).thenReturn(true);

        var mockKeyBindingContext1Binding2 = mock(KeyBinding.class);
        lenient().when(mockKeyBindingContext1Binding2.boundCharacters()).thenReturn(listOf(('c')));

        var mockKeyBindingContext1 = mock(KeyBindingContext.class);
        lenient().when(mockKeyBindingContext1.bindings())
                .thenReturn(listOf(mockKeyBindingContext1Binding1, mockKeyBindingContext1Binding2));

        var mockKeyBindingContext2Binding1 = mock(KeyBinding.class);
        lenient().when(mockKeyBindingContext2Binding1.boundCharacters()).thenReturn(listOf(('d')));

        var mockKeyBindingContext2Binding2 = mock(KeyBinding.class);
        lenient().when(mockKeyBindingContext2Binding2.boundCharacters()).thenReturn(listOf(('e')));

        var mockKeyBindingContext2 = mock(KeyBindingContext.class);
        lenient().when(mockKeyBindingContext2.bindings())
                .thenReturn(listOf(mockKeyBindingContext2Binding1, mockKeyBindingContext2Binding2));
        lenient().when(mockKeyBindingContext2.getBlocksAllLowerBindings()).thenReturn(true);

        var mockKeyBindingContext3Binding1 = mock(KeyBinding.class);
        lenient().when(mockKeyBindingContext3Binding1.boundCharacters()).thenReturn(listOf(('f')));
        var mockKeyBindingContext3 = mock(KeyBindingContext.class);
        lenient().when(mockKeyBindingContext3.bindings())
                .thenReturn(listOf(mockKeyBindingContext3Binding1));

        keyEventListener.addContext(mockKeyBindingContext1, 1);
        keyEventListener.addContext(mockKeyBindingContext2, 2);
        keyEventListener.addContext(mockKeyBindingContext3, 3);

        List<Character> activeKeysRepresentation = keyEventListener.activeKeysRepresentation();
        List<Character> activeKeysRepresentation2 = keyEventListener.activeKeysRepresentation();

        assertNotNull(activeKeysRepresentation);
        assertNotSame(activeKeysRepresentation, activeKeysRepresentation2);
        assertEquals(3, activeKeysRepresentation.size());
        assertTrue(activeKeysRepresentation.contains('f'));
        assertTrue(activeKeysRepresentation.contains('d'));
        assertTrue(activeKeysRepresentation.contains('e'));
    }

    @Test
    public void testKeyPressed() {
        var mockKeyBinding = mock(KeyBinding.class);
        lenient().when(mockKeyBinding.boundCharacters()).thenReturn(listOf((CHAR)));

        var mockKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockKeyBindingContext.bindings()).thenReturn(listOf(mockKeyBinding));

        keyEventListener.addContext(mockKeyBindingContext, 0);

        keyEventListener.press(CHAR, MOST_RECENT_TIMESTAMP);

        verify(mockKeyBinding, once()).press(MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testKeyReleased() {
        var mockKeyBinding = mock(KeyBinding.class);
        lenient().when(mockKeyBinding.boundCharacters()).thenReturn(listOf((CHAR)));

        var mockKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockKeyBindingContext.bindings()).thenReturn(listOf(mockKeyBinding));

        keyEventListener.addContext(mockKeyBindingContext, 0);

        keyEventListener.release(CHAR, MOST_RECENT_TIMESTAMP);

        verify(mockKeyBinding, once()).release(MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testContextBlocksLowerContextEvents() {
        var mockLowerKeyBinding = mock(KeyBinding.class);
        lenient().when(mockLowerKeyBinding.boundCharacters()).thenReturn(listOf((CHAR)));

        var mockLowerKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockLowerKeyBindingContext.bindings())
                .thenReturn(listOf(mockLowerKeyBinding));

        var upperKeyBinding = mock(KeyBinding.class);
        lenient().when(upperKeyBinding.boundCharacters()).thenReturn(listOf((CHAR)));

        var mockUpperKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockUpperKeyBindingContext.bindings()).thenReturn(listOf(upperKeyBinding));

        lenient().when(mockUpperKeyBindingContext.getBlocksAllLowerBindings()).thenReturn(true);

        keyEventListener.addContext(mockUpperKeyBindingContext, 0);
        keyEventListener.addContext(mockLowerKeyBindingContext, 1);

        keyEventListener.press(CHAR, MOST_RECENT_TIMESTAMP);

        verify(upperKeyBinding, once()).press(MOST_RECENT_TIMESTAMP);

        keyEventListener.release(CHAR, MOST_RECENT_TIMESTAMP);

        verify(upperKeyBinding, once()).release(MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testBindingBlocksLowerBindingEvents() {
        var mockLowerKeyBinding = mock(KeyBinding.class);
        lenient().when(mockLowerKeyBinding.boundCharacters()).thenReturn(listOf('a'));

        var mockLowerKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockLowerKeyBindingContext.bindings())
                .thenReturn(listOf(mockLowerKeyBinding));

        var mockUpperKeyBinding = mock(KeyBinding.class);
        lenient().when(mockUpperKeyBinding.boundCharacters()).thenReturn(listOf((CHAR)));

        var mockUpperKeyBindingContext = mock(KeyBindingContext.class);
        lenient().when(mockUpperKeyBindingContext.bindings())
                .thenReturn(listOf(mockUpperKeyBinding));

        lenient().when(mockUpperKeyBinding.getBlocksLowerBindings()).thenReturn(true);

        keyEventListener.addContext(mockUpperKeyBindingContext, 1);
        keyEventListener.addContext(mockLowerKeyBindingContext, 0);

        keyEventListener.press(CHAR, MOST_RECENT_TIMESTAMP);

        verify(mockUpperKeyBinding, once()).press(MOST_RECENT_TIMESTAMP);
        verify(mockLowerKeyBinding, never()).press(MOST_RECENT_TIMESTAMP);

        keyEventListener.release(CHAR, MOST_RECENT_TIMESTAMP);

        verify(mockUpperKeyBinding, once()).release(MOST_RECENT_TIMESTAMP);
        verify(mockLowerKeyBinding, never()).release(MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testPressOrReleaseAtInvalidTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                keyEventListener.press(CHAR, MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                keyEventListener.release(CHAR, MOST_RECENT_TIMESTAMP - 1));
    }
}
