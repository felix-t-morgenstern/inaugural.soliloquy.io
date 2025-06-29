package inaugural.soliloquy.io.audio;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.audio.Audio;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.entities.SoundsPlaying;
import soliloquy.specs.io.audio.factories.SoundFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;

public class AudioImpl implements Audio {
    private final Map<String, SoundType> SOUND_TYPES;
    private final SoundsPlaying SOUNDS_PLAYING;
    private final SoundFactory SOUND_FACTORY;

    public AudioImpl(SoundsPlaying soundsPlaying,
                     BiFunction<Function<String, SoundType>, SoundsPlaying, SoundFactory> soundFactoryFactory) {
        SOUND_TYPES = mapOf();
        SOUNDS_PLAYING = Check.ifNull(soundsPlaying, "soundsPlaying");
        SOUND_FACTORY = Check.ifNull(soundFactoryFactory, "soundFactoryFactory")
                .apply(SOUND_TYPES::get, SOUNDS_PLAYING);
    }

    @Override
    public SoundsPlaying soundsPlaying() {
        return SOUNDS_PLAYING;
    }

    @Override
    public SoundFactory soundFactory() {
        return SOUND_FACTORY;
    }

    @Override
    public SoundType getSoundType(String soundTypeId) throws IllegalArgumentException {
        var output = SOUND_TYPES.get(Check.ifNullOrEmpty(soundTypeId, "soundTypeId"));
        if (output == null) {
            throw new IllegalArgumentException(
                    "AudioImpl.getSoundType: soundTypeId (" + soundTypeId +
                            ") does not correspond to a valid SoundType");
        }

        return output;
    }

    @Override
    public Set<SoundType> soundTypes() {
        return setOf(SOUND_TYPES.values());
    }

    // This method is exposed only for AudioLoader#loadFromDirectory; it is unexposed on the
    // interface, since it should not be called for any other reason.
    public void addSoundType(SoundType soundType) {
        SOUND_TYPES.put(soundType.id(), soundType);
    }
}
