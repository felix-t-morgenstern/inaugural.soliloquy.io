package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.SpriteRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SpriteRenderableFactoryImplTests {
    private final FakeSprite SPRITE_SUPPORTS_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(true));
    private final FakeSprite SPRITE_NOT_SUPPORTS_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(false));
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = renderable -> {};
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = renderable -> {};

    private final UUID UUID = java.util.UUID.randomUUID();

    private SpriteRenderableFactory _spriteRenderableFactory;

    @BeforeEach
    void setUp() {
        _spriteRenderableFactory = new SpriteRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(SpriteRenderableFactory.class.getCanonicalName(),
                _spriteRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        SpriteRenderable spriteRenderableWithMouseEvents = _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);

        assertNotNull(spriteRenderableWithMouseEvents);
        assertTrue(spriteRenderableWithMouseEvents instanceof SpriteRenderableImpl);
        assertTrue(spriteRenderableWithMouseEvents.getCapturesMouseEvents());

        SpriteRenderable spriteRenderableWithoutMouseEvents = _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);

        assertNotNull(spriteRenderableWithoutMouseEvents);
        assertTrue(spriteRenderableWithoutMouseEvents instanceof SpriteRenderableImpl);
        assertFalse(spriteRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_NOT_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, null,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                null, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, null, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, null, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, null,
                UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
    }
}
