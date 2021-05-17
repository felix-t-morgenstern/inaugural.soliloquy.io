package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractRenderableWithArea implements RenderableWithArea {
    private final boolean CAPTURES_MOUSE_EVENTS;
    /** @noinspection rawtypes*/
    private final Action CLICK_ACTION;
    /** @noinspection rawtypes*/
    private final Action MOUSE_OVER_ACTION;
    /** @noinspection rawtypes*/
    private final Action MOUSE_LEAVE_ACTION;
    private final List<ColorShift> COLOR_SHIFTS;
    private final Consumer<Renderable> DELETE_CONSUMER;
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER;
    private final int Z;
    private final EntityUuid UUID;

    protected AbstractRenderableWithArea(List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,  Consumer<Renderable> deleteConsumer) {
        this(false, null, null, null, colorShifts, renderingAreaProvider, z, uuid, deleteConsumer);
    }

    /** @noinspection rawtypes*/
    protected AbstractRenderableWithArea(Action clickAction, Action mouseOverAction,
                                         Action mouseLeaveAction, List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        this(true, clickAction, mouseOverAction, mouseLeaveAction, colorShifts,
                renderingAreaProvider, z, uuid, deleteConsumer);
    }

    /** @noinspection rawtypes*/
    private AbstractRenderableWithArea(boolean capturesMouseEvents, Action clickAction,
                                       Action mouseOverAction, Action mouseLeaveAction,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
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
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
        //noinspection unchecked
        action.run(null);
    }

    abstract protected String className();

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
}
