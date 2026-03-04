package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.SpriteRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeSprite;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SpriteRenderableFactoryImplTests {
    private final FakeSprite SPRITE_SUPPORTS_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(true));
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;
    @Mock private ProviderAtTime<Color> BORDER_COLOR_PROVIDER;
    @Mock private Consumer<EventInputs> mockOnMouseOver;
    @Mock private Consumer<EventInputs> mockOnMouseLeave;
    @Mock private ProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private SpriteRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new SpriteRenderableFactoryImpl(mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        var spriteRenderableWithMouseEvents = factory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                null, null, mockOnMouseOver, mockOnMouseLeave, COLOR_SHIFTS,
                RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(spriteRenderableWithMouseEvents);
        assertInstanceOf(SpriteRenderableImpl.class, spriteRenderableWithMouseEvents);

        var spriteRenderableWithoutMouseEvents = factory.make(
                SPRITE_SUPPORTS_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(spriteRenderableWithoutMouseEvents);
        assertInstanceOf(SpriteRenderableImpl.class, spriteRenderableWithoutMouseEvents);
        assertFalse(spriteRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, null,
                        mockContainingComponent));

        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(SPRITE_SUPPORTS_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingComponent));
    }
}
