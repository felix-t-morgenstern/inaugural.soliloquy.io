package inaugural.soliloquy.graphics.io;

import com.conversantmedia.util.collection.spatial.RTreeFacade;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.graphics.rendering.FloatBox;

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
            if (roughResult.renderingDimensions.leftX() <= point.X &&
                    roughResult.renderingDimensions.topY() <= point.Y &&
                    roughResult.renderingDimensions.rightX() >= point.X &&
                    roughResult.renderingDimensions.bottomY() >= point.Y &&
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

    @Override
    public String getInterfaceName() {
        return MouseEventCapturingSpatialIndex.class.getCanonicalName();
    }
}
