package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class StaticMouseCursorTask implements Runnable {
    private final Function<String, Long> GET_MOUSE_IMAGE;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;
    private final Collection<StaticMouseCursorDefinitionDTO> STATIC_MOUSE_CURSOR_DTOS;
    private final Function<String, Consumer<StaticProvider<Long>>> PROCESS_RESULT;

    private static final EntityUuid PLACEHOLDER_ENTITY_UUID = new PlaceholderEntityUuid();

    /** @noinspection ConstantConditions*/
    public StaticMouseCursorTask(Function<String, Long> getMouseImage,
                                 Collection<StaticMouseCursorDefinitionDTO>
                                         staticMouseCursorDefinitionDTOs,
                                 StaticProviderFactory staticProviderFactory,
                                 Function<String, Consumer<StaticProvider<Long>>>
                                           processResult) {
        GET_MOUSE_IMAGE = Check.ifNull(getMouseImage, "getMouseImage");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
        Check.ifNull(staticMouseCursorDefinitionDTOs, "staticMouseCursorDefinitionDTOs");
        if (staticMouseCursorDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException("StaticMouseCursorTask: " +
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
                    .accept(STATIC_PROVIDER_FACTORY.make(PLACEHOLDER_ENTITY_UUID, mouseCursor,
                            mouseCursor, null));
        });
    }

    // NB: Look for duplicates somewhere
    private static class PlaceholderEntityUuid implements EntityUuid {
        @Override
        public long getMostSignificantBits() {
            return 0;
        }

        @Override
        public long getLeastSignificantBits() {
            return 0;
        }

        @Override
        public String getInterfaceName() {
            return null;
        }
    }
}
