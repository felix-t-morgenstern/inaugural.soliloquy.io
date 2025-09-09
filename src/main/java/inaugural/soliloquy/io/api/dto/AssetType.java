package inaugural.soliloquy.io.api.dto;

public enum AssetType {
    IMAGE(0),
    SPRITE(1),
    ANIMATION(2),
    GLOBAL_LOOPING_ANIMATION(3),
    IMAGE_ASSET_SET(4),
    FONT(5),
    MOUSE_CURSOR_IMAGE(6),
    ANIMATED_MOUSE_CURSOR_PROVIDER(7),
    STATIC_MOUSE_CURSOR_PROVIDER(8);

    private final int VALUE;

    AssetType(int value) {
        VALUE = value;
    }

    public int getValue() {
        return VALUE;
    }

    public static AssetType fromValue(int value) {
        return switch (value) {
            case 0 -> IMAGE;
            case 1 -> SPRITE;
            case 2 -> ANIMATION;
            case 3 -> GLOBAL_LOOPING_ANIMATION;
            case 4 -> IMAGE_ASSET_SET;
            case 5 -> FONT;
            case 6 -> MOUSE_CURSOR_IMAGE;
            case 7 -> ANIMATED_MOUSE_CURSOR_PROVIDER;
            case 8 -> STATIC_MOUSE_CURSOR_PROVIDER;
            default -> throw new IllegalArgumentException(
                    "AssetType: value (" + value + ") does not correspond to valid enum type");
        };
    }
}
