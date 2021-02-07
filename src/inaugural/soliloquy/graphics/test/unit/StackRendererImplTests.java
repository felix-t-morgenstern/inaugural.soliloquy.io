package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.rendering.StackRendererImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeRenderable;
import inaugural.soliloquy.graphics.test.fakes.FakeRenderableStack;
import inaugural.soliloquy.graphics.test.fakes.FakeRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.StackRenderer;

import static org.junit.jupiter.api.Assertions.*;

class StackRendererImplTests {
    private FakeRenderableStack _renderableStack;
    private FakeRenderer _renderer;

    private StackRenderer _stackRenderer;

    @BeforeEach
    void setUp() {
        _renderableStack = new FakeRenderableStack();
        _renderer = new FakeRenderer();

        _stackRenderer = new StackRendererImpl(_renderableStack, _renderer);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(null, _renderer));
        assertThrows(IllegalArgumentException.class,
                () -> new StackRendererImpl(_renderableStack, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(StackRenderer.class.getCanonicalName(), _stackRenderer.getInterfaceName());
    }

    @Test
    void testRender() {
        Renderable renderable1 = new FakeRenderable(1);
        Renderable renderable2 = new FakeRenderable(2);
        Renderable renderable3 = new FakeRenderable(2);

        _renderableStack.add(renderable1);
        _renderableStack.add(renderable2);
        _renderableStack.add(renderable3);

        _stackRenderer.render();

        assertEquals(3, _renderer.RENDERED.size());
        assertTrue(_renderer.RENDERED.get(0) == renderable2 ||
                _renderer.RENDERED.get(0) == renderable3);
        assertTrue(_renderer.RENDERED.get(1) == renderable2 ||
                _renderer.RENDERED.get(1) == renderable3);
        assertSame(renderable1, _renderer.RENDERED.get(2));
    }
}
