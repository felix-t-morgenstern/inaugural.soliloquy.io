package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.common.test.fakes.FakeRegistry;
import inaugural.soliloquy.graphics.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.GlobalLoopingAnimationPreloaderWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalLoopingAnimationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationPreloaderWorkerTests {
    private final HashMap<String, Animation> ANIMATIONS = new HashMap<>();
    private final ArrayList<GlobalLoopingAnimationDefinitionDTO>
            GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS = new ArrayList<>();
    private final String ANIMATION_ID_1 = "animationId1";
    private final String ANIMATION_ID_2 = "animationId2";
    private final FakeAnimation ANIMATION_1 = new FakeAnimation(ANIMATION_ID_1);
    private final FakeAnimation ANIMATION_2 = new FakeAnimation(ANIMATION_ID_2);
    private final FakeGlobalLoopingAnimationFactory GLOBAL_LOOPING_ANIMATION_FACTORY =
            new FakeGlobalLoopingAnimationFactory();
    private final FakeRegistry<GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS =
            new FakeRegistry<>(null);

    private GlobalLoopingAnimationPreloaderWorker _globalLoopingAnimationPreloaderWorker;

    @BeforeEach
    void setUp() {
        _globalLoopingAnimationPreloaderWorker = new GlobalLoopingAnimationPreloaderWorker(
                ANIMATIONS, GLOBAL_LOOPING_ANIMATION_FACTORY, GLOBAL_LOOPING_ANIMATIONS);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(null,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, GLOBAL_LOOPING_ANIMATIONS));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS,
                        null, GLOBAL_LOOPING_ANIMATIONS));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, null));
    }

    @Test
    void testRun() {
        ANIMATIONS.put(ANIMATION_ID_1, ANIMATION_1);
        ANIMATIONS.put(ANIMATION_ID_2, ANIMATION_2);
        String globalLoopingAnimationId1 = "globalLoopingAnimationId1";
        String globalLoopingAnimationId2 = "globalLoopingAnimationId2";
        int periodModuloOffset1 = 123;
        int periodModuloOffset2 = 456;
        GlobalLoopingAnimationDefinitionDTO definitionDTO1 =
                new GlobalLoopingAnimationDefinitionDTO(globalLoopingAnimationId1, ANIMATION_ID_1,
                        periodModuloOffset1);
        GlobalLoopingAnimationDefinitionDTO definitionDTO2 =
                new GlobalLoopingAnimationDefinitionDTO(globalLoopingAnimationId2, ANIMATION_ID_2,
                        periodModuloOffset2);
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.add(definitionDTO1);
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.add(definitionDTO2);

        _globalLoopingAnimationPreloaderWorker.run(GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS);

        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds.contains(globalLoopingAnimationId1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds.contains(globalLoopingAnimationId2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.contains(ANIMATION_1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.contains(ANIMATION_2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets
                .contains(periodModuloOffset1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets
                .contains(periodModuloOffset2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.size());
        assertEquals(2, GLOBAL_LOOPING_ANIMATIONS.size());
        assertTrue(GLOBAL_LOOPING_ANIMATIONS.contains(globalLoopingAnimationId1));
        assertTrue(GLOBAL_LOOPING_ANIMATIONS.contains(globalLoopingAnimationId2));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.contains(
                GLOBAL_LOOPING_ANIMATIONS.get(globalLoopingAnimationId1)));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.contains(
                GLOBAL_LOOPING_ANIMATIONS.get(globalLoopingAnimationId2)));
    }
}
