package inaugural.soliloquy.io.audio.infrastructure;

import inaugural.soliloquy.tools.Check;
import org.apache.commons.io.FilenameUtils;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.audio.infrastructure.AudioLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.files.Files.executionDirectory;

public class AudioLoaderImpl implements AudioLoader {
    private final Consumer<SoundType> ADD_SOUND_TYPE;
    private final Function<String, Function<String, Function<Integer, Function<Integer,
            SoundType>>>>
            SOUND_TYPE_FACTORY;

    private final Set<String> FILETYPES;

    public AudioLoaderImpl(Consumer<SoundType> addSoundType,
                           Function<String, Function<String, Function<Integer, Function<Integer,
                                   SoundType>>>> soundTypeFactory,
                           Set<String> filetypes) {
        ADD_SOUND_TYPE = Check.ifNull(addSoundType, "addSoundType");
        SOUND_TYPE_FACTORY = Check.ifNull(soundTypeFactory, "soundTypeFactory");

        FILETYPES = setOf(Check.ifNull(filetypes, "filetypes"));
    }

    @Override
    public void loadFromDirectory(String relativePath, Map<String, String> idsForFilenames,
                                  Map<String, Integer> defaultLoopStopMsById,
                                  Map<String, Integer> defaultLoopRestartByMs) {
        Check.ifNullOrEmpty(relativePath, "relativePath");
        Check.ifNull(idsForFilenames, "idsForFilenames");
        Check.ifNull(defaultLoopStopMsById, "defaultLoopStopMsById");
        Check.ifNull(defaultLoopRestartByMs, "defaultLoopRestartByMs");

        var absolutePath = executionDirectory() + relativePath;
        var filesWithProperExtension =
                new File(absolutePath).listFiles(new SoundsLoaderFilenameFilter());
        assert filesWithProperExtension != null;
        for (var fileWithProperExtension : filesWithProperExtension) {
            var fileWithProperExtensionName = fileWithProperExtension.getName();
            if (idsForFilenames.containsKey(fileWithProperExtensionName)) {
                var idForFilename = idsForFilenames.get(fileWithProperExtensionName);
                var fileRelativePath = relativePath + fileWithProperExtension.getName();
                var defaultLoopingStopMs = defaultLoopStopMsById.get(idForFilename);
                var defaultLoopingRestartMs = defaultLoopRestartByMs.get(idForFilename);
                var soundType = SOUND_TYPE_FACTORY
                        .apply(idForFilename)
                        .apply(fileRelativePath)
                        .apply(defaultLoopingStopMs)
                        .apply(defaultLoopingRestartMs);
                ADD_SOUND_TYPE.accept(soundType);
            }
        }
    }

    private class SoundsLoaderFilenameFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return !file.isDirectory() &&
                    FILETYPES.contains(FilenameUtils.getExtension(file.getName()));
        }
    }
}
