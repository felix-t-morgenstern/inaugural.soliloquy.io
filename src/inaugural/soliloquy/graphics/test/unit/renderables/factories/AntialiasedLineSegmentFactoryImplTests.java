package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;

import java.awt.*;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

class AntialiasedLineSegmentFactoryImplTests {
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> LENGTH_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final java.util.UUID UUID = java.util.UUID.randomUUID();
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER =
            r -> _updateZIndexInContainerInput = r;
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER =
            r -> _removeFromContainerInput = r;

    private Renderable _updateZIndexInContainerInput;
    private Renderable _removeFromContainerInput;

    private AntialiasedLineSegmentRenderableFactory _antialiasedLineSegmentRenderableFactory;

    @BeforeEach
    void setUp() {
        _antialiasedLineSegmentRenderableFactory =
                new AntialiasedLineSegmentRenderableFactoryImpl(UPDATE_Z_INDEX_IN_CONTAINER,
                        REMOVE_FROM_CONTAINER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderableFactoryImpl(null, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderableFactoryImpl(UPDATE_Z_INDEX_IN_CONTAINER,
                        null));
    }

    @Test
    void testMake() {
        AntialiasedLineSegmentRenderable antialiasedLineSegmentRenderable =
                _antialiasedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, COLOR_PROVIDER,
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID);

        assertNotNull(antialiasedLineSegmentRenderable);
        assertTrue(
                antialiasedLineSegmentRenderable instanceof AntialiasedLineSegmentRenderableImpl);

        int newZ = 456;
        antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, antialiasedLineSegmentRenderable.getZ());
        assertSame(antialiasedLineSegmentRenderable, _updateZIndexInContainerInput);

        antialiasedLineSegmentRenderable.delete();

        assertSame(antialiasedLineSegmentRenderable, _removeFromContainerInput);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(null, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        null, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, null,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        null,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        null, Z, UUID));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AntialiasedLineSegmentRenderableFactory.class.getCanonicalName(),
                _antialiasedLineSegmentRenderableFactory.getInterfaceName());
    }
}
