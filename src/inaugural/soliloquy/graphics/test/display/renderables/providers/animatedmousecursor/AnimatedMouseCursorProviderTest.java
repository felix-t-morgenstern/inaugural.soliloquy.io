package inaugural.soliloquy.graphics.test.display.renderables.providers.animatedmousecursor;

import inaugural.soliloquy.graphics.api.dto.MouseCursorImageDTO;
import inaugural.soliloquy.graphics.bootstrap.workers.MouseCursorImagePreloaderWorker;
import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.graphics.test.display.io.MouseCursorImplTest;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;

import java.util.ArrayList;
import java.util.HashMap;

class AnimatedMouseCursorProviderTest extends MouseCursorImplTest {
    private static final String MOUSE_CURSOR_IMAGE_1_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default.png";
    private static final String MOUSE_CURSOR_IMAGE_2_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default_2.png";
    private static final String MOUSE_CURSOR_IMAGE_3_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default_3.png";
    private static final String MOUSE_CURSOR_IMAGE_4_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default_4.png";
    private static final String MOUSE_CURSOR_IMAGE_5_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default_5.png";
    private static final String MOUSE_CURSOR_IMAGE_6_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default_6.png";

    protected static final String PROVIDER_ID = "providerId";

    protected static AnimatedMouseCursorProvider _animatedMouseCursorProvider;

    public static void graphicsPreloaderLoadAction() {
        ArrayList<Long> mouseCursorImages = new ArrayList<>();
        new ArrayList<String>() {{
            add(MOUSE_CURSOR_IMAGE_1_RELATIVE_LOCATION);
            add(MOUSE_CURSOR_IMAGE_2_RELATIVE_LOCATION);
            add(MOUSE_CURSOR_IMAGE_3_RELATIVE_LOCATION);
            add(MOUSE_CURSOR_IMAGE_4_RELATIVE_LOCATION);
            add(MOUSE_CURSOR_IMAGE_5_RELATIVE_LOCATION);
            add(MOUSE_CURSOR_IMAGE_6_RELATIVE_LOCATION);
        }}.forEach(imgLoc -> new MouseCursorImagePreloaderWorker(
                new MouseCursorImageDTO(imgLoc, 0, 0), relativeLocation -> mouseCursorImages::add)
                .run());
        HashMap<Integer, Long> mouseCursorsAtMs = new HashMap<Integer, Long>() {{
            put(0, mouseCursorImages.get(0));
            put(167, mouseCursorImages.get(1));
            put(333, mouseCursorImages.get(2));
            put(500, mouseCursorImages.get(3));
            put(667, mouseCursorImages.get(4));
            put(833, mouseCursorImages.get(5));
        }};
        int msDuration = 1000;
        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        int periodModuloOffset = msDuration - (int)(timestamp % msDuration);

        _animatedMouseCursorProvider = new AnimatedMouseCursorProviderImpl(
                "id", mouseCursorsAtMs, msDuration, periodModuloOffset, null, null);

        _mouseCursorProviders.put(PROVIDER_ID, _animatedMouseCursorProvider);
    }
}
