package inaugural.soliloquy.io.test.unit.graphics.rendering;

import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.tools.exception.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.FrameExecutor;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;

import java.util.List;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FrameExecutorImplTests {
    private final long GLOBAL_TIMESTAMP = randomLong();

    private final List<String> EVENTS_FIRED = listOf();

    private final String FRAME_BLOCKING_EVENT_1_NAME = "frameBlockingEvent1Name";
    private Long frameBlockingEvent1FiringTime;
    private final Consumer<Long> FRAME_BLOCKING_EVENT_1 = firingTime -> {
        EVENTS_FIRED.add(FRAME_BLOCKING_EVENT_1_NAME);
        frameBlockingEvent1FiringTime = firingTime;
    };

    private final String FRAME_BLOCKING_EVENT_2_NAME = "frameBlockingEvent2Name";
    private Long frameBlockingEvent2FiringTime;
    private final Consumer<Long> FRAME_BLOCKING_EVENT_2 = firingTime -> {
        EVENTS_FIRED.add(FRAME_BLOCKING_EVENT_2_NAME);
        frameBlockingEvent2FiringTime = firingTime;
    };

    @Mock private Component mockTopLevelComponent;
    @Mock private ComponentRenderer mockComponentRenderer;
    @Mock private Runnable mockReportFrameCompletion;

    private FrameExecutor frameExecutor;

    @BeforeEach
    public void setUp() {
        EVENTS_FIRED.clear();

        frameExecutor = new FrameExecutorImpl(mockComponentRenderer, 100, mockReportFrameCompletion);
    }

    @Test
    public void constructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameExecutorImpl(null, 1, mockReportFrameCompletion));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameExecutorImpl(mockComponentRenderer, 0, mockReportFrameCompletion));
    }

    @Test
    public void testSetTopLevelComponentWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> frameExecutor.setTopLevelComponent(null));
    }

    @Test
    public void testSetNewTopLevelComponentDeletesPrev() {
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);

        frameExecutor.setTopLevelComponent(mock(ComponentImpl.class));

        verify(mockTopLevelComponent, once()).delete();
    }

    @Test
    public void testExecuteFiresFrameBlockingEvents() {
        frameExecutor.setTopLevelComponent(mockTopLevelComponent);
        frameExecutor.registerFrameBlockingEvent(FRAME_BLOCKING_EVENT_1);
        frameExecutor.registerFrameBlockingEvent(FRAME_BLOCKING_EVENT_2);

        frameExecutor.execute(GLOBAL_TIMESTAMP);
        CheckedExceptionWrapper.sleep(30);

        assertEquals(2, EVENTS_FIRED.size());
        assertTrue(EVENTS_FIRED.get(0).equals(FRAME_BLOCKING_EVENT_1_NAME) ||
                EVENTS_FIRED.get(1).equals(FRAME_BLOCKING_EVENT_1_NAME));
        assertTrue(EVENTS_FIRED.get(0).equals(FRAME_BLOCKING_EVENT_2_NAME) ||
                EVENTS_FIRED.get(1).equals(FRAME_BLOCKING_EVENT_2_NAME));
        assertEquals(GLOBAL_TIMESTAMP, (long) frameBlockingEvent1FiringTime);
        assertEquals(GLOBAL_TIMESTAMP, (long) frameBlockingEvent2FiringTime);
        verify(mockComponentRenderer, once()).render(mockTopLevelComponent, GLOBAL_TIMESTAMP);
        verify(mockReportFrameCompletion, once()).run();
    }

    @Test
    public void testExecuteWithoutTopLevelComponent() {
        assertThrows(IllegalStateException.class, () -> frameExecutor.execute(GLOBAL_TIMESTAMP));
    }

    @Test
    public void testRegisterFrameBlockingEventWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                frameExecutor.registerFrameBlockingEvent(null));
    }
}
