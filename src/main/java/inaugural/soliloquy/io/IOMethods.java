package inaugural.soliloquy.io;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.UUID;

public class IOMethods {
    private final SoundsPlaying SOUNDS_PLAYING;
    private final SoundFactory SOUND_FACTORY;

    public IOMethods(SoundsPlaying soundsPlaying,
                     SoundFactory soundFactory) {
        SOUNDS_PLAYING = Check.ifNull(soundsPlaying, "soundsPlaying");
        SOUND_FACTORY = Check.ifNull(soundFactory, "soundFactory");
    }

    public Sound makeSound(String soundId) {
        return SOUND_FACTORY.make(soundId);
    }

    public Sound playSound(String soundId) {
        var sound = makeSound(soundId);
        sound.play();
        return sound;
    }

    public void pauseSound(UUID sound) {
        SOUNDS_PLAYING.getSound(sound).pause();
    }

    public void unpauseSound(UUID sound) {
        SOUNDS_PLAYING.getSound(sound).play();
    }
}
