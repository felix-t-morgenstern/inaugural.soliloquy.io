package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.rendering.FrameExecutor;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FrameExecutorImplTests {
    private final long GLOBAL_TIMESTAMP = 123456789L;

    @Mock
    private GlobalClock _mockGlobalClock;

    @Mock
    private StackRenderer _mockStackRenderer;

    private final ArrayList<String> EVENTS_FIRED = new ArrayList<>();

    private final String FRAME_BLOCKING_EVENT_1_NAME = "frameBlockingEvent1Name";
    private Long _frameBlockingEvent1FiringTime;
    private final Consumer<Long> FRAME_BLOCKING_EVENT_1 = firingTime -> {
        EVENTS_FIRED.add(FRAME_BLOCKING_EVENT_1_NAME);
        _frameBlockingEvent1FiringTime = firingTime;
    };

    private final String FRAME_BLOCKING_EVENT_2_NAME = "frameBlockingEvent2Name";
    private Long _frameBlockingEvent2FiringTime;
    private final Consumer<Long> FRAME_BLOCKING_EVENT_2 = firingTime -> {
        EVENTS_FIRED.add(FRAME_BLOCKING_EVENT_2_NAME);
        _frameBlockingEvent2FiringTime = firingTime;
    };

    private FrameExecutor _frameExecutor;

    @BeforeEach
    void setUp() {
        EVENTS_FIRED.clear();

        _mockGlobalClock = mock(GlobalClock.class);
        when(_mockGlobalClock.globalTimestamp()).thenReturn(GLOBAL_TIMESTAMP);

        _mockStackRenderer = mock(StackRenderer.class);

        _frameExecutor = new FrameExecutorImpl(_mockGlobalClock, _mockStackRenderer,100);
    }

    @Test
    void constructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FrameExecutorImpl(null, _mockStackRenderer, 1));
        assertThrows(IllegalArgumentException.class, () ->
                new FrameExecutorImpl(_mockGlobalClock, null, 1));
        assertThrows(IllegalArgumentException.class, () ->
                new FrameExecutorImpl(_mockGlobalClock, _mockStackRenderer, 0));
    }

    @Test
    void executeFiresFrameBlockingEvents() {
        _frameExecutor.registerFrameBlockingEvent(FRAME_BLOCKING_EVENT_1);
        _frameExecutor.registerFrameBlockingEvent(FRAME_BLOCKING_EVENT_2);

        _frameExecutor.execute();
        CheckedExceptionWrapper.sleep(30);

        assertEquals(2, EVENTS_FIRED.size());
        assertTrue(EVENTS_FIRED.get(0).equals(FRAME_BLOCKING_EVENT_1_NAME) ||
                EVENTS_FIRED.get(1).equals(FRAME_BLOCKING_EVENT_1_NAME));
        assertTrue(EVENTS_FIRED.get(0).equals(FRAME_BLOCKING_EVENT_2_NAME) ||
                EVENTS_FIRED.get(1).equals(FRAME_BLOCKING_EVENT_2_NAME));
        assertEquals(GLOBAL_TIMESTAMP, (long)_frameBlockingEvent1FiringTime);
        assertEquals(GLOBAL_TIMESTAMP, (long)_frameBlockingEvent2FiringTime);
        verify(_mockStackRenderer).render(GLOBAL_TIMESTAMP);
    }

    @Test
    void registerFrameBlockingEventWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _frameExecutor.registerFrameBlockingEvent(null));
    }

    @Test
    void getInterfaceName() {
        assertEquals(FrameExecutor.class.getCanonicalName(), _frameExecutor.getInterfaceName());
    }
}
