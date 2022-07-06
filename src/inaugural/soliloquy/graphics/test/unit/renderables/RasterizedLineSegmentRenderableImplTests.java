package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RasterizedLineSegmentRenderableImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = 789;
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = renderable ->
            _renderableRemovedFromContainer = renderable;
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = renderable ->
            _renderableUpdatedInContainer = renderable;

    private static Renderable _renderableUpdatedInContainer;
    private static Renderable _renderableRemovedFromContainer;

    private static final UUID UUID = java.util.UUID.randomUUID();

    private RasterizedLineSegmentRenderable _rasterizedLineSegmentRenderable;

    @BeforeEach
    void setUp() {        _rasterizedLineSegmentRenderable = new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, null,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                null, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, null, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RasterizedLineSegmentRenderable.class.getCanonicalName(),
                _rasterizedLineSegmentRenderable.getInterfaceName());
    }

    @Test
    void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, _rasterizedLineSegmentRenderable.getThicknessProvider());

        FakeProviderAtTime<Float> newThicknessProvider = new FakeProviderAtTime<>();

        _rasterizedLineSegmentRenderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, _rasterizedLineSegmentRenderable.getThicknessProvider());
    }

    @Test
    void testSetThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setThicknessProvider(null));
    }

    @Test
    void testGetAndSetStipplePattern() {
        assertEquals(STIPPLE_PATTERN, _rasterizedLineSegmentRenderable.getStipplePattern());

        short newStipplePattern = 789;
        _rasterizedLineSegmentRenderable.setStipplePattern(newStipplePattern);

        assertEquals(newStipplePattern, _rasterizedLineSegmentRenderable.getStipplePattern());
    }

    @Test
    void testSetStipplePatternWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStipplePattern((short) 0));
    }

    @Test
    void testGetAndSetStippleFactor() {
        assertEquals(STIPPLE_FACTOR, _rasterizedLineSegmentRenderable.getStippleFactor());

        short newStippleFactor = 234;
        _rasterizedLineSegmentRenderable.setStippleFactor(newStippleFactor);

        assertEquals(newStippleFactor, _rasterizedLineSegmentRenderable.getStippleFactor());
    }

    @Test
    void testSetStippleFactorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStippleFactor((short) 0));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStippleFactor((short) 257));
    }

    @Test
    void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, _rasterizedLineSegmentRenderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        _rasterizedLineSegmentRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, _rasterizedLineSegmentRenderable.getColorProvider());
    }

    @Test
    void testSetColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setColorProvider(null));
    }

    @Test
    void testSetAndGetRenderingDimensionsProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _rasterizedLineSegmentRenderable.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();
        _rasterizedLineSegmentRenderable
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _rasterizedLineSegmentRenderable.getRenderingDimensionsProvider());
    }

    @Test
    void testSetRenderingDimensionsProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setRenderingDimensionsProvider(null));
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _rasterizedLineSegmentRenderable.getZ());

        int newZ = 456;
        _rasterizedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, _rasterizedLineSegmentRenderable.getZ());
        assertSame(_rasterizedLineSegmentRenderable, _renderableUpdatedInContainer);
    }

    @Test
    void testDelete() {
        _rasterizedLineSegmentRenderable.delete();

        assertSame(_rasterizedLineSegmentRenderable, _renderableRemovedFromContainer);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _rasterizedLineSegmentRenderable.uuid());
    }
}
