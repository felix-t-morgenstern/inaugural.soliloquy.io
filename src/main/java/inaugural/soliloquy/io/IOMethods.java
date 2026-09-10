package inaugural.soliloquy.io;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class IOMethods {
    private final SoundsPlaying SOUNDS_PLAYING;
    private final SoundFactory SOUND_FACTORY;

    public IOMethods(SoundsPlaying soundsPlaying,
                     SoundFactory soundFactory) {
        SOUNDS_PLAYING = Check.ifNull(soundsPlaying, "soundsPlaying");
        SOUND_FACTORY = Check.ifNull(soundFactory, "soundFactory");
    }

    public Sound makeSound(String soundId, ProviderAtTime<Float> volumeProvider) {
        return SOUND_FACTORY.make(soundId, volumeProvider);
    }

    public Sound playSound(String soundId, ProviderAtTime<Float> volumeProvider) {
        var sound = makeSound(soundId, volumeProvider);
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
