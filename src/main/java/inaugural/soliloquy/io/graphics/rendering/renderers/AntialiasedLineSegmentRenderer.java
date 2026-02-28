package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Map;
import java.util.function.Supplier;

import static inaugural.soliloquy.io.graphics.rendering.renderers.BasicTriangleRenderer.Point.point;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static java.lang.Math.abs;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class AntialiasedLineSegmentRenderer
        implements Renderer<AntialiasedLineSegmentRenderable> {
    private final Supplier<Float> WINDOW_WIDTH_TO_HEIGHT_RATIO;
    private final TimestampValidator TIMESTAMP_VALIDATOR;
    private final TriangleSegmentRenderer TRIANGLE_SEGMENT_RENDERER;

    private static final Map<Float, Map<Float, Vertex>>
            GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION =
            mapOf();

    public AntialiasedLineSegmentRenderer(WindowResolutionManager windowResolutionManager,
                                          TimestampValidator timestampValidator,
                                          TriangleSegmentRenderer triangleSegmentRenderer) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
        WINDOW_WIDTH_TO_HEIGHT_RATIO = Check.ifNull(windowResolutionManager,
                "windowResolutionManager")::windowWidthToHeightRatio;
        TRIANGLE_SEGMENT_RENDERER =
                Check.ifNull(triangleSegmentRenderer, "triangleSegmentRenderer");
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    // TODO: Consider memoizing this whole damn thing, possibly with a memo lifetime?
    @Override
    public void render(AntialiasedLineSegmentRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        var thicknessGradientPercent =
                renderable.getThicknessGradientPercentProvider().provide(timestamp);
        var lengthGradientPercent =
                renderable.getLengthGradientPercentProvider().provide(timestamp);
        var thickness = renderable.getThicknessProvider().provide(timestamp);
        var color = renderable.getColorProvider().provide(timestamp);
        var vertex1 = renderable.getVertex1Provider().provide(timestamp);
        var vertex2 = renderable.getVertex2Provider().provide(timestamp);

        var x1 = vertex1.X;
        var y1 = vertex1.Y;
        var x2 = vertex2.X;
        var y2 = vertex2.Y;

        // The "antialiased line segment" is actually a rotatable rectangle, a percentage of
        // whose outer area is a gradient leading from transparency to its color; the renderable
        // just defines that rectangle in odd terms (i.e., the locations of its edges along one
        // of its central axes, and the distance from that axis to its other edges). Put
        // differently, the "line segment" can be thought of as nine rectangles; the middle one
        // is filled in with the specified color, and the outer ones start filled in where they
        // meet the middle rectangle, and have a gradient towards transparency on their outer
        // edges. This is what makes the line segment "fuzzy" or "antialiased". "Clockwise" here
        // refers to the rotational orientation from the perspective of v1 looking at v2. So, for
        // instance, if v2 is directly above v1, the "clockwise" points would be those on the
        // left side of the "line segment".

        // O---D------------------------------------D---O
        // |   |                                    |   |
        // P---I------------------------------------I---P
        // |   |                                    |   |
        // V   |                                    |   V
        // |   |                                    |   |
        // P---I------------------------------------I---P
        // |   |                                    |   |
        // O---D------------------------------------D---O

        // V - Vertices (for reference)
        // O - Outer
        // D - Distal (since they're furthest from their respective vertices)
        // P - Proximal (since they're closest)
        // I - Inner

        // Also, OpenGL does support antialiased lines out-of-the-box, but this functionality is
        // less flexible and is inconsistent across systems, but using CPU compute for this may
        // be inadvised (and perhaps there should be two rather than one triangle)

        Vertex v1OuterCcw;
        Vertex v1ProximalCcw;
        Vertex v1DistalCcw;
        Vertex v1InnerCcw;
        Vertex v1OuterCw;
        Vertex v1ProximalCw;
        Vertex v1DistalCw;
        Vertex v1InnerCw;
        Vertex v2OuterCcw;
        Vertex v2ProximalCcw;
        Vertex v2DistalCcw;
        Vertex v2InnerCcw;
        Vertex v2OuterCw;
        Vertex v2ProximalCw;
        Vertex v2DistalCw;
        Vertex v2InnerCw;

        if (x2 < x1) {
            x1 = vertex2.X;
            y1 = vertex2.Y;
            x2 = vertex1.X;
            y2 = vertex1.Y;
        }

        var rise = (y2 - y1);
        var run = (x2 - x1);

        if (run == 0) {
            // Logic for vertical line segments should be _MUCH_ simpler.

            if (rise == 0f) {
                // If there is simply no line at all, don't draw it.
                return;
            }

            // y2 will always be beneath y1.
            if (y2 < y1) {
                var placeholder = y2;
                y2 = y1;
                y1 = placeholder;
            }

            var vertexToOuter = thickness / 2f;
            var vertexToProximal = (thickness / 2f) * (1f - thicknessGradientPercent);
            var outerToDistal = ((y2 - y1) / 2f) * lengthGradientPercent;

            v1OuterCcw = vertexOf(x1 + vertexToOuter, y1);
            v1ProximalCcw = vertexOf(x1 + vertexToProximal, y1);
            v1DistalCcw = vertexOf(x1 + vertexToOuter, y1 + outerToDistal);
            v1InnerCcw = vertexOf(x1 + vertexToProximal, y1 + outerToDistal);
            v1OuterCw = vertexOf(x1 - vertexToOuter, y1);
            v1ProximalCw = vertexOf(x1 - vertexToProximal, y1);
            v1DistalCw = vertexOf(x1 - vertexToOuter, y1 + outerToDistal);
            v1InnerCw = vertexOf(x1 - vertexToProximal, y1 + outerToDistal);
            v2OuterCcw = vertexOf(x1 + vertexToOuter, y2);
            v2ProximalCcw = vertexOf(x1 + vertexToProximal, y2);
            v2DistalCcw = vertexOf(x1 + vertexToOuter, y2 - outerToDistal);
            v2InnerCcw = vertexOf(x1 + vertexToProximal, y2 - outerToDistal);
            v2OuterCw = vertexOf(x1 - vertexToOuter, y2);
            v2ProximalCw = vertexOf(x1 - vertexToProximal, y2);
            v2DistalCw = vertexOf(x1 - vertexToOuter, y2 - outerToDistal);
            v2InnerCw = vertexOf(x1 - vertexToProximal, y2 - outerToDistal);
        }
        else {
            // NB: Slopes are reversed, since Y values go from 0.0 at the top to 1.0 at the bottom

            var length = (float) Math.sqrt((rise * rise) + (run * run));

            var providedSlope = rise / run;
            var reciprocalSlope = -1.0f / providedSlope;
            var reciprocalSlopeAbs = abs(reciprocalSlope);

            // NB: This is the % to which the providedSlope is vertical
            var reciprocalSlopeVerticalComponent =
                    (reciprocalSlopeAbs) / (reciprocalSlopeAbs + 1.0f);

            if (reciprocalSlopeVerticalComponent > 0) {
                var distentionFactor = 1f;
                var widthToHeightRatio = WINDOW_WIDTH_TO_HEIGHT_RATIO.get();

                if (widthToHeightRatio > 1f) {
                    distentionFactor =
                            ((widthToHeightRatio - 1f) * reciprocalSlopeVerticalComponent) + 1f;
                }
                else if (widthToHeightRatio < 1f) {
                    distentionFactor =
                            1f - ((1f - widthToHeightRatio) * reciprocalSlopeVerticalComponent);
                }

                thickness *= distentionFactor;
            }

            var halfThickness = thickness / 2f;

            var outerCcwAdjustments = getAdjustments(halfThickness, reciprocalSlope);
            var outerCcwXAdjustment = outerCcwAdjustments.X;
            var outerCcwYAdjustment = outerCcwAdjustments.Y;

            var proximalCcwXAdjustment = outerCcwXAdjustment * (1f - thicknessGradientPercent);
            var proximalCcwYAdjustment = proximalCcwXAdjustment * reciprocalSlope;

            var lengthGradientAdjustments =
                    getAdjustments((length / 2f) * lengthGradientPercent, providedSlope);

            var lengthGradientXAdjust = lengthGradientAdjustments.X;
            var lengthGradientYAdjust = lengthGradientAdjustments.Y;

            v1OuterCcw = vertexOf(x1 + outerCcwXAdjustment, y1 + outerCcwYAdjustment);
            v1ProximalCcw = vertexOf(x1 + proximalCcwXAdjustment, y1 + proximalCcwYAdjustment);
            v1DistalCcw = vertexOf(v1OuterCcw.X + lengthGradientXAdjust,
                    v1OuterCcw.Y + lengthGradientYAdjust);
            v1InnerCcw = vertexOf(v1ProximalCcw.X + lengthGradientXAdjust,
                    v1ProximalCcw.Y + lengthGradientYAdjust);

            v1OuterCw = vertexOf(x1 - outerCcwXAdjustment, y1 - outerCcwYAdjustment);
            v1ProximalCw = vertexOf(x1 - proximalCcwXAdjustment, y1 - proximalCcwYAdjustment);
            v1DistalCw = vertexOf(v1OuterCw.X + lengthGradientXAdjust,
                    v1OuterCw.Y + lengthGradientYAdjust);
            v1InnerCw = vertexOf(v1ProximalCw.X + lengthGradientXAdjust,
                    v1ProximalCw.Y + lengthGradientYAdjust);

            v2OuterCcw = vertexOf(x2 + outerCcwXAdjustment, y2 + outerCcwYAdjustment);
            v2ProximalCcw = vertexOf(x2 + proximalCcwXAdjustment, y2 + proximalCcwYAdjustment);
            v2DistalCcw = vertexOf(v2OuterCcw.X - lengthGradientXAdjust,
                    v2OuterCcw.Y - lengthGradientYAdjust);
            v2InnerCcw = vertexOf(v2ProximalCcw.X - lengthGradientXAdjust,
                    v2ProximalCcw.Y - lengthGradientYAdjust);

            v2OuterCw = vertexOf(x2 - outerCcwXAdjustment, y2 - outerCcwYAdjustment);
            v2ProximalCw = vertexOf(x2 - proximalCcwXAdjustment, y2 - proximalCcwYAdjustment);
            v2DistalCw = vertexOf(v2OuterCw.X - lengthGradientXAdjust,
                    v2OuterCw.Y - lengthGradientYAdjust);
            v2InnerCw = vertexOf(v2ProximalCw.X - lengthGradientXAdjust,
                    v2ProximalCw.Y - lengthGradientYAdjust);
        }

        var transparent = transparent(color);

        var p1OuterCcw = point(v1OuterCcw, transparent);
        var p1ProximalCcw = point(v1ProximalCcw, transparent);
        var p1DistalCcw = point(v1DistalCcw, transparent);
        var p1InnerCcw = point(v1InnerCcw, color);
        var p1OuterCw = point(v1OuterCw, transparent);
        var p1ProximalCw = point(v1ProximalCw, transparent);
        var p1DistalCw = point(v1DistalCw, transparent);
        var p1InnerCw = point(v1InnerCw, color);
        var p2OuterCcw = point(v2OuterCcw, transparent);
        var p2ProximalCcw = point(v2ProximalCcw, transparent);
        var p2DistalCcw = point(v2DistalCcw, transparent);
        var p2InnerCcw = point(v2InnerCcw, color);
        var p2OuterCw = point(v2OuterCw, transparent);
        var p2ProximalCw = point(v2ProximalCw, transparent);
        var p2DistalCw = point(v2DistalCw, transparent);
        var p2InnerCw = point(v2InnerCw, color);

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1OuterCcw,
                p1DistalCcw,
                p1InnerCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCcw,
                p1OuterCcw,
                p1ProximalCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1ProximalCcw,
                p1ProximalCw,
                p1InnerCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCw,
                p1InnerCcw,
                p1ProximalCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1OuterCw,
                p1ProximalCw,
                p1InnerCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCw,
                p1OuterCw,
                p1DistalCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCcw,
                p1DistalCcw,
                p2DistalCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2DistalCcw,
                p1InnerCcw,
                p2InnerCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCcw,
                p2InnerCcw,
                p1InnerCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2InnerCcw,
                p1InnerCw,
                p2InnerCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1InnerCw,
                p2InnerCw,
                p1DistalCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p1DistalCw,
                p2DistalCw,
                p2InnerCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2InnerCcw,
                p2DistalCcw,
                p2OuterCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2OuterCcw,
                p2ProximalCcw,
                p2InnerCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2InnerCw,
                p2InnerCcw,
                p2ProximalCcw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2ProximalCcw,
                p2ProximalCw,
                p2InnerCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2InnerCw,
                p2ProximalCw,
                p2OuterCw
        );

        TRIANGLE_SEGMENT_RENDERER.draw(
                p2DistalCw,
                p2OuterCw,
                p2InnerCw
        );
    }

    private static Vertex getAdjustments(float lineSegment,
                                         float slope) {
        if (!GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.containsKey(lineSegment)) {
            GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.put(lineSegment, mapOf());
        }
        var memosForSegmentLength = GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.get(lineSegment);
        if (memosForSegmentLength.containsKey(slope)) {
            return memosForSegmentLength.get(slope);
        }
        else {
            var result1 = (float) Math.sqrt((lineSegment * lineSegment) / (1 + (slope * slope)));
            var result2 = result1 * slope;
            var result = vertexOf(result1, result2);
            memosForSegmentLength.put(slope, result);
            return result;
        }
    }

    private Color transparent(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }
}
