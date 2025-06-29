package inaugural.soliloquy.io.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class StaticMouseCursorPreloaderTask implements Runnable {
    private final Function<String, Long> GET_MOUSE_IMAGE;
    private final StaticMouseCursorProviderFactory STATIC_MOUSE_CURSOR_PROVIDER_FACTORY;
    private final Collection<StaticMouseCursorDefinitionDTO> STATIC_MOUSE_CURSOR_DTOS;
    private final Consumer<StaticMouseCursorProvider> PROCESS_RESULT;

    /** @noinspection ConstantConditions */
    public StaticMouseCursorPreloaderTask(Function<String, Long> getMouseCursorByRelativeLocation,
                                          Collection<StaticMouseCursorDefinitionDTO>
                                                  staticMouseCursorDefinitionDTOs,
                                          StaticMouseCursorProviderFactory
                                                  staticMouseCursorProviderFactory,
                                          Consumer<StaticMouseCursorProvider> processResult) {
        GET_MOUSE_IMAGE = Check.ifNull(getMouseCursorByRelativeLocation,
                "getMouseCursorByRelativeLocation");
        STATIC_MOUSE_CURSOR_PROVIDER_FACTORY =
                Check.ifNull(staticMouseCursorProviderFactory, "staticMouseCursorProviderFactory");
        Check.ifNull(staticMouseCursorDefinitionDTOs, "staticMouseCursorDefinitionDTOs");
        if (staticMouseCursorDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException("StaticMouseCursorPreloaderTask: " +
                    "staticMouseCursorDefinitionDTOs is empty");
        }
        staticMouseCursorDefinitionDTOs.forEach(staticMouseCursorDefinitionDTO -> {
            Check.ifNull(staticMouseCursorDefinitionDTO,
                    "staticMouseCursorDefinitionDTO within staticMouseCursorDefinitionDTOs");
            Check.ifNullOrEmpty(staticMouseCursorDefinitionDTO.Id,
                    "staticMouseCursorDefinitionDTO.Id within staticMouseCursorDefinitionDTOs");
            Check.ifNullOrEmpty(staticMouseCursorDefinitionDTO.ImageRelativeLocation,
                    "staticMouseCursorDefinitionDTO.ImageRelativeLocation within " +
                            "staticMouseCursorDefinitionDTOs ("
                            + staticMouseCursorDefinitionDTO.Id + ")");
        });
        STATIC_MOUSE_CURSOR_DTOS = staticMouseCursorDefinitionDTOs;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        STATIC_MOUSE_CURSOR_DTOS.forEach(staticMouseCursorDefinitionDTO -> PROCESS_RESULT.accept(
                STATIC_MOUSE_CURSOR_PROVIDER_FACTORY.make(
                        new StaticMouseCursorProviderDefinition(
                                staticMouseCursorDefinitionDTO.Id,
                                GET_MOUSE_IMAGE.apply(
                                        staticMouseCursorDefinitionDTO.ImageRelativeLocation)
                        ))));
    }
}
