package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.CircleRenderableImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.CircleRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.UUID;
import java.util.function.BiConsumer;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CircleRenderableImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockCenterProvider;
    @Mock private ProviderAtTime<Float> mockWidthProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private Component mockComponent;
    @Mock private BiConsumer<Component, Renderable> mockRemoveFromComponent;

    private CircleRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable =
                new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider,
                        Z, UUID, mockComponent, mockRemoveFromComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(null, mockWidthProvider, mockColorProvider, Z, UUID,
                        mockComponent, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, null, mockColorProvider, Z, UUID,
                        mockComponent, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockComponent, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null, Z, UUID,
                        mockComponent, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, null, mockComponent, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, UUID, null, mockRemoveFromComponent));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider,
                        mockColorProvider, Z, UUID, mockComponent, null));
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
        assertSame(mockComponent, renderable.component());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        verify(mockRemoveFromComponent, once()).accept(same(mockComponent), same(renderable));
    }
}
