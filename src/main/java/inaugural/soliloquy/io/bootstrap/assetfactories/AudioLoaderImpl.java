package inaugural.soliloquy.io.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import org.apache.commons.io.FilenameUtils;
import soliloquy.specs.io.audio.entities.SoundType;
import soliloquy.specs.io.bootstrap.assetfactories.AudioLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.files.Files.executionDirectory;

public class AudioLoaderImpl implements AudioLoader {
    private final Consumer<SoundType> ADD_SOUND_TYPE;
    private final QuadFunction<String, String, Integer, Integer, SoundType> SOUND_TYPE_FACTORY;

    private final Set<String> FILETYPES;

    public AudioLoaderImpl(Consumer<SoundType> addSoundType,
                           QuadFunction <String, String, Integer, Integer, SoundType> soundTypeFactory,
                           Set<String> filetypes) {
        ADD_SOUND_TYPE = Check.ifNull(addSoundType, "addSoundType");
        SOUND_TYPE_FACTORY = Check.ifNull(soundTypeFactory, "soundTypeFactory");

        FILETYPES = setOf(Check.ifNull(filetypes, "filetypes"));
    }

    @Override
    public void loadFromDirectory(String relativePath, Map<String, String> idsForFilenames,
                                  Map<String, Integer> defaultLoopStopMsById,
                                  Map<String, Integer> defaultLoopRestartMsById) {
        Check.ifNullOrEmpty(relativePath, "relativePath");
        Check.ifNull(idsForFilenames, "idsForFilenames");
        Check.ifNull(defaultLoopStopMsById, "defaultLoopStopMsById");
        Check.ifNull(defaultLoopRestartMsById, "defaultLoopRestartMsById");

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
                var defaultLoopingRestartMs = defaultLoopRestartMsById.get(idForFilename);
                var soundType = SOUND_TYPE_FACTORY
                        .apply(idForFilename, fileRelativePath, defaultLoopingStopMs, defaultLoopingRestartMs);
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
    @FunctionalInterface
    public interface QuadFunction<A,B,C,D,R> {

        R apply(A a, B b, C c, D d);

        default <V> QuadFunction<A, B, C, D, V> andThen(
                Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (A a, B b, C c, D d) -> after.apply(apply(a, b, c, d));
        }
    }
}
