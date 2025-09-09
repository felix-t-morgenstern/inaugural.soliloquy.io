package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.CircleRenderableImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.CircleRenderable;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CircleRenderableImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockCenterProvider;
    @Mock private ProviderAtTime<Float> mockWidthProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private Component mockComponent;

    private CircleRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable =
                new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider,
                        Z, UUID, mockComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(null, mockWidthProvider, mockColorProvider, Z, UUID,
                        mockComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, null, mockColorProvider, Z, UUID,
                        mockComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, null, mockComponent));
    }

    @Test
    public void testCenterProvider() {
        assertSame(mockCenterProvider, renderable.getCenterProvider());
    }

    @Test
    public void testSetCenterProvider() {
        //noinspection unchecked
        var newCenterProvider = (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

        renderable.setCenterProvider(newCenterProvider);

        assertSame(newCenterProvider, renderable.getCenterProvider());
    }

    @Test
    public void testSetCenterProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setCenterProvider(null));
    }

    @Test
    public void testWidthProvider() {
        assertSame(mockWidthProvider, renderable.getWidthProvider());
    }

    @Test
    public void testSetWidthProvider() {
        //noinspection unchecked
        var newWidthProvider = (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setWidthProvider(newWidthProvider);

        assertSame(newWidthProvider, renderable.getWidthProvider());
    }

    @Test
    public void testSetWidthProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setWidthProvider(null));
    }

    @Test
    public void testColorProvider() {
        assertSame(mockColorProvider, renderable.getColorProvider());
    }

    @Test
    public void testSetColorProvider() {
        //noinspection unchecked
        var newColorProvider = (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        renderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getColorProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setColorProvider(null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testSetAndGetZ() {
        assertEquals(Z, renderable.getZ());

        var newZ = randomInt();

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testComponent() {
        assertSame(mockComponent, renderable.containingComponent());
    }

    @Test
    public void testSetComponent() {
        ((CircleRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertTrue(renderable.isDeleted());
    }
}
