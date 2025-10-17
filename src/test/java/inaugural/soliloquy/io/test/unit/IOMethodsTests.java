package inaugural.soliloquy.io.test.unit;

import inaugural.soliloquy.io.IOMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IOMethodsTests {
    private final String SOUND_ID = randomString();

    @Mock private SoundsPlaying mockSoundsPlaying;
    @Mock private SoundFactory mockSoundFactory;
    @Mock private Sound mockSound;

    private IOMethods methods;

    @BeforeEach
    public void setUp() {
        lenient().when(mockSoundFactory.make(anyString())).thenReturn(mockSound);

        methods = new IOMethods(mockSoundsPlaying, mockSoundFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new IOMethods(null, mockSoundFactory));
        assertThrows(IllegalArgumentException.class, () -> new IOMethods(mockSoundsPlaying, null));
    }

    @Test
    public void testMakeSound() {
        var sound = methods.makeSound(SOUND_ID);

        assertSame(mockSound, sound);
        verify(mockSoundFactory, once()).make(SOUND_ID);
    }

    @Test
    public void testPlaySound() {
        var sound = methods.playSound(SOUND_ID);

        assertSame(mockSound, sound);
        verify(mockSoundFactory, once()).make(SOUND_ID);
        verify(mockSound, once()).play();
    }
}
