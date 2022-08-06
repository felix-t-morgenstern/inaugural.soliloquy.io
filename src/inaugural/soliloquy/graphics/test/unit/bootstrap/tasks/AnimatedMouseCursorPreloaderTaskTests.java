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

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** @noinspection FieldCanBeLocal*/
class AnimatedMouseCursorPreloaderTaskTests {
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

    private final HashMap<String, Long> MOUSE_CURSORS = new HashMap<String, Long>() {{
        put(MOUSE_CURSOR_IMG_1, MOUSE_CURSOR_1);
        put(MOUSE_CURSOR_IMG_2, MOUSE_CURSOR_2);
        put(MOUSE_CURSOR_IMG_3, MOUSE_CURSOR_3);
    }};

    private final ArrayList<AnimatedMouseCursorDefinitionDTO>
            ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS = new ArrayList<>();

    private final AnimatedMouseCursorFrameDefinitionDTO FRAME_1_DTO =
            new AnimatedMouseCursorFrameDefinitionDTO(MS_1, MOUSE_CURSOR_IMG_1);

    private final AnimatedMouseCursorFrameDefinitionDTO FRAME_2_DTO =
            new AnimatedMouseCursorFrameDefinitionDTO(MS_2, MOUSE_CURSOR_IMG_3);

    private final AnimatedMouseCursorDefinitionDTO ANIMATED_MOUSE_CURSOR_DTO =
            new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                            FRAME_1_DTO, FRAME_2_DTO
                    }, DURATION, OFFSET, PAUSED, TIMESTAMP);

    @Mock
    private AnimatedMouseCursorProviderFactory _animatedMouseCursorProviderFactoryMock;
    private AnimatedMouseCursorProviderDefinition _animatedMouseCursorProviderFactoryMockInput;
    @Mock
    private AnimatedMouseCursorProvider _animatedMouseCursorProviderMock;

    private ProviderAtTime<Long> _resultProvider;

    private AnimatedMouseCursorPreloaderTask _animatedMouseCursorPreloaderTask;

    @BeforeEach
    void setUp() {
        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS.add(ANIMATED_MOUSE_CURSOR_DTO);

        _animatedMouseCursorProviderMock = mock(AnimatedMouseCursorProvider.class);

        _animatedMouseCursorProviderFactoryMockInput = null;
        _animatedMouseCursorProviderFactoryMock = mock(AnimatedMouseCursorProviderFactory.class);
        when(_animatedMouseCursorProviderFactoryMock.make(any()))
                .thenAnswer(invocation -> {
                    _animatedMouseCursorProviderFactoryMockInput = invocation.getArgument(0);
                    return _animatedMouseCursorProviderMock;
                });

        _animatedMouseCursorPreloaderTask = new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS, _animatedMouseCursorProviderFactoryMock,
                provider -> _resultProvider = provider);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(null,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        null,
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(null);
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(null,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO("",
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    null, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {},
                                    DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] { FRAME_2_DTO },
                                    DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                            new AnimatedMouseCursorFrameDefinitionDTO(0, null)
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                            new AnimatedMouseCursorFrameDefinitionDTO(0, "")
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, MS_2 - 1, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, DURATION, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, -1, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, null));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorFrameDefinitionDTO[] {
                                                FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, TIMESTAMP + 1, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        null,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        _animatedMouseCursorProviderFactoryMock,
                        null));
    }

    @Test
    void testRun() {
        _animatedMouseCursorPreloaderTask.run();

        assertEquals(ANIMATED_MOUSE_CURSOR_ID,
                _animatedMouseCursorProviderFactoryMockInput.id());
        assertEquals(new HashMap<Integer, Long>() {{
                         put(MS_1, MOUSE_CURSOR_1);
                         put(MS_2, MOUSE_CURSOR_3);
                     }},
                _animatedMouseCursorProviderFactoryMockInput.cursorsAtMs());
        assertEquals(DURATION,
                _animatedMouseCursorProviderFactoryMockInput.msDuration());
        assertEquals(OFFSET,
                _animatedMouseCursorProviderFactoryMockInput.periodModuloOffset());
        assertEquals(PAUSED,
                _animatedMouseCursorProviderFactoryMockInput.pausedTimestamp());
        assertEquals(TIMESTAMP,
                _animatedMouseCursorProviderFactoryMockInput.mostRecentTimestamp());

        assertSame(_animatedMouseCursorProviderMock, _resultProvider);
    }
}
