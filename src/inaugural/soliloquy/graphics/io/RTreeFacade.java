package com.conversantmedia.util.collection.spatial;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.conversantmedia.util.collection.geometry.Rect2d;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RTreeFacade {
    private final RTree<RenderableWithAreaSearchObject> R_TREE;
    private final HashMap<RenderableWithArea, RenderableWithAreaSearchObject> ITEMS;
    private final float TOLERANCE = 0.001f;

    public RTreeFacade() {
        R_TREE = new RTree<>(new RenderableWithAreaSearchObjectRectBuilder(), 2, 8,
                RTree.Split.AXIAL);
        ITEMS = new HashMap<>();
    }

    public void put(RenderableWithArea renderableWithArea, FloatBox renderingDimensions) {
        if (ITEMS.containsKey(renderableWithArea)) {
            remove(renderableWithArea);
        }

        RenderableWithAreaSearchObject searchObject =
                new RenderableWithAreaSearchObject(renderableWithArea, renderingDimensions);
        ITEMS.put(renderableWithArea, searchObject);
        R_TREE.add(searchObject);
    }

    public List<RenderableWithAreaSearchObject> search(float x, float y) {
        ArrayList<RenderableWithAreaSearchObject> results = new ArrayList<>();
        Rect2d searchRect = new Rect2d(x - TOLERANCE, y - TOLERANCE,
                x + TOLERANCE, y + TOLERANCE);
        R_TREE.intersects(searchRect, results::add);
        return results;
    }

    public void remove(RenderableWithArea renderableWithArea) {
        R_TREE.remove(ITEMS.get(renderableWithArea));
        ITEMS.remove(renderableWithArea);
    }

    public static class RenderableWithAreaSearchObject {
        public RenderableWithArea _renderableWithArea;
        public FloatBox _renderingDimensions;

        private RenderableWithAreaSearchObject(RenderableWithArea renderableWithArea,
                                               FloatBox renderingDimensions) {
            _renderableWithArea = renderableWithArea;
            _renderingDimensions = renderingDimensions;
        }
    }

    private static class RenderableWithAreaSearchObjectRectBuilder
            implements RectBuilder<RenderableWithAreaSearchObject> {

        @Override
        public Rect2d getBBox(RenderableWithAreaSearchObject renderableWithAreaSearchObject) {
            return new Rect2d(
                    renderableWithAreaSearchObject._renderingDimensions.leftX(),
                    renderableWithAreaSearchObject._renderingDimensions.topY(),
                    renderableWithAreaSearchObject._renderingDimensions.rightX(),
                    renderableWithAreaSearchObject._renderingDimensions.bottomY()
            );
        }

        @Override
        public Rect2d getMbr(HyperPoint p1, HyperPoint p2) {
            final Point2d point1 = (Point2d)p1;
            final Point2d point2 = (Point2d)p2;
            return new Rect2d(point1, point2);
        }
    }
}
