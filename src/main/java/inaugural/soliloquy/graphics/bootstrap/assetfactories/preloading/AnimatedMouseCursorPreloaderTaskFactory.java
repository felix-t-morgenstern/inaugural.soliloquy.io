package inaugural.soliloquy.graphics.bootstrap.assetfactories.preloading;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.AnimatedMouseCursorPreloaderTask;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetPreloaderTaskFactory;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnimatedMouseCursorPreloaderTaskFactory
        implements AssetPreloaderTaskFactory<AnimatedMouseCursorDefinitionDTO,
        AnimatedMouseCursorPreloaderTask> {
    private final Function<String, Long> GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION;
    private final AnimatedMouseCursorProviderFactory FACTORY;
    private final Consumer<AnimatedMouseCursorProvider> PROCESS_RESULT;

    public AnimatedMouseCursorPreloaderTaskFactory(Function<String, Long>
                                                           getMouseCursorsByRelativeLocation,
                                                   AnimatedMouseCursorProviderFactory factory,
                                                   Consumer<AnimatedMouseCursorProvider>
                                                           processResult) {
        GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION =
                Check.ifNull(getMouseCursorsByRelativeLocation,
                        "getMouseCursorsByRelativeLocation");
        FACTORY = Check.ifNull(factory, "factory");
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public AnimatedMouseCursorPreloaderTask make(
            Collection<AnimatedMouseCursorDefinitionDTO> animatedMouseCursorDefinitionDTOs)
            throws IllegalArgumentException {
        return new AnimatedMouseCursorPreloaderTask(
                GET_MOUSE_CURSORS_BY_RELATIVE_LOCATION,
                Check.ifNull(animatedMouseCursorDefinitionDTOs,
                        "animatedMouseCursorDefinitionDTOs"),
                FACTORY,
                PROCESS_RESULT
        );
    }
}
