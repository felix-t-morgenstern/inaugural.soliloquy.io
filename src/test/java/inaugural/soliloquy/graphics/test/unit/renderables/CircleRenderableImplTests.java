package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.CircleRenderableImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.CircleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CircleRenderableImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockCenterProvider;
    @Mock private ProviderAtTime<Float> mockWidthProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private RenderableStack mockRenderableStack;

    private CircleRenderable circleRenderable;

    @Before
    public void setUp() {
        circleRenderable = new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider, Z, UUID, mockRenderableStack);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(null, mockWidthProvider, mockColorProvider, Z, UUID, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, null, mockColorProvider, Z, UUID, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider, Z, null, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider, Z, UUID, null));
    }

    @Test
    public void testCenterProvider() {
        assertSame(mockCenterProvider, circleRenderable.getCenterProvider());
    }

    @Test
    public void testSetCenterProvider() {
        //noinspection unchecked
        var newCenterProvider = (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

        circleRenderable.setCenterProvider(newCenterProvider);

        assertSame(newCenterProvider, circleRenderable.getCenterProvider());
    }

    @Test
    public void testSetCenterProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> circleRenderable.setCenterProvider(null));
    }

    @Test
    public void testWidthProvider() {
        assertSame(mockWidthProvider, circleRenderable.getWidthProvider());
    }

    @Test
    public void testSetWidthProvider() {
        //noinspection unchecked
        var newWidthProvider = (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        circleRenderable.setWidthProvider(newWidthProvider);

        assertSame(newWidthProvider, circleRenderable.getWidthProvider());
    }

    @Test
    public void testSetWidthProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> circleRenderable.setWidthProvider(null));
    }

    @Test
    public void testColorProvider() {
        assertSame(mockColorProvider, circleRenderable.getColorProvider());
    }

    @Test
    public void testSetColorProvider() {
        //noinspection unchecked
        var newColorProvider = (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        circleRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, circleRenderable.getColorProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> circleRenderable.setColorProvider(null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, circleRenderable.uuid());
    }

    @Test
    public void testSetAndGetZ() {
        assertEquals(Z, circleRenderable.getZ());

        var newZ = randomInt();

        circleRenderable.setZ(newZ);

        assertEquals(newZ, circleRenderable.getZ());
    }

    @Test
    public void testContainingStack() {
        assertSame(mockRenderableStack, circleRenderable.containingStack());
    }

    @Test
    public void testDelete() {
        circleRenderable.delete();

        verify(mockRenderableStack).remove(circleRenderable);
    }

    @Test
    public void testGetInterfaceName() {
        assertEquals(CircleRenderable.class.getCanonicalName(),
                circleRenderable.getInterfaceName());
    }
}
