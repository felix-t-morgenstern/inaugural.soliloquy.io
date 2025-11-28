package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.tools.testing.Mock;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

abstract class AbstractRenderableWithMouseEventsHandlerTests
        extends AbstractRenderableHandlerTests {
    protected final int ON_PRESS_BUTTON = randomInt();
    protected final String ON_PRESS_CONSUMER_ID = randomString();
    protected final int ON_RELEASE_BUTTON = randomInt();
    protected final String ON_RELEASE_CONSUMER_ID = randomString();
    protected final String ON_MOUSE_OVER_CONSUMER_ID = randomString();
    protected final String ON_MOUSE_LEAVE_CONSUMER_ID = randomString();
    @SuppressWarnings("rawtypes") protected final Mock.LookupAndEntitiesWithId<Consumer>
            MOCK_CONSUMERS_AND_LOOKUP =
            generateMockLookupFunctionWithId(Consumer.class, ON_PRESS_CONSUMER_ID, ON_RELEASE_CONSUMER_ID,
                    ON_MOUSE_OVER_CONSUMER_ID, ON_MOUSE_LEAVE_CONSUMER_ID);
    @SuppressWarnings("unchecked") protected final Consumer<EventInputs> MOCK_ON_PRESS_CONSUMER =
            MOCK_CONSUMERS_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") protected final Consumer<EventInputs> MOCK_ON_RELEASE_CONSUMER =
            MOCK_CONSUMERS_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("unchecked") protected final Consumer<EventInputs> MOCK_ON_MOUSE_OVER_CONSUMER =
            MOCK_CONSUMERS_AND_LOOKUP.entities.get(2);
    @SuppressWarnings("unchecked") protected final Consumer<EventInputs> MOCK_ON_MOUSE_LEAVE_CONSUMER =
            MOCK_CONSUMERS_AND_LOOKUP.entities.get(3);
    @SuppressWarnings("rawtypes") protected final Function<String, Consumer> MOCK_GET_CONSUMER =
            MOCK_CONSUMERS_AND_LOOKUP.lookup;

    protected Map<Integer, String> onPressIds;
    protected Map<Integer, String> onReleaseIds;

    protected void setUp() {
        onPressIds = mapOf(pairOf(ON_PRESS_BUTTON, ON_PRESS_CONSUMER_ID));
        onReleaseIds = mapOf(pairOf(ON_RELEASE_BUTTON, ON_RELEASE_CONSUMER_ID));
    }

    protected void setUpMockRenderable(RenderableWithMouseEvents mockRenderable) {
        super.setUpMockRenderable(mockRenderable);

        when(mockRenderable.pressConsumerIds()).thenReturn(onPressIds);
        when(mockRenderable.releaseConsumerIds()).thenReturn(onReleaseIds);
        when(mockRenderable.mouseOverConsumerId()).thenReturn(ON_MOUSE_OVER_CONSUMER_ID);
        when(mockRenderable.mouseLeaveConsumerId()).thenReturn(ON_MOUSE_LEAVE_CONSUMER_ID);
    }

    protected void verifyWritten(RenderableWithMouseEvents mockRenderable) {
        super.verifyWritten(mockRenderable);

        verify(mockRenderable, once()).pressConsumerIds();
        verify(mockRenderable, once()).releaseConsumerIds();
        verify(mockRenderable, once()).mouseOverConsumerId();
        verify(mockRenderable, once()).mouseLeaveConsumerId();
    }

    protected void verifyRead() {
        verify(MOCK_GET_CONSUMER, once()).apply(ON_PRESS_CONSUMER_ID);
        verify(MOCK_GET_CONSUMER, once()).apply(ON_RELEASE_CONSUMER_ID);
        verify(MOCK_GET_CONSUMER, once()).apply(ON_MOUSE_OVER_CONSUMER_ID);
        verify(MOCK_GET_CONSUMER, once()).apply(ON_MOUSE_LEAVE_CONSUMER_ID);
    }
}
