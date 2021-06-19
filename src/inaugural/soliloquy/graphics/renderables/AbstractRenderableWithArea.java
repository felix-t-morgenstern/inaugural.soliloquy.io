package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

abstract class AbstractRenderableWithArea extends AbstractRenderableWithDimensions
        implements RenderableWithArea {
    private final List<ColorShift> COLOR_SHIFTS;

    protected boolean _capturesMouseEvents;

    private Map<Integer, Action<Long>> _onPress;
    private Map<Integer, Action<Long>> _onRelease;
    private Action<Long> _onMouseOver;
    private Action<Long> _onMouseLeave;
    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;

    protected AbstractRenderableWithArea(List<ColorShift> colorShifts,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(false, null, null, null, null, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    protected AbstractRenderableWithArea(Map<Integer, Action<Long>> onPress,
                                         Map<Integer, Action<Long>> onRelease,
                                         Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                         List<ColorShift> colorShifts,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(true, onPress, onRelease, onMouseOver, onMouseLeave, colorShifts,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    // TODO: If onPress or onRelease are null, ensure that a Map is created in their stead
    private AbstractRenderableWithArea(boolean capturesMouseEvents,
                                       Map<Integer, Action<Long>> onPress,
                                       Map<Integer, Action<Long>> onRelease,
                                       Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid,
                                       Consumer<Renderable> updateZIndexInContainer,
                                       Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
        _capturesMouseEvents = capturesMouseEvents;
        _onPress = onPress == null ? new HashMap<>() : onPress;
        _onRelease = onRelease;
        _onMouseOver = onMouseOver;
        _onMouseLeave = onMouseLeave;
        COLOR_SHIFTS = Check.ifNull(colorShifts, "colorShifts");
        setBorderColorProvider(borderColorProvider);
        setBorderThicknessProvider(borderThicknessProvider);
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

    // TODO: Ensure that event only responds to correct mouse button
    // TODO: Ensure checking whether such a mouse event for the provided button exists
    // TODO: Ensure that mouse button event Actions receive proper timestamp
    // TODO: Ensure that mouse events test whether timestamps are out of date
    @Override
    public void press(int mouseButton, long timestamp) throws UnsupportedOperationException {
        callAction(_onPress.get(mouseButton), timestamp, "press");
    }

    @Override
    public void setOnPress(int mouseButton, Action<Long> onPress) {
        throwIfNotSupportingMouseEvents("setOnPress");
        _onPress.put(mouseButton, onPress);
    }

    // TODO: Test and implement; ensure that returned object is clone
    // TODO: Ensure that mouse button links to actions are accurate
    @Override
    public Map<Integer, String> pressActionIds() {
        return null;
    }

    // TODO: Ensure that event only responds to correct mouse button
    // TODO: Ensure checking whether such a mouse event for the provided button exists
    // TODO: Ensure that mouse button event Actions receive proper timestamp
    // TODO: Ensure that mouse events test whether timestamps are out of date
    @Override
    public void release(int mouseButton, long timestamp) throws UnsupportedOperationException {
        callAction(_onRelease.get(mouseButton), timestamp, "release");
    }

    @Override
    public void setOnRelease(int mouseButton, Action<Long> onRelease) {
        throwIfNotSupportingMouseEvents("setOnRelease");
        _onPress.put(mouseButton, onRelease);
    }

    // TODO: Test and implement; ensure that returned object is clone
    // TODO: Ensure that mouse button links to actions are accurate
    @Override
    public Map<Integer, String> releaseActionIds() {
        return null;
    }

    @Override
    public void mouseOver(long timestamp) throws UnsupportedOperationException {
        callAction(_onMouseOver, timestamp, "mouseOver");
    }

    @Override
    public void setOnMouseOver(Action<Long> onMouseOver) {
        throwIfNotSupportingMouseEvents("setOnMouseOver");
        _onMouseOver = onMouseOver;
    }

    // TODO: Ensure that correct value is returned
    @Override
    public String mouseOverActionId() {
        return null;
    }

    @Override
    public void mouseLeave(long timestamp) throws UnsupportedOperationException {
        callAction(_onMouseLeave, timestamp, "mouseLeave");
    }

    @Override
    public void setOnMouseLeave(Action<Long> onMouseLeave) {
        throwIfNotSupportingMouseEvents("setOnMouseLeave");
        _onMouseLeave = onMouseLeave;
    }

    // TODO: Ensure that correct value is returned
    @Override
    public String mouseLeaveActionId() {
        return null;
    }

    // TODO: Ensure that timestamp is fed
    // TODO: Can avoid accepting methodName as parameter; may not be worth the time
    private void callAction(Action<Long> action, long timestamp, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
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

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return _borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        if (borderThicknessProvider != null && _borderColorProvider == null) {
            throw new IllegalArgumentException(className() + ".setBorderColorProvider: cannot " +
                    "set borderThicknessProvider to non-null while borderColorProvider is null");
        }
        _borderThicknessProvider = borderThicknessProvider;
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return _borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        if (_borderThicknessProvider != null && borderColorProvider == null) {
            throw new IllegalArgumentException(className() + ".setBorderColorProvider: cannot " +
                    "set borderColorProvider to null while borderThicknessProvider is non-null");
        }
        _borderColorProvider = borderColorProvider;
    }
}
