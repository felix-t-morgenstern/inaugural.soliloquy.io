package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.io.bootstrap.tasks.GlobalLoopingAnimationPreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GlobalLoopingAnimationPreloaderTaskTests {
    private final String GLOBAL_LOOPING_ANIMATION_ID = randomString();
    private final int PERIOD_MODULO_OFFSET = randomIntWithInclusiveFloor(0);

    private final String ANIMATION_ID = randomString();
    private final LookupAndEntitiesWithId<Animation> MOCK_ANIMATION_AND_LOOKUP =
            generateMockLookupFunctionWithId(Animation.class, ANIMATION_ID);
    private final Animation MOCK_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.entities.getFirst();
    private final Function<String, Animation> MOCK_GET_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.lookup;

    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation;
    @Mock private Consumer<GlobalLoopingAnimation> mockProcessResult;

    private List<GlobalLoopingAnimationDefinitionDTO> mockDefinitionDtos;

    @Mock private Function<GlobalLoopingAnimationDefinition, GlobalLoopingAnimation>
            mockGlobalLoopingAnimationFactory;
    private GlobalLoopingAnimationDefinition factoryCapture;

    private GlobalLoopingAnimationPreloaderTask globalLoopingAnimationPreloaderTask;

    @BeforeEach
    public void setUp() {
        var definitionDTO =
                new GlobalLoopingAnimationDefinitionDTO(GLOBAL_LOOPING_ANIMATION_ID, ANIMATION_ID,
                        PERIOD_MODULO_OFFSET);
        mockDefinitionDtos = generateMockList(definitionDTO);

        lenient().when(mockGlobalLoopingAnimationFactory.apply(any())).thenAnswer(invocation -> {
            factoryCapture = invocation.getArgument(0);
            return mockGlobalLoopingAnimation;
        });

        globalLoopingAnimationPreloaderTask = new GlobalLoopingAnimationPreloaderTask(
                MOCK_GET_ANIMATION, mockDefinitionDtos,
                mockGlobalLoopingAnimationFactory, mockProcessResult);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(null,
                        mockDefinitionDtos, mockGlobalLoopingAnimationFactory,
                        mockProcessResult));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        mockDefinitionDtos, null,
                        mockProcessResult));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        null, mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(), mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf((GlobalLoopingAnimationDefinitionDTO) null),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        null, ANIMATION_ID,
                                        PERIOD_MODULO_OFFSET)
                        ),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        "", ANIMATION_ID,
                                        PERIOD_MODULO_OFFSET)
                        ),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        GLOBAL_LOOPING_ANIMATION_ID, null,
                                        PERIOD_MODULO_OFFSET)
                        ),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        GLOBAL_LOOPING_ANIMATION_ID, "",
                                        PERIOD_MODULO_OFFSET)
                        ),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        listOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        GLOBAL_LOOPING_ANIMATION_ID, ANIMATION_ID,
                                        -1)
                        ),
                        mockGlobalLoopingAnimationFactory,
                        mockProcessResult));

        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationPreloaderTask(MOCK_GET_ANIMATION,
                        mockDefinitionDtos, mockGlobalLoopingAnimationFactory,
                        null));
    }

    @Test
    public void testRun() {
        globalLoopingAnimationPreloaderTask.run();

        verify(MOCK_GET_ANIMATION, once()).apply(ANIMATION_ID);
        verify(mockGlobalLoopingAnimationFactory, once()).apply(isNotNull());
        verify(mockProcessResult, once()).accept(mockGlobalLoopingAnimation);
        assertEquals(GLOBAL_LOOPING_ANIMATION_ID, factoryCapture.ID);
        assertSame(MOCK_ANIMATION, factoryCapture.ANIMATION);
        assertEquals(PERIOD_MODULO_OFFSET, factoryCapture.PERIOD_MODULO_OFFSET);
        assertNull(factoryCapture.PAUSE_TIMESTAMP);
    }
}
