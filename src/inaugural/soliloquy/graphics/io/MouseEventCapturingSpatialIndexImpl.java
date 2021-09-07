package inaugural.soliloquy.graphics.io;

import com.conversantmedia.util.collection.spatial.RTreeFacade;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.io.MouseEventCapturingSpatialIndex;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;

public class MouseEventCapturingSpatialIndexImpl
        implements MouseEventCapturingSpatialIndex {
    private final RTreeFacade R_TREE;

    public MouseEventCapturingSpatialIndexImpl() {
        R_TREE = new RTreeFacade();
    }

    @Override
    public RenderableWithArea getCapturingRenderableAtPoint(float x, float y)
            throws IllegalArgumentException {
        List<RTreeFacade.RenderableWithAreaSearchObject> roughResults = R_TREE.search(x, y);
        int highestZThusFar = Integer.MIN_VALUE;
        RenderableWithArea renderableWithAreaWithHighestZThusFar = null;
        for(RTreeFacade.RenderableWithAreaSearchObject roughResult : roughResults) {
            if(roughResult._renderingDimensions.leftX() <= x &&
                    roughResult._renderingDimensions.topY() <= y &&
                    roughResult._renderingDimensions.rightX() >= x &&
                    roughResult._renderingDimensions.bottomY() >= y &&
                    roughResult._renderableWithArea.getZ() > highestZThusFar) {
                highestZThusFar = roughResult._renderableWithArea.getZ();
                renderableWithAreaWithHighestZThusFar = roughResult._renderableWithArea;
            }
        }
        return renderableWithAreaWithHighestZThusFar;
    }

    @Override
    public void putRenderable(RenderableWithArea renderableWithArea, FloatBox renderingDimensions)
            throws IllegalArgumentException {
        Check.ifNull(renderableWithArea, "renderableWithArea");
        Check.ifNull(renderingDimensions, "renderingDimensions");
        if (!renderableWithArea.getCapturesMouseEvents()) {
            throw new IllegalArgumentException(
                    "MouseEventCapturingSpatialIndexImpl.putRenderable: renderable must capture " +
                            "mouse events");
        }
        R_TREE.put(renderableWithArea, renderingDimensions);
    }

    @Override
    public void removeRenderable(RenderableWithArea renderableWithArea)
            throws IllegalArgumentException {
        R_TREE.remove(Check.ifNull(renderableWithArea, "renderableWithArea"));
    }

    @Override
    public String getInterfaceName() {
        return MouseEventCapturingSpatialIndex.class.getCanonicalName();
    }
}
