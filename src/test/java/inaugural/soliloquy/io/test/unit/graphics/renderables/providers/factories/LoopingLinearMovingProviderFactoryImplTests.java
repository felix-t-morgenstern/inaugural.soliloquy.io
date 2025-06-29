package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeLoopingLinearMovingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class LoopingLinearMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();
    private final LoopingLinearMovingProvider<Float> FACTORY_1_OUTPUT =
            new FakeLoopingLinearMovingProvider<>();
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Integer, Function<Integer, Function<Map,
            Function<Long, Function<Long, LoopingLinearMovingProvider>>>>>>
            FACTORY_1 =
            uuid -> periodDuration -> periodModuloOffset -> valuesAtTime -> pausedTimestamp ->
                    mostRecentTimestamp -> {
                        factory1InputUuid = uuid;
                        factory1InputPeriodDuration = periodDuration;
                        factory1InputPeriodModuloOffset = periodModuloOffset;
                        factory1InputValuesAtTimes = valuesAtTime;
                        factory1InputPausedTimestamp = pausedTimestamp;
                        factory1InputMostRecentTimestamp = mostRecentTimestamp;
                        return FACTORY_1_OUTPUT;
                    };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    private final LoopingLinearMovingProvider<FloatBox> FACTORY_2_OUTPUT =
            new FakeLoopingLinearMovingProvider<>();
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Integer, Function<Integer, Function<Map, Function<Long,
            Function<Long, LoopingLinearMovingProvider>>>>>> FACTORY_2 =
            _ -> _ -> _ -> _ -> _ -> _ -> FACTORY_2_OUTPUT;

    private UUID factory1InputUuid;
    private int factory1InputPeriodDuration;
    private int factory1InputPeriodModuloOffset;
    /** @noinspection rawtypes */
    private Map factory1InputValuesAtTimes;
    private Long factory1InputPausedTimestamp;
    private Long factory1InputMostRecentTimestamp;

    private LoopingLinearMovingProviderFactory loopingLinearMovingProviderFactory;

    @BeforeEach
    public void setUp() {
        loopingLinearMovingProviderFactory = new LoopingLinearMovingProviderFactoryImpl(
                mapOf(
                        pairOf(FACTORY_1_TYPE_NAME, FACTORY_1),
                        pairOf(FACTORY_2_TYPE_NAME, FACTORY_2)
                )
        );
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(mapOf(pairOf(null, FACTORY_1))));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(mapOf(pairOf("", FACTORY_1))));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(
                        mapOf(pairOf(FACTORY_1_TYPE_NAME, null))));
    }

    @Test
    public void testMake() {
        var uuid = UUID.randomUUID();
        var periodDuration = 456;
        var periodModuloOffset = 123;
        var valuesAtTimestamps = mapOf(
                pairOf(0, 123f),
                pairOf(1, 456f),
                pairOf(2, 789f)
        );
        var pausedTimestamp = 123123L;
        var mostRecentTimestamp = 456456L;

        var provider = loopingLinearMovingProviderFactory
                .make(uuid, periodDuration, periodModuloOffset, valuesAtTimestamps,
                        pausedTimestamp, mostRecentTimestamp);

        assertSame(FACTORY_1_OUTPUT, provider);
        assertSame(uuid, factory1InputUuid);
        assertEquals(periodDuration, factory1InputPeriodDuration);
        assertEquals(periodModuloOffset, factory1InputPeriodModuloOffset);
        assertSame(valuesAtTimestamps, factory1InputValuesAtTimes);
        assertEquals(pausedTimestamp, factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, factory1InputMostRecentTimestamp);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.
}
