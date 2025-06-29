package inaugural.soliloquy.io.test.unit.audio.entities;

import inaugural.soliloquy.io.audio.entities.SoundsPlayingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.Sound;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SoundsPlayingImplTests {
    private final UUID MOCK_SOUND_1_UUID = UUID.randomUUID();
    private final UUID MOCK_SOUND_2_UUID = UUID.randomUUID();
    private final UUID MOCK_SOUND_3_UUID = UUID.randomUUID();

    @Mock private Sound mockSound1;
    @Mock private Sound mockSound2;
    @Mock private Sound mockSound3;

    private SoundsPlayingImpl soundsPlaying;

    @BeforeEach
    void setUp() {
        lenient().when(mockSound1.uuid()).thenReturn(MOCK_SOUND_1_UUID);
        lenient().when(mockSound2.uuid()).thenReturn(MOCK_SOUND_2_UUID);
        lenient().when(mockSound3.uuid()).thenReturn(MOCK_SOUND_3_UUID);

        soundsPlaying = new SoundsPlayingImpl();
    }

    @Test
    public void testRegisterAndRemoveSound() {
        soundsPlaying.registerSound(mockSound1);

        assertTrue(soundsPlaying.isPlayingSound(MOCK_SOUND_1_UUID));

        soundsPlaying.removeSound(mockSound1);

        assertFalse(soundsPlaying.isPlayingSound(MOCK_SOUND_1_UUID));
    }

    @Test
    public void testIterator() {
        soundsPlaying.registerSound(mockSound1);
        soundsPlaying.registerSound(mockSound2);
        soundsPlaying.registerSound(mockSound3);

        var fromIterator = soundsPlaying.representation();

        assertEquals(3, fromIterator.size());
        assertTrue(fromIterator.contains(mockSound1));
        assertTrue(fromIterator.contains(mockSound2));
        assertTrue(fromIterator.contains(mockSound3));
    }

    @Test
    public void testRepresentation() {
        soundsPlaying.registerSound(mockSound1);

        var allSoundsPlaying1 = soundsPlaying.representation();
        var allSoundsPlaying2 = soundsPlaying.representation();

        assertNotSame(allSoundsPlaying1, allSoundsPlaying2);
        assertEquals(1, allSoundsPlaying1.size());
        assertTrue(allSoundsPlaying1.contains(mockSound1));
    }

    @Test
    public void testGetSoundWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> soundsPlaying.getSound(null));
    }

    @Test
    public void testIsPlayingSoundWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> soundsPlaying.isPlayingSound(null));
    }

    @Test
    public void testGetSound() {
        soundsPlaying.registerSound(mockSound1);

        var sound = soundsPlaying.getSound(MOCK_SOUND_1_UUID);

        assertSame(sound, mockSound1);
    }

    @Test
    public void testRegisterAndRemoveWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> soundsPlaying.registerSound(null));
        assertThrows(IllegalArgumentException.class, () -> soundsPlaying.removeSound(null));
    }
}
