package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.HashMap;
import java.util.Map;

class TextLineRendererTest extends DisplayTest {
    protected final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    protected final static String RELATIVE_LOCATION_TRAJAN = "./res/fonts/Trajan Pro Regular.ttf";
    protected final static float MAX_LOSSLESS_FONT_SIZE_TRAJAN = 100f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN = 0.5f;
    protected final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN = 0.2f;
    protected final static float LEADING_ADJUSTMENT = 0f;
    protected final static String RELATIVE_LOCATION_OSWALD = "./res/fonts/Oswald-VariableFont_wght.ttf";
    protected final static float MAX_LOSSLESS_FONT_SIZE_OSWALD = 200f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD = 0.25f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC_OSWALD = 0.5f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC_OSWALD =
            0.5f;
    protected final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD = 0.1f;
    protected final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
            new HashMap<>();
    protected final static Map<Character, Float> GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT =
            new HashMap<>();

    protected static FontDefinition FontDefinition;
    protected static Renderer<TextLineRenderable> TextLineRenderer;
}
