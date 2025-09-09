package inaugural.soliloquy.io.mouse;

import com.conversantmedia.util.collection.spatial.RTreeFacade;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.input.mouse.MouseEventCapturingSpatialIndex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;

import java.util.Comparator;

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
        var roughResults = R_TREE.search(point.X, point.Y, timestamp);
        var capturingResults = roughResults.stream()
                .filter(result -> {
                    var dimens = result.getRenderingDimensionsProvider().provide(timestamp);
                    return dimens.LEFT_X <= point.X &&
                            dimens.TOP_Y <= point.Y &&
                            dimens.RIGHT_X >= point.X &&
                            dimens.BOTTOM_Y >= point.Y &&
                            result.capturesMouseEventAtPoint(point, timestamp);
                });
        var sortedByZ =
                capturingResults.sorted(Comparator.comparingInt(Renderable::getZ).reversed());
        var sortedByTierAndZ = sortedByZ.sorted(Comparator.comparingInt(r -> r.containingComponent().tier()));

        return sortedByTierAndZ.findFirst().orElse(null);
    }

    @Override
    public void putRenderable(RenderableWithMouseEvents renderable)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");
        R_TREE.put(renderable);
    }

    @Override
    public void removeRenderable(RenderableWithMouseEvents renderableWithMouseEvents)
            throws IllegalArgumentException {
        R_TREE.remove(Check.ifNull(renderableWithMouseEvents, "renderableWithMouseEvents"));
    }
}
