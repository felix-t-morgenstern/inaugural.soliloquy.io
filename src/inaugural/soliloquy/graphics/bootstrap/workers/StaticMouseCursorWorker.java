package inaugural.soliloquy.graphics.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.StaticMouseCursorDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class StaticMouseCursorWorker implements Runnable {
    private final Function<String, Long> GET_MOUSE_IMAGE;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;
    private final Collection<StaticMouseCursorDTO> STATIC_MOUSE_CURSOR_DTOS;
    private final Function<String, Consumer<StaticProvider<Long>>> PROCESS_RESULT;

    private static final EntityUuid PLACEHOLDER_ENTITY_UUID = new PlaceholderEntityUuid();

    public StaticMouseCursorWorker(Function<String, Long> getMouseImage,
                                   StaticProviderFactory staticProviderFactory,
                                   Collection<StaticMouseCursorDTO> staticMouseCursorDTOS,
                                   Function<String, Consumer<StaticProvider<Long>>>
                                           processResult) {
        GET_MOUSE_IMAGE = Check.ifNull(getMouseImage, "getMouseImage");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
        Check.ifNull(staticMouseCursorDTOS, "staticMouseCursorDTOS");
        if (staticMouseCursorDTOS.isEmpty()) {
            throw new IllegalArgumentException("StaticMouseCursorWorker: " +
                    "staticMouseCursorDTOS is empty");
        }
        staticMouseCursorDTOS.forEach(staticMouseCursorDTO -> {
            Check.ifNull(staticMouseCursorDTO,
                    "staticMouseCursorDTO within staticMouseCursorDTOs");
            Check.ifNullOrEmpty(staticMouseCursorDTO.Id,
                    "staticMouseCursorDTO.Id within staticMouseCursorDTOs");
            Check.ifNullOrEmpty(staticMouseCursorDTO.ImageRelativeLocation,
                    "staticMouseCursorDTO.ImageRelativeLocation within staticMouseCursorDTOs (" +
                    staticMouseCursorDTO.Id + ")");
        });
        STATIC_MOUSE_CURSOR_DTOS = staticMouseCursorDTOS;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        STATIC_MOUSE_CURSOR_DTOS.forEach(staticMouseCursorDTO -> {
            long mouseCursor = GET_MOUSE_IMAGE.apply(staticMouseCursorDTO.ImageRelativeLocation);
            PROCESS_RESULT.apply(staticMouseCursorDTO.Id)
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
