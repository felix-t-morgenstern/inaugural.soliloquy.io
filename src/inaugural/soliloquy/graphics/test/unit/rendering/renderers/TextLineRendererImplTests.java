package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TextLineRendererImplTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final Color DEFAULT_COLOR = Color.BLACK;

    private TextLineRenderer _textLineRenderer;

    @BeforeEach
    void setUp() {
        _textLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                DEFAULT_COLOR);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(null, FLOAT_BOX_FACTORY, DEFAULT_COLOR));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(RENDERING_BOUNDARIES, null, DEFAULT_COLOR));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY, null));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        FakeFont font = new FakeFont();
        float lineHeight = 0.25f;
        String textLine = "Text line";
        HashMap<Integer, ProviderAtTime<Color>> colorProviderIndices = new HashMap<>();
        colorProviderIndices.put(4, new FakeStaticProviderAtTime<>(Color.RED));
        ArrayList<Integer> italicIndices = new ArrayList<>();
        italicIndices.add(2);
        italicIndices.add(6);
        ArrayList<Integer> boldIndices = new ArrayList<>();
        boldIndices.add(3);
        boldIndices.add(5);
        ProviderAtTime<FloatBox> renderingAreaProvider =
                new FakeStaticProviderAtTime<>(new FakeFloatBox(0f, 0f, 1f, 1f));
        FakeEntityUuid id = new FakeEntityUuid();
        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                textLine, colorProviderIndices, italicIndices, boldIndices, renderingAreaProvider,
                id);



        assertThrows(IllegalArgumentException.class, () -> _textLineRenderer.render(null, 0L));

        textLineRenderable.RenderingAreaProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.RenderingAreaProvider = renderingAreaProvider;

        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Font = font;

        textLineRenderable.LineHeight = 0f;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.LineHeight = 0.25f;

        colorProviderIndices.put(null, new FakeStaticProviderAtTime<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(null);

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(6);

        colorProviderIndices.put(-1, new FakeStaticProviderAtTime<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(-1);

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(textLine.length());

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object)(null));

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove((Object)(-1));

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove(2);

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove((Object)(textLine.length()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object)(null));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove((Object)(-1));

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove((Object)(textLine.length()));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove(2);

        textLineRenderable.Uuid = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Uuid = id;
    }

    @Test
    void testTextLineLengthWithInvalidParams() {
        FakeFont font = new FakeFont();
        float lineHeight = 0.25f;
        String textLine = "Text line";
        HashMap<Integer, ProviderAtTime<Color>> colorProviderIndices = new HashMap<>();
        colorProviderIndices.put(4, new FakeStaticProviderAtTime<>(Color.RED));
        ArrayList<Integer> italicIndices = new ArrayList<>();
        italicIndices.add(2);
        italicIndices.add(6);
        ArrayList<Integer> boldIndices = new ArrayList<>();
        boldIndices.add(3);
        boldIndices.add(5);
        ProviderAtTime<FloatBox> renderingAreaProvider =
                new FakeStaticProviderAtTime<>(new FakeFloatBox(0f, 0f, 1f, 1f));
        FakeEntityUuid id = new FakeEntityUuid();
        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                textLine, colorProviderIndices, italicIndices, boldIndices, renderingAreaProvider,
                id);



        assertThrows(IllegalArgumentException.class, () -> _textLineRenderer.render(null, 0L));

        textLineRenderable.RenderingAreaProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.RenderingAreaProvider = renderingAreaProvider;

        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.Font = font;

        textLineRenderable.LineHeight = 0f;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.LineHeight = 0.25f;

        colorProviderIndices.put(null, new FakeStaticProviderAtTime<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorProviderIndices.remove(null);

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorProviderIndices.remove(6);

        colorProviderIndices.put(-1, new FakeStaticProviderAtTime<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorProviderIndices.remove(-1);

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorProviderIndices.remove(textLine.length());

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object)(null));

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        italicIndices.remove((Object)(-1));

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        italicIndices.remove(2);

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        italicIndices.remove((Object)(textLine.length()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object)(null));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        boldIndices.remove((Object)(-1));

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        boldIndices.remove((Object)(textLine.length()));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        boldIndices.remove(2);

        textLineRenderable.Uuid = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.Uuid = id;
    }

    @Test
    void testTextLineLength() {
        FakeFontStyleInfo plain = new FakeFontStyleInfo();
        FakeFontStyleInfo italic = new FakeFontStyleInfo();
        FakeFontStyleInfo bold = new FakeFontStyleInfo();
        FakeFontStyleInfo boldItalic = new FakeFontStyleInfo();
        FakeFont font = new FakeFont(plain, italic, bold, boldItalic);

        float glyphHeight = 0.1f;
        FakeFloatBox glyphA = new FakeFloatBox(0.0f, 0.0f, 0.356659007059121f, glyphHeight);
        FakeFloatBox glyphAItalic = new FakeFloatBox(0.0f, 0.0f, 0.48381785202459f, glyphHeight);
        FakeFloatBox glyphABold = new FakeFloatBox(0.0f, 0.0f, 0.677026478f, glyphHeight);
        FakeFloatBox glyphABoldItalic = new FakeFloatBox(0.0f, 0.0f, 0.24048836420184f,
                glyphHeight);
        FakeFloatBox glyphB = new FakeFloatBox(0.0f, 0.0f, 0.213723488507345f, glyphHeight);
        FakeFloatBox glyphBItalic = new FakeFloatBox(0.0f, 0.0f, 0.331731488913315f, glyphHeight);
        FakeFloatBox glyphBBold = new FakeFloatBox(0.0f, 0.0f, 0.709300081504505f, glyphHeight);
        FakeFloatBox glyphBBoldItalic = new FakeFloatBox(0.0f, 0.0f, 0.0767894524389122f,
                glyphHeight);

        plain.Glyphs.put('A', glyphA);
        italic.Glyphs.put('A', glyphAItalic);
        bold.Glyphs.put('A', glyphABold);
        boldItalic.Glyphs.put('A', glyphABoldItalic);
        plain.Glyphs.put('B', glyphB);
        italic.Glyphs.put('B', glyphBItalic);
        bold.Glyphs.put('B', glyphBBold);
        boldItalic.Glyphs.put('B', glyphBBoldItalic);

        float textureWidthToHeightRatio = 0.12f;
        float textureWidthToHeightRatioItalic = 0.34f;
        float textureWidthToHeightRatioBold = 0.56f;
        float textureWidthToHeightRatioBoldItalic = 0.78f;
        plain.TextureWidthToHeightRatio = textureWidthToHeightRatio;
        italic.TextureWidthToHeightRatio = textureWidthToHeightRatioItalic;
        bold.TextureWidthToHeightRatio = textureWidthToHeightRatioBold;
        boldItalic.TextureWidthToHeightRatio = textureWidthToHeightRatioBoldItalic;

        float lineHeight = 0.5f;
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        ArrayList<Integer> italicIndices = new ArrayList<Integer>(){{
            add(1);
            add(9);
        }};
        ArrayList<Integer> boldIndices = new ArrayList<Integer>(){{
            add(6);
            add(14);
        }};

        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                lineText, null, italicIndices, boldIndices,
                new FakeStaticProviderAtTime<>(new FloatBoxImpl(0f, 0f, 1f, 1f)),
                new FakeEntityUuid());

        float textLineLength = _textLineRenderer.textLineLength(textLineRenderable);

        float expectedTextLineLength = ((glyphA.width() * 1 * textureWidthToHeightRatio) +
                (glyphAItalic.width() * 5 * textureWidthToHeightRatioItalic) +
                (glyphABoldItalic.width() * 2 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBoldItalic.width() * 1 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBold.width() * 5 * textureWidthToHeightRatioBold) +
                (glyphB.width() * 2 * textureWidthToHeightRatio)) *
                (lineHeight / glyphHeight);

        // NB: Test is accurate to four significant digits; inaccuracy beyond that point is likely
        //     due to floating point rounding discrepancies
        assertEquals(Math.round(expectedTextLineLength * 10000f) / 10000f,
                Math.round(textLineLength * 10000f) / 10000f);
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeFont font = new FakeFont();
        float lineHeight = 0.5f;
        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                "", null, null, null,
                new FakeStaticProviderAtTime<>(new FakeFloatBox(0f, 0f, 1f, 1f)),
                new FakeEntityUuid());
        long timestamp = 100L;
        _textLineRenderer.render(textLineRenderable, timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable, timestamp - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        TextLineRenderable.class.getCanonicalName() + ">",
                _textLineRenderer.getInterfaceName());
    }
}
