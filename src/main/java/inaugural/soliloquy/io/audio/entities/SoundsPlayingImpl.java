package inaugural.soliloquy.io.audio.entities;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.audio.entities.Sound;
import soliloquy.specs.io.audio.entities.SoundsPlaying;

import java.util.*;

public class SoundsPlayingImpl implements SoundsPlaying {
    private final Map<UUID, Sound> SOUNDS_PLAYING;

    public SoundsPlayingImpl() {
        SOUNDS_PLAYING = new HashMap<>();
    }

    @Override
    public List<Sound> representation() {
        return new ArrayList<>(SOUNDS_PLAYING.values());
    }

    @Override
    public boolean isPlayingSound(UUID soundId) throws IllegalArgumentException {
        return SOUNDS_PLAYING.containsKey(Check.ifNull(soundId, "soundId"));
    }

    @Override
    public Sound getSound(UUID soundId) throws IllegalArgumentException {
        return SOUNDS_PLAYING.get(Check.ifNull(soundId, "soundId"));
    }

    @Override
    public void registerSound(Sound sound) throws IllegalArgumentException {
        SOUNDS_PLAYING.put(Check.ifNull(sound, "sound").uuid(),
                sound);
    }

    @Override
    public void removeSound(Sound sound) throws IllegalArgumentException {
        SOUNDS_PLAYING.remove(
                Check.ifNull(sound, "sound").uuid(),
                sound);
    }
}
