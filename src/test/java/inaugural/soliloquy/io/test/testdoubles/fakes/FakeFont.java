package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.assets.FontStyleInfo;

public class FakeFont implements Font {
    public String Id;
    public FakeFontStyleInfo Plain;
    public FakeFontStyleInfo Italic;
    public FakeFontStyleInfo Bold;
    public FakeFontStyleInfo BoldItalic;

    public FakeFont() {
    }

    public FakeFont(String id) {
        Id = id;
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
        return Id;
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
