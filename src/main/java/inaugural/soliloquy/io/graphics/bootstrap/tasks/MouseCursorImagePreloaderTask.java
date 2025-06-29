package inaugural.soliloquy.io.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.MouseCursorImageDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.MouseCursorImageDefinition;

import java.util.Collection;
import java.util.function.Consumer;

public class MouseCursorImagePreloaderTask implements Runnable {
    private final Collection<MouseCursorImageDefinitionDTO> MOUSE_CURSOR_IMAGE_DEFINITION_DTOS;
    private final MouseCursorImageFactory MOUSE_CURSOR_IMAGE_FACTORY;
    private final Consumer<MouseCursorImageFactory.Output> PROCESS_RESULT;

    public MouseCursorImagePreloaderTask(Collection<MouseCursorImageDefinitionDTO>
                                                 mouseCursorImageDefinitionDTOs,
                                         MouseCursorImageFactory mouseCursorImageFactory,
                                         Consumer<MouseCursorImageFactory.Output> processResult) {
        MOUSE_CURSOR_IMAGE_DEFINITION_DTOS =
                Check.ifNull(mouseCursorImageDefinitionDTOs, "mouseCursorImageDefinitionDTOs");
        MOUSE_CURSOR_IMAGE_FACTORY =
                Check.ifNull(mouseCursorImageFactory, "mouseCursorImageFactory");
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    @Override
    public void run() {
        MOUSE_CURSOR_IMAGE_DEFINITION_DTOS.forEach(dto ->
                PROCESS_RESULT.accept(MOUSE_CURSOR_IMAGE_FACTORY.make(
                        new MouseCursorImageDefinition(
                                dto.RelativeLocation,
                                dto.HotspotX,
                                dto.HotspotY
                        )
                )));
    }
}
