package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.Image;
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
import java.util.function.Supplier;

abstract class AbstractRenderableWithArea extends AbstractRenderableWithDimensions
        implements RenderableWithArea {
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS;
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    protected boolean _capturesMouseEvents;

    private Map<Integer, Action<Long>> _onPress;
    private Map<Integer, Action<Long>> _onRelease;
    private Action<Long> _onMouseOver;
    private Action<Long> _onMouseLeave;
    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;

    protected AbstractRenderableWithArea(List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(false, null, null, null, null, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    protected AbstractRenderableWithArea(Map<Integer, Action<Long>> onPress,
                                         Map<Integer, Action<Long>> onRelease,
                                         Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         EntityUuid uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer)
    {
        this(true, onPress, onRelease, onMouseOver, onMouseLeave,
                colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    private AbstractRenderableWithArea(boolean capturesMouseEvents,
                                       Map<Integer, Action<Long>> onPress,
                                       Map<Integer, Action<Long>> onRelease,
                                       Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid,
                                       Consumer<Renderable> updateZIndexInContainer,
                                       Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
        _capturesMouseEvents = capturesMouseEvents;
        _onPress = onPress == null ? new HashMap<>() : onPress;
        _onRelease = onRelease == null ? new HashMap<>() : onRelease;
        _onMouseOver = onMouseOver;
        _onMouseLeave = onMouseLeave;
        COLOR_SHIFT_PROVIDERS = Check.ifNull(colorShiftProviders, "colorShiftProviders");
        setBorderColorProvider(borderColorProvider);
        setBorderThicknessProvider(borderThicknessProvider);
        TIMESTAMP_VALIDATOR = new TimestampValidator(null);
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
    public void press(int mouseButton, long timestamp) throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "press");
        callAction(_onPress.get(mouseButton), timestamp, "press");
    }

    @Override
    public void setOnPress(int mouseButton, Action<Long> onPress) {
        throwIfNotSupportingMouseEvents("setOnPress");
        throwOnInvalidButton(mouseButton, "setOnPress");
        if (onPress == null) {
            _onPress.remove(mouseButton);
        }
        else {
            _onPress.put(mouseButton, onPress);
        }
    }

    @Override
    public Map<Integer, String> pressActionIds() {
        return getActionIds(_onPress);
    }

    @Override
    public void release(int mouseButton, long timestamp) throws UnsupportedOperationException {
        throwOnInvalidButton(mouseButton, "release");
        callAction(_onRelease.get(mouseButton), timestamp, "release");
    }

    @Override
    public void setOnRelease(int mouseButton, Action<Long> onRelease) {
        throwIfNotSupportingMouseEvents("setOnRelease");
        throwOnInvalidButton(mouseButton, "setOnRelease");
        if (onRelease == null) {
            _onRelease.remove(mouseButton);
        }
        else {
            _onRelease.put(mouseButton, onRelease);
        }
    }

    @Override
    public Map<Integer, String> releaseActionIds() {
        return getActionIds(_onRelease);
    }

    private void throwOnInvalidButton(int button, String methodName) {
        if (button < 0 || button > 7) {
            throw new IllegalArgumentException(className() + "." + methodName +
                    ": mouseButton (" + button + ") must be between 0 and 7");
        }
    }

    private Map<Integer, String> getActionIds(Map<Integer, Action<Long>> actions) {
        HashMap<Integer, String> actionIds = new HashMap<>();
        actions.forEach((button, action) -> actionIds.put(button, action.id()));
        return actionIds;
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

    @Override
    public String mouseOverActionId() {
        return actionId(_onMouseOver, "mouseOverActionId");
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

    @Override
    public String mouseLeaveActionId() {
        return actionId(_onMouseLeave, "mouseLeaveActionId");
    }

    private String actionId(Action<Long> action, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        if (action == null) {
            return null;
        }
        else {
            return action.id();
        }
    }

    private void callAction(Action<Long> action, long timestamp, String methodName) {
        throwIfNotSupportingMouseEvents(methodName);
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (action != null) {
            action.run(timestamp);
        }
    }

    private void throwIfNotSupportingMouseEvents(String methodName) {
        if (!_capturesMouseEvents) {
            throw new UnsupportedOperationException(className() + "." + methodName +
                    ": mouse events not supported");
        }
    }

    abstract protected String className();

    @Override
    public List<ProviderAtTime<ColorShift>> colorShiftProviders() {
        return COLOR_SHIFT_PROVIDERS;
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

    protected boolean capturesMouseEventAtPoint(float x, float y, long timestamp,
                                                Supplier<AssetSnippet> snippetSupplier) {
        throwIfNotSupportingMouseEvents("capturesMouseEventAtPoint");
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        Check.throwOnLtValue(x, 0f, "x");
        Check.throwOnLtValue(y, 0f, "y");
        Check.throwOnGtValue(x, 1f, "x");
        Check.throwOnGtValue(y, 1f, "y");
        FloatBox renderingArea = _renderingAreaProvider.provide(timestamp);
        if (x < renderingArea.leftX()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: x (" + x
                    + ") is to the left of left boundary of renderable (" + renderingArea.leftX() +
                    ")");
        }
        if (x > renderingArea.rightX()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: x (" + x
                    + ") is to the right of right boundary of renderable (" + renderingArea.rightX()
                    + ")");
        }
        if (y < renderingArea.topY()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: y (" + y
                    + ") is above top boundary of renderable (" + renderingArea.topY() + ")");
        }
        if (y > renderingArea.bottomY()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: y (" + y
                    + ") is below bottom boundary of renderable (" + renderingArea.bottomY() +
                    ")");
        }
        AssetSnippet snippet = snippetSupplier.get();
        float offsetX = 0f;
        float offsetY = 0f;
        if (snippet instanceof AnimationFrameSnippet) {
            offsetX = ((AnimationFrameSnippet)snippet).offsetX();
            offsetY = ((AnimationFrameSnippet)snippet).offsetY();
        }
        Image image = snippet.image();
        int imageX =
                (int)((((x - offsetX) - renderingArea.leftX()) / renderingArea.width())
                        * (snippet.rightX() - snippet.leftX())) + snippet.leftX();
        int imageY =
                (int)((((y - offsetY) - renderingArea.topY()) / renderingArea.height())
                        * (snippet.bottomY() - snippet.topY())) + snippet.topY();
        return image.capturesMouseEventsAtPixel(imageX, imageY);
    }
}
