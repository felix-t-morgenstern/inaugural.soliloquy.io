package inaugural.soliloquy.io.persistence.audio;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.io.audio.entities.Sound;

import java.util.UUID;
import java.util.function.BiFunction;

public class SoundHandler extends AbstractTypeHandler<Sound> {
    private final BiFunction<String, UUID, Sound> SOUND_FACTORY;

    public SoundHandler(BiFunction<String, UUID, Sound> soundFactory) {
        SOUND_FACTORY = Check.ifNull(soundFactory, "soundFactory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Sound read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        var dto = JSON.fromJson(data, SoundDTO.class);
        var sound = SOUND_FACTORY.apply(dto.type, UUID.fromString(dto.uuid));
        sound.setIsLooping(dto.looping);
        sound.setVolume(dto.vol);
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
        return sound;
    }

    @Override
    public String typeHandled() {
        return null;
    }

    @Override
    public String write(Sound sound) {
        Check.ifNull(sound, "sound");

        var soundDTO = new SoundDTO();
        soundDTO.uuid = sound.uuid().toString();
        soundDTO.type = sound.soundType().id();
        soundDTO.paused = sound.isPaused();
        soundDTO.muted = sound.isMuted();
        soundDTO.vol = sound.getVolume();
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
        double vol;
        int msPos;
        boolean looping;
        Integer loopingStopMs;
        Integer loopingRestartMs;
    }
}
