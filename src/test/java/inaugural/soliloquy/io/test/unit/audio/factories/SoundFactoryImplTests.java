package inaugural.soliloquy.io.test.unit.audio.factories;

import inaugural.soliloquy.io.audio.entities.SoundImpl;
import inaugural.soliloquy.io.audio.factories.SoundFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.entities.SoundsPlaying;

import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static inaugural.soliloquy.tools.testing.Mock.LookupAndEntitiesWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundFactoryImplTests {
    private final String SOUND_TYPE_ID = randomString();
    private final LookupAndEntitiesWithId<SoundType> MOCK_SOUND_TYPE_AND_LOOKUP =
            generateMockLookupFunctionWithId(SoundType.class, SOUND_TYPE_ID);
    private final Function<String, SoundType> MOCK_GET_SOUND_TYPE =
            MOCK_SOUND_TYPE_AND_LOOKUP.lookup;
    private final SoundType MOCK_SOUND_TYPE = MOCK_SOUND_TYPE_AND_LOOKUP.entities.getFirst();
    private final int DEFAULT_LOOPING_RESTART_MS = 123;
    private final int DEFAULT_LOOPING_STOP_MS = 456;
    private final String RELATIVE_PATH =
            "\\src\\test\\resources\\sounds\\Kevin_MacLeod_-_Living_Voyage.mp3";

    @Mock private SoundsPlaying mockSoundsPlaying;

    private SoundFactoryImpl soundFactory;

    @BeforeEach
    public void setUp() {
        lenient().when(MOCK_SOUND_TYPE.relativePath()).thenReturn(RELATIVE_PATH);
        lenient().when(MOCK_SOUND_TYPE.defaultLoopingStopMs()).thenReturn(DEFAULT_LOOPING_STOP_MS);
        lenient().when(MOCK_SOUND_TYPE.defaultLoopingRestartMs()).thenReturn(DEFAULT_LOOPING_RESTART_MS);

        mockSoundsPlaying = mock(SoundsPlaying.class);

        soundFactory = new SoundFactoryImpl(MOCK_GET_SOUND_TYPE, mockSoundsPlaying);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SoundFactoryImpl(null, mockSoundsPlaying));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundFactoryImpl(MOCK_GET_SOUND_TYPE, null));
    }

    @Test
    public void testMake() {
        var sound = soundFactory.make(SOUND_TYPE_ID);

        assertNotNull(sound);
        assertTrue(sound instanceof SoundImpl);
        assertSame(MOCK_SOUND_TYPE, sound.soundType());
        assertEquals(DEFAULT_LOOPING_RESTART_MS, sound.getLoopingRestartMs());
        assertEquals(DEFAULT_LOOPING_STOP_MS, sound.getLoopingStopMs());
        verify(MOCK_GET_SOUND_TYPE, once()).apply(SOUND_TYPE_ID);
        verify(mockSoundsPlaying, once()).registerSound(sound);
    }

    @Test
    public void testMakeWithUuid() {
        var uuid = UUID.randomUUID();

        var sound = soundFactory.make(SOUND_TYPE_ID, uuid);

        assertEquals(uuid, sound.uuid());
    }

    @Test
    public void testMakeWithInvalidSoundTypeId() {
        assertThrows(IllegalArgumentException.class, () -> soundFactory.make(null));
        assertThrows(IllegalArgumentException.class,
                () -> soundFactory.make("InvalidSoundTypeId!"));
    }

    @Test
    public void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> soundFactory.make(null));
        assertThrows(IllegalArgumentException.class, () -> soundFactory.make(""));
        assertThrows(IllegalArgumentException.class, () -> soundFactory.make(RELATIVE_PATH, null));
        assertThrows(IllegalArgumentException.class,
                () -> soundFactory.make(null, UUID.randomUUID()));
        assertThrows(IllegalArgumentException.class,
                () -> soundFactory.make("", UUID.randomUUID()));
    }
}
