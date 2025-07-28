package inaugural.soliloquy.io.mouse;

import com.conversantmedia.util.collection.spatial.RTreeFacade;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.input.mouse.MouseEventCapturingSpatialIndex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.ui.Component;

import java.util.Comparator;
import java.util.function.Function;

public class MouseEventCapturingSpatialIndexImpl
        implements MouseEventCapturingSpatialIndex {
    private final RTreeFacade R_TREE;

    public MouseEventCapturingSpatialIndexImpl() {
        R_TREE = new RTreeFacade();
    }

    // TODO: Verify and use timestamp
    @Override
    public RenderableWithMouseEvents getCapturingRenderableAtPoint(Vertex point, long timestamp)
            throws IllegalArgumentException {
        var roughResults = R_TREE.search(point.X, point.Y);
        var capturingResults = roughResults.stream()
                .filter(result -> result.renderingDimensions.LEFT_X <= point.X &&
                        result.renderingDimensions.TOP_Y <= point.Y &&
                        result.renderingDimensions.RIGHT_X >= point.X &&
                        result.renderingDimensions.BOTTOM_Y >= point.Y &&
                        result.renderable.capturesMouseEventAtPoint(point,
                                timestamp))
                .map(result -> result.renderable);
        var sortedByZ = capturingResults.sorted(Comparator.comparingInt(Renderable::getZ).reversed());
        var sortedByTierAndZ = sortedByZ.sorted(Comparator.comparingInt(r -> r.component().tier()));

        return sortedByTierAndZ.findFirst().orElse(null);
    }

    @Override
    public void putRenderable(RenderableWithMouseEvents renderableWithMouseEvents,
                              FloatBox renderingDimensions)
            throws IllegalArgumentException {
        Check.ifNull(renderableWithMouseEvents, "renderableWithMouseEvents");
        Check.ifNull(renderingDimensions, "renderingDimensions");
        if (!renderableWithMouseEvents.getCapturesMouseEvents()) {
            throw new IllegalArgumentException(
                    "MouseEventCapturingSpatialIndexImpl.putRenderable: renderable must capture " +
                            "mouse events");
        }
        R_TREE.put(renderableWithMouseEvents, renderingDimensions);
    }

    @Override
    public void removeRenderable(RenderableWithMouseEvents renderableWithMouseEvents)
            throws IllegalArgumentException {
        R_TREE.remove(Check.ifNull(renderableWithMouseEvents, "renderableWithMouseEvents"));
    }
}
