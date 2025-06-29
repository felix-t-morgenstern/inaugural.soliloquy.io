package inaugural.soliloquy.io.test.unit.audio;

import inaugural.soliloquy.io.audio.AudioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.Audio;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.function.BiFunction;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AudioImplTests {
    private final String SOUND_TYPE_ID = randomString();

    @Mock SoundType mockSoundType;
    @Mock SoundsPlaying mockSoundsPlaying;
    @Mock SoundFactory mockSoundFactory;
    @Mock BiFunction<Function<String, SoundType>, SoundsPlaying, SoundFactory>
            mockSoundFactoryFactory;

    private Function<String, SoundType> soundFactoryInputGetSoundType;
    private SoundsPlaying soundFactoryInputSoundsPlaying;

    private Audio audio;

    @BeforeEach
    public void setUp() {
        lenient().when(mockSoundType.id()).thenReturn(SOUND_TYPE_ID);
        lenient().when(mockSoundFactoryFactory.apply(any(), any())).thenAnswer(invocation -> {
            soundFactoryInputGetSoundType = invocation.getArgument(0);
            soundFactoryInputSoundsPlaying = invocation.getArgument(1);
            return mockSoundFactory;
        });

        audio = new AudioImpl(mockSoundsPlaying, mockSoundFactoryFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new AudioImpl(null, mockSoundFactoryFactory));
        assertThrows(IllegalArgumentException.class, () -> new AudioImpl(mockSoundsPlaying, null));
    }

    @Test
    public void testSoundsPlaying() {
        assertSame(mockSoundsPlaying, audio.soundsPlaying());
    }

    @Test
    public void testAddAndGetSoundType() {
        ((AudioImpl) audio).addSoundType(mockSoundType);

        var soundType = audio.getSoundType(SOUND_TYPE_ID);

        assertSame(mockSoundType, soundType);
    }

    @Test
    public void testGetSoundTypeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> audio.getSoundType(null));
        assertThrows(IllegalArgumentException.class, () -> audio.getSoundType(""));
        assertThrows(IllegalArgumentException.class, () -> audio.getSoundType("invalid id"));
    }

    @Test
    public void testSoundTypes() {
        ((AudioImpl) audio).addSoundType(mockSoundType);

        var soundTypes = audio.soundTypes();

        assertEquals(setOf(mockSoundType), soundTypes);
    }

    @Test
    public void testSoundFactory() {
        assertSame(mockSoundFactory, audio.soundFactory());
    }

    @Test
    public void testGetSoundTypeAndSoundsPlayingPassedIntoSoundFactoryInConstructor() {
        // A SoundType needs to be added, to verify that the SoundType retrieval operation
        // exposed by Audio is the same fed into the soundFactoryFactory, since
        // Audio#getSoundType hides the retrieval operation's method reference from the end user
        // by design
        ((AudioImpl) audio).addSoundType(mockSoundType);

        assertEquals(mockSoundsPlaying, soundFactoryInputSoundsPlaying);
        assertEquals(audio.getSoundType(SOUND_TYPE_ID),
                soundFactoryInputGetSoundType.apply(SOUND_TYPE_ID));
        verify(mockSoundFactoryFactory, once()).apply(any(), any());
    }
}
