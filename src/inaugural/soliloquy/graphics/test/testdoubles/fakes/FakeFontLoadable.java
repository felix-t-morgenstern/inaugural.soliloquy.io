package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import inaugural.soliloquy.graphics.assets.FontImpl;
import soliloquy.specs.common.factories.CoordinateFactory;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.glGetInteger;

public class FakeFontLoadable extends FontImpl implements Font {
    private final FontDefinition FONT_DEFINITION;
    private final FloatBoxFactory FLOAT_BOX_FACTORY;
    private final CoordinateFactory COORDINATE_FACTORY;
    
    private FontStyleInfo _plain;
    private FontStyleInfo _italic;
    private FontStyleInfo _bold;
    private FontStyleInfo _boldItalic;

    public FakeFontLoadable(FontDefinition fontDefinition, 
                            FloatBoxFactory floatBoxFactory,
                            CoordinateFactory coordinateFactory) {
        super();
        FONT_DEFINITION = fontDefinition;
        FLOAT_BOX_FACTORY = floatBoxFactory;
        COORDINATE_FACTORY = coordinateFactory;
    }

    public void load() {
        if (MAXIMUM_TEXTURE_DIMENSION_SIZE < 0) {
            MAXIMUM_TEXTURE_DIMENSION_SIZE = glGetInteger(GL_MAX_TEXTURE_SIZE);
        }


        java.awt.Font fontFromFile = loadFontFromFile(FONT_DEFINITION.relativeLocation(), 
                FONT_DEFINITION.maxLosslessFontSize());

        java.awt.Font fontFromFileItalic = fontFromFile.deriveFont(java.awt.Font.ITALIC);

        java.awt.Font fontFromFileBold = fontFromFile.deriveFont(java.awt.Font.BOLD);

        java.awt.Font fontFromFileBoldItalic = fontFromFile.deriveFont(
                java.awt.Font.ITALIC | java.awt.Font.BOLD);


        _plain = loadFontStyle(fontFromFile, FONT_DEFINITION.plain(),
                FONT_DEFINITION.leadingAdjustment(), FLOAT_BOX_FACTORY, COORDINATE_FACTORY);

        _italic = loadFontStyle(fontFromFileItalic, FONT_DEFINITION.italic(),
                FONT_DEFINITION.leadingAdjustment(), FLOAT_BOX_FACTORY, COORDINATE_FACTORY);

        _bold = loadFontStyle(fontFromFileBold, FONT_DEFINITION.bold(),
                FONT_DEFINITION.leadingAdjustment(), FLOAT_BOX_FACTORY, COORDINATE_FACTORY);

        _boldItalic = loadFontStyle(fontFromFileBoldItalic, FONT_DEFINITION.boldItalic(),
                FONT_DEFINITION.leadingAdjustment(), FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
    }

    @Override
    public String id() throws IllegalStateException {
        return null;
    }

    @Override
    public FontStyleInfo plain() {
        return _plain;
    }

    @Override
    public FontStyleInfo italic() {
        return _italic;
    }

    @Override
    public FontStyleInfo bold() {
        return _bold;
    }

    @Override
    public FontStyleInfo boldItalic() {
        return _boldItalic;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
