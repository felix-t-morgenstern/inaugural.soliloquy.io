package inaugural.soliloquy.graphics.api;

public enum WindowResolution {
    RES_WINDOWED_FULLSCREEN(0, 0),

    // TODO: Consider minimum resolution to support
    RES_800x600(800, 600),
    RES_832x624(832, 624),
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

    public final int WIDTH;
    public final int HEIGHT;

    WindowResolution(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
    }

    @Override
    public String toString() {
        if (this == WindowResolution.RES_WINDOWED_FULLSCREEN) {
            return "Windowed Fullscreen";
        }

        return WIDTH + " x " + HEIGHT;
    }
}
