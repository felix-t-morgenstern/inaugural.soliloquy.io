package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.FiniteAnimationRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAnimation;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRenderableFactoryImplTests {
    private final String ANIMATION_SUPPORTING_ID = "animationSupportingId";
    private final FakeAnimation ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_SUPPORTING_ID, true);
    private final String ANIMATION_NOT_SUPPORTING_ID = "animationNotSupportingId";
    private final FakeAnimation ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_NOT_SUPPORTING_ID, false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeAction<EventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<EventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final long START_TIMESTAMP = 111L;
    private final Long PAUSED_TIMESTAMP = -456L;
    private final Long TIMESTAMP = -123L;

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteAnimationRenderableFactory factory;

    @BeforeEach
    public void setUp() {

        factory = new FiniteAnimationRenderableFactoryImpl(mockRenderingBoundaries,
                mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        var finiteAnimationRenderableWithMouseEvents =
                factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                );
        var finiteAnimationRenderableWithoutMouseEvents =
                factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, START_TIMESTAMP,
                        PAUSED_TIMESTAMP
                );

        assertNotNull(finiteAnimationRenderableWithMouseEvents);
        assertNotNull(finiteAnimationRenderableWithoutMouseEvents);
        assertInstanceOf(FiniteAnimationRenderableImpl.class,
                finiteAnimationRenderableWithMouseEvents);
        assertInstanceOf(FiniteAnimationRenderableImpl.class,
                finiteAnimationRenderableWithoutMouseEvents);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        null, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, START_TIMESTAMP,
                        PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent, START_TIMESTAMP,
                        PAUSED_TIMESTAMP
                ));


        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        null, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS, null, Z, UUID,
                        mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                        null, mockContainingComponent, START_TIMESTAMP, PAUSED_TIMESTAMP
                ));
    }
}
