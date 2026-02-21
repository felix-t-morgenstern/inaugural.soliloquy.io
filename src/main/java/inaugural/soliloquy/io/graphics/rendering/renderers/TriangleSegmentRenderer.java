package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static inaugural.soliloquy.io.graphics.rendering.renderers.BasicTriangleRenderer.Point;
import static inaugural.soliloquy.io.graphics.rendering.renderers.BasicTriangleRenderer.Point.point;
import static inaugural.soliloquy.tools.Tools.valIsInRange;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.contains;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;
import static inaugural.soliloquy.tools.valueobjects.Vertex.*;
import static inaugural.soliloquy.tools.valueobjects.Vertex.distance;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TriangleSegmentRenderer {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final BasicTriangleRenderer BASIC_TRIANGLE_RENDERER;

    public TriangleSegmentRenderer(RenderingBoundaries renderingBoundaries,
                                   BasicTriangleRenderer basicTriangleRenderer) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        BASIC_TRIANGLE_RENDERER = Check.ifNull(basicTriangleRenderer, "basicTriangleRenderer");
    }

    public void draw(
            Vertex vertex1,
            Color color1,
            Vertex vertex2,
            Color color2,
            Vertex vertex3,
            Color color3,
            Integer textureId,
            Float textureTileWidth,
            Float textureTileHeight
    ) {
        var triangleEncompassingDimens = polygonEncompassingDimens(vertex1, vertex2, vertex3);

        var renderingBoundaries = RENDERING_BOUNDARIES.currentBoundaries();
        var dimensBoundsIntersect = intersection(triangleEncompassingDimens, renderingBoundaries);
        if (dimensBoundsIntersect == null) {
            return;
        }

        var points = Collections.<Point>listOf();
        var verticesAndColors = listOf(
                pairOf(vertex1, color1),
                pairOf(vertex2, color2),
                pairOf(vertex3, color3)
        );

        addPoints(
                verticesAndColors,
                renderingBoundaries,
                triangleEncompassingDimens,
                textureTileWidth,
                textureTileHeight,
                points,
                0,
                null
        );

        renderingBoundaries.corners().forEach(corner -> {
            if (contains(triangleEncompassingDimens, corner) &&
                    pointIsInTriangle(
                            corner,
                            vertex1,
                            vertex2,
                            vertex3
                    )
            ) {
                points.add(makePoint(
                        corner,
                        verticesAndColors,
                        triangleEncompassingDimens,
                        textureTileWidth,
                        textureTileHeight
                ));
            }
        });

        if (points.size() == 3) {
            BASIC_TRIANGLE_RENDERER.draw(
                    points.get(0),
                    points.get(1),
                    points.get(2),
                    textureId
            );
        }
        else {
            var centroid = getVerticesCentroid(points.stream().map(Point::loc));
            var centroidPoint = makePoint(
                    centroid,
                    verticesAndColors,
                    triangleEncompassingDimens,
                    textureTileWidth,
                    textureTileHeight
            );
            points.sort(Comparator.comparingDouble(
                    p -> Math.atan2(p.loc().Y - centroid.Y, p.loc().X - centroid.X)));
            for (var i = 0; i < points.size(); i++) {
                BASIC_TRIANGLE_RENDERER.draw(
                        points.get(i),
                        points.get((i + 1) % points.size()),
                        centroidPoint,
                        textureId
                );
            }
        }
    }

    private void addPoints(List<Pair<Vertex, Color>> verticesAndColors,
                           FloatBox renderingBoundaries,
                           FloatBox encompassingDimens,
                           Float textureTileWidth,
                           Float textureTileHeight,
                           List<Point> points,
                           int index,
                           Boolean prevWasIn) {
        // index is allowed to go to 3 to "loop around" to the first vertex, in case there are
        // intersects between vertices 1 and 3 which need to be captured

        boolean currentIsIn;
        var indexInArray = index % 3;
        var currentVertex = verticesAndColors.get(indexInArray).FIRST;
        // (again, index will be 3 when looping 'back' to the start)
        var prevVertex = verticesAndColors.get((index + 2) % 3).FIRST;
        var slope = slope(currentVertex, prevVertex);
        if (contains(renderingBoundaries, currentVertex)) {
            // current is in...
            currentIsIn = true;

            if (index < 3) {
                points.add(point(
                        currentVertex,
                        verticesAndColors.get(index).SECOND,
                        texCoordinates(
                                currentVertex,
                                encompassingDimens,
                                textureTileWidth,
                                textureTileHeight
                        )
                ));
            }

            if (index > 0 && !prevWasIn) {
                // prev wasn't in...
                var intersects = getIntersects(
                        prevVertex,
                        currentVertex,
                        slope,
                        renderingBoundaries,
                        1
                );
                // (If prev wasn't in and current is in, that implies 1 and only 1 intersect)
                points.add(makePoint(intersects.getFirst(), verticesAndColors, encompassingDimens,
                        textureTileWidth, textureTileHeight));
            }
            // If prev was also in, it would have been added, and there will have been no
            // intersects to capture
        }
        else {
            // current is out...
            currentIsIn = false;

            if (index > 0) {
                getIntersects(
                        prevVertex,
                        currentVertex,
                        slope,
                        renderingBoundaries,
                        prevWasIn ? 1 : 2
                ).forEach(i -> {
                    points.add(makePoint(i, verticesAndColors, encompassingDimens,
                            textureTileWidth, textureTileHeight));
                });
            }
        }


        if (index < 3) {
            addPoints(verticesAndColors, renderingBoundaries, encompassingDimens, textureTileWidth,
                    textureTileHeight, points, index + 1, currentIsIn);
        }
    }

    private Point makePoint(Vertex loc,
                            List<Pair<Vertex, Color>> verticesAndColors,
                            FloatBox encompassingDimens,
                            Float textureTileWidth,
                            Float textureTileHeight) {
        return point(
                loc,
                triangulateColor(
                        loc,
                        verticesAndColors.getFirst(),
                        verticesAndColors.get(1),
                        verticesAndColors.get(2)
                ),
                texCoordinates(
                        loc,
                        encompassingDimens,
                        textureTileWidth,
                        textureTileHeight
                )
        );
    }

    private List<Vertex> getIntersects(Vertex v1,
                                       Vertex v2,
                                       float slope,
                                       FloatBox renderingDimensions,
                                       int maxExpectedDimens) {
        var intersects = Collections.<Vertex>listOf();

        addVerticalIntersect(
                v1,
                v2,
                slope,
                intersects,
                renderingDimensions.LEFT_X,
                renderingDimensions.TOP_Y,
                renderingDimensions.BOTTOM_Y
        );

        if (intersects.size() < maxExpectedDimens) {
            addVerticalIntersect(
                    v1,
                    v2,
                    slope,
                    intersects,
                    renderingDimensions.RIGHT_X,
                    renderingDimensions.TOP_Y,
                    renderingDimensions.BOTTOM_Y
            );
        }
        else {
            return intersects;
        }

        if (intersects.size() < maxExpectedDimens) {
            addHorizontalIntersect(
                    v1,
                    v2,
                    slope,
                    intersects,
                    renderingDimensions.TOP_Y,
                    renderingDimensions.LEFT_X,
                    renderingDimensions.RIGHT_X
            );
        }
        else {
            return intersects;
        }

        if (intersects.size() < maxExpectedDimens) {
            addHorizontalIntersect(
                    v1,
                    v2,
                    slope,
                    intersects,
                    renderingDimensions.BOTTOM_Y,
                    renderingDimensions.LEFT_X,
                    renderingDimensions.RIGHT_X
            );
        }

        return intersects;
    }

    private void addVerticalIntersect(Vertex v1,
                                      Vertex v2,
                                      float slope,
                                      List<Vertex> intersects,
                                      float xIntersectVal,
                                      float lowerY,
                                      float upperY) {
        if (slope == Float.POSITIVE_INFINITY || slope == Float.NEGATIVE_INFINITY) {
            return;
        }
        var yIntersectAtAxis = yIntersectAtX(slope, v1, xIntersectVal);
        if (yIntersectAtAxis >= lowerY && yIntersectAtAxis <= upperY &&
                valIsInRange(yIntersectAtAxis, v1.Y, v2.Y)) {
            intersects.add(vertexOf(xIntersectVal, yIntersectAtAxis));
        }
    }

    private void addHorizontalIntersect(Vertex v1,
                                        Vertex v2,
                                        float slope,
                                        List<Vertex> intersects,
                                        float yIntersectVal,
                                        float lowerX,
                                        float upperX) {
        if (slope == Float.POSITIVE_INFINITY || slope == Float.NEGATIVE_INFINITY) {
            if (valIsInRange(v1.X, lowerX, upperX) && valIsInRange(yIntersectVal, v1.Y, v2.Y)) {
                intersects.add(vertexOf(v1.X, yIntersectVal));
            }
            else {
                return;
            }
        }
        var xIntersectAtAxis = xIntersectAtY(slope, v1, yIntersectVal);
        if (xIntersectAtAxis >= lowerX && xIntersectAtAxis <= upperX &&
                valIsInRange(xIntersectAtAxis, v1.X, v2.X)) {
            intersects.add(vertexOf(xIntersectAtAxis, yIntersectVal));
        }
    }

    private Color triangulateColor(
            Vertex point,
            Pair<Vertex, Color> renderingVertex1,
            Pair<Vertex, Color> renderingVertex2,
            Pair<Vertex, Color> renderingVertex3
    ) {

        if (point.equals(renderingVertex1.FIRST)) {
            return renderingVertex1.SECOND;
        }
        if (point.equals(renderingVertex2.FIRST)) {
            return renderingVertex2.SECOND;
        }
        if (point.equals(renderingVertex3.FIRST)) {
            return renderingVertex3.SECOND;
        }

        var color1Weight = getWeight(
                renderingVertex1.FIRST,
                point,
                renderingVertex2.FIRST,
                renderingVertex3.FIRST
        );
        var color2Weight = getWeight(
                renderingVertex2.FIRST,
                point,
                renderingVertex1.FIRST,
                renderingVertex3.FIRST
        );
        var color3Weight = getWeight(
                renderingVertex3.FIRST,
                point,
                renderingVertex1.FIRST,
                renderingVertex2.FIRST
        );

        return new Color(
                channelAverage(
                        renderingVertex1.SECOND, color1Weight,
                        renderingVertex2.SECOND, color2Weight,
                        renderingVertex3.SECOND, color3Weight,
                        Color::getRed
                ),
                channelAverage(
                        renderingVertex1.SECOND, color1Weight,
                        renderingVertex2.SECOND, color2Weight,
                        renderingVertex3.SECOND, color3Weight,
                        Color::getGreen
                ),
                channelAverage(
                        renderingVertex1.SECOND, color1Weight,
                        renderingVertex2.SECOND, color2Weight,
                        renderingVertex3.SECOND, color3Weight,
                        Color::getBlue
                ),
                channelAverage(
                        renderingVertex1.SECOND, color1Weight,
                        renderingVertex2.SECOND, color2Weight,
                        renderingVertex3.SECOND, color3Weight,
                        Color::getAlpha
                )
        );
    }

    private int channelAverage(
            Color color1, float color1Weight,
            Color color2, float color2Weight,
            Color color3, float color3Weight,
            Function<Color, Integer> getChannel) {
        return
                Math.min(
                        Math.round(
                                (getChannel.apply(color1) * color1Weight) +
                                        (getChannel.apply(color2) * color2Weight) +
                                        (getChannel.apply(color3) * color3Weight)
                        ),
                        255
                );
    }

    private float getWeight(Vertex source,
                            Vertex point,
                            Vertex oppositeVertex1,
                            Vertex oppositeVertex2) {
        var sourceToPointSlope = slope(source, point);
        var sourceToPointYInt = yIntersectAtX(sourceToPointSlope, point, 0);
        var oppositeSegmentSlope = slope(oppositeVertex1, oppositeVertex2);
        var oppositeSegmentYInt = yIntersectAtX(oppositeSegmentSlope, oppositeVertex1, 0);

        var intersectX = (oppositeSegmentYInt - sourceToPointYInt) /
                (sourceToPointSlope - oppositeSegmentSlope);

        var intersect =
                vertexOf(intersectX, (oppositeSegmentSlope * intersectX) + oppositeSegmentYInt);

        var sourceToPointDist = distance(source, point);
        var sourceToIntersectDist = distance(source, intersect);

        return 1f - (sourceToPointDist / sourceToIntersectDist);
    }

    private Vertex texCoordinates(Vertex vertex,
                                  FloatBox triangleEncompassingDimens,
                                  Float textureTileWidth,
                                  Float textureTileHeight) {
        if (textureTileWidth == null || textureTileHeight == null) {
            return null;
        }
        return vertexOf(
                (vertex.X - triangleEncompassingDimens.LEFT_X) / textureTileWidth,
                (vertex.Y - triangleEncompassingDimens.TOP_Y) / textureTileHeight
        );
    }
}
