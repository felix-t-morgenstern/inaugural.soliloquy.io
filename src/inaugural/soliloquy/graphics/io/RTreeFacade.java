package com.conversantmedia.util.collection.spatial;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.conversantmedia.util.collection.geometry.Rect2d;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RTreeFacade {
    private final RTree<RenderableWithMouseEventsSearchObject> R_TREE;
    private final HashMap<RenderableWithMouseEvents, RenderableWithMouseEventsSearchObject> ITEMS;
    /** @noinspection FieldCanBeLocal */
    private final float TOLERANCE = 0.001f;

    public RTreeFacade() {
        R_TREE = new RTree<>(new RenderableWithMouseEventsSearchObjectRectBuilder(), 2, 8,
                RTree.Split.AXIAL);
        ITEMS = new HashMap<>();
    }

    public void put(RenderableWithMouseEvents renderableWithMouseEvents,
                    FloatBox renderingDimensions) {
        if (ITEMS.containsKey(renderableWithMouseEvents)) {
            remove(renderableWithMouseEvents);
        }

        RenderableWithMouseEventsSearchObject searchObject =
                new RenderableWithMouseEventsSearchObject(
                        renderableWithMouseEvents,
                        renderingDimensions
                );
        ITEMS.put(renderableWithMouseEvents, searchObject);
        R_TREE.add(searchObject);
    }

    public List<RenderableWithMouseEventsSearchObject> search(float x, float y) {
        ArrayList<RenderableWithMouseEventsSearchObject> results = new ArrayList<>();
        Rect2d searchRect = new Rect2d(x - TOLERANCE, y - TOLERANCE,
                x + TOLERANCE, y + TOLERANCE);
        R_TREE.intersects(searchRect, results::add);
        return results;
    }

    public void remove(RenderableWithMouseEvents renderable) {
        R_TREE.remove(ITEMS.get(renderable));
        ITEMS.remove(renderable);
    }

    public static class RenderableWithMouseEventsSearchObject {
        public RenderableWithMouseEvents _renderableWithMouseEvents;
        public FloatBox _renderingDimensions;

        private RenderableWithMouseEventsSearchObject(RenderableWithMouseEvents renderable,
                                                      FloatBox renderingDimensions) {
            _renderableWithMouseEvents = renderable;
            _renderingDimensions = renderingDimensions;
        }
    }

    private static class RenderableWithMouseEventsSearchObjectRectBuilder
            implements RectBuilder<RenderableWithMouseEventsSearchObject> {

        @Override
        public Rect2d getBBox(RenderableWithMouseEventsSearchObject
                                      renderableWithMouseEventsSearchObject) {
            return new Rect2d(
                    renderableWithMouseEventsSearchObject._renderingDimensions.leftX(),
                    renderableWithMouseEventsSearchObject._renderingDimensions.topY(),
                    renderableWithMouseEventsSearchObject._renderingDimensions.rightX(),
                    renderableWithMouseEventsSearchObject._renderingDimensions.bottomY()
            );
        }

        @Override
        public Rect2d getMbr(HyperPoint p1, HyperPoint p2) {
            final Point2d point1 = (Point2d) p1;
            final Point2d point2 = (Point2d) p2;
            return new Rect2d(point1, point2);
        }
    }
}
