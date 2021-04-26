package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
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

        FloatBox renderingArea = Check.ifNull(finiteAnimationRenderable.renderingAreaProvider(),
                "finiteAnimationRenderable.renderingAreaProvider()")
                .provide(timestamp);

        validateRenderableWithAreaMembers(renderingArea, finiteAnimationRenderable.colorShifts(),
                finiteAnimationRenderable.id(), "finiteAnimationRenderable");

        validateTimestamp(timestamp, "FiniteAnimationRenderable");

        if (timestamp < finiteAnimationRenderable.startTimestamp()) {
            return;
        }

        if (timestamp >= finiteAnimationRenderable.endTimestamp()) {
            finiteAnimationRenderable.delete();
            return;
        }

        AnimationFrameSnippet snippet =  finiteAnimationRenderable.provide(timestamp);

        super.render(renderingArea, snippet, Color.WHITE);
    }

    private static final FiniteAnimationRenderable ARCHETYPE = new FiniteAnimationRenderable() {
        @Override
        public AnimationFrameSnippet getArchetype() {
            return new AnimationFrameSnippet() {
                @Override
                public float offsetX() {
                    return 0;
                }

                @Override
                public float offsetY() {
                    return 0;
                }

                @Override
                public Image image() {
                    return null;
                }

                @Override
                public int leftX() {
                    return 0;
                }

                @Override
                public int topY() {
                    return 0;
                }

                @Override
                public int rightX() {
                    return 0;
                }

                @Override
                public int bottomY() {
                    return 0;
                }

                @Override
                public String getInterfaceName() {
                    return AnimationFrameSnippet.class.getCanonicalName();
                }
            };
        }

        @Override
        public AnimationFrameSnippet provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public EntityUuid id() {
            return null;
        }

        @Override
        public ProviderAtTime<FloatBox> renderingAreaProvider() {
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
        public long startTimestamp() {
            return 0;
        }

        @Override
        public long endTimestamp() {
            return 0;
        }

        @Override
        public String getInterfaceName() {
            return FiniteAnimationRenderable.class.getCanonicalName();
        }
    };
}
