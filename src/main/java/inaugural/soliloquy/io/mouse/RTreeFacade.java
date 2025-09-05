package com.conversantmedia.util.collection.spatial;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.conversantmedia.util.collection.geometry.Rect2d;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;

import java.util.List;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class RTreeFacade {
    private final Set<RenderableWithMouseEvents> ITEMS;
    /** @noinspection FieldCanBeLocal */
    private final float TOLERANCE = 0.001f;

    public RTreeFacade() {
        ITEMS = setOf();
    }

    public void put(RenderableWithMouseEvents renderable) {
        ITEMS.add(renderable);
    }

    public List<RenderableWithMouseEvents> search(float x, float y, long timestamp) {
        var rTree = new RTree<>(new RTreeFacadeRectBuilder(), 2, 8, RTree.Split.AXIAL);
        ITEMS.stream()
                .filter(RenderableWithMouseEvents::getCapturesMouseEvents)
                .forEach(item -> rTree.add(pairOf(item, timestamp)));
        var results = Collections.<RenderableWithMouseEvents>listOf();
        var searchRect = new Rect2d(x - TOLERANCE, y - TOLERANCE, x + TOLERANCE, y + TOLERANCE);
        rTree.intersects(
                searchRect,
                renderableAndTimestamp -> results.add(renderableAndTimestamp.FIRST)
        );
        return results;
    }

    public void remove(RenderableWithMouseEvents renderable) {
        ITEMS.remove(renderable);
    }

    private static class RTreeFacadeRectBuilder
            implements RectBuilder<Pair<RenderableWithMouseEvents, Long>> {

        @Override
        public Rect2d getBBox(Pair<RenderableWithMouseEvents, Long> renderableAndTimestamp) {
            var dimens = renderableAndTimestamp.FIRST.getRenderingDimensionsProvider()
                    .provide(renderableAndTimestamp.SECOND);
            return new Rect2d(
                    dimens.LEFT_X,
                    dimens.TOP_Y,
                    dimens.RIGHT_X,
                    dimens.BOTTOM_Y
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
