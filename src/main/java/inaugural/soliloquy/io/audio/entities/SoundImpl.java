package inaugural.soliloquy.io.audio.entities;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundType;

import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.files.Files.getLocalFile;

public class SoundImpl implements Sound {
    private final UUID UUID;
    private final SoundType SOUND_TYPE;
    private final Consumer<Sound> PUBLISH_SOUND_STOPPED;

    private final Media MEDIA;
    private final MediaPlayer MEDIA_PLAYER;

    private boolean isPaused;
    private boolean isStopped;
    private boolean isMuted;
    private boolean isLooping;

    private int loopStopMs;
    private int loopRestartMs;

    private volatile boolean isReady;

    private int durationMs;

    private double volume;

    public SoundImpl(UUID uuid, SoundType soundType, Consumer<Sound> publishSoundStopped) {
        UUID = Check.ifNull(uuid, "uuid");
        SOUND_TYPE = Check.ifNull(soundType, "soundType");
        PUBLISH_SOUND_STOPPED = Check.ifNull(publishSoundStopped, "publishSoundStopped");

        new JFXPanel();
        var localFile = getLocalFile(soundType.relativePath());
        MEDIA = new Media(localFile.toURI().toString());
        MEDIA_PLAYER = new MediaPlayer(MEDIA);

        isPaused = true;
        isStopped = false;
        isMuted = false;
        isLooping = false;
        isReady = false;
        volume = 1.0;

        MEDIA_PLAYER.setOnReady(() ->
        {
            loopStopMs = durationMs = (int) MEDIA_PLAYER.getTotalDuration().toMillis();
            isReady = true;
            MEDIA_PLAYER
                    .setOnMarker(mediaMarkerEvent -> setMillisecondPosition(loopRestartMs + 1));
            MEDIA_PLAYER.setOnEndOfMedia(this::stop);
        });
        while (!isReady) {
            Thread.onSpinWait();
        }
    }

    @Override
    public UUID uuid() throws IllegalStateException {
        return UUID;
    }

    @Override
    public SoundType soundType() {
        return SOUND_TYPE;
    }

    @Override
    public void play() throws UnsupportedOperationException {
        throwWhenStopped("play");
        MEDIA_PLAYER.play();
        isPaused = false;
    }

    @Override
    public void pause() {
        throwWhenStopped("pause");
        MEDIA_PLAYER.pause();
        isPaused = true;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isPlaying() {
        return !isPaused && !isStopped;
    }

    @Override
    public void stop() throws UnsupportedOperationException {
        MEDIA_PLAYER.stop();
        MEDIA_PLAYER.dispose();
        isPaused = false;
        isStopped = true;
        isMuted = true;

        PUBLISH_SOUND_STOPPED.accept(this);
    }

    @Override
    public void mute() {
        throwWhenStopped("mute");
        MEDIA_PLAYER.setVolume(0.0);
        isMuted = true;
    }

    public void unmute() throws UnsupportedOperationException {
        throwWhenStopped("unmute");
        MEDIA_PLAYER.setVolume(volume);
        isMuted = false;
    }

    @Override
    public boolean isMuted() throws UnsupportedOperationException {
        throwWhenStopped("isMuted");
        return isMuted;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public double getVolume() throws UnsupportedOperationException {
        throwWhenStopped("getVolume");
        return volume;
    }

    @Override
    public void setVolume(double volume)
            throws IllegalArgumentException, UnsupportedOperationException {
        throwWhenStopped("setVolume");
        if (!isMuted) {
            MEDIA_PLAYER.setVolume(volume);
        }
        this.volume = volume;
    }

    @Override
    public int getMillisecondLength() {
        while (!isReady) {
            CheckedExceptionWrapper.sleep(10);
        }
        return durationMs;
    }

    @Override
    public int getMillisecondPosition() {
        throwWhenStopped("getMillisecondPosition");
        while (!isReady) {
            CheckedExceptionWrapper.sleep(10);
        }
        return (int) MEDIA_PLAYER.getCurrentTime().toMillis();
    }

    @Override
    public void setMillisecondPosition(int ms)
            throws IllegalArgumentException, UnsupportedOperationException {
        throwWhenStopped("setMillisecondPosition");
        MEDIA_PLAYER.seek(Duration.millis(ms));
    }

    @Override
    public boolean getIsLooping() throws UnsupportedOperationException {
        throwWhenStopped("getIsLooping");
        return isLooping;
    }

    @Override
    public void setIsLooping(boolean isLooping) throws UnsupportedOperationException {
        throwWhenStopped("setIsLooping");
        if (isLooping) {
            MEDIA.getMarkers().put("", new Duration(loopStopMs));
        }
        else {
            MEDIA_PLAYER.setOnEndOfMedia(this::stop);
        }
        this.isLooping = isLooping;
    }

    @Override
    public int getLoopingStopMs() throws UnsupportedOperationException {
        return loopStopMs;
    }

    @Override
    public void setLoopingStopMs(Integer stopMs) throws IllegalArgumentException {
        throwWhenStopped("setLoopingStopMs");
        if (stopMs == null) {
            stopMs = durationMs;
        }
        else if (stopMs > durationMs) {
            throw new IllegalArgumentException(
                    "SoundImpl.setLoopingStopMs: stopMs cannot exceed Sound duration");
        }
        else if (stopMs <= loopRestartMs) {
            throw new IllegalArgumentException(
                    "SoundImpl.setLoopingStopMs: stopMs cannot exceed restartMs");
        }
        loopStopMs = Check.ifNonNegative(stopMs, "stopMs");
        if (isLooping) {
            MEDIA.getMarkers().clear();
            MEDIA.getMarkers().put("", new Duration(loopStopMs));
        }
    }

    @Override
    public int getLoopingRestartMs() throws UnsupportedOperationException {
        return loopRestartMs;
    }

    @Override
    public void setLoopingRestartMs(int restartMs) throws IllegalArgumentException {
        throwWhenStopped("setLoopingRestartMs");
        if (restartMs > durationMs) {
            throw new IllegalArgumentException(
                    "SoundImpl.setLoopingRestartMs: restartMs cannot exceed Sound duration");
        }
        else if (restartMs >= loopStopMs) {
            throw new IllegalArgumentException(
                    "SoundImpl.setLoopingStopMs: restartMs cannot exceed stopMs");
        }
        loopRestartMs = Check.ifNonNegative(restartMs, "restartMs");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Sound sound)) {
            return false;
        }
        return sound.uuid().equals(UUID);
    }

    private void throwWhenStopped(String methodName) {
        if (isStopped) {
            throw new UnsupportedOperationException("Sound." + methodName +
                    ": Sound has already been stopped");
        }
    }
}
