package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SpriteRenderableImpl implements SpriteRenderable {
    private final Sprite SPRITE;
    private final ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;
    private final ProviderAtTime<Color> BORDER_COLOR_PROVIDER;
    private final boolean CAPTURES_MOUSE_EVENTS;
    /** @noinspection rawtypes*/
    private final Action CLICK_ACTION;
    /** @noinspection rawtypes*/
    private final Action MOUSE_OVER_ACTION;
    /** @noinspection rawtypes*/
    private final Action MOUSE_LEAVE_ACTION;
    private final List<ColorShift> COLOR_SHIFTS;
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER;
    private final int Z;
    private final EntityUuid UUID;
    private final Consumer<SpriteRenderable> DELETE_CONSUMER;

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                EntityUuid uuid, Consumer<SpriteRenderable> deleteConsumer) {
        this(sprite, borderThicknessProvider, borderColorProvider, false, null, null, null,
                colorShifts, renderingAreaProvider, z, uuid, deleteConsumer);
    }

    /** @noinspection rawtypes*/
    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider, Action clickAction,
                                Action mouseOverAction, Action mouseLeaveAction,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                EntityUuid uuid, Consumer<SpriteRenderable> deleteConsumer) {
        this(sprite, borderThicknessProvider, borderColorProvider, true, clickAction,
                mouseOverAction, mouseLeaveAction, colorShifts, renderingAreaProvider, z, uuid,
                deleteConsumer);
    }

    /** @noinspection rawtypes*/
    private SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 boolean capturesMouseEvents, Action clickAction,
                                 Action mouseOverAction, Action mouseLeaveAction,
                                 List<ColorShift> colorShifts,
                                 ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                 EntityUuid uuid, Consumer<SpriteRenderable> deleteConsumer) {
        SPRITE = Check.ifNull(sprite, "sprite");
        BORDER_THICKNESS_PROVIDER = Check.ifNull(borderThicknessProvider,
                "borderThicknessProvider");
        BORDER_COLOR_PROVIDER = Check.ifNull(borderColorProvider, "borderColorProvider");
        CAPTURES_MOUSE_EVENTS = capturesMouseEvents;
        CLICK_ACTION = clickAction;
        MOUSE_OVER_ACTION = mouseOverAction;
        MOUSE_LEAVE_ACTION = mouseLeaveAction;
        COLOR_SHIFTS = Check.ifNull(colorShifts, "colorShifts");
        RENDERING_AREA_PROVIDER = Check.ifNull(renderingAreaProvider, "renderingAreaProvider");
        Z = z;
        UUID = Check.ifNull(uuid, "uuid");
        DELETE_CONSUMER = Check.ifNull(deleteConsumer, "deleteConsumer");
    }

    @Override
    public Sprite sprite() {
        return SPRITE;
    }

    @Override
    public ProviderAtTime<Float> borderThicknessProvider() {
        return BORDER_THICKNESS_PROVIDER;
    }

    @Override
    public ProviderAtTime<Color> borderColorProvider() {
        return BORDER_COLOR_PROVIDER;
    }

    @Override
    public boolean capturesMouseEvents() {
        return CAPTURES_MOUSE_EVENTS;
    }

    @Override
    public void click() throws UnsupportedOperationException {
        callAction(CLICK_ACTION, "click");
    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {
        callAction(MOUSE_OVER_ACTION, "mouseOver");
    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {
        callAction(MOUSE_LEAVE_ACTION, "mouseLeave");
    }

    /** @noinspection rawtypes*/ // TODO: Can avoid accepting methodName as parameter; may not be worth the time
    private void callAction(Action action, String methodName) {
        if (!CAPTURES_MOUSE_EVENTS) {
            throw new UnsupportedOperationException("SpriteRenderableImpl." + methodName +
                    ": mouse events not supported");
        }
        //noinspection unchecked
        action.run(null);
    }

    @Override
    public List<ColorShift> colorShifts() {
        return COLOR_SHIFTS;
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RENDERING_AREA_PROVIDER;
    }

    @Override
    public int z() {
        return Z;
    }

    // NB: deleted SpriteRenderables should _NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        DELETE_CONSUMER.accept(this);
    }

    @Override
    public EntityUuid uuid() {
        return UUID;
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderable.class.getCanonicalName();
    }
}
