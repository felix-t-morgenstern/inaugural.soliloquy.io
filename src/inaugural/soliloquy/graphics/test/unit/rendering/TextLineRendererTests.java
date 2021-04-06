package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.TextLineRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeFloatBoxFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.fakes.FakeRenderingBoundaries;
import inaugural.soliloquy.graphics.test.fakes.FakeTextLineRenderable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextLineRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

    private Renderer<TextLineRenderable> _textLineRenderer;

    @BeforeEach
    void setUp() {
        _textLineRenderer = new TextLineRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(RENDERING_BOUNDARIES, null));
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
                () -> _textLineRenderer.render(textLineRenderable));
        textLineRenderable.Font = font;

        textLineRenderable.LineHeight = 0f;
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        textLineRenderable.LineHeight = 0.25f;

        colorIndices.put(null, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        colorIndices.remove(null);

        colorIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        colorIndices.remove(6);

        colorIndices.put(-1, Color.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        colorIndices.remove(-1);

        colorIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        colorIndices.remove(textLine.length());

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove((Object)(null));

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove((Object)(-1));

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        italicIndices.remove((Object)(textLine.length()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(null));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(-1));

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> _textLineRenderer.render(textLineRenderable));
        boldIndices.remove((Object)(textLine.length()));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        TextLineRenderable.class.getCanonicalName() + ">",
                _textLineRenderer.getInterfaceName());
    }
}
