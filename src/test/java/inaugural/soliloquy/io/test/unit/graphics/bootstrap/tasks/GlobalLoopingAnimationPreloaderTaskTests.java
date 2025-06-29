package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.GlobalLoopingAnimationPreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAnimation;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGlobalLoopingAnimationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;


import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalLoopingAnimationPreloaderTaskTests {
    private final Map<String, Animation> ANIMATIONS = mapOf();
    private final List<GlobalLoopingAnimationDefinitionDTO>
            GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS = listOf();
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
    private final List<GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS = listOf();

    private GlobalLoopingAnimationPreloaderTask globalLoopingAnimationPreloaderTask;

    @BeforeEach
    public void setUp() {
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

        globalLoopingAnimationPreloaderTask = new GlobalLoopingAnimationPreloaderTask(
                ANIMATIONS::get, GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS,
                GLOBAL_LOOPING_ANIMATION_FACTORY, GLOBAL_LOOPING_ANIMATIONS::add);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(null,
                        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS, GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS, null,
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        null, GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(), GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf((GlobalLoopingAnimationDefinitionDTO) null),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                    null, ANIMATION_ID_1,
                                    PERIOD_MODULO_OFFSET_1)
                        ),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                    "", ANIMATION_ID_1,
                                    PERIOD_MODULO_OFFSET_1)
                        ),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, null,
                                    PERIOD_MODULO_OFFSET_1)
                        ),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, "",
                                    PERIOD_MODULO_OFFSET_1)
                        ),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                    GLOBAL_LOOPING_ANIMATION_ID_1, ANIMATION_ID_1,
                                    -1)
                        ),
                        GLOBAL_LOOPING_ANIMATION_FACTORY,
                        GLOBAL_LOOPING_ANIMATIONS::add));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(ANIMATIONS::get,
                        GLOBAL_LOOPING_ANIMATION_DEFINITION_DTOS, GLOBAL_LOOPING_ANIMATION_FACTORY,
                        null));
    }

    @Test
    public void testRun() {
        globalLoopingAnimationPreloaderTask.run();

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
        GLOBAL_LOOPING_ANIMATION_FACTORY.Outputs.forEach(
                factoryOutput -> assertTrue(GLOBAL_LOOPING_ANIMATIONS.contains(factoryOutput)));
    }
}
