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

abstract class AbstractRenderableWithArea extends AbstractRenderable implements RenderableWithArea {
    private final boolean CAPTURES_MOUSE_EVENTS;
    /** @noinspection rawtypes*/
    private final Action ON_CLICK;
    /** @noinspection rawtypes*/
    private final Action ON_MOUSE_OVER;
    /** @noinspection rawtypes*/
    private final Action ON_MOUSE_LEAVE;
    private final List<ColorShift> COLOR_SHIFTS;

    protected AbstractRenderableWithArea(List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,  Consumer<Renderable> removeFromContainer)
    {
        this(false, null, null, null, colorShifts, renderingAreaProvider, z, uuid, removeFromContainer);
    }

    /** @noinspection rawtypes*/
    protected AbstractRenderableWithArea(Action onClick, Action onMouseOver, Action onMouseLeave,
                                         List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid, Consumer<Renderable> removeFromContainer)
    {
        this(true, onClick, onMouseOver, onMouseLeave, colorShifts,
                renderingAreaProvider, z, uuid, removeFromContainer);
    }

    /** @noinspection rawtypes*/
    private AbstractRenderableWithArea(boolean capturesMouseEvents, Action onClick,
                                       Action onMouseOver, Action onMouseLeave,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, removeFromContainer);
        CAPTURES_MOUSE_EVENTS = capturesMouseEvents;
        ON_CLICK = onClick;
        ON_MOUSE_OVER = onMouseOver;
        ON_MOUSE_LEAVE = onMouseLeave;
        COLOR_SHIFTS = Check.ifNull(colorShifts, "colorShifts");
    }

    @Override
    public boolean capturesMouseEvents() {
        return CAPTURES_MOUSE_EVENTS;
    }

    @Override
    public void click() throws UnsupportedOperationException {
        callAction(ON_CLICK, "click");
    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {
        callAction(ON_MOUSE_OVER, "mouseOver");
    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {
        callAction(ON_MOUSE_LEAVE, "mouseLeave");
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
}
