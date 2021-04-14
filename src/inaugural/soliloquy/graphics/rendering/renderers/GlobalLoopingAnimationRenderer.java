package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.RenderableAnimation;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.util.List;

public class GlobalLoopingAnimationRenderer extends CanRenderSnippets<GlobalLoopingAnimationRenderable> {
    public GlobalLoopingAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                          FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
    }

    @Override
    public void render(GlobalLoopingAnimationRenderable globalLoopingAnimationRenderable,
                       long timestamp)
            throws IllegalArgumentException {
        validateRenderableWithArea(globalLoopingAnimationRenderable,
                "globalLoopingAnimationRenderable");

        Check.ifNull(globalLoopingAnimationRenderable.loopingAnimation(),
                "globalLoopingAnimationRenderable.loopingAnimation()");

        validateTimestamp(timestamp, "GlobalLoopingAnimationRenderer");

        super.render(globalLoopingAnimationRenderable.renderingArea(),
                globalLoopingAnimationRenderable.loopingAnimation().currentSnippet(timestamp),
                1.0f, 1.0f, 1.0f, 1.0f);
    }

    private final static GlobalLoopingAnimationRenderable ARCHETYPE =
            new GlobalLoopingAnimationRenderable() {
                @Override
                public String getInterfaceName() {
                    return GlobalLoopingAnimationRenderable.class.getCanonicalName();
                }

                @Override
                public EntityUuid id() {
                    return null;
                }

                @Override
                public FloatBox renderingArea() {
                    return null;
                }

                @Override
                public int z() {
                    return 0;
                }

                @Override
                public void delete() {

                }

                @Override
                public boolean capturesMouseEvents() {
                    return false;
                }

                @Override
                public void click() throws UnsupportedOperationException {

                }

                @Override
                public void mouseOver() throws UnsupportedOperationException {

                }

                @Override
                public void mouseLeave() throws UnsupportedOperationException {

                }

                @Override
                public List<ColorShift> colorShifts() {
                    return null;
                }

                @Override
                public RenderableAnimation loopingAnimation() {
                    return null;
                }
            };
}
