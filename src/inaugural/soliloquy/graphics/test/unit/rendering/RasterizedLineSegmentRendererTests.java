package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeRasterizedLineSegmentRenderable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.RendererType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RasterizedLineSegmentRendererTests {
    private RasterizedLineSegmentRenderer _lineSegmentRenderer;

    @BeforeEach
    void setUp() {
        _lineSegmentRenderer = new RasterizedLineSegmentRenderer();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RendererType.class.getCanonicalName() + "<" +
                        RasterizedLineSegmentRenderable.class.getCanonicalName() + ">",
                _lineSegmentRenderer.getInterfaceName());
    }

    @Test
    void testRenderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(null));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0x0000,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0x0000,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        0, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        257, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0001f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        -0.0001f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0001f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, -0.0001f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0001f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, -0.0001f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0f, 1.0001f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0f, -0.0001f,
                        0.5f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        -1.0001f, 0.5f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        1.0001f, 0.5f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, -1.0001f, 0.5f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 1.0001f, 0.5f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5001f, 0.5f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, -1.5001f, 0.5f, 1)
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, 0.5001f, 1)
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        0.5f, 0.5f, 0.5f, -1.5001f, 1)
        ));
    }
}
