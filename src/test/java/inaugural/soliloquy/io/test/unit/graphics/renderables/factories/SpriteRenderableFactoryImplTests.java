package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.SpriteRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeSprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class SpriteRenderableFactoryImplTests {
    private final FakeSprite SPRITE_SUPPORTS_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(true));
    private final FakeSprite SPRITE_NOT_SUPPORTS_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(false));
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = listOf();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private SpriteRenderableFactory spriteRenderableFactory;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);

        spriteRenderableFactory = new SpriteRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableFactoryImpl(null));
    }

    @Test
    public void testMake() {
        SpriteRenderable spriteRenderableWithMouseEvents = spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(spriteRenderableWithMouseEvents);
        assertInstanceOf(SpriteRenderableImpl.class, spriteRenderableWithMouseEvents);
        assertTrue(spriteRenderableWithMouseEvents.getCapturesMouseEvents());

        SpriteRenderable spriteRenderableWithoutMouseEvents = spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack);

        assertNotNull(spriteRenderableWithoutMouseEvents);
        assertInstanceOf(SpriteRenderableImpl.class, spriteRenderableWithoutMouseEvents);
        assertFalse(spriteRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_NOT_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, null,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                null, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, null, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableFactory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null
        ));
    }
}
