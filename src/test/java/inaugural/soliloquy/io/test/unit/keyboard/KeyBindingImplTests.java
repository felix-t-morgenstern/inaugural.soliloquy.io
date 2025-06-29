package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyBindingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.keyboard.KeyBinding;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KeyBindingImplTests {
    private final String KEY_PRESS_ACTION_ID = randomString();
    private final String KEY_RELEASE_ACTION_ID = randomString();
    private final Long TIMESTAMP = randomLong();
    private final char[] CHARACTERS = new char[]{randomChar(), randomChar(), randomChar()};

    @Mock private Action<Long> mockKeyPressAction;
    @Mock private Action<Long> mockKeyReleaseAction;

    private KeyBinding keyBinding;

    @BeforeEach
    public void setUp() {
        lenient().when(mockKeyPressAction.id()).thenReturn(KEY_PRESS_ACTION_ID);
        lenient().when(mockKeyReleaseAction.id()).thenReturn(KEY_RELEASE_ACTION_ID);

        keyBinding = new KeyBindingImpl(CHARACTERS);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new KeyBindingImpl(null));
    }

    @Test
    public void testBoundCharacters() {
        var boundCharacters = keyBinding.boundCharacters();

        assertNotNull(boundCharacters);
        assertNotSame(keyBinding.boundCharacters(), boundCharacters);
        assertEquals(CHARACTERS.length, boundCharacters.size());
        for (var c : CHARACTERS) {
            assertTrue(boundCharacters.contains(c));
        }
    }

    @Test
    public void testSetOnPressAndOnPressActionIdAndPress() {
        assertNull(keyBinding.onPressActionId());

        keyBinding.setOnPress(mockKeyPressAction);

        keyBinding.press(TIMESTAMP);

        assertEquals(KEY_PRESS_ACTION_ID, keyBinding.onPressActionId());
        verify(mockKeyPressAction, once()).run(TIMESTAMP);
    }

    @Test
    public void testSetOnReleaseAndOnReleaseActionIdAndRelease() {
        assertNull(keyBinding.onReleaseActionId());

        keyBinding.setOnRelease(mockKeyReleaseAction);

        keyBinding.release(TIMESTAMP);

        assertEquals(KEY_RELEASE_ACTION_ID, keyBinding.onReleaseActionId());
        verify(mockKeyReleaseAction, once()).run(TIMESTAMP);
    }

    @Test
    public void testSetAndGetBlocksLowerBindings() {
        keyBinding.setBlocksLowerBindings(true);

        assertTrue(keyBinding.getBlocksLowerBindings());
    }

    @Test
    public void testPressAndReleaseWithInvalidArgs() {
        keyBinding.press(TIMESTAMP);
        keyBinding.release(TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () -> keyBinding.press(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () -> keyBinding.release(TIMESTAMP - 1));
    }
}
