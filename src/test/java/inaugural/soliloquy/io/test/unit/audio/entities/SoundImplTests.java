package inaugural.soliloquy.io.test.unit.audio.entities;

import inaugural.soliloquy.io.audio.entities.SoundImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// The test suite here uses timers to verify behavior, which inflate test time. Until this suite
// is refactored to be more expedient, it should only be run when the class in question is changed.
//@Disabled
@ExtendWith(MockitoExtension.class)
public class SoundImplTests {
    private final UUID UUID = randomUUID();

    @Mock private SoundType mockSoundType;
    @Mock private ProviderAtTime<Float> mockVolProvider;
    @Mock private Consumer<Sound> mockPublishSoundStopped;

    private Sound sound;

    @BeforeEach
    void setUp() {
        var relativePath = "\\src\\test\\resources\\sounds\\Kevin_MacLeod_-_Living_Voyage.mp3";
        lenient().when(mockSoundType.relativePath()).thenReturn(relativePath);

        sound = new SoundImpl(UUID, mockSoundType, mockVolProvider, mockPublishSoundStopped);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SoundImpl(null, mockSoundType, mockVolProvider, mockPublishSoundStopped));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundImpl(UUID, null, mockVolProvider, mockPublishSoundStopped));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundImpl(UUID, mockSoundType, null, mockPublishSoundStopped));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundImpl(UUID, mockSoundType, mockVolProvider, null));
    }

    @Test
    public void testId() {
        assertSame(sound.uuid(), UUID);
    }

    @Test
    public void testEquals() {
        var equalSound =
                new SoundImpl(UUID, mockSoundType, mockVolProvider, mockPublishSoundStopped);
        var unequalSound = new SoundImpl(randomUUID(), mockSoundType, mockVolProvider,
                mockPublishSoundStopped);
        assertEquals(equalSound, sound);
        assertNotEquals(unequalSound, sound);
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
    public void testGetAndSetVolumeProvider() {
        assertSame(mockVolProvider, sound.getVolumeProvider());

        @SuppressWarnings("unchecked") ProviderAtTime<Float> newMockProvider =
                mock(ProviderAtTime.class);

        sound.setVolumeProvider(newMockProvider);

        assertSame(newMockProvider, sound.getVolumeProvider());
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

        verify(mockPublishSoundStopped, once()).accept(sound);
    }

    @Test
    public void testEndOfSoundRemovesSoundFromSoundsPlaying() throws InterruptedException {
        var msLength = sound.getMillisecondLength();
        sound.setMillisecondPosition(msLength - 10);
        sound.play();
        Thread.sleep(3000);

        assertTrue(sound.isStopped());
        verify(mockPublishSoundStopped, once()).accept(sound);
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
        assertThrows(UnsupportedOperationException.class, () -> sound.getVolumeProvider());
        assertThrows(UnsupportedOperationException.class,
                () -> sound.setVolumeProvider(mockVolProvider));
        assertThrows(UnsupportedOperationException.class, () -> sound.getMillisecondPosition());
        assertThrows(UnsupportedOperationException.class, () -> sound.setLoopingStopMs(456));
        assertThrows(UnsupportedOperationException.class, () -> sound.setLoopingRestartMs(123));
    }
}
