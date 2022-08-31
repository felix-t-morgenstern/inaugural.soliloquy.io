package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Math.abs;
import static org.lwjgl.opengl.GL11.*;

public class AntialiasedLineSegmentRenderer
        extends AbstractPointDrawingRenderer<AntialiasedLineSegmentRenderable> {
    private final Supplier<Float> WINDOW_WIDTH_TO_HEIGHT_RATIO;

    private static final HashMap<Float, HashMap<Float, Vertex>>
            GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION =
            new HashMap<>();
    private static final HashMap<Float, Float> HALVING_MEMOIZATION = new HashMap<>();
    private static final HashMap<Float, Float> SQUARE_ROOT_MEMOIZATION = new HashMap<>();
    private static final HashMap<Float, Float> SQUARING_MEMOIZATION = new HashMap<>();

    private static final AntialiasedLineSegmentRenderable ARCHETYPE =
            new AntialiasedLineSegmentRenderableArchetype();

    /** @noinspection ConstantConditions */
    public AntialiasedLineSegmentRenderer(WindowResolutionManager windowResolutionManager,
                                          Long mostRecentTimestamp) {
        super(ARCHETYPE, mostRecentTimestamp);
        Check.ifNull(windowResolutionManager, "windowResolutionManager");
        WINDOW_WIDTH_TO_HEIGHT_RATIO = windowResolutionManager::windowWidthToHeightRatio;
    }

    // TODO: Consider memoizing this whole damn thing, possibly with a memo lifetime?
    @Override
    public void render(AntialiasedLineSegmentRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        Check.ifNull(renderable, "renderable");

        Check.ifNull(renderable.getThicknessGradientPercentProvider(),
                "renderable.getThicknessGradientPercentProvider()");
        Check.ifNull(renderable.getLengthGradientPercentProvider(),
                "renderable.getLengthGradientPercentProvider()");
        Check.ifNull(renderable.getThicknessProvider(), "renderable.getThicknessProvider()");
        Check.ifNull(renderable.getColorProvider(), "renderable.getColorProvider()");
        Check.ifNull(renderable.getVertex1Provider(), "renderable.getVertex1Provider()");
        Check.ifNull(renderable.getVertex2Provider(), "renderable.getVertex2Provider()");

        Float thicknessGradientPercent =
                renderable.getThicknessGradientPercentProvider().provide(timestamp);
        Check.ifNull(thicknessGradientPercent, "provided thicknessGradientPercent");
        Check.isBetweenZeroAndOne(thicknessGradientPercent, "provided thicknessGradientPercent");

        Float lengthGradientPercent =
                renderable.getLengthGradientPercentProvider().provide(timestamp);
        Check.ifNull(lengthGradientPercent, "provided lengthGradientPercent");
        Check.isBetweenZeroAndOne(lengthGradientPercent, "provided lengthGradientPercent");

        Float thickness = renderable.getThicknessProvider().provide(timestamp);
        Check.ifNull(thickness, "provided thickness");
        Check.throwOnLteZero(thickness, "provided thickness");

        Color color = renderable.getColorProvider().provide(timestamp);
        Check.ifNull(color, "color");

        Vertex vertex1 = renderable.getVertex1Provider().provide(timestamp);
        Check.ifNull(vertex1, "provided vertex1");
        Vertex vertex2 = renderable.getVertex2Provider().provide(timestamp);
        Check.ifNull(vertex2, "provided vertex2");

        unbindMeshAndShader();

        float x1 = vertex1.x;
        float y1 = vertex1.y;
        float x2 = vertex2.x;
        float y2 = vertex2.y;

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
            x1 = vertex2.x;
            y1 = vertex2.y;
            x2 = vertex1.x;
            y2 = vertex1.y;
        }

        float rise = (y2 - y1);
        float run = (x2 - x1);

        if (run == 0) {
            // Logic for vertical line segments should be _MUCH_ simpler.

            if (rise == 0f) {
                // If there is simply no line at all, don't draw it. Maybe this should throw an
                // exception?
                return;
            }

            // y2 will always be beneath y1.
            if (y2 < y1) {
                float placeholder = y2;
                y2 = y1;
                y1 = placeholder;
            }

            float vertexToOuter = halve(thickness);
            float vertexToProximal = halve(thickness) * (1f - thicknessGradientPercent);
            float outerToDistal = halve(y2 - y1) * lengthGradientPercent;

            v1OuterCcw = Vertex.of(x1 + vertexToOuter, y1);
            v1ProximalCcw = Vertex.of(x1 + vertexToProximal, y1);
            v1DistalCcw = Vertex.of(x1 + vertexToOuter, y1 + outerToDistal);
            v1InnerCcw = Vertex.of(x1 + vertexToProximal, y1 + outerToDistal);
            v1OuterCw = Vertex.of(x1 - vertexToOuter, y1);
            v1ProximalCw = Vertex.of(x1 - vertexToProximal, y1);
            v1DistalCw = Vertex.of(x1 - vertexToOuter, y1 + outerToDistal);
            v1InnerCw = Vertex.of(x1 - vertexToProximal, y1 + outerToDistal);
            v2OuterCcw = Vertex.of(x1 + vertexToOuter, y2);
            v2ProximalCcw = Vertex.of(x1 + vertexToProximal, y2);
            v2DistalCcw = Vertex.of(x1 + vertexToOuter, y2 - outerToDistal);
            v2InnerCcw = Vertex.of(x1 + vertexToProximal, y2 - outerToDistal);
            v2OuterCw = Vertex.of(x1 - vertexToOuter, y2);
            v2ProximalCw = Vertex.of(x1 - vertexToProximal, y2);
            v2DistalCw = Vertex.of(x1 - vertexToOuter, y2 - outerToDistal);
            v2InnerCw = Vertex.of(x1 - vertexToProximal, y2 - outerToDistal);
        }
        else {
            // NB: Slopes are reversed, since y values go from 0.0 at the top to 1.0 at the bottom

            float length = sqRoot(square(rise) + square(run));

            float providedSlope = rise / run;
            float reciprocalSlope = -1.0f / providedSlope;
            float reciprocalSlopeAbs = abs(reciprocalSlope);

            // NB: This is the % to which the providedSlope is vertical
            float reciprocalSlopeVerticalComponent =
                    (reciprocalSlopeAbs) / (reciprocalSlopeAbs + 1.0f);

            if (reciprocalSlopeVerticalComponent > 0) {
                float distentionFactor = 1f;
                float widthToHeightRatio = WINDOW_WIDTH_TO_HEIGHT_RATIO.get();

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

            float halfThickness = halve(thickness);

            Vertex outerCcwAdjustments = getAdjustments(halfThickness, reciprocalSlope);
            float outerCcwXAdjustment = outerCcwAdjustments.x;
            float outerCcwYAdjustment = outerCcwAdjustments.y;

            float proximalCcwXAdjustment = outerCcwXAdjustment * (1f - thicknessGradientPercent);
            float proximalCcwYAdjustment = proximalCcwXAdjustment * reciprocalSlope;

            Vertex lengthGradientAdjustments =
                    getAdjustments(halve(length) * lengthGradientPercent, providedSlope);

            float lengthGradientXAdjust = lengthGradientAdjustments.x;
            float lengthGradientYAdjust = lengthGradientAdjustments.y;

            v1OuterCcw = Vertex.of(x1 + outerCcwXAdjustment, y1 + outerCcwYAdjustment);
            v1ProximalCcw = Vertex.of(x1 + proximalCcwXAdjustment, y1 + proximalCcwYAdjustment);
            v1DistalCcw = Vertex.of(v1OuterCcw.x + lengthGradientXAdjust,
                    v1OuterCcw.y + lengthGradientYAdjust);
            v1InnerCcw = Vertex.of(v1ProximalCcw.x + lengthGradientXAdjust,
                    v1ProximalCcw.y + lengthGradientYAdjust);

            v1OuterCw = Vertex.of(x1 - outerCcwXAdjustment, y1 - outerCcwYAdjustment);
            v1ProximalCw = Vertex.of(x1 - proximalCcwXAdjustment, y1 - proximalCcwYAdjustment);
            v1DistalCw = Vertex.of(v1OuterCw.x + lengthGradientXAdjust,
                    v1OuterCw.y + lengthGradientYAdjust);
            v1InnerCw = Vertex.of(v1ProximalCw.x + lengthGradientXAdjust,
                    v1ProximalCw.y + lengthGradientYAdjust);

            v2OuterCcw = Vertex.of(x2 + outerCcwXAdjustment, y2 + outerCcwYAdjustment);
            v2ProximalCcw = Vertex.of(x2 + proximalCcwXAdjustment, y2 + proximalCcwYAdjustment);
            v2DistalCcw = Vertex.of(v2OuterCcw.x - lengthGradientXAdjust,
                    v2OuterCcw.y - lengthGradientYAdjust);
            v2InnerCcw = Vertex.of(v2ProximalCcw.x - lengthGradientXAdjust,
                    v2ProximalCcw.y - lengthGradientYAdjust);

            v2OuterCw = Vertex.of(x2 - outerCcwXAdjustment, y2 - outerCcwYAdjustment);
            v2ProximalCw = Vertex.of(x2 - proximalCcwXAdjustment, y2 - proximalCcwYAdjustment);
            v2DistalCw = Vertex.of(v2OuterCw.x - lengthGradientXAdjust,
                    v2OuterCw.y - lengthGradientYAdjust);
            v2InnerCw = Vertex.of(v2ProximalCw.x - lengthGradientXAdjust,
                    v2ProximalCw.y - lengthGradientYAdjust);
        }

        glBegin(GL_TRIANGLES);

        setDrawColor(transparent(color));
        drawPoint(v1OuterCcw);
        drawPoint(v1DistalCcw);
        setDrawColor(color);
        drawPoint(v1InnerCcw);

        drawPoint(v1InnerCcw);
        setDrawColor(transparent(color));
        drawPoint(v1OuterCcw);
        drawPoint(v1ProximalCcw);

        drawPoint(v1ProximalCcw);
        drawPoint(v1ProximalCw);
        setDrawColor(color);
        drawPoint(v1InnerCcw);

        drawPoint(v1InnerCw);
        drawPoint(v1InnerCcw);
        setDrawColor(transparent(color));
        drawPoint(v1ProximalCw);

        drawPoint(v1OuterCw);
        drawPoint(v1ProximalCw);
        setDrawColor(color);
        drawPoint(v1InnerCw);

        drawPoint(v1InnerCw);
        setDrawColor(transparent(color));
        drawPoint(v1OuterCw);
        drawPoint(v1DistalCw);

        setDrawColor(color);
        drawPoint(v1InnerCcw);
        setDrawColor(transparent(color));
        drawPoint(v1DistalCcw);
        drawPoint(v2DistalCcw);

        drawPoint(v2DistalCcw);
        setDrawColor(color);
        drawPoint(v1InnerCcw);
        drawPoint(v2InnerCcw);

        drawPoint(v1InnerCcw);
        drawPoint(v2InnerCcw);
        drawPoint(v1InnerCw);

        drawPoint(v2InnerCcw);
        drawPoint(v1InnerCw);
        drawPoint(v2InnerCw);

        drawPoint(v1InnerCw);
        drawPoint(v2InnerCw);
        setDrawColor(transparent(color));
        drawPoint(v1DistalCw);

        drawPoint(v1DistalCw);
        drawPoint(v2DistalCw);
        setDrawColor(color);
        drawPoint(v2InnerCw);

        drawPoint(v2InnerCcw);
        setDrawColor(transparent(color));
        drawPoint(v2DistalCcw);
        drawPoint(v2OuterCcw);

        drawPoint(v2OuterCcw);
        drawPoint(v2ProximalCcw);
        setDrawColor(color);
        drawPoint(v2InnerCcw);

        drawPoint(v2InnerCw);
        drawPoint(v2InnerCcw);
        setDrawColor(transparent(color));
        drawPoint(v2ProximalCcw);

        drawPoint(v2ProximalCcw);
        drawPoint(v2ProximalCw);
        setDrawColor(color);
        drawPoint(v2InnerCw);

        drawPoint(v2InnerCw);
        setDrawColor(transparent(color));
        drawPoint(v2ProximalCw);
        drawPoint(v2OuterCw);

        drawPoint(v2DistalCw);
        drawPoint(v2OuterCw);
        setDrawColor(color);
        drawPoint(v2InnerCw);

        glEnd();
    }

    private static Vertex getAdjustments(float lineSegment,
                                                     float slope) {
        if (!GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.containsKey(lineSegment)) {
            GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.put(lineSegment, new HashMap<>());
        }
        HashMap<Float, Vertex> memosForSegmentLength =
                GET_OUTER_CCW_X_ADJUSTMENT_MEMOIZATION.get(lineSegment);
        if (memosForSegmentLength.containsKey(slope)) {
            return memosForSegmentLength.get(slope);
        }
        else {
            float result1 =
                    sqRoot(square(lineSegment) / (1 + square(slope)));
            float result2 = result1 * slope;
            Vertex result = Vertex.of(result1, result2);
            memosForSegmentLength.put(slope, result);
            return result;
        }
    }

    private static float halve(float value) {
        return simpleOperationMemoized(value, v -> v / 2f, HALVING_MEMOIZATION);
    }

    private static float sqRoot(float value) {
        return simpleOperationMemoized(value, v -> (float) Math.sqrt(v), SQUARE_ROOT_MEMOIZATION);
    }

    private static float square(float value) {
        return simpleOperationMemoized(value, v -> v * v, SQUARING_MEMOIZATION);
    }

    private static float simpleOperationMemoized(float value,
                                                 Function<Float, Float> operation,
                                                 HashMap<Float, Float> memoization) {
        if (memoization.containsKey(value)) {
            return memoization.get(value);
        }

        float result = operation.apply(value);
        memoization.put(value, result);
        return result;
    }

    @Override
    protected String className() {
        return "AntialiasedLineSegmentRenderer";
    }

    private static class AntialiasedLineSegmentRenderableArchetype
            implements AntialiasedLineSegmentRenderable {
        @Override
        public String getInterfaceName() {
            return AntialiasedLineSegmentRenderable.class.getCanonicalName();
        }

        @Override
        public ProviderAtTime<Vertex> getVertex1Provider() {
            return null;
        }

        @Override
        public void setVertex1Provider(ProviderAtTime<Vertex> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Vertex> getVertex2Provider() {
            return null;
        }

        @Override
        public void setVertex2Provider(ProviderAtTime<Vertex> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Float> getThicknessProvider() {
            return null;
        }

        @Override
        public void setThicknessProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getColorProvider() {
            return null;
        }

        @Override
        public void setColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Float> getThicknessGradientPercentProvider() {
            return null;
        }

        @Override
        public void setThicknessGradientPercentProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Float> getLengthGradientPercentProvider() {
            return null;
        }

        @Override
        public void setLengthGradientPercentProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public void setZ(int i) {

        }

        @Override
        public RenderableStack containingStack() {
            return null;
        }

        @Override
        public void delete() {

        }

        @Override
        public UUID uuid() {
            return null;
        }
    }
}
