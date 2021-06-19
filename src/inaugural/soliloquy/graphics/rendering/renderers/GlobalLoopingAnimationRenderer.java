package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

public class GlobalLoopingAnimationRenderer
        extends CanRenderSnippets<GlobalLoopingAnimationRenderable> {
    private final ColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR;

    public GlobalLoopingAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                          FloatBoxFactory floatBoxFactory,
                                          ColorShiftStackAggregator colorShiftStackAggregator) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
        COLOR_SHIFT_STACK_AGGREGATOR = Check.ifNull(colorShiftStackAggregator,
                "colorShiftStackAggregator");
    }

    @Override
    public void render(GlobalLoopingAnimationRenderable globalLoopingAnimationRenderable,
                       long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(globalLoopingAnimationRenderable, "globalLoopingAnimationRenderable");

        FloatBox renderingArea =
                Check.ifNull(globalLoopingAnimationRenderable.getRenderingDimensionsProvider(),
                        "globalLoopingAnimationRenderable.getRenderingDimensionsProvider()")
                        .provide(timestamp);

        validateRenderableWithAreaMembers(renderingArea,
                globalLoopingAnimationRenderable.colorShifts(),
                globalLoopingAnimationRenderable.uuid(), "globalLoopingAnimationRenderable");

        Check.ifNull(globalLoopingAnimationRenderable.getGlobalLoopingAnimation(),
                "globalLoopingAnimationRenderable.getGlobalLoopingAnimation()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        NetColorShifts netColorShifts = COLOR_SHIFT_STACK_AGGREGATOR.aggregate(
                globalLoopingAnimationRenderable.colorShifts(), timestamp);

        super.render(
                renderingArea,
                globalLoopingAnimationRenderable.getGlobalLoopingAnimation().provide(timestamp),
                INTACT_COLOR,
                netColorShifts.netColorRotationShift());
    }

    private final static GlobalLoopingAnimationRenderable ARCHETYPE =
            new GlobalLoopingAnimationRenderable() {
                @Override
                public GlobalLoopingAnimation getGlobalLoopingAnimation() {
                    return null;
                }

                @Override
                public void setGlobalLoopingAnimation(GlobalLoopingAnimation globalLoopingAnimation) throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
                    return null;
                }

                @Override
                public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox> providerAtTime) throws IllegalArgumentException {

                }

                @Override
                public EntityUuid uuid() {
                    return null;
                }

                @Override
                public int getZ() {
                    return 0;
                }

                @Override
                public void setZ(int i) {

                }

                @Override
                public void delete() {

                }

                @Override
                public boolean getCapturesMouseEvents() {
                    return false;
                }

                @Override
                public void setCapturesMouseEvents(boolean b) throws IllegalArgumentException {

                }

                @Override
                public void press(int i, long l) throws UnsupportedOperationException, IllegalArgumentException {

                }

                @Override
                public void setOnPress(int i, Action<Long> action) throws IllegalArgumentException {

                }

                @Override
                public Map<Integer, String> pressActionIds() {
                    return null;
                }

                @Override
                public void release(int i, long l) throws UnsupportedOperationException, IllegalArgumentException {

                }

                @Override
                public void setOnRelease(int i, Action<Long> action) throws IllegalArgumentException {

                }

                @Override
                public Map<Integer, String> releaseActionIds() {
                    return null;
                }

                @Override
                public void mouseOver(long l) throws UnsupportedOperationException, IllegalArgumentException {

                }

                @Override
                public String mouseOverActionId() {
                    return null;
                }

                @Override
                public void mouseLeave(long l) throws UnsupportedOperationException, IllegalArgumentException {

                }

                @Override
                public String mouseLeaveActionId() {
                    return null;
                }

                @Override
                public void setOnMouseOver(Action action) {

                }

                @Override
                public void setOnMouseLeave(Action action) {

                }

                @Override
                public List<ColorShift> colorShifts() {
                    return null;
                }

                @Override
                public ProviderAtTime<Float> getBorderThicknessProvider() {
                    return null;
                }

                @Override
                public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

                }

                @Override
                public ProviderAtTime<Color> getBorderColorProvider() {
                    return null;
                }

                @Override
                public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

                }

                @Override
                public String getInterfaceName() {
                    return GlobalLoopingAnimationRenderable.class.getCanonicalName();
                }
            };
}
