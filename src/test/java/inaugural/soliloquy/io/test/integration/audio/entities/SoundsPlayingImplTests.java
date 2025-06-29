package inaugural.soliloquy.io.test.integration.audio.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.audio.Audio;
import soliloquy.specs.io.audio.entities.Sound;

import static inaugural.soliloquy.io.test.integration.audio.IntegrationTestsSetup.*;
import static org.junit.jupiter.api.Assertions.*;

public class SoundsPlayingImplTests {
    private Audio audio;
    private Sound soundFinite;
    private Sound soundLooping;

    @BeforeEach
    void setUp() {
        audio = integrationTestAudio();

        soundFinite = audio.soundFactory().make(SOUND_TYPE_ID_FINITE);
        soundLooping = audio.soundFactory().make(SOUND_TYPE_ID_LOOPING);
    }

    @Test
    public void testRegisterAndRemoveSound() {
        audio.soundsPlaying().registerSound(soundFinite);

        assertTrue(audio.soundsPlaying().isPlayingSound(soundFinite.uuid()));

        audio.soundsPlaying().removeSound(soundFinite);

        assertFalse(audio.soundsPlaying().isPlayingSound(soundFinite.uuid()));
    }

    @Test
    public void testRepresentation() {
        var allSoundsPlaying1 = audio.soundsPlaying().representation();
        var allSoundsPlaying2 = audio.soundsPlaying().representation();

        assertNotSame(allSoundsPlaying1, allSoundsPlaying2);
        assertEquals(2, allSoundsPlaying1.size());
        assertTrue(allSoundsPlaying1.contains(soundFinite));
        assertTrue(allSoundsPlaying1.contains(soundLooping));
    }

    @Test
    public void testGetSoundWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> audio.soundsPlaying().getSound(null));
    }

    @Test
    public void testIsPlayingSoundWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> audio.soundsPlaying().isPlayingSound(null));
    }

    @Test
    public void testGetSound() {
        var soundPlaying = audio.soundsPlaying().getSound(soundFinite.uuid());

        assertSame(soundFinite, soundPlaying);
    }

    @Test
    public void testRegisterAndRemoveWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> audio.soundsPlaying().registerSound(null));
        assertThrows(IllegalArgumentException.class, () -> audio.soundsPlaying().removeSound(null));
    }
}
