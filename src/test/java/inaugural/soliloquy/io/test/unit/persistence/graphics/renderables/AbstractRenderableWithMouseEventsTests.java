package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.tools.testing.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.shared.HasId;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

abstract class AbstractRenderableWithMouseEventsTests<TAsset extends HasId>
        extends AbstractRenderableHandlerTests {
    protected final String ASSET_ID = randomString();

    protected final String BORDER_THICKNESS = randomString();
    protected final String BORDER_COLOR = randomString();

    protected final int ON_PRESS_BUTTON = randomInt();
    protected final String ON_PRESS_ACTION_ID = randomString();
    protected final int ON_RELEASE_BUTTON = randomInt();
    protected final String ON_RELEASE_ACTION_ID = randomString();
    protected final String ON_MOUSE_OVER_ACTION_ID = randomString();
    protected final String ON_MOUSE_LEAVE_ACTION_ID = randomString();
    @SuppressWarnings("rawtypes") protected final Mock.LookupAndEntitiesWithId<Action>
            MOCK_ACTIONS_AND_LOOKUP =
            generateMockLookupFunctionWithId(Action.class, ON_PRESS_ACTION_ID, ON_RELEASE_ACTION_ID,
                    ON_MOUSE_OVER_ACTION_ID, ON_MOUSE_LEAVE_ACTION_ID);
    @SuppressWarnings("unchecked") protected final Action<EventInputs> MOCK_ON_PRESS_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") protected final Action<EventInputs> MOCK_ON_RELEASE_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("unchecked") protected final Action<EventInputs> MOCK_ON_MOUSE_OVER_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(2);
    @SuppressWarnings("unchecked") protected final Action<EventInputs> MOCK_ON_MOUSE_LEAVE_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(3);
    @SuppressWarnings("rawtypes") protected final Function<String, Action> MOCK_GET_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.lookup;

    protected final String COLOR_SHIFT = randomString();
    protected final String AREA = randomString();

    @org.mockito.Mock protected ProviderAtTime<Float> mockBorderThicknessProvider;
    @org.mockito.Mock protected ProviderAtTime<Color> mockBorderColorProvider;
    @org.mockito.Mock protected ProviderAtTime<FloatBox> mockAreaProvider;

    @org.mockito.Mock protected ColorShift mockShift;
    @org.mockito.Mock protected TypeHandler<ColorShift> mockShiftHandler;

    protected TAsset mockAsset;
    protected Function<String, TAsset> mockGetAsset;

    protected Map<Integer, String> onPressIds;
    protected Map<Integer, String> onReleaseIds;

    protected void setUpMouseEventsTests() {
        onPressIds = mapOf(pairOf(ON_PRESS_BUTTON, ON_PRESS_ACTION_ID));
        onReleaseIds = mapOf(pairOf(ON_RELEASE_BUTTON, ON_RELEASE_ACTION_ID));
    }

    protected void setUpImageAssetRenderableTests(Class<TAsset> clazz) {
        var mockAssetAndLookup = generateMockLookupFunctionWithId(clazz, ASSET_ID);
        mockAsset = mockAssetAndLookup.entities.getFirst();
        mockGetAsset = mockAssetAndLookup.lookup;

        hydrateMockHandler(mockProviderHandler,
                pairOf(mockBorderThicknessProvider, BORDER_THICKNESS),
                pairOf(mockBorderColorProvider, BORDER_COLOR),
                pairOf(mockAreaProvider, AREA)
        );

        hydrateMockHandler(mockShiftHandler,
                pairOf(mockShift, COLOR_SHIFT)
        );
    }

    protected void setUpMockImageAssetRenderable(ImageAssetRenderable mockRenderable) {
        when(mockRenderable.getBorderThicknessProvider()).thenReturn(mockBorderThicknessProvider);
        when(mockRenderable.getBorderColorProvider()).thenReturn(mockBorderColorProvider);
        when(mockRenderable.colorShifts()).thenReturn(listOf(mockShift));
        when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(mockAreaProvider);
    }

    protected void setupMockRenderableWithMouseEvents(RenderableWithMouseEvents mockRenderable) {
        when(mockRenderable.pressActionIds()).thenReturn(onPressIds);
        when(mockRenderable.releaseActionIds()).thenReturn(onReleaseIds);
        when(mockRenderable.mouseOverActionId()).thenReturn(ON_MOUSE_OVER_ACTION_ID);
        when(mockRenderable.mouseLeaveActionId()).thenReturn(ON_MOUSE_LEAVE_ACTION_ID);
    }

    protected void verifyMockImageAssetRenderableWritten(ImageAssetRenderable mockRenderable) {
        verify(mockRenderable, once()).getBorderThicknessProvider();
        verify(mockProviderHandler, once()).write(mockBorderThicknessProvider);
        verify(mockRenderable, once()).getBorderColorProvider();
        verify(mockProviderHandler, once()).write(mockBorderColorProvider);
        verify(mockRenderable, once()).colorShifts();
        verify(mockShiftHandler, once()).write(mockShift);
        verify(mockRenderable, once()).getRenderingDimensionsProvider();
        verify(mockProviderHandler, once()).write(mockAreaProvider);
    }

    protected void verifyMockRenderableWithMouseEventsWritten(
            RenderableWithMouseEvents mockRenderable) {
        verify(mockRenderable, once()).pressActionIds();
        verify(mockRenderable, once()).releaseActionIds();
        verify(mockRenderable, once()).mouseOverActionId();
        verify(mockRenderable, once()).mouseLeaveActionId();
    }

    protected void verifyMockImageAssetRenderableRead() {
        verify(mockGetAsset, once()).apply(ASSET_ID);
        verify(mockProviderHandler, once()).read(BORDER_THICKNESS);
        verify(mockProviderHandler, once()).read(BORDER_COLOR);
        verify(mockShiftHandler, once()).read(COLOR_SHIFT);
        verify(mockProviderHandler, once()).read(AREA);
    }

    protected void verifyMockRenderableWithMouseEventsRead() {
        verify(MOCK_GET_ACTION, once()).apply(ON_PRESS_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_RELEASE_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_MOUSE_OVER_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_MOUSE_LEAVE_ACTION_ID);
    }
}
