package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.List;

public class FiniteAnimationRenderer
        extends CanRenderSnippets<FiniteAnimationRenderable>
        implements Renderer<FiniteAnimationRenderable> {
    public FiniteAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                   FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
    }

    @Override
    public void render(FiniteAnimationRenderable finiteAnimationRenderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(finiteAnimationRenderable, "finiteAnimationRenderable");

        validateRenderableWithArea(finiteAnimationRenderable, "finiteAnimationRenderable");

        validateTimestamp(timestamp, "FiniteAnimationRenderable");

        if (timestamp < finiteAnimationRenderable.startTimestamp()) {
            return;
        }

        if (timestamp >= finiteAnimationRenderable.endTimestamp()) {
            finiteAnimationRenderable.delete();
            return;
        }

        AnimationFrameSnippet snippet =  finiteAnimationRenderable.currentSnippet(timestamp);

        super.render(finiteAnimationRenderable.renderingArea(), snippet,
                1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static final FiniteAnimationRenderable ARCHETYPE = new FiniteAnimationRenderable() {
        @Override
        public long startTimestamp() {
            return 0;
        }

        @Override
        public long endTimestamp() {
            return 0;
        }

        @Override
        public AnimationFrameSnippet currentSnippet(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

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
        public EntityUuid id() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return FiniteAnimationRenderable.class.getCanonicalName();
        }
    };
}
