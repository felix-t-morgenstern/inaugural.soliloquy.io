package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.rendering.FloatBox;

public class FakeFont implements Font {
    @Override
    public FloatBox getUvCoordinatesForGlyph(char c) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphItalic(char c) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBold(char c) throws IllegalArgumentException {
        return null;
    }

    @Override
    public int textureIdItalic() {
        return 0;
    }

    @Override
    public int textureIdBold() {
        return 0;
    }

    @Override
    public String id() throws IllegalStateException {
        return null;
    }

    @Override
    public int textureId() {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
