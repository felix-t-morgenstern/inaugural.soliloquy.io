package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorFrameDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.AnimatedMouseCursorPreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;


import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

/** @noinspection FieldCanBeLocal */
public class AnimatedMouseCursorPreloaderTaskTests {
    private final String MOUSE_CURSOR_IMG_1 = "mouseCursorImg1";
    private final String MOUSE_CURSOR_IMG_2 = "mouseCursorImg2";
    private final String MOUSE_CURSOR_IMG_3 = "mouseCursorImg3";

    private final long MOUSE_CURSOR_1 = 123L;
    private final long MOUSE_CURSOR_2 = 456L;
    private final long MOUSE_CURSOR_3 = 789L;

    private final int MS_1 = 0;
    private final int MS_2 = 111;

    private final int DURATION = 999;
    private final int OFFSET = 888;
    private final Long PAUSED = 666L;
    private final Long TIMESTAMP = 777L;

    private final String ANIMATED_MOUSE_CURSOR_ID = "animatedMouseCursorId";

    private final Map<String, Long> MOUSE_CURSORS = mapOf(
            pairOf(MOUSE_CURSOR_IMG_1, MOUSE_CURSOR_1),
            pairOf(MOUSE_CURSOR_IMG_2, MOUSE_CURSOR_2),
            pairOf(MOUSE_CURSOR_IMG_3, MOUSE_CURSOR_3)
    );

    private final List<AnimatedMouseCursorDefinitionDTO>
            ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS = listOf();

    private final AnimatedMouseCursorFrameDefinitionDTO FRAME_1_DTO =
            new AnimatedMouseCursorFrameDefinitionDTO(MS_1, MOUSE_CURSOR_IMG_1);

    private final AnimatedMouseCursorFrameDefinitionDTO FRAME_2_DTO =
            new AnimatedMouseCursorFrameDefinitionDTO(MS_2, MOUSE_CURSOR_IMG_3);

    private final AnimatedMouseCursorDefinitionDTO ANIMATED_MOUSE_CURSOR_DTO =
            new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                    new AnimatedMouseCursorFrameDefinitionDTO[]{
                            FRAME_1_DTO, FRAME_2_DTO
                    }, DURATION, OFFSET, PAUSED, TIMESTAMP);

    @Mock
    private AnimatedMouseCursorProviderFactory animatedMouseCursorProviderFactoryMock;
    private AnimatedMouseCursorProviderDefinition animatedMouseCursorProviderFactoryMockInput;
    @Mock
    private AnimatedMouseCursorProvider animatedMouseCursorProviderMock;

    private ProviderAtTime<Long> resultProvider;

    private AnimatedMouseCursorPreloaderTask animatedMouseCursorPreloaderTask;

    @BeforeEach
    public void setUp() {
        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS.add(ANIMATED_MOUSE_CURSOR_DTO);

        animatedMouseCursorProviderMock = mock(AnimatedMouseCursorProvider.class);

        animatedMouseCursorProviderFactoryMockInput = null;
        animatedMouseCursorProviderFactoryMock = mock(AnimatedMouseCursorProviderFactory.class);
        when(animatedMouseCursorProviderFactoryMock.make(any()))
                .thenAnswer(invocation -> {
                    animatedMouseCursorProviderFactoryMockInput = invocation.getArgument(0);
                    return animatedMouseCursorProviderMock;
                });

        animatedMouseCursorPreloaderTask = new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS, animatedMouseCursorProviderFactoryMock,
                provider -> resultProvider = provider);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(null,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        null,
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf((AnimatedMouseCursorDefinitionDTO) null),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(null,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO("",
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                null, DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{},
                                DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{FRAME_2_DTO},
                                DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        new AnimatedMouseCursorFrameDefinitionDTO(0, null)
                                }, DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        new AnimatedMouseCursorFrameDefinitionDTO(0, "")
                                }, DURATION, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, MS_2 - 1, OFFSET, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, DURATION, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, -1, PAUSED, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, OFFSET, PAUSED, null)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        listOf(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                new AnimatedMouseCursorFrameDefinitionDTO[]{
                                        FRAME_1_DTO, FRAME_2_DTO
                                }, DURATION, OFFSET, TIMESTAMP + 1, TIMESTAMP)
                        ),
                        animatedMouseCursorProviderFactoryMock,
                        provider -> resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        null,
                        provider -> resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        animatedMouseCursorProviderFactoryMock,
                        null));
    }

    @Test
    public void testRun() {
        animatedMouseCursorPreloaderTask.run();

        assertEquals(ANIMATED_MOUSE_CURSOR_ID,
                animatedMouseCursorProviderFactoryMockInput.id());
        assertEquals(mapOf(
                        pairOf(MS_1, MOUSE_CURSOR_1),
                        pairOf(MS_2, MOUSE_CURSOR_3)
                ),
                animatedMouseCursorProviderFactoryMockInput.cursorsAtMs());
        assertEquals(DURATION,
                animatedMouseCursorProviderFactoryMockInput.msDuration());
        assertEquals(OFFSET,
                animatedMouseCursorProviderFactoryMockInput.periodModuloOffset());
        assertEquals(PAUSED,
                animatedMouseCursorProviderFactoryMockInput.pausedTimestamp());
        assertEquals(TIMESTAMP,
                animatedMouseCursorProviderFactoryMockInput.mostRecentTimestamp());

        assertSame(animatedMouseCursorProviderMock, resultProvider);
    }
}
