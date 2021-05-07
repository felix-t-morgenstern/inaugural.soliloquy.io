package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;

public class FakeFont implements Font {
    public FakeFontStyleInfo Plain;
    public FakeFontStyleInfo Italic;
    public FakeFontStyleInfo Bold;
    public FakeFontStyleInfo BoldItalic;

    public FakeFont() {

    }

    public FakeFont(FakeFontStyleInfo plain, FakeFontStyleInfo italic, FakeFontStyleInfo bold,
                    FakeFontStyleInfo boldItalic) {
        Plain = plain;
        Italic = italic;
        Bold = bold;
        BoldItalic = boldItalic;
    }

    @Override
    public String id() throws IllegalStateException {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public FontStyleInfo plain() {
        return Plain;
    }

    @Override
    public FontStyleInfo italic() {
        return Italic;
    }

    @Override
    public FontStyleInfo bold() {
        return Bold;
    }

    @Override
    public FontStyleInfo boldItalic() {
        return BoldItalic;
    }
}
