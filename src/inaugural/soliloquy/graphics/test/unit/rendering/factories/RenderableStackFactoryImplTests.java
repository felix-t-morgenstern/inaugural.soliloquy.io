package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.factories.RenderableStackFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

import static org.junit.jupiter.api.Assertions.*;

class RenderableStackFactoryImplTests {
    private RenderableStackFactory _renderableStackFactory;

    @BeforeEach
    void setUp() {
        _renderableStackFactory = new RenderableStackFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RenderableStackFactory.class.getCanonicalName(),
                _renderableStackFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        RenderableStack renderableStack = _renderableStackFactory.make();

        assertNotNull(renderableStack);
        assertTrue(renderableStack instanceof RenderableStackImpl);
    }
}
