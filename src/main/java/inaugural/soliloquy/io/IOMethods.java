package inaugural.soliloquy.io;

import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.UUID;

public class IOMethods {
    private final SoundsPlaying SOUNDS_PLAYING;
    private final SoundFactory SOUND_FACTORY;

    public IOMethods(SoundsPlaying soundsPlaying,
                     SoundFactory soundFactory) {
        SOUNDS_PLAYING = soundsPlaying;
        SOUND_FACTORY = soundFactory;
    }

    public UUID makeSound(String soundId) {
        var sound = SOUND_FACTORY.make(soundId);
        return sound.uuid();
    }

    public UUID playSound(String soundId) {
        var sound = SOUND_FACTORY.make(soundId);
        sound.play();
        return sound.uuid();
    }

    public void pauseSound(UUID sound) {
        SOUNDS_PLAYING.getSound(sound).pause();
    }

    public void unpauseSound(UUID sound) {
        SOUNDS_PLAYING.getSound(sound).play();
    }
}
