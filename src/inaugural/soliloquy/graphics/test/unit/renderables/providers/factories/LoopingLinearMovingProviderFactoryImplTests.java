package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeLoopingLinearMovingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class LoopingLinearMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();
    private final LoopingLinearMovingProvider<Float> FACTORY_1_OUTPUT =
            new FakeLoopingLinearMovingProvider<>();
    /** @noinspection rawtypes*/
    private final Function<EntityUuid, Function<Integer, Function<Integer, Function<Map,
            Function<Long, Function<Long, Function<Object, LoopingLinearMovingProvider>>>>>>>
            FACTORY_1 =
            uuid -> periodDuration -> periodModuloOffset -> valuesAtTime -> pausedTimestamp ->
                    mostRecentTimestamp -> archetype -> {
                        _factory1InputUuid = uuid;
                        _factory1InputPeriodDuration = periodDuration;
                        _factory1InputPeriodModuloOffset = periodModuloOffset;
                        _factory1InputValuesAtTimes = valuesAtTime;
                        _factory1InputPausedTimestamp = pausedTimestamp;
                        _factory1InputMostRecentTimestamp = mostRecentTimestamp;
                        _factory1InputArchetype = (float) archetype;
                        return FACTORY_1_OUTPUT;
                    };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    private final LoopingLinearMovingProvider<FloatBox> FACTORY_2_OUTPUT =
            new FakeLoopingLinearMovingProvider<>();
    /** @noinspection rawtypes*/
    private final Function<EntityUuid, Function<Integer, Function<Integer, Function<Map, Function<Long, Function<Long,
            Function<Object, LoopingLinearMovingProvider>>>>>>> FACTORY_2 =
            uuid -> periodDuration -> periodModuloOffset -> valuesAtTime -> pausedTimestamp ->
                    mostRecentTimestamp -> archetype -> {
                        _factory2InputUuid = uuid;
                        _factory2InputPeriodDuration = periodDuration;
                        _factory2InputPeriodModuloOffset = periodModuloOffset;
                        _factory2InputValuesAtTimes = valuesAtTime;
                        _factory2InputPausedTimestamp = pausedTimestamp;
                        _factory2InputMostRecentTimestamp = mostRecentTimestamp;
                        _factory2InputArchetype = (FloatBox) archetype;
                        return FACTORY_2_OUTPUT;
                    };

    private EntityUuid _factory1InputUuid;
    private int _factory1InputPeriodDuration;
    private int _factory1InputPeriodModuloOffset;
    /** @noinspection rawtypes*/
    private Map _factory1InputValuesAtTimes;
    private Long _factory1InputPausedTimestamp;
    private Long _factory1InputMostRecentTimestamp;
    private float _factory1InputArchetype;

    private EntityUuid _factory2InputUuid;
    private int _factory2InputPeriodDuration;
    private int _factory2InputPeriodModuloOffset;
    /** @noinspection rawtypes*/
    private Map _factory2InputValuesAtTimes;
    private Long _factory2InputPausedTimestamp;
    private Long _factory2InputMostRecentTimestamp;
    private FloatBox _factory2InputArchetype;

    private LoopingLinearMovingProviderFactory _loopingLinearMovingProviderFactory;

    @BeforeEach
    void setUp() {
        //noinspection rawtypes
        _loopingLinearMovingProviderFactory = new LoopingLinearMovingProviderFactoryImpl(
                new HashMap<String, Function<EntityUuid, Function<Integer, Function<Integer,
                        Function<Map, Function<Long, Function<Long, Function<Object,
                                LoopingLinearMovingProvider>>>>>>>>() {{
                    put(FACTORY_1_TYPE_NAME, FACTORY_1);
                    put(FACTORY_2_TYPE_NAME, FACTORY_2);
                }}
        );
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(null));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid, Function<Integer,
                                Function<Integer, Function<Map, Function<Long, Function<Long,
                                        Function<Object, LoopingLinearMovingProvider>>>>>>>>() {{
                            put(null, FACTORY_1);
                        }}));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid, Function<Integer,
                                Function<Integer, Function<Map, Function<Long, Function<Long,
                                        Function<Object, LoopingLinearMovingProvider>>>>>>>>() {{
                            put("", FACTORY_1);
                        }}));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid, Function<Integer,
                                Function<Integer, Function<Map, Function<Long, Function<Long,
                                        Function<Object, LoopingLinearMovingProvider>>>>>>>>() {{
                            put(FACTORY_1_TYPE_NAME, null);
                        }}));
    }

    @Test
    void testMake() {
        FakeEntityUuid uuid = new FakeEntityUuid();
        int periodDuration = 456;
        int periodModuloOffset = 123;
        HashMap<Integer, Float> valuesAtTimestamps = new HashMap<Integer, Float>() {{
            put(0, 123f);
            put(1, 456f);
            put(2, 789f);
        }};
        Long pausedTimestamp = 123123L;
        Long mostRecentTimestamp = 456456L;
        float archetype = 0.123123f;

        LoopingLinearMovingProvider<Float> provider = _loopingLinearMovingProviderFactory
                .make(uuid, periodDuration, periodModuloOffset, valuesAtTimestamps,
                        pausedTimestamp, mostRecentTimestamp, archetype);

        assertSame(FACTORY_1_OUTPUT, provider);
        assertSame(uuid, _factory1InputUuid);
        assertEquals(periodDuration, _factory1InputPeriodDuration);
        assertEquals(periodModuloOffset, _factory1InputPeriodModuloOffset);
        assertSame(valuesAtTimestamps, _factory1InputValuesAtTimes);
        assertEquals(pausedTimestamp, _factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, _factory1InputMostRecentTimestamp);
        assertEquals(archetype, _factory1InputArchetype);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.

    @Test
    void testGetInterfaceName() {
        assertEquals(LoopingLinearMovingProviderFactory.class.getCanonicalName(),
                _loopingLinearMovingProviderFactory.getInterfaceName());
    }
}
