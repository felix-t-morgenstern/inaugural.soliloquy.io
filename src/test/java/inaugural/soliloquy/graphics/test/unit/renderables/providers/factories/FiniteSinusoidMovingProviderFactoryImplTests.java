package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.random.Random.randomFloat;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FiniteSinusoidMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();
    /** @noinspection rawtypes*/
    @Mock private FiniteSinusoidMovingProvider _factory1Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<List<Float>, Function<Long, Function<Long,
            FiniteSinusoidMovingProvider>>>>> FACTORY_1 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                _factory1InputUuid = uuid;
                _factory1InputValuesAtTimes = valuesAtTime;
                _factory1InputTransitionSharpnesses = sharpnesses;
                _factory1InputPausedTimestamp = pausedTimestamp;
                _factory1InputMostRecentTimestamp = mostRecentTimestamp;
                _factory1Output = mock(FiniteSinusoidMovingProvider.class);
                return _factory1Output;
            };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    /** @noinspection rawtypes*/
    @Mock private FiniteSinusoidMovingProvider _factory2Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<List<Float>, Function<Long, Function<Long,
            FiniteSinusoidMovingProvider>>>>> FACTORY_2 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                _factory2InputUuid = uuid;
                _factory2InputValuesAtTimes = valuesAtTime;
                _factory2InputTransitionSharpnesses = sharpnesses;
                _factory2InputPausedTimestamp = pausedTimestamp;
                _factory2InputMostRecentTimestamp = mostRecentTimestamp;
                _factory2Output = mock(FiniteSinusoidMovingProvider.class);
                return _factory2Output;
            };

    private UUID _factory1InputUuid;
    /** @noinspection rawtypes */
    private Map _factory1InputValuesAtTimes;
    private List<Float> _factory1InputTransitionSharpnesses;
    private Long _factory1InputPausedTimestamp;
    private Long _factory1InputMostRecentTimestamp;

    @SuppressWarnings("unused")
    private UUID _factory2InputUuid;
    /** @noinspection rawtypes, unused */
    private Map _factory2InputValuesAtTimes;
    private List<Float> _factory2InputTransitionSharpnesses;
    @SuppressWarnings("unused")
    private Long _factory2InputPausedTimestamp;
    @SuppressWarnings("unused")
    private Long _factory2InputMostRecentTimestamp;

    private FiniteSinusoidMovingProviderFactory _finiteSinusoidMovingProviderFactory;

    @BeforeEach
    void setUp() {
        //noinspection rawtypes
        _finiteSinusoidMovingProviderFactory = new FiniteSinusoidMovingProviderFactoryImpl(
                new HashMap<String, Function<UUID, Function<Map, Function<List<Float>,
                        Function<Long, Function<Long, FiniteSinusoidMovingProvider>>>>>>() {{
                    put(FACTORY_1_TYPE_NAME, FACTORY_1);
                    put(FACTORY_2_TYPE_NAME, FACTORY_2);
                }});
    }

    /** @noinspection rawtypes */
    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(new HashMap<>()));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
                        new HashMap<String, Function<UUID, Function<Map, Function<List<Float>,
                                Function<Long, Function<Long, FiniteSinusoidMovingProvider>>>>>>() {{
                            put(null, FACTORY_1);
                        }}));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
                        new HashMap<String, Function<UUID, Function<Map, Function<List<Float>,
                                Function<Long, Function<Long, FiniteSinusoidMovingProvider>>>>>>() {{
                            put("", FACTORY_1);
                        }}));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
                        new HashMap<String, Function<UUID, Function<Map, Function<List<Float>,
                                Function<Long, Function<Long, FiniteSinusoidMovingProvider>>>>>>() {{
                            put(FACTORY_1_TYPE_NAME, null);
                        }}));
    }

    @Test
    void testMake() {
        UUID uuid = UUID.randomUUID();
        HashMap<Long, Float> valuesAtTimestamps = new HashMap<Long, Float>() {{
            put(0L, randomFloat());
            put(1L, randomFloat());
            put(2L, randomFloat());
        }};
        ArrayList<Float> transitionSharpnesses = new ArrayList<Float>() {{
            add(randomFloat());
            add(randomFloat());
        }};
        Long pausedTimestamp = randomLong();
        Long mostRecentTimestamp = pausedTimestamp + 1;

        FiniteSinusoidMovingProvider<Float> provider = _finiteSinusoidMovingProviderFactory
                .make(uuid, valuesAtTimestamps, transitionSharpnesses, pausedTimestamp,
                        mostRecentTimestamp);

        assertNotNull(provider);
        assertSame(_factory1Output, provider);
        assertSame(uuid, _factory1InputUuid);
        assertSame(valuesAtTimestamps, _factory1InputValuesAtTimes);
        assertSame(transitionSharpnesses, _factory1InputTransitionSharpnesses);
        assertEquals(pausedTimestamp, _factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, _factory1InputMostRecentTimestamp);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteSinusoidMovingProviderFactory.class.getCanonicalName(),
                _finiteSinusoidMovingProviderFactory.getInterfaceName());
    }
}
