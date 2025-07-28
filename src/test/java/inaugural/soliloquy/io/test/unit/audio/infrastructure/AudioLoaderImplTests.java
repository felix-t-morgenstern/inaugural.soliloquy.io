package inaugural.soliloquy.io.test.unit.audio.infrastructure;

import inaugural.soliloquy.io.audio.infrastructure.AudioLoaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.infrastructure.AudioLoader;

import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AudioLoaderImplTests {
    private final Map<String, String> IDS_FOR_FILENAMES = mapOf();
    private final Map<String, Integer> DEFAULT_LOOPING_STOP_MS_FOR_IDS = mapOf();
    private final Map<String, Integer> DEFAULT_LOOPING_RESTART_MS_FOR_IDS = mapOf();

    private final String DIR_RELATIVE_PATH = "\\src\\test\\resources\\sounds\\";

    private final String ID_1 = randomString();
    private final String RELATIVE_PATH_1 =
            "exit-the-premises-by-kevin-macleod-from-filmmusic-io.mp3";
    private final Integer DEFAULT_LOOPING_STOP_MS_1 = 14850;
    private final Integer DEFAULT_LOOPING_RESTART_MS_1 = 7520;

    private final String ID_2 = randomString();
    private final String RELATIVE_PATH_2 = "Kevin_MacLeod_-_Living_Voyage.mp3";
    private final Integer DEFAULT_LOOPING_STOP_MS_2 = null;
    private final Integer DEFAULT_LOOPING_RESTART_MS_2 = null;

    @Mock private Consumer<SoundType> mockAddSoundType;
    @Mock private AudioLoaderImpl.QuadFunction<String, String, Integer, Integer, SoundType>
            mockSoundTypeFactory;
    @Mock private SoundType mockSoundType1;
    @Mock private SoundType mockSoundType2;

    private AudioLoader audioLoader;

    @SuppressWarnings("ConstantConditions")
    @BeforeEach
    void setUp() {
        IDS_FOR_FILENAMES.put(RELATIVE_PATH_1, ID_1);
        DEFAULT_LOOPING_STOP_MS_FOR_IDS.put(ID_1, DEFAULT_LOOPING_STOP_MS_1);
        DEFAULT_LOOPING_RESTART_MS_FOR_IDS.put(ID_1, DEFAULT_LOOPING_RESTART_MS_1);

        IDS_FOR_FILENAMES.put(RELATIVE_PATH_2, ID_2);
        DEFAULT_LOOPING_STOP_MS_FOR_IDS.put(ID_2, DEFAULT_LOOPING_STOP_MS_2);
        DEFAULT_LOOPING_RESTART_MS_FOR_IDS.put(ID_2, DEFAULT_LOOPING_RESTART_MS_2);

        audioLoader =
                new AudioLoaderImpl(mockAddSoundType, mockSoundTypeFactory, setOf("mp3", "wav"));
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new AudioLoaderImpl(null, mockSoundTypeFactory, setOf()));
        assertThrows(IllegalArgumentException.class,
                () -> new AudioLoaderImpl(mockAddSoundType, null, setOf()));
        assertThrows(IllegalArgumentException.class,
                () -> new AudioLoaderImpl(mockAddSoundType, mockSoundTypeFactory, null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testLoadFromDirectory() {
        when(mockSoundTypeFactory.apply(anyString(), anyString(), any(), any()))
                .thenReturn(mockSoundType1)
                .thenReturn(mockSoundType2);

        audioLoader.loadFromDirectory(DIR_RELATIVE_PATH, IDS_FOR_FILENAMES,
                DEFAULT_LOOPING_STOP_MS_FOR_IDS, DEFAULT_LOOPING_RESTART_MS_FOR_IDS);

        verify(mockAddSoundType, times(2)).accept(any());
        verify(mockAddSoundType, once()).accept(mockSoundType1);
        verify(mockAddSoundType, once()).accept(mockSoundType2);

        verify(mockSoundTypeFactory, once()).apply(ID_1, DIR_RELATIVE_PATH + RELATIVE_PATH_1,
                DEFAULT_LOOPING_STOP_MS_1, DEFAULT_LOOPING_RESTART_MS_1);
        verify(mockSoundTypeFactory, once()).apply(ID_2, DIR_RELATIVE_PATH + RELATIVE_PATH_2,
                DEFAULT_LOOPING_STOP_MS_2, DEFAULT_LOOPING_RESTART_MS_2);
    }

    @Test
    public void testLoadFromDirectoryWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> audioLoader.loadFromDirectory(null, IDS_FOR_FILENAMES,
                        DEFAULT_LOOPING_STOP_MS_FOR_IDS, DEFAULT_LOOPING_RESTART_MS_FOR_IDS));
        assertThrows(IllegalArgumentException.class,
                () -> audioLoader.loadFromDirectory("", IDS_FOR_FILENAMES,
                        DEFAULT_LOOPING_STOP_MS_FOR_IDS, DEFAULT_LOOPING_RESTART_MS_FOR_IDS));
        assertThrows(IllegalArgumentException.class,
                () -> audioLoader.loadFromDirectory(DIR_RELATIVE_PATH, null,
                        DEFAULT_LOOPING_STOP_MS_FOR_IDS, DEFAULT_LOOPING_RESTART_MS_FOR_IDS));
        assertThrows(IllegalArgumentException.class,
                () -> audioLoader.loadFromDirectory(DIR_RELATIVE_PATH, IDS_FOR_FILENAMES, null,
                        DEFAULT_LOOPING_RESTART_MS_FOR_IDS));
        assertThrows(IllegalArgumentException.class,
                () -> audioLoader.loadFromDirectory(DIR_RELATIVE_PATH, IDS_FOR_FILENAMES,
                        DEFAULT_LOOPING_STOP_MS_FOR_IDS, null));
    }
}
