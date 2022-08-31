package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRenderableWithDimensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RenderableStackImplTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int Z = randomInt();
    @Mock private FloatBox _mockRenderingDimensions;
    @Mock private RenderableStack _mockContainingStack;

    private RenderableStack _topLevelRenderableStack;
    private RenderableStack _containedRenderableStack;

    @BeforeEach
    void setUp() {
        _mockRenderingDimensions = mock(FloatBox.class);
        _mockContainingStack = mock(RenderableStack.class);

        _topLevelRenderableStack = new RenderableStackImpl();
        _containedRenderableStack =
                new RenderableStackImpl(UUID, Z, _mockRenderingDimensions, _mockContainingStack);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(null, Z, _mockRenderingDimensions,
                        _mockContainingStack));
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(UUID, Z, null, _mockContainingStack));
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(UUID, Z, _mockRenderingDimensions, null));
    }

    @Test
    void testUuid() {
        assertNull(_topLevelRenderableStack.uuid());
        assertEquals(UUID, _containedRenderableStack.uuid());
    }

    @Test
    void testAddAndRepresentation() {
        Renderable renderable1 = new FakeRenderableWithDimensions(1);
        Renderable renderable2 = new FakeRenderableWithDimensions(2);
        Renderable renderable3 = new FakeRenderableWithDimensions(1);

        _containedRenderableStack.add(renderable1);
        _containedRenderableStack.add(renderable2);
        _containedRenderableStack.add(renderable3);

        Map<Integer, List<Renderable>> representation =
                _containedRenderableStack.renderablesByZIndexRepresentation();
        Map<Integer, List<Renderable>> representation2 =
                _containedRenderableStack.renderablesByZIndexRepresentation();
        representation2.remove(2);

        assertNotNull(representation);

        assertNotSame(representation, representation2);
        assertEquals(representation.size() - 1, representation2.size());

        List<Renderable> zIndex1 = representation.get(1);
        assertEquals(2, zIndex1.size());
        assertTrue(zIndex1.contains(renderable1));
        assertTrue(zIndex1.contains(renderable3));

        List<Renderable> zIndex2 = representation.get(2);
        assertEquals(1, zIndex2.size());
        assertTrue(zIndex2.contains(renderable2));
    }

    @Test
    void testAddSameRenderableUpdatesZIndex() {
        FakeRenderableWithDimensions renderable = new FakeRenderableWithDimensions(1);

        _containedRenderableStack.add(renderable);

        renderable.Z = 123;

        _containedRenderableStack.add(renderable);

        Map<Integer, List<Renderable>> representation =
                _containedRenderableStack.renderablesByZIndexRepresentation();

        assertEquals(1, representation.size());
        assertEquals(1, representation.get(123).size());
        assertSame(renderable, representation.get(123).get(0));
    }

    @Test
    void testRemove() {
        Renderable mockRenderable = mock(Renderable.class);
        int z = randomInt();
        when(mockRenderable.getZ()).thenReturn(z);

        _topLevelRenderableStack.add(mockRenderable);

        assertTrue(_topLevelRenderableStack.renderablesByZIndexRepresentation().containsKey(z));

        _topLevelRenderableStack.remove(mockRenderable);

        assertFalse(_topLevelRenderableStack.renderablesByZIndexRepresentation().containsKey(z));
    }

    @Test
    void testRenderingDimensions() {
        assertSame(WHOLE_SCREEN, _topLevelRenderableStack.renderingDimensions());
        assertSame(_mockRenderingDimensions, _containedRenderableStack.renderingDimensions());
    }

    @Test
    void testContainingStack() {
        assertNull(_topLevelRenderableStack.containingStack());
        assertSame(_mockContainingStack, _containedRenderableStack.containingStack());
    }

    @Test
    void testClear() {
        Renderable renderable = new FakeRenderableWithDimensions(0);

        _containedRenderableStack.add(renderable);

        _containedRenderableStack.clearContainedRenderables();

        Map<Integer, List<Renderable>> representation =
                _containedRenderableStack.renderablesByZIndexRepresentation();

        assertEquals(0, representation.size());
    }

    @Test
    void testSetAndGetZ() {
        assertEquals(Z, _containedRenderableStack.getZ());
        assertEquals(0, _topLevelRenderableStack.getZ());

        int newZ = randomInt();

        _containedRenderableStack.setZ(newZ);
        assertThrows(UnsupportedOperationException.class, () -> _topLevelRenderableStack.setZ(randomInt()));

        assertEquals(newZ, _containedRenderableStack.getZ());

        verify(_mockContainingStack, times(1)).add(_containedRenderableStack);
    }

    @Test
    void testDelete() {
        Renderable mockRenderable1 = mock(Renderable.class);
        Renderable mockRenderable2 = mock(Renderable.class);
        Renderable mockRenderable3 = mock(Renderable.class);

        _topLevelRenderableStack.add(mockRenderable1);
        _topLevelRenderableStack.add(mockRenderable2);
        _topLevelRenderableStack.add(mockRenderable3);

        _topLevelRenderableStack.delete();

        verify(mockRenderable1, times(1)).delete();
        verify(mockRenderable2, times(1)).delete();
        verify(mockRenderable3, times(1)).delete();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RenderableStack.class.getCanonicalName(),
                _containedRenderableStack.getInterfaceName());
        assertEquals(RenderableStack.class.getCanonicalName(),
                _topLevelRenderableStack.getInterfaceName());
    }
}
