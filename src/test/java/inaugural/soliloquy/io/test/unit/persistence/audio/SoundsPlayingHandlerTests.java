package inaugural.soliloquy.io.test.unit.persistence.audio;

import inaugural.soliloquy.io.persistence.audio.SoundsPlayingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundsPlayingHandlerTests {
    private final String SOUND_1 = randomString();
    private final String SOUND_2 = randomString();
    private final String SOUND_3 = randomString();

    private final String WRITTEN_VALUE =
            String.format("{\"soundDTOs\":[\"%s\",\"%s\",\"%s\"]}", SOUND_1, SOUND_2, SOUND_3);

    @Mock private SoundsPlaying mockSoundsPlaying;
    @Mock private Sound mockSound1;
    @Mock private Sound mockSound2;
    @Mock private Sound mockSound3;
    @Mock private TypeHandler<Sound> mockSoundHandler;

    private TypeHandler<SoundsPlaying> soundsPlayingHandler;

    @BeforeEach
    void setUp() {
        lenient().when(mockSoundsPlaying.representation()).thenReturn(listOf(
            mockSound1,
            mockSound2,
            mockSound3
        ));

        lenient().when(mockSoundHandler.write(mockSound1)).thenReturn(SOUND_1);
        lenient().when(mockSoundHandler.write(mockSound2)).thenReturn(SOUND_2);
        lenient().when(mockSoundHandler.write(mockSound3)).thenReturn(SOUND_3);
        lenient().when(mockSoundHandler.read(SOUND_1)).thenReturn(mockSound1);
        lenient().when(mockSoundHandler.read(SOUND_2)).thenReturn(mockSound2);
        lenient().when(mockSoundHandler.read(SOUND_3)).thenReturn(mockSound3);

        soundsPlayingHandler = new SoundsPlayingHandler(mockSoundHandler, mockSoundsPlaying);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SoundsPlayingHandler(null, mockSoundsPlaying));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundsPlayingHandler(mockSoundHandler, null));
    }

    @Test
    public void testWrite() {
        String writtenValue = soundsPlayingHandler.write(mockSoundsPlaying);

        assertEquals(WRITTEN_VALUE, writtenValue);
    }

    @Test
    public void testRead() {
        SoundsPlaying soundsPlaying = soundsPlayingHandler.read(WRITTEN_VALUE);

        assertNull(soundsPlaying);
        verify(mockSoundsPlaying, once()).removeSound(mockSound1);
        verify(mockSoundsPlaying, once()).removeSound(mockSound2);
        verify(mockSoundsPlaying, once()).removeSound(mockSound3);

        verify(mockSoundHandler, once()).read(SOUND_1);
        verify(mockSoundHandler, once()).read(SOUND_2);
        verify(mockSoundHandler, once()).read(SOUND_3);

        verify(mockSoundsPlaying, once()).registerSound(mockSound1);
        verify(mockSoundsPlaying, once()).registerSound(mockSound2);
        verify(mockSoundsPlaying, once()).registerSound(mockSound3);
    }

    @Test
    public void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> soundsPlayingHandler.read(null));
        assertThrows(IllegalArgumentException.class, () -> soundsPlayingHandler.read(""));
    }
}
