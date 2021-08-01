package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.StackRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderableWithDimensions;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderableStack;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import static org.junit.jupiter.api.Assertions.*;

class StackRendererImplTests {
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private FakeRenderableStack _renderableStack;
    private FakeRenderer _renderer;

    private StackRenderer _stackRenderer;

    @BeforeEach
    void setUp() {
        _renderableStack = new FakeRenderableStack();
        _renderer = new FakeRenderer();

        _stackRenderer = new StackRendererImpl(_renderableStack, _renderer, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(null, _renderer, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(_renderableStack, null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(StackRenderer.class.getCanonicalName(), _stackRenderer.getInterfaceName());
    }

    @Test
    void testRender() {
        Renderable renderable1 = new FakeRenderableWithDimensions(1);
        Renderable renderable2 = new FakeRenderableWithDimensions(2);
        Renderable renderable3 = new FakeRenderableWithDimensions(2);

        _renderableStack.add(renderable1);
        _renderableStack.add(renderable2);
        _renderableStack.add(renderable3);

        _stackRenderer.render(MOST_RECENT_TIMESTAMP);

        assertEquals(3, _renderer.Rendered.size());
        assertTrue(_renderer.Rendered.get(0) == renderable2 ||
                _renderer.Rendered.get(0) == renderable3);
        assertTrue(_renderer.Rendered.get(1) == renderable2 ||
                _renderer.Rendered.get(1) == renderable3);
        assertSame(renderable1, _renderer.Rendered.get(2));
        assertEquals(3, _renderer.Timestamps.size());
        assertEquals(MOST_RECENT_TIMESTAMP, (long)_renderer.Timestamps.get(0));
        assertEquals(MOST_RECENT_TIMESTAMP, (long)_renderer.Timestamps.get(1));
        assertEquals(MOST_RECENT_TIMESTAMP, (long)_renderer.Timestamps.get(2));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeRenderableWithDimensions renderable = new FakeRenderableWithDimensions(0);
        _renderableStack.add(renderable);

        assertThrows(IllegalArgumentException.class, () ->
                _stackRenderer.render(MOST_RECENT_TIMESTAMP - 1L));
    }
}
