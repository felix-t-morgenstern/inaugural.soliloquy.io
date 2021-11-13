package inaugural.soliloquy.graphics.test.unit.bootstrap.workers;

import inaugural.soliloquy.common.test.fakes.FakeRegistry;
import inaugural.soliloquy.graphics.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.workers.GlobalLoopingAnimationPreloaderWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
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
    private final String GLOBAL_LOOPING_ANIMATION_ID_1 = "globalLoopingAnimationId1";
    private final String GLOBAL_LOOPING_ANIMATION_ID_2 = "globalLoopingAnimationId2";
    private final int PERIOD_MODULO_OFFSET_1 = 123;
    private final int PERIOD_MODULO_OFFSET_2 = 456;
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
        ANIMATIONS.put(ANIMATION_ID_1, ANIMATION_1);
        ANIMATIONS.put(ANIMATION_ID_2, ANIMATION_2);
        GlobalLoopingAnimationDefinitionDTO definitionDTO1 =
                new GlobalLoopingAnimationDefinitionDTO(GLOBAL_LOOPING_ANIMATION_ID_1,
                        ANIMATION_ID_1, PERIOD_MODULO_OFFSET_1);
        GlobalLoopingAnimationDefinitionDTO definitionDTO2 =
                new GlobalLoopingAnimationDefinitionDTO(GLOBAL_LOOPING_ANIMATION_ID_2,
                        ANIMATION_ID_2, PERIOD_MODULO_OFFSET_2);
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.add(definitionDTO1);
        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS.add(definitionDTO2);

        _globalLoopingAnimationPreloaderWorker = new GlobalLoopingAnimationPreloaderWorker(
                ANIMATIONS::get, GLOBAL_LOOPING_ANIMATION_FACTORY,
                GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS, GLOBAL_LOOPING_ANIMATIONS::add);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(null,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS,
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        null, GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS,
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, null,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, new ArrayList<>(),
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(null);
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(new GlobalLoopingAnimationDefinitionDTO(
                                    null, ANIMATION_ID_1,
                                    PERIOD_MODULO_OFFSET_1));
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(new GlobalLoopingAnimationDefinitionDTO(
                                    "", ANIMATION_ID_1,
                                    PERIOD_MODULO_OFFSET_1));
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, null,
                                    PERIOD_MODULO_OFFSET_1));
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, "",
                                    PERIOD_MODULO_OFFSET_1));
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        new ArrayList<GlobalLoopingAnimationDefinitionDTO>() {{
                            add(new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, ANIMATION_ID_1,
                                    -1));
                        }},
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderWorker(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_FACTORY, GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS,
                        null));
    }

    @Test
    void testRun() {
        _globalLoopingAnimationPreloaderWorker.run();

        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds
                .contains(GLOBAL_LOOPING_ANIMATION_ID_1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputIds
                .contains(GLOBAL_LOOPING_ANIMATION_ID_2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.contains(ANIMATION_1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputAnimations.contains(ANIMATION_2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets.size());
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets
                .contains(PERIOD_MODULO_OFFSET_1));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.InputPeriodModuloOffsets
                .contains(PERIOD_MODULO_OFFSET_2));
        assertEquals(2, GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.size());
        assertEquals(2, GLOBAL_LOOPING_ANIMATIONS.size());
        assertTrue(GLOBAL_LOOPING_ANIMATIONS.contains(GLOBAL_LOOPING_ANIMATION_ID_1));
        assertTrue(GLOBAL_LOOPING_ANIMATIONS.contains(GLOBAL_LOOPING_ANIMATION_ID_2));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.contains(
                GLOBAL_LOOPING_ANIMATIONS.get(GLOBAL_LOOPING_ANIMATION_ID_1)));
        assertTrue(GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.contains(
                GLOBAL_LOOPING_ANIMATIONS.get(GLOBAL_LOOPING_ANIMATION_ID_2)));
    }
}
