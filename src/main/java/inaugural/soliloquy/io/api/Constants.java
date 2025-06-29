package inaugural.soliloquy.io.api;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

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

    public final static FloatBox WHOLE_SCREEN = floatBoxOf(0f, 0f, 1f, 1f);
    public final static ProviderAtTime<FloatBox> WHOLE_SCREEN_PROVIDER =
            new StaticProviderImpl<>(UUID.randomUUID(), WHOLE_SCREEN, null);

    public final static int LEFT_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_1;
    public final static int RIGHT_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_2;
    public final static int MIDDLE_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_3;
    public final static int[] ALL_SUPPORTED_MOUSE_BUTTONS = new int[] {
            LEFT_MOUSE_BUTTON,
            RIGHT_MOUSE_BUTTON,
            MIDDLE_MOUSE_BUTTON
    };
}
