package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGlobalLoopingAnimation;
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
import soliloquy.specs.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
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
public class GlobalLoopingAnimationRenderableFactoryImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final Map<Integer, Consumer<EventInputs>> ON_PRESS_CONSUMERS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private Consumer<EventInputs> mockOnMouseOver;
    @Mock private Consumer<EventInputs> mockOnMouseLeave;
    @Mock private ProviderAtTime<FloatBox> mockRenderingAreaProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private GlobalLoopingAnimationRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new GlobalLoopingAnimationRenderableFactoryImpl(mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        var globalLoopingAnimationRenderableSupportingMouseEvents =
                factory.make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent
                );

        var globalLoopingAnimationRenderableNotSupportingMouseEvents =
                factory.make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, mockBorderColorProvider, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent
                );

        assertNotNull(globalLoopingAnimationRenderableSupportingMouseEvents);
        assertNotNull(globalLoopingAnimationRenderableNotSupportingMouseEvents);
        assertInstanceOf(GlobalLoopingAnimationRenderableImpl.class,
                globalLoopingAnimationRenderableSupportingMouseEvents);
        assertInstanceOf(GlobalLoopingAnimationRenderableImpl.class,
                globalLoopingAnimationRenderableNotSupportingMouseEvents);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(null, mockBorderThicknessProvider, mockBorderColorProvider, ON_PRESS_CONSUMERS,
                        null, mockOnMouseOver, mockOnMouseLeave, COLOR_SHIFTS,
                        mockRenderingAreaProvider,
                        Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                        mockBorderColorProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                        null, ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                        null, ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, null, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, mockRenderingAreaProvider, Z, null,
                        mockContainingComponent));

        assertThrows(IllegalArgumentException.class, () -> factory
                .make(null, mockBorderThicknessProvider, mockBorderColorProvider, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        mockBorderColorProvider, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, null, COLOR_SHIFTS, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, null, COLOR_SHIFTS, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, mockBorderColorProvider, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, mockBorderColorProvider, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, mockBorderColorProvider, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent));
    }
}
