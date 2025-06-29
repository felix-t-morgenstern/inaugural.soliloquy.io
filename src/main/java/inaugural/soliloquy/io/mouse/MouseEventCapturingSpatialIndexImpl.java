package inaugural.soliloquy.io.mouse;

import com.conversantmedia.util.collection.spatial.RTreeFacade;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.mouse.MouseEventCapturingSpatialIndex;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;

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
        var highestZThusFar = Integer.MIN_VALUE;
        RenderableWithMouseEvents renderableWithHighestZThusFar = null;
        for (var roughResult : roughResults) {
            if (roughResult.renderingDimensions.LEFT_X <= point.X &&
                    roughResult.renderingDimensions.TOP_Y <= point.Y &&
                    roughResult.renderingDimensions.RIGHT_X >= point.X &&
                    roughResult.renderingDimensions.BOTTOM_Y >= point.Y &&
                    roughResult.renderableWithMouseEvents.getZ() > highestZThusFar &&
                    roughResult.renderableWithMouseEvents
                            .capturesMouseEventAtPoint(point, timestamp)) {
                highestZThusFar = roughResult.renderableWithMouseEvents.getZ();
                renderableWithHighestZThusFar = roughResult.renderableWithMouseEvents;
            }
        }
        return renderableWithHighestZThusFar;
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
