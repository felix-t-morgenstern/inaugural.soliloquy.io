package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

class AntialiasedLineSegmentRenderableImplTests {
    private final FakeProviderAtTime<Pair<Float, Float>> VERTEX_1_LOCATION_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Pair<Float, Float>> VERTEX_2_LOCATION_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> LENGTH_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER =
            r -> _updateZIndexInContainerInput = r;
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER =
            r -> _removeFromContainerInput = r;

    private Renderable _updateZIndexInContainerInput;
    private Renderable _removeFromContainerInput;

    private AntialiasedLineSegmentRenderable _antialiasedLineSegmentRenderable;

    @BeforeEach
    void setUp() {
        _antialiasedLineSegmentRenderable = new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        );
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                null,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                null,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                null,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                null,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                null,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                null,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                null,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_LOCATION_PROVIDER,
                VERTEX_2_LOCATION_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
    }

    @Test
    void testSetAndGetVertexLocationProviders() {
        assertSame(VERTEX_1_LOCATION_PROVIDER,
                _antialiasedLineSegmentRenderable.getVertex1LocationProvider());
        assertSame(VERTEX_2_LOCATION_PROVIDER,
                _antialiasedLineSegmentRenderable.getVertex2LocationProvider());

        FakeProviderAtTime<Pair<Float, Float>> newVertex1LocationProvider =
                new FakeProviderAtTime<>();
        FakeProviderAtTime<Pair<Float, Float>> newVertex2LocationProvider =
                new FakeProviderAtTime<>();

        _antialiasedLineSegmentRenderable.setVertex1LocationProvider(newVertex1LocationProvider);
        _antialiasedLineSegmentRenderable.setVertex2LocationProvider(newVertex2LocationProvider);

        assertSame(newVertex1LocationProvider,
                _antialiasedLineSegmentRenderable.getVertex1LocationProvider());
        assertSame(newVertex2LocationProvider,
                _antialiasedLineSegmentRenderable.getVertex2LocationProvider());
    }

    @Test
    void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, _antialiasedLineSegmentRenderable.getThicknessProvider());

        FakeProviderAtTime<Float> newThicknessProvider = new FakeProviderAtTime<>();

        _antialiasedLineSegmentRenderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, _antialiasedLineSegmentRenderable.getThicknessProvider());
    }

    @Test
    void testSetThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _antialiasedLineSegmentRenderable.setThicknessProvider(null));
    }

    @Test
    void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, _antialiasedLineSegmentRenderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        _antialiasedLineSegmentRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, _antialiasedLineSegmentRenderable.getColorProvider());
    }

    @Test
    void testGetAndSetThicknessGradientPercentProvider() {
        assertSame(THICKNESS_GRADIENT_PERCENT_PROVIDER,
                _antialiasedLineSegmentRenderable.getThicknessGradientPercentProvider());

        ProviderAtTime<Float> newThicknessGradientPercentProvider = new FakeProviderAtTime<>();

        _antialiasedLineSegmentRenderable
                .setThicknessGradientPercentProvider(newThicknessGradientPercentProvider);

        assertSame(newThicknessGradientPercentProvider,
                _antialiasedLineSegmentRenderable.getThicknessGradientPercentProvider());
    }

    @Test
    void testGetAndSetLengthGradientPercentProvider() {
        assertSame(LENGTH_GRADIENT_PERCENT_PROVIDER,
                _antialiasedLineSegmentRenderable.getLengthGradientPercentProvider());

        ProviderAtTime<Float> newLengthGradientPercentProvider = new FakeProviderAtTime<>();

        _antialiasedLineSegmentRenderable
                .setLengthGradientPercentProvider(newLengthGradientPercentProvider);

        assertSame(newLengthGradientPercentProvider,
                _antialiasedLineSegmentRenderable.getLengthGradientPercentProvider());
    }

    @Test
    void testSetColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _antialiasedLineSegmentRenderable.setColorProvider(null));
    }

    @Test
    void testSetVertexLocationProvidersWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _antialiasedLineSegmentRenderable.setVertex1LocationProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _antialiasedLineSegmentRenderable.setVertex2LocationProvider(null));
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _antialiasedLineSegmentRenderable.getZ());

        int newZ = 456;
        _antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, _antialiasedLineSegmentRenderable.getZ());
        assertSame(_antialiasedLineSegmentRenderable, _updateZIndexInContainerInput);
    }

    @Test
    void testDelete() {
        _antialiasedLineSegmentRenderable.delete();

        assertSame(_antialiasedLineSegmentRenderable, _removeFromContainerInput);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AntialiasedLineSegmentRenderable.class.getCanonicalName(),
                _antialiasedLineSegmentRenderable.getInterfaceName());
    }
}
