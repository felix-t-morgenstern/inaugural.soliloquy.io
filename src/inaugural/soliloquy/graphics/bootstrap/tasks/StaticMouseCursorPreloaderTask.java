package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StaticMouseCursorPreloaderTask implements Runnable {
    private final Function<String, Long> GET_MOUSE_IMAGE;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;
    private final Collection<StaticMouseCursorDefinitionDTO> STATIC_MOUSE_CURSOR_DTOS;
    private final Function<String, Consumer<StaticProvider<Long>>> PROCESS_RESULT;

    private static final UUID PLACEHOLDER_UUID = new UUID(0, 0);

    /** @noinspection ConstantConditions*/
    public StaticMouseCursorPreloaderTask(Function<String, Long> getMouseCursorByRelativeLocation,
                                          Collection<StaticMouseCursorDefinitionDTO>
                                         staticMouseCursorDefinitionDTOs,
                                          StaticProviderFactory staticProviderFactory,
                                          Function<String, Consumer<StaticProvider<Long>>>
                                           processResult) {
        GET_MOUSE_IMAGE = Check.ifNull(getMouseCursorByRelativeLocation,
                "getMouseCursorByRelativeLocation");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
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
        STATIC_MOUSE_CURSOR_DTOS.forEach(staticMouseCursorDefinitionDTO -> {
            long mouseCursor =
                    GET_MOUSE_IMAGE.apply(staticMouseCursorDefinitionDTO.ImageRelativeLocation);
            PROCESS_RESULT.apply(staticMouseCursorDefinitionDTO.Id)
                    .accept(STATIC_PROVIDER_FACTORY.make(PLACEHOLDER_UUID, mouseCursor,
                            mouseCursor, null));
        });
    }
}
