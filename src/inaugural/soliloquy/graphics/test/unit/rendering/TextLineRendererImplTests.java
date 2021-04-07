package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.TextLineRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TextLineRendererImplTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

    private TextLineRenderer _textLineRenderer;

    @BeforeEach
    void setUp() {
        _textLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(RENDERING_BOUNDARIES, null));
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
        HashMap<Integer, Color> colorIndices = new HashMap<>();
        colorIndices.put(4, Color.RED);
        ArrayList<Integer> italicIndices = new ArrayList<>();
        italicIndices.add(2);
        italicIndices.add(6);
        ArrayList<Integer> boldIndices = new ArrayList<>();
        boldIndices.add(3);
        boldIndices.add(5);
        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                textLine, colorIndices, italicIndices, boldIndices);



        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.Font = font;

        textLineRenderable.LineHeight = 0f;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.LineHeight = 0.25f;

        colorIndices.put(null, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(null);

        colorIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(6);

        colorIndices.put(-1, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(-1);

        colorIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(textLine.length());

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
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove(2);

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove((Object)(textLine.length()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object)(null));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(-1));

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(textLine.length()));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)3);
    }

    @Test
    void testTextLineLengthWithInvalidParams() {
        FakeFont font = new FakeFont();
        float lineHeight = 0.25f;
        String textLine = "Text line";
        HashMap<Integer, Color> colorIndices = new HashMap<>();
        colorIndices.put(4, Color.RED);
        ArrayList<Integer> italicIndices = new ArrayList<>();
        italicIndices.add(2);
        italicIndices.add(6);
        ArrayList<Integer> boldIndices = new ArrayList<>();
        boldIndices.add(3);
        boldIndices.add(5);
        FakeTextLineRenderable textLineRenderable = new FakeTextLineRenderable(font, lineHeight,
                textLine, colorIndices, italicIndices, boldIndices);



        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.Font = font;

        textLineRenderable.LineHeight = 0f;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        textLineRenderable.LineHeight = 0.25f;

        colorIndices.put(null, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(null);

        colorIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(6);

        colorIndices.put(-1, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(-1);

        colorIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.textLineLength(textLineRenderable));
        colorIndices.remove(textLine.length());

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
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove(2);

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove((Object)(textLine.length()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object)(null));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(-1));

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(textLine.length()));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)3);
    }

    @Test
    void testTextLineLength() {
        FakeFont font = new FakeFont();
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
        font.Glyphs.put('A', glyphA);
        font.GlyphsItalic.put('A', glyphAItalic);
        font.GlyphsBold.put('A', glyphABold);
        font.GlyphsBoldItalic.put('A', glyphABoldItalic);
        font.Glyphs.put('B', glyphB);
        font.GlyphsItalic.put('B', glyphBItalic);
        font.GlyphsBold.put('B', glyphBBold);
        font.GlyphsBoldItalic.put('B', glyphBBoldItalic);

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
                lineText, null, italicIndices, boldIndices);

        float textLineLength = _textLineRenderer.textLineLength(textLineRenderable);

        float expectedTextLineLength = ((glyphA.width() * 1) +
                (glyphAItalic.width() * 5) +
                (glyphABoldItalic.width() * 2) +
                (glyphBBoldItalic.width() * 1) +
                (glyphBBold.width() * 5) +
                (glyphB.width() * 2)) *
                (lineHeight / glyphHeight);

        // NB: Test is accurate to four significant digits; inaccuracy beyond that point is likely
        //     due to floating point rounding discrepancies
        assertEquals(Math.round(expectedTextLineLength * 10000f) / 10000f,
                Math.round(textLineLength * 10000f) / 10000f);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        TextLineRenderable.class.getCanonicalName() + ">",
                _textLineRenderer.getInterfaceName());
    }
}
