package inaugural.soliloquy.io.api;

import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.arrayInts;
import static java.util.UUID.fromString;
import static org.lwjgl.glfw.GLFW.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class Constants {
    // Dependency constants
    public final static String SUBSCRIBE_TO_NEXT_MOUSE_EVENT = "subscribeToNextMouseEvent";
    public final static String STATIC_PROVIDER_FACTORY = "staticProviderFactory";
    public final static UUID NULL_PROVIDER_UUID =
            fromString("cec9fcb6-4dde-4d61-99f8-0ceef03d0ffd");
    public final static String NULL_PROVIDER = "nullProvider";
    public final static UUID WHOLE_SCREEN_PROVIDER_UUID =
            fromString("201e3ed2-bc56-40ff-8290-7461f9683b60");
    public final static String WHOLE_SCREEN_PROVIDER = "wholeScreenProvider";
    public final static String TEXT_LINE_RENDERER = "textLineRenderer";

    // IO Methods
    public final static String MAKE_SOUND_METHOD_NAME = "makeSound";
    public final static String PLAY_SOUND_METHOD_NAME = "playSound";
    public final static String PAUSE_SOUND_METHOD_NAME = "pauseSound";
    public final static String UNPAUSE_SOUND_METHOD_NAME = "unpauseSound";

    // Timing constants
    public final static String GMT = "GMT";
    public final static int MS_PER_SECOND = 1000;

    // Color constants
    public final static float MAX_CHANNEL_VAL = 255f;
    public final static Color INTACT_COLOR = Color.WHITE;

    // Frame reporting constants
    public final static String CONSOLE_FRAME_RATE_REPORTER = "consoleFrameRateReporter";

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
    public final static Vertex SCREEN_CENTER = vertexOf(0.5f, 0.5f);

    public final static int LEFT_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_LEFT;
    public final static int RIGHT_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_RIGHT;
    public final static int MIDDLE_MOUSE_BUTTON = GLFW_MOUSE_BUTTON_MIDDLE;
    public final static int[] ALL_SUPPORTED_MOUSE_BUTTONS = arrayInts(
            LEFT_MOUSE_BUTTON,
            RIGHT_MOUSE_BUTTON,
            MIDDLE_MOUSE_BUTTON
    );

    // Key constants
    public final static int[] ALL_KEYS = arrayInts(
            GLFW_KEY_UNKNOWN,
            GLFW_KEY_SPACE,
            GLFW_KEY_APOSTROPHE,
            GLFW_KEY_COMMA,
            GLFW_KEY_MINUS,
            GLFW_KEY_PERIOD,
            GLFW_KEY_SLASH,
            GLFW_KEY_0,
            GLFW_KEY_1,
            GLFW_KEY_2,
            GLFW_KEY_3,
            GLFW_KEY_4,
            GLFW_KEY_5,
            GLFW_KEY_6,
            GLFW_KEY_7,
            GLFW_KEY_8,
            GLFW_KEY_9,
            GLFW_KEY_SEMICOLON,
            GLFW_KEY_EQUAL,
            GLFW_KEY_A,
            GLFW_KEY_B,
            GLFW_KEY_C,
            GLFW_KEY_D,
            GLFW_KEY_E,
            GLFW_KEY_F,
            GLFW_KEY_G,
            GLFW_KEY_H,
            GLFW_KEY_I,
            GLFW_KEY_J,
            GLFW_KEY_K,
            GLFW_KEY_L,
            GLFW_KEY_M,
            GLFW_KEY_N,
            GLFW_KEY_O,
            GLFW_KEY_P,
            GLFW_KEY_Q,
            GLFW_KEY_R,
            GLFW_KEY_S,
            GLFW_KEY_T,
            GLFW_KEY_U,
            GLFW_KEY_V,
            GLFW_KEY_W,
            GLFW_KEY_X,
            GLFW_KEY_Y,
            GLFW_KEY_Z,
            GLFW_KEY_LEFT_BRACKET,
            GLFW_KEY_BACKSLASH,
            GLFW_KEY_RIGHT_BRACKET,
            GLFW_KEY_GRAVE_ACCENT,
            GLFW_KEY_WORLD_1,
            GLFW_KEY_WORLD_2,
            GLFW_KEY_ESCAPE,
            GLFW_KEY_ENTER,
            GLFW_KEY_TAB,
            GLFW_KEY_BACKSPACE,
            GLFW_KEY_INSERT,
            GLFW_KEY_DELETE,
            GLFW_KEY_RIGHT,
            GLFW_KEY_LEFT,
            GLFW_KEY_DOWN,
            GLFW_KEY_UP,
            GLFW_KEY_PAGE_UP,
            GLFW_KEY_PAGE_DOWN,
            GLFW_KEY_HOME,
            GLFW_KEY_END,
            GLFW_KEY_CAPS_LOCK,
            GLFW_KEY_SCROLL_LOCK,
            GLFW_KEY_NUM_LOCK,
            GLFW_KEY_PRINT_SCREEN,
            GLFW_KEY_PAUSE,
            GLFW_KEY_F1,
            GLFW_KEY_F2,
            GLFW_KEY_F3,
            GLFW_KEY_F4,
            GLFW_KEY_F5,
            GLFW_KEY_F6,
            GLFW_KEY_F7,
            GLFW_KEY_F8,
            GLFW_KEY_F9,
            GLFW_KEY_F10,
            GLFW_KEY_F11,
            GLFW_KEY_F12,
            GLFW_KEY_F13,
            GLFW_KEY_F14,
            GLFW_KEY_F15,
            GLFW_KEY_F16,
            GLFW_KEY_F17,
            GLFW_KEY_F18,
            GLFW_KEY_F19,
            GLFW_KEY_F20,
            GLFW_KEY_F21,
            GLFW_KEY_F22,
            GLFW_KEY_F23,
            GLFW_KEY_F24,
            GLFW_KEY_F25,
            GLFW_KEY_KP_0,
            GLFW_KEY_KP_1,
            GLFW_KEY_KP_2,
            GLFW_KEY_KP_3,
            GLFW_KEY_KP_4,
            GLFW_KEY_KP_5,
            GLFW_KEY_KP_6,
            GLFW_KEY_KP_7,
            GLFW_KEY_KP_8,
            GLFW_KEY_KP_9,
            GLFW_KEY_KP_DECIMAL,
            GLFW_KEY_KP_DIVIDE,
            GLFW_KEY_KP_MULTIPLY,
            GLFW_KEY_KP_SUBTRACT,
            GLFW_KEY_KP_ADD,
            GLFW_KEY_KP_ENTER,
            GLFW_KEY_KP_EQUAL,
            GLFW_KEY_LEFT_SHIFT,
            GLFW_KEY_LEFT_CONTROL,
            GLFW_KEY_LEFT_ALT,
            GLFW_KEY_LEFT_SUPER,
            GLFW_KEY_RIGHT_SHIFT,
            GLFW_KEY_RIGHT_CONTROL,
            GLFW_KEY_RIGHT_ALT,
            GLFW_KEY_RIGHT_SUPER,
            GLFW_KEY_MENU,
            GLFW_KEY_LAST
    );
}
