package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.TriangleRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

class TriangleRenderableFactoryImplTests {
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_3_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = randomFloatWithInclusiveFloor(0f);
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = randomFloatWithInclusiveFloor(0f);
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = r -> {};
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = r -> {};

    private TriangleRenderableFactory _triangleRenderableFactory;

    @BeforeEach
    void setUp() {
        _triangleRenderableFactory = new TriangleRenderableFactoryImpl();
    }

    @Test
    void testMake() {
        TriangleRenderable triangleRenderable = _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER);

        assertNotNull(triangleRenderable);
        assertTrue(triangleRenderable instanceof TriangleRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(null, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        null, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, null,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        null, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, null,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, randomFloatWithInclusiveCeiling(-0.001f),
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        randomFloatWithInclusiveCeiling(-0.001f), null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, null,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        null, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, null, null, null, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TriangleRenderableFactory.class.getCanonicalName(),
                _triangleRenderableFactory.getInterfaceName());
    }
}
