package com.conversantmedia.util.collection.spatial;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.conversantmedia.util.collection.geometry.Rect2d;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class RTreeFacade {
    private final RTree<RenderableWithMouseEventsSearchObject> R_TREE;
    private final Map<RenderableWithMouseEvents, RenderableWithMouseEventsSearchObject> ITEMS;
    /** @noinspection FieldCanBeLocal */
    private final float TOLERANCE = 0.001f;

    public RTreeFacade() {
        R_TREE = new RTree<>(new RenderableWithMouseEventsSearchObjectRectBuilder(), 2, 8,
                RTree.Split.AXIAL);
        ITEMS = mapOf();
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
        List<RenderableWithMouseEventsSearchObject> results = listOf();
        var searchRect = new Rect2d(x - TOLERANCE, y - TOLERANCE, x + TOLERANCE, y + TOLERANCE);
        R_TREE.intersects(searchRect, results::add);
        return results;
    }

    public void remove(RenderableWithMouseEvents renderable) {
        R_TREE.remove(ITEMS.get(renderable));
        ITEMS.remove(renderable);
    }

    public static class RenderableWithMouseEventsSearchObject {
        public RenderableWithMouseEvents renderableWithMouseEvents;
        public FloatBox renderingDimensions;

        private RenderableWithMouseEventsSearchObject(RenderableWithMouseEvents renderable,
                                                      FloatBox renderingDimensions) {
            renderableWithMouseEvents = renderable;
            this.renderingDimensions = renderingDimensions;
        }
    }

    private static class RenderableWithMouseEventsSearchObjectRectBuilder
            implements RectBuilder<RenderableWithMouseEventsSearchObject> {

        @Override
        public Rect2d getBBox(RenderableWithMouseEventsSearchObject
                                      renderableWithMouseEventsSearchObject) {
            return new Rect2d(
                    renderableWithMouseEventsSearchObject.renderingDimensions.LEFT_X,
                    renderableWithMouseEventsSearchObject.renderingDimensions.TOP_Y,
                    renderableWithMouseEventsSearchObject.renderingDimensions.RIGHT_X,
                    renderableWithMouseEventsSearchObject.renderingDimensions.BOTTOM_Y
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
