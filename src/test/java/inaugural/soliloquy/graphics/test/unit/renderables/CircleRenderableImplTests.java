package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.CircleRenderableImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.CircleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CircleRenderableImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockCenterProvider;
    @Mock private ProviderAtTime<Float> mockWidthProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private RenderableStack mockRenderableStack;

    private CircleRenderable circleRenderable;

    @BeforeEach
    public void setUp() {
        circleRenderable =
                new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider,
                        Z, UUID, mockRenderableStack);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(null, mockWidthProvider, mockColorProvider, Z, UUID,
                        mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, null, mockColorProvider, Z, UUID,
                        mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, null, mockRenderableStack));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, UUID, null));
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
    public void testSetCenterProviderWithInvalidArgs() {
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
    public void testSetWidthProviderWithInvalidArgs() {
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
    public void testSetColorProviderWithInvalidArgs() {
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
}
