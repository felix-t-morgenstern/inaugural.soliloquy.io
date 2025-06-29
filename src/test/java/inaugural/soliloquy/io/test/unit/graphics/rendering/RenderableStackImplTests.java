package inaugural.soliloquy.io.test.unit.graphics.rendering;

import inaugural.soliloquy.io.graphics.rendering.RenderableStackImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithDimensions;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RenderableStackImplTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int Z = randomInt();
    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundariesProvider;
    @Mock private RenderableStack mockContainingStack;

    private RenderableStack topLevelRenderableStack;
    private RenderableStack containedRenderableStack;

    @BeforeEach
    public void setUp() {
        topLevelRenderableStack = new RenderableStackImpl();
        containedRenderableStack =
                new RenderableStackImpl(UUID, Z, mockRenderingBoundariesProvider,
                        mockContainingStack);
    }

    @Test
    public void testConstructorCallsAddOnContainingStack() {
        verify(mockContainingStack).add(containedRenderableStack);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(null, Z, mockRenderingBoundariesProvider,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(UUID, Z, null, mockContainingStack));
        assertThrows(IllegalArgumentException.class,
                () -> new RenderableStackImpl(UUID, Z, mockRenderingBoundariesProvider, null));
    }

    @Test
    public void testUuid() {
        assertNull(topLevelRenderableStack.uuid());
        assertEquals(UUID, containedRenderableStack.uuid());
    }

    @Test
    public void testAddAndRepresentation() {
        var renderable1 = mock(RenderableWithDimensions.class);
        when(renderable1.getZ()).thenReturn(1);
        var renderable2 = mock(RenderableWithDimensions.class);
        when(renderable2.getZ()).thenReturn(2);
        var renderable3 = mock(RenderableWithDimensions.class);
        when(renderable3.getZ()).thenReturn(1);

        containedRenderableStack.add(renderable1);
        containedRenderableStack.add(renderable2);
        containedRenderableStack.add(renderable3);

        var representation =
                containedRenderableStack.renderablesByZIndexRepresentation();
        var representation2 =
                containedRenderableStack.renderablesByZIndexRepresentation();
        representation2.remove(2);

        assertNotNull(representation);

        assertNotSame(representation, representation2);
        assertEquals(representation.size() - 1, representation2.size());

        var zIndex1 = representation.get(1);
        assertEquals(2, zIndex1.size());
        assertTrue(zIndex1.contains(renderable1));
        assertTrue(zIndex1.contains(renderable3));

        var zIndex2 = representation.get(2);
        assertEquals(1, zIndex2.size());
        assertTrue(zIndex2.contains(renderable2));
    }

    @Test
    public void testAddSameRenderableUpdatesZIndex() {
        var renderable = mock(RenderableWithDimensions.class);
        when(renderable.getZ()).thenReturn(1);

        containedRenderableStack.add(renderable);

        when(renderable.getZ()).thenReturn(123);

        containedRenderableStack.add(renderable);

        var representation =
                containedRenderableStack.renderablesByZIndexRepresentation();

        assertEquals(1, representation.size());
        assertEquals(1, representation.get(123).size());
        assertSame(renderable, representation.get(123).getFirst());
    }

    @Test
    public void testStackCannotCallAddOnAnotherStackThatDoesNotHaveItAsItsContainingStack() {
        var stackThatDoesNotHaveTopLevelStackAsItsContainingStack = mock(RenderableStack.class);
        when(stackThatDoesNotHaveTopLevelStackAsItsContainingStack.containingStack())
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> containedRenderableStack
                .add(stackThatDoesNotHaveTopLevelStackAsItsContainingStack));
    }

    @Test
    public void testRemove() {
        var mockRenderable = mock(Renderable.class);
        var z = randomInt();
        when(mockRenderable.getZ()).thenReturn(z);

        topLevelRenderableStack.add(mockRenderable);

        assertTrue(topLevelRenderableStack.renderablesByZIndexRepresentation().containsKey(z));

        topLevelRenderableStack.remove(mockRenderable);

        assertFalse(topLevelRenderableStack.renderablesByZIndexRepresentation().containsKey(z));
    }

    @Test
    public void testGetRenderingBoundariesProvider() {
        assertSame(WHOLE_SCREEN_PROVIDER,
                topLevelRenderableStack.getRenderingBoundariesProvider());
        assertSame(mockRenderingBoundariesProvider,
                containedRenderableStack.getRenderingBoundariesProvider());
    }

    @Test
    public void testSetRenderingBoundariesProvider() {
        //noinspection unchecked
        ProviderAtTime<FloatBox> newProvider = mock(ProviderAtTime.class);

        containedRenderableStack.setRenderingBoundariesProvider(newProvider);

        assertSame(newProvider, containedRenderableStack.getRenderingBoundariesProvider());
    }

    @Test
    public void testSetRenderingBoundariesProviderForTopLevelStack() {
        //noinspection unchecked
        ProviderAtTime<FloatBox> newProvider = mock(ProviderAtTime.class);

        assertThrows(UnsupportedOperationException.class,
                () -> topLevelRenderableStack.setRenderingBoundariesProvider(newProvider));
    }

    @Test
    public void testSetRenderingBoundariesProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> containedRenderableStack.setRenderingBoundariesProvider(null));
    }

    @Test
    public void testContainingStack() {
        assertNull(topLevelRenderableStack.containingStack());
        assertSame(mockContainingStack, containedRenderableStack.containingStack());
    }

    @Test
    public void testClear() {
        containedRenderableStack.add(mock(RenderableWithDimensions.class));

        containedRenderableStack.clearContainedRenderables();

        var representation = containedRenderableStack.renderablesByZIndexRepresentation();

        assertEquals(0, representation.size());
    }

    @Test
    public void testSetAndGetZ() {
        assertEquals(Z, containedRenderableStack.getZ());
        assertEquals(0, topLevelRenderableStack.getZ());

        var newZ = randomInt();

        containedRenderableStack.setZ(newZ);
        assertThrows(UnsupportedOperationException.class,
                () -> topLevelRenderableStack.setZ(randomInt()));

        assertEquals(newZ, containedRenderableStack.getZ());

        // Add was already called once in containedRenderableStack's constructor
        verify(mockContainingStack, times(2)).add(containedRenderableStack);
    }

    @Test
    public void testDelete() {
        var mockRenderable1 = mock(Renderable.class);
        var mockRenderable2 = mock(Renderable.class);
        var mockRenderable3 = mock(Renderable.class);

        topLevelRenderableStack.add(mockRenderable1);
        topLevelRenderableStack.add(mockRenderable2);
        topLevelRenderableStack.add(mockRenderable3);

        topLevelRenderableStack.delete();

        verify(mockRenderable1).delete();
        verify(mockRenderable2).delete();
        verify(mockRenderable3).delete();
    }
}
