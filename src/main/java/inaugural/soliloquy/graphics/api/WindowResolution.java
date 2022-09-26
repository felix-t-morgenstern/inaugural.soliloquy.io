package inaugural.soliloquy.graphics.api;

import java.util.HashMap;
import java.util.Map;

public enum WindowResolution {
    RES_TEST_SQUARE(1800, 1800),

    RES_INVALID(-1, -1),

    RES_WINDOWED_FULLSCREEN(0, 0),

    RES_1024x768(1024, 768),
    RES_1152x872(1152, 872),
    RES_1280x720(1280, 720),
    RES_1280x800(1280, 800),
    RES_1280x1024(1280, 1024),
    RES_1600x900(1600, 900),
    RES_1600x1200(1600, 1200),
    RES_1680x1050(1680, 1050),
    RES_1920x1080(1920, 1080),
    RES_1920x1200(1920, 1200),
    RES_2048x1536(2048, 1536),
    RES_2560x1440(2560, 1440),
    RES_3200x1800(3200, 1800),
    RES_3840x1620(3840, 1620),
    RES_3840x2160(3840, 2160);

    private static final Map<Integer, Map<Integer, WindowResolution>> BY_WIDTH_AND_HEIGHT =
            new HashMap<>();

    static {
        for (WindowResolution w : values()) {
            if (!BY_WIDTH_AND_HEIGHT.containsKey(w.WIDTH)) {
                BY_WIDTH_AND_HEIGHT.put(w.WIDTH, new HashMap<>());
            }
            BY_WIDTH_AND_HEIGHT.get(w.WIDTH).put(w.HEIGHT, w);
        }
    }

    public final int WIDTH;
    public final int HEIGHT;

    WindowResolution(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
    }

    public static WindowResolution getFromWidthAndHeight(int width, int height) {
        if (width <= 0 || height <= 0) {
            return WindowResolution.RES_INVALID;
        }

        if (BY_WIDTH_AND_HEIGHT.containsKey(width)) {
            if (BY_WIDTH_AND_HEIGHT.get(width).containsKey(height)) {
                return BY_WIDTH_AND_HEIGHT.get(width).get(height);
            }
        }

        return WindowResolution.RES_INVALID;
    }

    @Override
    public String toString() {
        if (this == WindowResolution.RES_WINDOWED_FULLSCREEN) {
            return "Windowed Fullscreen";
        }

        return WIDTH + " x " + HEIGHT;
    }

    public float widthToHeightRatio() {
        if (this == WindowResolution.RES_INVALID) {
            throw new UnsupportedOperationException(
                    "Cannot obtain width to height ratio of RES_INVALID");
        }
        if (this == WindowResolution.RES_WINDOWED_FULLSCREEN) {
            throw new UnsupportedOperationException(
                    "Cannot obtain width to height ratio of RES_WINDOWED_FULLSCREEN");
        }

        return (float) WIDTH / (float) HEIGHT;
    }
}
