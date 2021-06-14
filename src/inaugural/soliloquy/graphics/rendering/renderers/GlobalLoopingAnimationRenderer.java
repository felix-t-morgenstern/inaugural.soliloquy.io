package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

public class GlobalLoopingAnimationRenderer
        extends CanRenderSnippets<GlobalLoopingAnimationRenderable> {
    public GlobalLoopingAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                          FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
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

        super.render(renderingArea,
                globalLoopingAnimationRenderable.getGlobalLoopingAnimation().provide(timestamp),
                INTACT_COLOR);
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
                public void click() throws UnsupportedOperationException {

                }

                @Override
                public void setOnClick(Action action) {

                }

                @Override
                public void mouseOver() throws UnsupportedOperationException {

                }

                @Override
                public void setOnMouseOver(Action action) {

                }

                @Override
                public void mouseLeave() throws UnsupportedOperationException {

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
