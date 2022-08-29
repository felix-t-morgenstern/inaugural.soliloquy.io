package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetRenderableImplTests {
    private final FakeImageAssetSet IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS =
            new FakeImageAssetSet(true);
    private final FakeImageAssetSet IMAGE_ASSET_SET_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeImageAssetSet(false);
    private final String TYPE = "type";
    private final String DIRECTION = "direction";
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = 123;
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithMouseEventsUpdateZIndexInConsumerInput =
                            renderable;
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithoutMouseEventsUpdateZIndexInConsumerInput =
                            renderable;
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithMouseEventsRemoveFromConsumerInput = renderable;
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithoutMouseEventsRemoveFromConsumerInput = renderable;

    private static Renderable
            _imageAssetSetRenderableWithMouseEventsUpdateZIndexInConsumerInput;
    private static Renderable
            _imageAssetSetRenderableWithoutMouseEventsUpdateZIndexInConsumerInput;
    private static Renderable _imageAssetSetRenderableWithMouseEventsRemoveFromConsumerInput;
    private static Renderable _imageAssetSetRenderableWithoutMouseEventsRemoveFromConsumerInput;

    private final UUID UUID = java.util.UUID.randomUUID();

    private ImageAssetSetRenderable _imageAssetSetRenderableWithMouseEvents;
    private ImageAssetSetRenderable _imageAssetSetRenderableWithoutMouseEvents;

    @BeforeEach
    void setUp() {
        ON_PRESS_ACTIONS.put(2, ON_PRESS_ACTION);

        _imageAssetSetRenderableWithMouseEvents = new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER);

        _imageAssetSetRenderableWithoutMouseEvents = new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_NOT_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                null, TYPE, DIRECTION, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_NOT_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS,
                null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, BORDER_COLOR_PROVIDER,
                RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, null,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, ON_PRESS_ACTIONS, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                null, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                null, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, null,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONSUMER,
                null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ImageAssetSetRenderable.class.getCanonicalName(),
                _imageAssetSetRenderableWithMouseEvents.getInterfaceName());
        assertEquals(ImageAssetSetRenderable.class.getCanonicalName(),
                _imageAssetSetRenderableWithoutMouseEvents.getInterfaceName());
    }

    @Test
    void testGetAndSetImageAssetSet() {
        assertSame(IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS,
                _imageAssetSetRenderableWithMouseEvents.getImageAssetSet());
        assertSame(IMAGE_ASSET_SET_NOT_SUPPORTING_MOUSE_EVENTS,
                _imageAssetSetRenderableWithoutMouseEvents.getImageAssetSet());

        FakeImageAssetSet newImageAssetSet = new FakeImageAssetSet(true);

        _imageAssetSetRenderableWithMouseEvents.setImageAssetSet(newImageAssetSet);
        _imageAssetSetRenderableWithoutMouseEvents.setImageAssetSet(newImageAssetSet);

        assertSame(newImageAssetSet, _imageAssetSetRenderableWithMouseEvents.getImageAssetSet());
        assertSame(newImageAssetSet,
                _imageAssetSetRenderableWithoutMouseEvents.getImageAssetSet());
    }

    @Test
    void testSetImageAssetSetWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setImageAssetSet(null));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setImageAssetSet(null));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setImageAssetSet(
                        new FakeImageAssetSet(false)));
    }

    @Test
    void testGetAndSetType() {
        assertEquals(TYPE, _imageAssetSetRenderableWithMouseEvents.getType());
        assertEquals(TYPE, _imageAssetSetRenderableWithoutMouseEvents.getType());

        _imageAssetSetRenderableWithMouseEvents.setType("");
        _imageAssetSetRenderableWithoutMouseEvents.setType("");

        assertNull(_imageAssetSetRenderableWithMouseEvents.getType());
        assertNull(_imageAssetSetRenderableWithoutMouseEvents.getType());
    }

    @Test
    void testGetAndSetDirection() {
        assertEquals(DIRECTION, _imageAssetSetRenderableWithMouseEvents.getDirection());
        assertEquals(DIRECTION, _imageAssetSetRenderableWithoutMouseEvents.getDirection());

        _imageAssetSetRenderableWithMouseEvents.setDirection("");
        _imageAssetSetRenderableWithoutMouseEvents.setDirection("");

        assertNull(_imageAssetSetRenderableWithMouseEvents.getDirection());
        assertNull(_imageAssetSetRenderableWithoutMouseEvents.getDirection());
    }

    @Test
    void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                _imageAssetSetRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                _imageAssetSetRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        _imageAssetSetRenderableWithMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);
        _imageAssetSetRenderableWithoutMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                _imageAssetSetRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                _imageAssetSetRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    void testSetBorderThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    void testSetBorderColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setBorderColorProvider(null));
    }

    @Test
    void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                _imageAssetSetRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                _imageAssetSetRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        _imageAssetSetRenderableWithMouseEvents.setBorderColorProvider(newBorderColorProvider);
        _imageAssetSetRenderableWithoutMouseEvents.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                _imageAssetSetRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                _imageAssetSetRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(_imageAssetSetRenderableWithoutMouseEvents.getCapturesMouseEvents());

        _imageAssetSetRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(_imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setOnPress(2, new FakeAction<>()));

        _imageAssetSetRenderableWithMouseEvents.setOnPress(2, ON_PRESS_ACTION);

        long timestamp = 456456L;
        _imageAssetSetRenderableWithMouseEvents.press(2, timestamp);
        assertEquals(1, ON_PRESS_ACTION.NumberOfTimesCalled);
        assertEquals(1, ON_PRESS_ACTION.Inputs.size());
        assertEquals(timestamp, (long) ON_PRESS_ACTION.Inputs.get(0));

        FakeAction<Long> newOnPress = new FakeAction<>();
        _imageAssetSetRenderableWithMouseEvents.setOnPress(2, newOnPress);

        _imageAssetSetRenderableWithMouseEvents.press(2, timestamp + 1);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));

        _imageAssetSetRenderableWithMouseEvents.press(0, timestamp + 2);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _imageAssetSetRenderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        _imageAssetSetRenderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        _imageAssetSetRenderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        _imageAssetSetRenderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds =
                _imageAssetSetRenderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setOnRelease(2, new FakeAction<>()));

        long timestamp = 456456L;
        _imageAssetSetRenderableWithMouseEvents.release(2, timestamp);

        FakeAction<Long> newOnRelease = new FakeAction<>();
        _imageAssetSetRenderableWithMouseEvents.setOnRelease(2, newOnRelease);

        _imageAssetSetRenderableWithMouseEvents.release(2, timestamp + 1);
        assertEquals(1, newOnRelease.NumberOfTimesCalled);
        assertEquals(1, newOnRelease.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnRelease.Inputs.get(0));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _imageAssetSetRenderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        _imageAssetSetRenderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        _imageAssetSetRenderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        _imageAssetSetRenderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                _imageAssetSetRenderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(8, timestamp + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        long timestamp = 456456L;
        _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp);
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_OVER.Inputs.get(0));

        FakeAction<Long> newOnMouseOver = new FakeAction<>();
        _imageAssetSetRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp + 1);
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.mouseOverActionId());

        _imageAssetSetRenderableWithMouseEvents.setOnMouseOver(null);

        assertNull(_imageAssetSetRenderableWithMouseEvents.mouseOverActionId());

        _imageAssetSetRenderableWithMouseEvents.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId,
                _imageAssetSetRenderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setOnMouseLeave(ON_MOUSE_LEAVE));

        long timestamp = 456456L;
        _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp);
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_LEAVE.Inputs.get(0));

        FakeAction<Long> newOnMouseLeave = new FakeAction<>();
        _imageAssetSetRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp + 1);
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.mouseLeaveActionId());

        _imageAssetSetRenderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(_imageAssetSetRenderableWithMouseEvents.mouseLeaveActionId());

        _imageAssetSetRenderableWithMouseEvents.setOnMouseLeave(
                new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId,
                _imageAssetSetRenderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    void testMouseEventCallsToOutdatedTimestamps() {
        long timestamp = 456456L;

        _imageAssetSetRenderableWithMouseEvents.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp - 1));

        _imageAssetSetRenderableWithMouseEvents.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp));

        _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp + 1));

        _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.mouseLeave(timestamp + 2));
    }

    @Test
    void testColorShiftProviders() {
        assertSame(COLOR_SHIFT_PROVIDERS,
                _imageAssetSetRenderableWithMouseEvents.colorShiftProviders());
        assertSame(COLOR_SHIFT_PROVIDERS,
                _imageAssetSetRenderableWithoutMouseEvents.colorShiftProviders());
    }

    @Test
    void testGetAndSetRenderingDimensionsProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _imageAssetSetRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _imageAssetSetRenderableWithoutMouseEvents.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        _imageAssetSetRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        _imageAssetSetRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _imageAssetSetRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                _imageAssetSetRenderableWithoutMouseEvents.getRenderingDimensionsProvider());
    }

    @Test
    void testSetRenderingDimensionsProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithMouseEvents.setRenderingDimensionsProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents.setRenderingDimensionsProvider(null));
    }

    @Test
    void testGetAndSetZ() {
        assertSame(Z, _imageAssetSetRenderableWithMouseEvents.getZ());
        assertSame(Z, _imageAssetSetRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        _imageAssetSetRenderableWithMouseEvents.setZ(newZ);
        _imageAssetSetRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, _imageAssetSetRenderableWithMouseEvents.getZ());
        assertEquals(newZ, _imageAssetSetRenderableWithoutMouseEvents.getZ());

        assertSame(_imageAssetSetRenderableWithMouseEvents,
                _imageAssetSetRenderableWithMouseEventsUpdateZIndexInConsumerInput);
        assertSame(_imageAssetSetRenderableWithoutMouseEvents,
                _imageAssetSetRenderableWithoutMouseEventsUpdateZIndexInConsumerInput);
    }

    @Test
    void testCapturesMouseEventAtPointForSprite() {
        FakeSprite imageAsset = new FakeSprite();
        imageAsset.LeftX = 250;
        imageAsset.RightX = 750;
        imageAsset.TopY = 1000;
        imageAsset.BottomY = 2500;
        FakeImage image = new FakeImage(1000, 3000);
        image.Width = 1000;
        image.Height = 3000;
        imageAsset.Image = image;
        IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS.ImageAsset = imageAsset;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -2f, 0.75f, 0.5f);

        _imageAssetSetRenderableWithMouseEvents.setType(TYPE);
        _imageAssetSetRenderableWithMouseEvents.setDirection(DIRECTION);
        boolean capturesMouseEventAtPoint = _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0.123f, 0.456f, 789L);

        assertTrue(capturesMouseEventAtPoint);
        assertEquals(1, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.size());
        assertEquals(TYPE, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.get(0).getItem1());
        assertEquals(DIRECTION, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.get(0).getItem2());
        ArrayList<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                ((FakeImage) imageAsset.Image).CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) ((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem1());
        assertEquals(
                (int) ((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem2());
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long) RENDERING_AREA_PROVIDER.TimestampInputs.get(0));
    }

    @Test
    void testCapturesMouseEventAtPointForAnimation() {
        FakeAnimationFrameSnippet animationFrameSnippet = new FakeAnimationFrameSnippet();
        animationFrameSnippet.OffsetX = 0.0123f;
        animationFrameSnippet.OffsetY = 0.0456f;
        FakeAnimation animation = new FakeAnimation(789789);
        IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS.ImageAsset = animation;
        animation.AnimationFrameSnippet = animationFrameSnippet;
        animationFrameSnippet.LeftX = 250;
        animationFrameSnippet.RightX = 750;
        animationFrameSnippet.TopY = 1000;
        animationFrameSnippet.BottomY = 2500;
        FakeImage snippetImage = (FakeImage) animationFrameSnippet.Image;
        snippetImage.Width = 1000;
        snippetImage.Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -2f, 0.75f, 0.5f);

        _imageAssetSetRenderableWithMouseEvents.setType(TYPE);
        _imageAssetSetRenderableWithMouseEvents.setDirection(DIRECTION);
        boolean capturesMouseEventAtPoint = _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0.123f, 0.456f, 789L);

        assertTrue(capturesMouseEventAtPoint);
        assertEquals(1, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.size());
        assertEquals(TYPE, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.get(0).getItem1());
        assertEquals(DIRECTION, IMAGE_ASSET_SET_SUPPORTING_MOUSE_EVENTS
                .GetImageAssetForTypeAndDirectionInputs.get(0).getItem2());
        ArrayList<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                snippetImage.CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) (((((0.123f - 0.0123f) - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem1());
        assertEquals(
                (int) (((((0.456f - 0.0456f) - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000))
                        + 1000),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem2());
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long) RENDERING_AREA_PROVIDER.TimestampInputs.get(0));
    }

    @Test
    void testCapturesMouseEventAtPointWithInvalidParams() {
        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                _imageAssetSetRenderableWithoutMouseEvents
                        .capturesMouseEventAtPoint(.5f, .5f, 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(.5f, .5f, 1.5f, 1.5f);

        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.5f - verySmallNumber, .75f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(1f + verySmallNumber, .75f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.75f, .5f - verySmallNumber, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.75f, 1.5f + verySmallNumber, 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0f - verySmallNumber, .25f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0.5f + verySmallNumber, .25f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.25f, 0f - verySmallNumber, 0L));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.25f, 0.5f + verySmallNumber, 0L));
    }

    @Test
    void testDelete() {
        _imageAssetSetRenderableWithMouseEvents.delete();
        assertSame(_imageAssetSetRenderableWithMouseEvents,
                _imageAssetSetRenderableWithMouseEventsRemoveFromConsumerInput);

        _imageAssetSetRenderableWithoutMouseEvents.delete();
        assertSame(_imageAssetSetRenderableWithoutMouseEvents,
                _imageAssetSetRenderableWithoutMouseEventsRemoveFromConsumerInput);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _imageAssetSetRenderableWithMouseEvents.uuid());
        assertSame(UUID, _imageAssetSetRenderableWithoutMouseEvents.uuid());
    }
}
