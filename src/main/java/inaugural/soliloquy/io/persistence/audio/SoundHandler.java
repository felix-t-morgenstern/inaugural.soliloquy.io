package inaugural.soliloquy.io.persistence.audio;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import org.apache.commons.lang3.function.TriFunction;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class SoundHandler extends AbstractTypeHandler<Sound> {
    private final TriFunction<String, ProviderAtTime<Float>, UUID, Sound> SOUND_FACTORY;
    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;

    public SoundHandler(TriFunction<String, ProviderAtTime<Float>, UUID, Sound> soundFactory,
                        @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler) {
        SOUND_FACTORY = Check.ifNull(soundFactory, "soundFactory");
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Sound read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        var dto = JSON.fromJson(data, SoundDTO.class);
        var volumeProvider = PROVIDER_HANDLER.read(dto.vol);
        var sound = SOUND_FACTORY.apply(dto.type, volumeProvider, UUID.fromString(dto.uuid));
        sound.setIsLooping(dto.looping);
        if (dto.muted) {
            sound.mute();
        }
        else {
            sound.unmute();
        }
        sound.setMillisecondPosition(dto.msPos);
        if (dto.paused) {
            sound.pause();
        }
        else {
            sound.play();
        }
        if (dto.loopingStopMs != null) {
            sound.setLoopingStopMs(dto.loopingStopMs);
        }
        if (dto.loopingRestartMs != null) {
            sound.setLoopingRestartMs(dto.loopingRestartMs);
        }
        return sound;
    }

    @Override
    public String write(Sound sound) {
        Check.ifNull(sound, "sound");

        var soundDTO = new SoundDTO();
        soundDTO.uuid = sound.uuid().toString();
        soundDTO.type = sound.soundType().id();
        soundDTO.paused = sound.isPaused();
        soundDTO.muted = sound.isMuted();
        soundDTO.vol = PROVIDER_HANDLER.write(sound.getVolumeProvider());
        soundDTO.msPos = sound.getMillisecondPosition();
        soundDTO.looping = sound.getIsLooping();
        soundDTO.loopingRestartMs = sound.getLoopingRestartMs();
        soundDTO.loopingStopMs = sound.getLoopingStopMs();

        return JSON.toJson(soundDTO);
    }

    private static class SoundDTO {
        String uuid;
        String type;
        boolean paused;
        boolean muted;
        String vol;
        int msPos;
        boolean looping;
        Integer loopingStopMs;
        Integer loopingRestartMs;
    }
}
