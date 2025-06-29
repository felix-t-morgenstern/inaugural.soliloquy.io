package inaugural.soliloquy.io.test.integration.audio.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.audio.Audio;
import soliloquy.specs.io.audio.entities.Sound;

import static inaugural.soliloquy.io.test.integration.audio.IntegrationTestsSetup.SOUND_TYPE_ID_FINITE;
import static inaugural.soliloquy.io.test.integration.audio.IntegrationTestsSetup.integrationTestAudio;
import static org.junit.jupiter.api.Assertions.*;

public class SoundImplTests {
    private Audio audio;
    private Sound sound;

    @BeforeEach
    public void setUp() {
        audio = integrationTestAudio();

        sound = audio.soundFactory().make(SOUND_TYPE_ID_FINITE);
    }

    @Test
    public void testIsPaused() {
        assertTrue(sound.isPaused());

        sound.play();

        assertFalse(sound.isPaused());

        sound.pause();

        assertTrue(sound.isPaused());

        sound.stop();

        assertFalse(sound.isPaused());
    }

    @Test
    public void testIsPlaying() {
        assertFalse(sound.isPlaying());

        sound.play();

        assertTrue(sound.isPlaying());

        sound.pause();

        assertFalse(sound.isPlaying());

        sound.play();

        assertTrue(sound.isPlaying());

        sound.stop();

        assertFalse(sound.isPlaying());
    }

    @Test
    public void testIsMuted() {
        assertFalse(sound.isMuted());

        sound.mute();

        assertTrue(sound.isMuted());

        sound.unmute();

        assertFalse(sound.isMuted());
    }

    @Test
    public void testIsStopped() {
        assertFalse(sound.isStopped());

        sound.play();

        assertFalse(sound.isStopped());

        sound.mute();

        assertFalse(sound.isStopped());

        sound.stop();

        assertTrue(sound.isStopped());
    }

    @Test
    public void testGetVolume() {
        assertEquals(1.0, sound.getVolume());

        sound.setVolume(0.5);

        assertEquals(0.5, sound.getVolume());

        sound.mute();

        assertEquals(0.5, sound.getVolume());
    }

    @Test
    public void testGetMillisecondLength() {
        var millisecondLength = sound.getMillisecondLength();

        assertEquals(208587, millisecondLength);
    }

    @Test
    public void testGetMillisecondPosition() throws InterruptedException {
        final var timeToWait = 1000;

        assertEquals(0, sound.getMillisecondPosition());

        sound.setVolume(0.0);
        sound.play();
        Thread.sleep(timeToWait);
        sound.pause();
        var msPosition = sound.getMillisecondPosition();
        // NB: When the Sound is not playing, this method should always return the same value.
        var msPosition2 = sound.getMillisecondPosition();

        // NB: At present, there is some delay between when _sound.pause() is called, and when
        // the Sound actually successfully pauses
        assertTrue(Math.abs(timeToWait - msPosition) <= 250);
        assertEquals(msPosition, msPosition2);
    }

    @Test
    public void testIsLooping() {
        sound.setVolume(0.0);

        assertFalse(sound.getIsLooping());

        sound.setIsLooping(true);

        assertTrue(sound.getIsLooping());
    }

    @Test
    public void testSetLoopingStopAndRestartMs() throws InterruptedException {
        final var stopMs = 2000;
        final var restartMs = 1000;

        sound.setLoopingStopMs(stopMs);
        sound.setLoopingRestartMs(restartMs);
        sound.setIsLooping(true);
        sound.mute();
        sound.play();

        // NB: I am aware that this test is not deterministic due to potential race conditions; I
        // am electing to test the side effects of this method in this way, because there are no
        // other ways to measure side effects.
        final var numberOfTestsToRun = 100;
        var numberOfTestsRan = 0;
        var numberOfLoopsRun = 0;
        var prevMsPosition = -1;
        while (numberOfTestsRan < numberOfTestsToRun) {
            var msPosition = sound.getMillisecondPosition();
            if (msPosition < prevMsPosition) {
                numberOfLoopsRun++;
            }
            if (numberOfLoopsRun > 0) {
                assertTrue(msPosition >= restartMs - 5,
                        "msPosition: " + msPosition + ", restartMs: " + restartMs + ", " +
                                "numberOfTestsRan: " + numberOfTestsRan);
            }
            assertTrue(msPosition < stopMs + 250,
                    "msPosition: " + msPosition + ", stopMs: " + stopMs + ", " +
                            "numberOfTestsRan: " + numberOfTestsRan);
            Thread.sleep(100);
            numberOfTestsRan++;
            prevMsPosition = msPosition;
        }
        assertTrue(numberOfLoopsRun >= 6);
    }

    @Test
    public void testSetLoopingStartOrStopInvalidValues() {
        final var soundLength = sound.getMillisecondLength();

        assertThrows(IllegalArgumentException.class, () -> sound.setLoopingStopMs(-1));
        assertThrows(IllegalArgumentException.class, () -> sound.setLoopingRestartMs(-1));
        assertThrows(IllegalArgumentException.class,
                () -> sound.setLoopingStopMs(soundLength + 1));
        assertThrows(IllegalArgumentException.class,
                () -> sound.setLoopingRestartMs(soundLength + 1));

        final var stopMs = 456;
        final var restartMs = 123;

        sound.setLoopingStopMs(stopMs);
        sound.setLoopingRestartMs(restartMs);

        assertThrows(IllegalArgumentException.class, () -> sound.setLoopingStopMs(restartMs));
        assertThrows(IllegalArgumentException.class, () -> sound.setLoopingRestartMs(stopMs));
    }

    @Test
    public void testEndOfSoundRemovesSoundFromSoundsPlaying() throws InterruptedException {
        sound.setVolume(0);
        var msLength = sound.getMillisecondLength();
        sound.setMillisecondPosition(msLength - 10);
        sound.play();
        Thread.sleep(3000);

        assertTrue(sound.isStopped());
        assertFalse(audio.soundsPlaying().isPlayingSound(sound.uuid()));
    }

    @Test
    public void testOperationsOnStoppedSound() {
        sound.stop();

        assertThrows(UnsupportedOperationException.class, () -> sound.play());
        assertThrows(UnsupportedOperationException.class, () -> sound.pause());
        assertThrows(UnsupportedOperationException.class, () -> sound.mute());
        assertThrows(UnsupportedOperationException.class, () -> sound.unmute());
        assertThrows(UnsupportedOperationException.class, () -> sound.getIsLooping());
        assertThrows(UnsupportedOperationException.class, () -> sound.setIsLooping(true));
        assertThrows(UnsupportedOperationException.class, () -> sound.getVolume());
        assertThrows(UnsupportedOperationException.class, () -> sound.setVolume(0));
        assertThrows(UnsupportedOperationException.class, () -> sound.getMillisecondPosition());
        assertThrows(UnsupportedOperationException.class, () -> sound.setLoopingStopMs(456));
        assertThrows(UnsupportedOperationException.class, () -> sound.setLoopingRestartMs(123));
    }
}
