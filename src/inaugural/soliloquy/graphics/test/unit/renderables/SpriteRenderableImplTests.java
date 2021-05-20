package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SpriteRenderableImplTests {
    private final FakeSprite SPRITE = new FakeSprite();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    /** @noinspection rawtypes*/
    private final FakeAction CLICK_ACTION = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction MOUSE_OVER_ACTION = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction MOUSE_LEAVE_ACTION = new FakeAction();
    private final ArrayList<ColorShift> COLOR_SHIFTS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable> SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER =
            renderable -> _spriteRenderableWithMouseEventsDeleteConsumerInput = renderable;
    private final Consumer<Renderable>
            SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER =
            renderable -> _spriteRenderableWithoutMouseEventsDeleteConsumerInput = renderable;

    private static Renderable _spriteRenderableWithMouseEventsDeleteConsumerInput;
    private static Renderable _spriteRenderableWithoutMouseEventsDeleteConsumerInput;

    private SpriteRenderable _spriteRenderableWithMouseEvents;
    private SpriteRenderable _spriteRenderableWithoutMouseEvents;

    @BeforeEach
    void setUp() {
        _spriteRenderableWithMouseEvents = new SpriteRenderableImpl(SPRITE, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, CLICK_ACTION, MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION,
                COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER);
        _spriteRenderableWithoutMouseEvents = new SpriteRenderableImpl(SPRITE,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER);
    }

    @Test
    void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                UUID, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, null, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                UUID, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, null, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                UUID, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, null, RENDERING_AREA_PROVIDER, Z,
                UUID, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, null, Z,
                UUID, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                null, SPRITE_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, CLICK_ACTION,
                MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                UUID, null
        ));

        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, null, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                RENDERING_AREA_PROVIDER, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                null, Z, UUID,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, null,
                SPRITE_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(SpriteRenderable.class.getCanonicalName(),
                _spriteRenderableWithMouseEvents.getInterfaceName());
    }

    @Test
    void testSprite() {
        assertSame(SPRITE, _spriteRenderableWithMouseEvents.sprite());
        assertSame(SPRITE, _spriteRenderableWithoutMouseEvents.sprite());
    }

    @Test
    void testBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                _spriteRenderableWithMouseEvents.borderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                _spriteRenderableWithoutMouseEvents.borderThicknessProvider());
    }

    @Test
    void testBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER, _spriteRenderableWithMouseEvents.borderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                _spriteRenderableWithoutMouseEvents.borderColorProvider());
    }

    @Test
    void testCapturesMouseEvents() {
        assertTrue(_spriteRenderableWithMouseEvents.capturesMouseEvents());
        assertFalse(_spriteRenderableWithoutMouseEvents.capturesMouseEvents());
    }

    @Test
    void testClick() {
        assertThrows(UnsupportedOperationException.class,
                _spriteRenderableWithoutMouseEvents::click);

        _spriteRenderableWithMouseEvents.click();
        assertEquals(1, CLICK_ACTION.NumberOfTimesCalled);
        assertEquals(1, CLICK_ACTION.Inputs.size());
        assertNull(CLICK_ACTION.Inputs.get(0));
    }

    @Test
    void testMouseOver() {
        assertThrows(UnsupportedOperationException.class,
                _spriteRenderableWithoutMouseEvents::mouseOver);

        _spriteRenderableWithMouseEvents.mouseOver();
        assertEquals(1, MOUSE_OVER_ACTION.NumberOfTimesCalled);
        assertEquals(1, MOUSE_OVER_ACTION.Inputs.size());
        assertNull(MOUSE_OVER_ACTION.Inputs.get(0));
    }

    @Test
    void testMouseLeave() {
        assertThrows(UnsupportedOperationException.class,
                _spriteRenderableWithoutMouseEvents::mouseLeave);

        _spriteRenderableWithMouseEvents.mouseLeave();
        assertEquals(1, MOUSE_LEAVE_ACTION.NumberOfTimesCalled);
        assertEquals(1, MOUSE_LEAVE_ACTION.Inputs.size());
        assertNull(MOUSE_LEAVE_ACTION.Inputs.get(0));
    }

    @Test
    void testColorShifts() {
        assertSame(COLOR_SHIFTS, _spriteRenderableWithMouseEvents.colorShifts());
        assertSame(COLOR_SHIFTS, _spriteRenderableWithoutMouseEvents.colorShifts());
    }

    @Test
    void testRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _spriteRenderableWithMouseEvents.renderingAreaProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _spriteRenderableWithoutMouseEvents.renderingAreaProvider());
    }

    @Test
    void testZ() {
        assertSame(Z, _spriteRenderableWithMouseEvents.z());
        assertSame(Z, _spriteRenderableWithoutMouseEvents.z());
    }

    @Test
    void testDelete() {
        _spriteRenderableWithMouseEvents.delete();
        assertSame(_spriteRenderableWithMouseEventsDeleteConsumerInput,
                _spriteRenderableWithMouseEvents);

        _spriteRenderableWithoutMouseEvents.delete();
        assertSame(_spriteRenderableWithoutMouseEventsDeleteConsumerInput,
                _spriteRenderableWithoutMouseEvents);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _spriteRenderableWithMouseEvents.uuid());
        assertSame(UUID, _spriteRenderableWithoutMouseEvents.uuid());
    }
}
