package inaugural.soliloquy.io.test.unit.persistence.audio;

import com.google.gson.JsonSyntaxException;
import inaugural.soliloquy.io.persistence.audio.SoundHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.UUID;
import java.util.function.BiFunction;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundHandlerTests {
    private final UUID UUID = randomUUID();
    private final String SOUND_TYPE_ID = randomString();
    private final boolean IS_PAUSED = randomBoolean();
    private final boolean IS_MUTED = randomBoolean();
    private final double VOLUME = randomDouble();
    private final int MS_POSITION = randomInt();
    private final boolean IS_LOOPING = randomBoolean();
    private final int LOOPING_STOP_MS = randomInt();
    private final int LOOPING_RESTART_MS = randomInt();

    @Mock private SoundType mockSoundType;
    @Mock private Sound mockSound;
    @Mock private BiFunction<String, UUID, Sound> mockSoundFactory;

    private TypeHandler<Sound> soundHandler;

    private final String DATA = String.format(
            "{\"uuid\":\"%s\",\"type\":\"%s\",\"paused\":%b,\"muted\":%b,\"vol\":%s,\"msPos\":%d," +
                    "\"looping\":%b,\"loopingStopMs\":%d,\"loopingRestartMs\":%d}",
            UUID, SOUND_TYPE_ID, IS_PAUSED, IS_MUTED, VOLUME, MS_POSITION, IS_LOOPING,
            LOOPING_STOP_MS, LOOPING_RESTART_MS);

    @BeforeEach
    public void setUp() {
        lenient().when(mockSoundType.id()).thenReturn(SOUND_TYPE_ID);

        lenient().when(mockSound.uuid()).thenReturn(UUID);
        lenient().when(mockSound.soundType()).thenReturn(mockSoundType);
        lenient().when(mockSound.isPaused()).thenReturn(IS_PAUSED);
        lenient().when(mockSound.isMuted()).thenReturn(IS_MUTED);
        lenient().when(mockSound.getVolume()).thenReturn(VOLUME);
        lenient().when(mockSound.getMillisecondPosition()).thenReturn(MS_POSITION);
        lenient().when(mockSound.getIsLooping()).thenReturn(IS_LOOPING);
        lenient().when(mockSound.getLoopingStopMs()).thenReturn(LOOPING_STOP_MS);
        lenient().when(mockSound.getLoopingRestartMs()).thenReturn(LOOPING_RESTART_MS);

        lenient().when(mockSoundFactory.apply(anyString(), any())).thenReturn(mockSound);

        soundHandler = new SoundHandler(mockSoundFactory);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new SoundHandler(null));
    }

    @Test
    public void testWrite() {
        var writtenValue = soundHandler.write(mockSound);

        assertEquals(DATA, writtenValue);
    }

    @Test
    public void testWriteInvalid() {
        assertThrows(IllegalArgumentException.class, () -> soundHandler.write(null));
    }

    @Test
    public void testRead() {
        var readValue = soundHandler.read(DATA);

        assertNotNull(readValue);
        assertSame(mockSound, readValue);
        verify(mockSoundFactory, once()).apply(eq(SOUND_TYPE_ID), eq(UUID));
        verify(mockSound, once()).setIsLooping(IS_LOOPING);
        verify(mockSound, once()).setVolume(VOLUME);
        if (IS_MUTED) {
            verify(mockSound, once()).mute();
        }
        else {
            verify(mockSound, once()).unmute();
        }
        verify(mockSound, once()).setMillisecondPosition(MS_POSITION);
        if (IS_PAUSED) {
            verify(mockSound, once()).pause();
        }
        else {
            verify(mockSound, once()).play();
        }
    }

    @Test
    public void testReadInvalid() {
        assertThrows(IllegalArgumentException.class, () -> soundHandler.read(null));
        assertThrows(IllegalArgumentException.class, () -> soundHandler.read(""));
        assertThrows(JsonSyntaxException.class, () ->
                soundHandler
                        .read("{\"soundTypeId\":\"SoundTypeId\",\"isPaused\":true," +
                                "\"isMuted\":true,\"volume\":0.5,\"msPosition\":100," +
                                "\"isLooping\":true"));
    }
}
