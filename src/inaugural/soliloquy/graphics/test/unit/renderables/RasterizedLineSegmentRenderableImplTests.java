package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RasterizedLineSegmentRenderableImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = 789;
    private final EntityUuid UUID = new FakeEntityUuid();
    private final Consumer<RasterizedLineSegmentRenderable> DELETE_CONSUMER =
            rasterizedLineSegmentRenderable ->
                    _consumedRasterizedLineSegmentRenderable = rasterizedLineSegmentRenderable;

    private static RasterizedLineSegmentRenderable _consumedRasterizedLineSegmentRenderable;

    private RasterizedLineSegmentRenderable _rasterizedLineSegmentRenderable;

    @BeforeEach
    void setUp() {
        _rasterizedLineSegmentRenderable = new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, null,
                RENDERING_AREA_PROVIDER, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                null, Z, UUID, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, null, DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RasterizedLineSegmentRenderable.class.getCanonicalName(),
                _rasterizedLineSegmentRenderable.getInterfaceName());
    }

    @Test
    void testThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, _rasterizedLineSegmentRenderable.thicknessProvider());
    }

    @Test
    void testStipplePattern() {
        assertEquals(STIPPLE_PATTERN, _rasterizedLineSegmentRenderable.stipplePattern());
    }

    @Test
    void testStippleFactor() {
        assertEquals(STIPPLE_FACTOR, _rasterizedLineSegmentRenderable.stippleFactor());
    }

    @Test
    void testColorProvider() {
        assertSame(COLOR_PROVIDER, _rasterizedLineSegmentRenderable.colorProvider());
    }

    @Test
    void testRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _rasterizedLineSegmentRenderable.renderingAreaProvider());
    }

    @Test
    void testZ() {
        assertEquals(Z, _rasterizedLineSegmentRenderable.z());
    }

    @Test
    void testDelete() {
        _rasterizedLineSegmentRenderable.delete();

        assertSame(_rasterizedLineSegmentRenderable, _consumedRasterizedLineSegmentRenderable);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _rasterizedLineSegmentRenderable.uuid());
    }
}
