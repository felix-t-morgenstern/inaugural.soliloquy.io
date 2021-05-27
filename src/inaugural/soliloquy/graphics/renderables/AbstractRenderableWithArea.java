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

abstract class AbstractRenderableWithArea extends AbstractRenderableWithDimensions
        implements RenderableWithArea {
    private final List<ColorShift> COLOR_SHIFTS;

    private boolean _capturesMouseEvents;
    /** @noinspection rawtypes*/
    private Action _onClick;
    /** @noinspection rawtypes*/
    private Action _onMouseOver;
    /** @noinspection rawtypes*/
    private Action _onMouseLeave;

    protected AbstractRenderableWithArea(List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(false, null, null, null, colorShifts, renderingAreaProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    /** @noinspection rawtypes*/
    protected AbstractRenderableWithArea(Action onClick, Action onMouseOver, Action onMouseLeave,
                                         List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(true, onClick, onMouseOver, onMouseLeave, colorShifts,
                renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    /** @noinspection rawtypes*/
    private AbstractRenderableWithArea(boolean capturesMouseEvents, Action onClick,
                                       Action onMouseOver, Action onMouseLeave,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid,
                                       Consumer<Renderable> updateZIndexInContainer,
                                       Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
        _capturesMouseEvents = capturesMouseEvents;
        _onClick = onClick;
        _onMouseOver = onMouseOver;
        _onMouseLeave = onMouseLeave;
        COLOR_SHIFTS = Check.ifNull(colorShifts, "colorShifts");
    }

    protected void throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport() {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new IllegalArgumentException(className() +
                    ": underlying asset does not support capturing mouse events");
        }
    }

    @Override
    public boolean getCapturesMouseEvents() {
        return _capturesMouseEvents;
    }

    // TODO: Ensure that this value can only be set to true when the underlying asset supports mouse event capturing
    @Override
    public void setCapturesMouseEvents(boolean capturesMouseEvents) {
        if (!underlyingAssetSupportsMouseEvents()) {
            throw new UnsupportedOperationException(className() + ".setCapturesMouseEvents: " +
                    "underlying asset does not support mouse event capturing");
        }

        _capturesMouseEvents = capturesMouseEvents;
    }

    /** @noinspection BooleanMethodIsAlwaysInverted*/
    protected abstract boolean underlyingAssetSupportsMouseEvents();

    @Override
    public void click() throws UnsupportedOperationException {
        callAction(_onClick, "click");
    }

    @Override
    public void setOnClick(Action onClick) {
        throwIfNotSupportingMouseEvents("setOnClick");
        _onClick = onClick;
    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {
        callAction(_onMouseOver, "mouseOver");
    }

    @Override
    public void setOnMouseOver(Action onMouseOver) {
        throwIfNotSupportingMouseEvents("setOnMouseOver");
        _onMouseOver = onMouseOver;
    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {
        callAction(_onMouseLeave, "mouseLeave");
    }

    @Override
    public void setOnMouseLeave(Action onMouseLeave) {
        throwIfNotSupportingMouseEvents("setOnMouseLeave");
        _onMouseLeave = onMouseLeave;
    }

    /** @noinspection rawtypes*/ // TODO: Can avoid accepting methodName as parameter; may not be worth the time
    private void callAction(Action action, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        //noinspection unchecked
        action.run(null);
    }

    private void throwIfNotSupportingMouseEvents(String methodName) {
        if (!_capturesMouseEvents) {
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
    }

    abstract protected String className();

    @Override
    public List<ColorShift> colorShifts() {
        return COLOR_SHIFTS;
    }
}
