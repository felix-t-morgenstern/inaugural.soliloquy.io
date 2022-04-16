package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.AnimatedMouseCursorTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/** @noinspection FieldCanBeLocal*/
class AnimatedMouseCursorTaskTests {
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
    private final long PAUSED = 666L;
    private final long TIMESTAMP = 777L;

    private final String ANIMATED_MOUSE_CURSOR_ID = "animatedMouseCursorId";

    private final HashMap<String, Long> MOUSE_CURSORS = new HashMap<String, Long>() {{
        put(MOUSE_CURSOR_IMG_1, MOUSE_CURSOR_1);
        put(MOUSE_CURSOR_IMG_2, MOUSE_CURSOR_2);
        put(MOUSE_CURSOR_IMG_3, MOUSE_CURSOR_3);
    }};

    private final ArrayList<AnimatedMouseCursorDefinitionDTO>
            ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS = new ArrayList<>();

    private final AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO FRAME_1_DTO =
            new AnimatedMouseCursorDefinitionDTO
                    .AnimatedMouseCursorFrameDTO(MS_1, MOUSE_CURSOR_IMG_1);

    private final AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO FRAME_2_DTO =
            new AnimatedMouseCursorDefinitionDTO
                    .AnimatedMouseCursorFrameDTO(MS_2, MOUSE_CURSOR_IMG_3);

    private final AnimatedMouseCursorDefinitionDTO ANIMATED_MOUSE_CURSOR_DTO =
            new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                    new AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO[] {
                            FRAME_1_DTO, FRAME_2_DTO
                    }, DURATION, OFFSET, PAUSED, TIMESTAMP);

    @Mock
    private AnimatedMouseCursorProviderFactory _animatedMouseCursorProviderFactoryMock;
    @Mock
    private AnimatedMouseCursorProvider _animatedMouseCursorProviderMock;

    private ProviderAtTime<Long> _resultProvider;

    private AnimatedMouseCursorTask _animatedMouseCursorTask;

    @BeforeEach
    void setUp() {
        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS.add(ANIMATED_MOUSE_CURSOR_DTO);

        _animatedMouseCursorProviderFactoryMock = mock(AnimatedMouseCursorProviderFactory.class);
        _animatedMouseCursorProviderMock = mock(AnimatedMouseCursorProvider.class);

        when(_animatedMouseCursorProviderFactoryMock.make(
                Mockito.anyString(), Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt(),
                Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(_animatedMouseCursorProviderMock);

        _animatedMouseCursorTask = new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS, _animatedMouseCursorProviderFactoryMock,
                provider -> _resultProvider = provider);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(null,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        null,
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<>(),
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(null);
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(null,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO("",
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    null, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {},
                                    DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                            FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO.AnimatedMouseCursorFrameDTO[] {
                                            new AnimatedMouseCursorDefinitionDTO
                                                    .AnimatedMouseCursorFrameDTO(
                                                    0, null)
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                new AnimatedMouseCursorDefinitionDTO
                                                        .AnimatedMouseCursorFrameDTO(0, "")
                                    }, DURATION, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, MS_2 - 1, OFFSET, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, DURATION, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, -1, PAUSED, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, PAUSED, null));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        new ArrayList<AnimatedMouseCursorDefinitionDTO>() {{
                            add(new AnimatedMouseCursorDefinitionDTO(ANIMATED_MOUSE_CURSOR_ID,
                                    new AnimatedMouseCursorDefinitionDTO
                                            .AnimatedMouseCursorFrameDTO[] {
                                                    FRAME_1_DTO, FRAME_2_DTO
                                    }, DURATION, OFFSET, TIMESTAMP + 1, TIMESTAMP));
                        }},
                        _animatedMouseCursorProviderFactoryMock,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        null,
                        provider -> _resultProvider = provider));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorTask(MOUSE_CURSORS::get,
                        ANIMATED_MOUSE_CURSOR_DEFINITION_DTOS,
                        _animatedMouseCursorProviderFactoryMock,
                        null));
    }

    @Test
    void testRun() {
        _animatedMouseCursorTask.run();

        verify(_animatedMouseCursorProviderFactoryMock).make(ANIMATED_MOUSE_CURSOR_ID,
                new HashMap<Integer, Long>() {{
                    put(MS_1, MOUSE_CURSOR_1);
                    put(MS_2, MOUSE_CURSOR_3);
                }}, DURATION, OFFSET, PAUSED, TIMESTAMP);
        assertSame(_animatedMouseCursorProviderMock, _resultProvider);
    }
}
