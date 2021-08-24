package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.common.test.fakes.FakeListFactory;
import inaugural.soliloquy.common.test.fakes.FakeMapFactory;
import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderableWithDimensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.factories.ListFactory;
import soliloquy.specs.common.factories.MapFactory;
import soliloquy.specs.common.infrastructure.List;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import static org.junit.jupiter.api.Assertions.*;

class RenderableStackImplTests {
    private RenderableStack _renderableStack;

    @BeforeEach
    void setUp() {
        MapFactory mapFactory = new FakeMapFactory();
        ListFactory listFactory = new FakeListFactory();

        _renderableStack = new RenderableStackImpl(mapFactory, listFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(null, new FakeListFactory()));
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(new FakeMapFactory(), null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RenderableStack.class.getCanonicalName(),
                _renderableStack.getInterfaceName());
    }

    @Test
    void testAddAndSnapshot() {
        Renderable renderable1 = new FakeRenderableWithDimensions(1);
        Renderable renderable2 = new FakeRenderableWithDimensions(2);
        Renderable renderable3 = new FakeRenderableWithDimensions(1);

        _renderableStack.add(renderable1);
        _renderableStack.add(renderable2);
        _renderableStack.add(renderable3);

        Map<Integer, List<Renderable>> snapshot = _renderableStack.snapshot();

        assertNotNull(snapshot);

        assertNotNull(snapshot.getFirstArchetype());
        assertNotNull(snapshot.getSecondArchetype());
        assertEquals(List.class.getCanonicalName() + "<" +
                Renderable.class.getCanonicalName() + ">",
                snapshot.getSecondArchetype().getInterfaceName());

        List<Renderable> zIndex1 = snapshot.get(1);
        assertNotNull(zIndex1.getArchetype());
        assertEquals(Renderable.class.getCanonicalName(),
                zIndex1.getArchetype().getInterfaceName());
        assertEquals(2, zIndex1.size());
        assertTrue(zIndex1.contains(renderable1));
        assertTrue(zIndex1.contains(renderable3));

        List<Renderable> zIndex2 = snapshot.get(2);
        assertNotNull(zIndex2.getArchetype());
        assertEquals(Renderable.class.getCanonicalName(),
                zIndex2.getArchetype().getInterfaceName());
        assertEquals(1, zIndex2.size());
        assertTrue(zIndex2.contains(renderable2));
    }

    @Test
    void testAddSameRenderableUpdatesZIndex() {
        FakeRenderableWithDimensions renderable = new FakeRenderableWithDimensions(1);

        _renderableStack.add(renderable);

        renderable.Z = 123;

        _renderableStack.add(renderable);

        Map<Integer, List<Renderable>> snapshot = _renderableStack.snapshot();

        assertEquals(1, snapshot.size());
        assertEquals(1, snapshot.get(123).size());
        assertSame(renderable, snapshot.get(123).get(0));
    }

    @Test
    void testClear() {
        Renderable renderable = new FakeRenderableWithDimensions(0);

        _renderableStack.add(renderable);

        _renderableStack.clearContainedRenderables();

        Map<Integer, List<Renderable>> snapshot = _renderableStack.snapshot();

        assertEquals(0, snapshot.size());
    }
}
