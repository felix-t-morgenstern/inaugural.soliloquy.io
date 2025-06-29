package inaugural.soliloquy.io.persistence.audio;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;

public class SoundsPlayingHandler extends AbstractTypeHandler<SoundsPlaying> {
    private final TypeHandler<Sound> SOUND_HANDLER;
    private final SoundsPlaying SOUNDS_PLAYING;

    public SoundsPlayingHandler(TypeHandler<Sound> soundHandler, SoundsPlaying soundsPlaying) {
        SOUND_HANDLER = Check.ifNull(soundHandler, "soundHandler");
        SOUNDS_PLAYING = Check.ifNull(soundsPlaying, "soundsPlaying");
    }

    @SuppressWarnings("unchecked")
    @Override
    public SoundsPlaying read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        SOUNDS_PLAYING.representation().forEach(SOUNDS_PLAYING::removeSound);

        var dto = JSON.fromJson(data, SoundsPlayingDTO.class);
        for (var soundJson : dto.soundDTOs) {
            SOUNDS_PLAYING.registerSound(SOUND_HANDLER.read(soundJson));
        }

        return null;
    }

    @Override
    public String typeHandled() {
        return null;
    }

    @Override
    public String write(SoundsPlaying soundsPlaying) {
        var dto = new SoundsPlayingDTO();
        var listOfSoundsPlaying = SOUNDS_PLAYING.representation();
        var jsonObjects = new String[listOfSoundsPlaying.size()];
        var index = 0;
        for (var soundPlaying : listOfSoundsPlaying) {
            jsonObjects[index++] = SOUND_HANDLER.write(soundPlaying);
        }
        dto.soundDTOs = jsonObjects;
        return JSON.toJson(dto);
    }

    private static class SoundsPlayingDTO {
        String[] soundDTOs;
    }
}
