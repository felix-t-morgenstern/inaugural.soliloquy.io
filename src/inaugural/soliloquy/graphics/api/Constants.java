package inaugural.soliloquy.graphics.api;

import inaugural.soliloquy.graphics.rendering.factories.FloatBoxFactoryImpl;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;

public class Constants {
    // Timing constants
    public final static String GMT = "GMT";
    public final static int MS_PER_SECOND = 1000;

    // Color constants
    public final static float MAX_CHANNEL_VAL = 255f;
    public final static Color INTACT_COLOR = Color.WHITE;

    // Graphics preloading constants
    public final static String IMAGES_PRELOADING_COMPONENT = "imagesPreloadingComponent";
    public final static String FONTS_PRELOADING_COMPONENT = "fontsPreloadingComponent";
    public final static String SPRITES_PRELOADING_COMPONENT = "spritesPreloadingComponent";
    public final static String ANIMATIONS_PRELOADING_COMPONENT = "animationsPreloadingComponent";
    public final static String IMAGE_ASSET_SETS_PRELOADING_COMPONENT =
            "imageAssetSetsPreloadingComponent";
    public final static String GLOBAL_LOOPING_ANIMATIONS_PRELOADING_COMPONENT =
            "globalLoopingAnimationsPreloadingComponent";

    // Mouse cursor constants
    public final static String STANDARD_ARROW_MOUSE_CURSOR_ID = "standardArrowMouseCursor";
    public final static String STANDARD_I_BEAM_MOUSE_CURSOR_ID = "standardIBeamMouseCursor";
    public final static String STANDARD_CROSSHAIR_CURSOR_ID = "standardCrosshairCursor";
    public final static String STANDARD_HAND_CURSOR_ID = "standardHandCursor";
    public final static String STANDARD_H_RESIZE_CURSOR_ID = "standardHResizeCursor";
    public final static String STANDARD_V_RESIZE_CURSOR_ID = "standardVResizeCursor";

    public final static FloatBox WHOLE_SCREEN = new FloatBoxFactoryImpl().make(0f, 0f, 1f, 1f);
}
