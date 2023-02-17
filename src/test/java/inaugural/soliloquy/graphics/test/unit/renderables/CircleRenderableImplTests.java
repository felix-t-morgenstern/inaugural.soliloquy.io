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

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CircleRenderableImplTests {
    @Mock private ProviderAtTime<Vertex> mockCenterProvider;
    @Mock private ProviderAtTime<Float> mockWidthProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;

    private CircleRenderable circleRenderable;

    @Before
    public void setUp() {
        circleRenderable =
                new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, mockColorProvider);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(null, mockWidthProvider, mockColorProvider));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, null, mockColorProvider));
        assertThrows(IllegalArgumentException.class,
                () -> new CircleRenderableImpl(mockCenterProvider, mockWidthProvider, null));
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
    public void testGetInterfaceName() {
        assertEquals(CircleRenderable.class.getCanonicalName(),
                circleRenderable.getInterfaceName());
    }
}
