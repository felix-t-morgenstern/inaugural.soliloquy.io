package inaugural.soliloquy.io.audio.factories;

import inaugural.soliloquy.io.audio.entities.SoundImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.UUID;
import java.util.function.Function;

public class SoundFactoryImpl implements SoundFactory {
    private final Function<String, SoundType> GET_SOUND_TYPE;
    private final SoundsPlaying SOUNDS_PLAYING;

    public SoundFactoryImpl(Function<String, SoundType> getSoundType, SoundsPlaying soundsPlaying) {
        GET_SOUND_TYPE = Check.ifNull(getSoundType, "getSoundType");
        SOUNDS_PLAYING = Check.ifNull(soundsPlaying, "soundsPlaying");
    }

    public Sound make(String soundTypeId) throws IllegalArgumentException {
        return make(soundTypeId, UUID.randomUUID());
    }

    @Override
    public Sound make(String soundTypeId, UUID uuid) throws IllegalArgumentException {
        Check.ifNullOrEmpty(soundTypeId, "soundTypeId");
        Check.ifNull(uuid, "uuid");
        var soundType = GET_SOUND_TYPE.apply(soundTypeId);
        if (soundType == null) {
            throw new IllegalArgumentException(
                    "SoundFactoryImpl.make: soundTypeId must correspond to a valid (i.e. " +
                            "registered) sound type id");
        }

        var sound = new SoundImpl(uuid, soundType, SOUNDS_PLAYING::removeSound);

        if (soundType.defaultLoopingStopMs() != null) {
            sound.setIsLooping(true);
            sound.setLoopingStopMs(soundType.defaultLoopingStopMs());
        }
        if (soundType.defaultLoopingRestartMs() != null) {
            sound.setLoopingRestartMs(soundType.defaultLoopingRestartMs());
        }
        SOUNDS_PLAYING.registerSound(sound);
        return sound;
    }
}
