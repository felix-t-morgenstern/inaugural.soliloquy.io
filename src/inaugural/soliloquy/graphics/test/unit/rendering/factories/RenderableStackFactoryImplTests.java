package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.common.test.fakes.FakeListFactory;
import inaugural.soliloquy.common.test.fakes.FakeMapFactory;
import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.factories.RenderableStackFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

import static org.junit.jupiter.api.Assertions.*;

class RenderableStackFactoryImplTests {
    private final FakeMapFactory MAP_FACTORY = new FakeMapFactory();
    private final FakeListFactory LIST_FACTORY = new FakeListFactory();

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
        RenderableStack renderableStack = _renderableStackFactory.make(MAP_FACTORY, LIST_FACTORY);

        assertNotNull(renderableStack);
        assertTrue(renderableStack instanceof RenderableStackImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _renderableStackFactory.make(null, LIST_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                _renderableStackFactory.make(MAP_FACTORY, null));
    }
}
