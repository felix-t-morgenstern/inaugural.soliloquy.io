package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFiniteLinearMovingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();
    private final FiniteLinearMovingProvider<Float> FACTORY_1_OUTPUT =
            new FakeFiniteLinearMovingProvider<>();
    /** @noinspection rawtypes*/
    private final Function<EntityUuid,Function<Map,Function<Long, Function<Long,
            FiniteLinearMovingProvider>>>> FACTORY_1 =
            uuid -> valuesAtTime -> pausedTimestamp -> mostRecentTimestamp -> {
                _factory1InputUuid = uuid;
                _factory1InputValuesAtTimes = valuesAtTime;
                _factory1InputPausedTimestamp = pausedTimestamp;
                _factory1InputMostRecentTimestamp = mostRecentTimestamp;
                return FACTORY_1_OUTPUT;
            };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    private final FiniteLinearMovingProvider<FloatBox> FACTORY_2_OUTPUT =
            new FakeFiniteLinearMovingProvider<>();
    /** @noinspection rawtypes*/
    private final Function<EntityUuid,Function<Map,Function<Long, Function<Long,
            FiniteLinearMovingProvider>>>> FACTORY_2 =
            uuid -> valuesAtTime -> pausedTimestamp -> mostRecentTimestamp -> {
                _factory2InputUuid = uuid;
                _factory2InputValuesAtTimes = valuesAtTime;
                _factory2InputPausedTimestamp = pausedTimestamp;
                _factory2InputMostRecentTimestamp = mostRecentTimestamp;
                return FACTORY_2_OUTPUT;
            };

    private EntityUuid _factory1InputUuid;
    /** @noinspection rawtypes*/
    private Map _factory1InputValuesAtTimes;
    private Long _factory1InputPausedTimestamp;
    private Long _factory1InputMostRecentTimestamp;

    private EntityUuid _factory2InputUuid;
    /** @noinspection rawtypes*/
    private Map _factory2InputValuesAtTimes;
    private Long _factory2InputPausedTimestamp;
    private Long _factory2InputMostRecentTimestamp;

    private FiniteLinearMovingProviderFactory _finiteLinearMovingProviderFactory;

    @BeforeEach
    void setUp() {
        //noinspection rawtypes
        _finiteLinearMovingProviderFactory = new FiniteLinearMovingProviderFactoryImpl(
                new HashMap<String, Function<EntityUuid,Function<Map,Function<Long, Function<Long,
                        FiniteLinearMovingProvider>>>>>() {{
                    put(FACTORY_1_TYPE_NAME, FACTORY_1);
                    put(FACTORY_2_TYPE_NAME, FACTORY_2);
                }});
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(new HashMap<>()));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid,Function<Map,Function<Long, Function<Long,
                                FiniteLinearMovingProvider>>>>>() {{
                            put(null, FACTORY_1);
                        }}));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid,Function<Map,Function<Long, Function<Long,
                                FiniteLinearMovingProvider>>>>>() {{
                            put("", FACTORY_1);
                        }}));
        //noinspection rawtypes
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        new HashMap<String, Function<EntityUuid,Function<Map,Function<Long, Function<Long,
                                FiniteLinearMovingProvider>>>>>() {{
                            put(FACTORY_1_TYPE_NAME, null);
                        }}));
    }

    @Test
    void testMake() {
        FakeEntityUuid uuid = new FakeEntityUuid();
        HashMap<Long, Float> valuesAtTimestamps = new HashMap<Long, Float>() {{
            put(0L, 123f);
            put(1L, 456f);
            put(2L, 789f);
        }};
        Long pausedTimestamp = 123123L;
        Long mostRecentTimestamp = 456456L;

        FiniteLinearMovingProvider<Float> provider = _finiteLinearMovingProviderFactory
                .make(uuid, valuesAtTimestamps, pausedTimestamp, mostRecentTimestamp);

        assertSame(FACTORY_1_OUTPUT, provider);
        assertSame(uuid, _factory1InputUuid);
        assertSame(valuesAtTimestamps, _factory1InputValuesAtTimes);
        assertEquals(pausedTimestamp, _factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, _factory1InputMostRecentTimestamp);
    }
}
