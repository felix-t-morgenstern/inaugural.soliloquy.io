package inaugural.soliloquy.graphics.test.unit.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDTO;
import inaugural.soliloquy.graphics.bootstrap.workers.AnimatedMouseCursorWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/** @noinspection FieldCanBeLocal*/
class AnimatedMouseCursorWorkerTests {
    private final String MOUSE_CURSOR_IMG_1 = "mouseCursorImg1";
    private final String MOUSE_CURSOR_IMG_2 = "mouseCursorImg2";
    private final String MOUSE_CURSOR_IMG_3 = "mouseCursorImg3";

    private final long MOUSE_CURSOR_1 = 123L;
    private final long MOUSE_CURSOR_2 = 456L;
    private final long MOUSE_CURSOR_3 = 789L;

    private final long MS_1 = 111L;
    private final long MS_2 = 222L;

    private final int DURATION = 999;
    private final int OFFSET = 888;
    private final long PAUSED = 777L;
    private final long TIMESTAMP = 666L;

    private final String ANIMATED_MOUSE_CURSOR_ID = "animatedMouseCursorId";

    private final HashMap<String, Long> MOUSE_CURSORS = new HashMap<String, Long>() {{
        put(MOUSE_CURSOR_IMG_1, MOUSE_CURSOR_1);
        put(MOUSE_CURSOR_IMG_2, MOUSE_CURSOR_2);
        put(MOUSE_CURSOR_IMG_3, MOUSE_CURSOR_3);
    }};

    private final AnimatedMouseCursorDTO ANIMATED_MOUSE_CURSOR_DTO = new AnimatedMouseCursorDTO();

    @Mock
    private AnimatedMouseCursorProviderFactory _animatedMouseCursorProviderFactoryMock;
    @Mock
    private AnimatedMouseCursorProvider _animatedMouseCursorProviderMock;

    private ProviderAtTime<Long> _resultProvider;

    private AnimatedMouseCursorWorker _animatedMouseCursorWorker;

    @BeforeEach
    void setUp() {
        AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO frame1DTO =
                new AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO();
        frame1DTO.Ms = MS_1;
        frame1DTO.Img = MOUSE_CURSOR_IMG_1;

        AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO frame2DTO =
                new AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO();
        frame2DTO.Ms = MS_2;
        frame2DTO.Img = MOUSE_CURSOR_IMG_3;

        ANIMATED_MOUSE_CURSOR_DTO.Id = ANIMATED_MOUSE_CURSOR_ID;
        ANIMATED_MOUSE_CURSOR_DTO.Frames =
                new AnimatedMouseCursorDTO.AnimatedMouseCursorFrameDTO[] {
                        frame1DTO, frame2DTO
                };
        ANIMATED_MOUSE_CURSOR_DTO.Duration = DURATION;
        ANIMATED_MOUSE_CURSOR_DTO.Offset = OFFSET;
        ANIMATED_MOUSE_CURSOR_DTO.Paused = PAUSED;
        ANIMATED_MOUSE_CURSOR_DTO.Timestamp = TIMESTAMP;

        _animatedMouseCursorProviderFactoryMock = mock(AnimatedMouseCursorProviderFactory.class);
        _animatedMouseCursorProviderMock = mock(AnimatedMouseCursorProvider.class);

        when(_animatedMouseCursorProviderFactoryMock.make(
                Mockito.anyString(), Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt(),
                Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(_animatedMouseCursorProviderMock);

        _animatedMouseCursorWorker = new AnimatedMouseCursorWorker(ANIMATED_MOUSE_CURSOR_DTO,
                MOUSE_CURSORS::get, _animatedMouseCursorProviderFactoryMock,
                provider -> _resultProvider = provider);
    }

    @Test
    void testConstructorWithInvalidParams() {
        fail("Implement this!");
    }

    @Test
    void testRun() {
        _animatedMouseCursorWorker.run();

        verify(_animatedMouseCursorProviderFactoryMock).make(ANIMATED_MOUSE_CURSOR_ID,
                new HashMap<Long, Long>() {{
                    put(MS_1, MOUSE_CURSOR_1);
                    put(MS_2, MOUSE_CURSOR_3);
                }}, DURATION, OFFSET, PAUSED, TIMESTAMP);
        assertSame(_animatedMouseCursorProviderMock, _resultProvider);
    }
}
