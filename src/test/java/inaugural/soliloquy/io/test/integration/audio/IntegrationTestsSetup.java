package inaugural.soliloquy.io.test.integration.audio;

import inaugural.soliloquy.io.audio.AudioImpl;
import inaugural.soliloquy.io.audio.entities.SoundTypeImpl;
import inaugural.soliloquy.io.audio.entities.SoundsPlayingImpl;
import inaugural.soliloquy.io.audio.factories.SoundFactoryImpl;
import inaugural.soliloquy.io.audio.infrastructure.AudioLoaderImpl;
import soliloquy.specs.io.audio.Audio;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class IntegrationTestsSetup {
    public final static String SOUND_TYPE_ID_FINITE = "SoundIdFinite";

    public final static String SOUND_TYPE_ID_LOOPING = "SoundIdLooping";
    public final static Integer SOUND_TYPE_DEFAULT_LOOP_STOP_MS = 14850;
    public final static Integer SOUND_TYPE_DEFAULT_LOOP_RESTART_MS = 7520;

    private final static String RESOURCES_PATH = "\\src\\test\\resources\\sounds\\";
    private final static String RESOURCE_FINITE = "Kevin_MacLeod_-_Living_Voyage.mp3";
    private final static String RESOURCE_LOOPING =
            "exit-the-premises-by-kevin-macleod-from-filmmusic-io.mp3";

    public static Audio integrationTestAudio() {
        var audio = new AudioImpl(new SoundsPlayingImpl(), SoundFactoryImpl::new);

        var audioLoader = new AudioLoaderImpl(audio::addSoundType,
                id -> relPath -> defaultLoopStopMs -> defaultLoopRestartMs -> new SoundTypeImpl(id,
                        relPath, defaultLoopStopMs, defaultLoopRestartMs),
                setOf("mp3", "wav"));
        audioLoader.loadFromDirectory(
                RESOURCES_PATH,
                mapOf(
                        pairOf(RESOURCE_FINITE, SOUND_TYPE_ID_FINITE),
                        pairOf(RESOURCE_LOOPING, SOUND_TYPE_ID_LOOPING)
                ),
                mapOf(
                        pairOf(SOUND_TYPE_ID_LOOPING, SOUND_TYPE_DEFAULT_LOOP_STOP_MS)
                ),
                mapOf(
                        pairOf(SOUND_TYPE_ID_LOOPING, SOUND_TYPE_DEFAULT_LOOP_RESTART_MS)
                )
        );

        return audio;
    }
}
