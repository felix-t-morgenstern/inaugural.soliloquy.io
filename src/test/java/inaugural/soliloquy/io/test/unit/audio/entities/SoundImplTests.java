package inaugural.soliloquy.io.test.unit.audio.entities;

import inaugural.soliloquy.io.audio.entities.SoundImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundType;

import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundImplTests {
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private SoundType soundType;
    @Mock private Consumer<Sound> publishSoundStopped;

    private Sound sound;

    @BeforeEach
    void setUp() {
        var relativePath = "\\src\\test\\resources\\sounds\\Kevin_MacLeod_-_Living_Voyage.mp3";
        lenient().when(soundType.relativePath()).thenReturn(relativePath);

        sound = new SoundImpl(UUID, soundType, publishSoundStopped);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new SoundImpl(null, soundType, publishSoundStopped));
        assertThrows(IllegalArgumentException.class, () -> new SoundImpl(UUID, null, publishSoundStopped));
        assertThrows(IllegalArgumentException.class, () -> new SoundImpl(UUID, soundType, null));
    }

    @Test
    public void testId() {
        assertSame(sound.uuid(), UUID);
    }

    @Test
    public void testEquals() {
        Sound sound2 = new SoundImpl(UUID, soundType, publishSoundStopped);
        assertEquals(sound, sound2);
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
    public void testGetAndSetLoopingStopAndRestartMs() {
        final var stopMs = 2000;
        final var restartMs = 1000;

        sound.setLoopingStopMs(stopMs);
        sound.setLoopingRestartMs(restartMs);

        assertEquals(stopMs, sound.getLoopingStopMs());
        assertEquals(restartMs, sound.getLoopingRestartMs());
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
    public void testStopRemovesSoundFromSoundsPlaying() {
        sound.stop();

        verify(publishSoundStopped, once()).accept(sound);
    }

    @Test
    public void testEndOfSoundRemovesSoundFromSoundsPlaying() throws InterruptedException {
        sound.setVolume(0);
        var msLength = sound.getMillisecondLength();
        sound.setMillisecondPosition(msLength - 10);
        sound.play();
        Thread.sleep(3000);

        assertTrue(sound.isStopped());
        verify(publishSoundStopped, once()).accept(sound);
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
