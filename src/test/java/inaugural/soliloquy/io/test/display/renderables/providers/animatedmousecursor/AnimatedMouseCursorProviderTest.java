package inaugural.soliloquy.io.test.display.renderables.providers.animatedmousecursor;

import inaugural.soliloquy.io.api.dto.MouseCursorImageDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.MouseCursorImageFactoryImpl;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import inaugural.soliloquy.io.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.io.test.display.mouse.mousecursor.MouseCursorImplTest;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class AnimatedMouseCursorProviderTest extends MouseCursorImplTest {
    private static final String MOUSE_CURSOR_IMAGE_1_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default.png";
    private static final String MOUSE_CURSOR_IMAGE_2_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default_2.png";
    private static final String MOUSE_CURSOR_IMAGE_3_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default_3.png";
    private static final String MOUSE_CURSOR_IMAGE_4_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default_4.png";
    private static final String MOUSE_CURSOR_IMAGE_5_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default_5.png";
    private static final String MOUSE_CURSOR_IMAGE_6_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default_6.png";

    protected static final String PROVIDER_ID = "providerId";

    protected static AnimatedMouseCursorProvider AnimatedMouseCursorProvider;

    public static void graphicsPreloaderLoadAction() {
        List<MouseCursorImageDefinitionDTO> mouseCursorImageDefinitionDTOs = listOf();
        List<Long> mouseCursorImages = listOf();
        listOf(
            MOUSE_CURSOR_IMAGE_1_RELATIVE_LOCATION,
            MOUSE_CURSOR_IMAGE_2_RELATIVE_LOCATION,
            MOUSE_CURSOR_IMAGE_3_RELATIVE_LOCATION,
            MOUSE_CURSOR_IMAGE_4_RELATIVE_LOCATION,
            MOUSE_CURSOR_IMAGE_5_RELATIVE_LOCATION,
            MOUSE_CURSOR_IMAGE_6_RELATIVE_LOCATION
        ).forEach(imgLoc -> mouseCursorImageDefinitionDTOs.add(new MouseCursorImageDefinitionDTO(
                imgLoc, 0, 0
        )));
        new MouseCursorImagePreloaderTask(
                mouseCursorImageDefinitionDTOs,
                new MouseCursorImageFactoryImpl(),
                output -> mouseCursorImages.add(output.id()))
                .run();
        var mouseCursorsAtMs = mapOf(
            pairOf(0, mouseCursorImages.get(0)),
            pairOf(167, mouseCursorImages.get(1)),
            pairOf(333, mouseCursorImages.get(2)),
            pairOf(500, mouseCursorImages.get(3)),
            pairOf(667, mouseCursorImages.get(4)),
            pairOf(833, mouseCursorImages.get(5))
        );
        var msDuration = 1000;
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var periodModuloOffset = msDuration - (int) (timestamp % msDuration);

        AnimatedMouseCursorProvider = new AnimatedMouseCursorProviderImpl(
                "id", mouseCursorsAtMs, msDuration, periodModuloOffset, null, null);

        MouseCursorProviders.put(PROVIDER_ID, AnimatedMouseCursorProvider);
    }
}
