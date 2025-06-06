package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

abstract class CanRenderSnippets<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable>
        implements Renderer<TRenderable> {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    protected Supplier<Float> getScreenWidthToHeightRatio;

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                WindowResolutionManager windowResolutionManager,
                                Long mostRecentTimestamp) {
        this(renderingBoundaries, mostRecentTimestamp);
        Check.ifNull(windowResolutionManager, "windowResolutionManager");
        getScreenWidthToHeightRatio = windowResolutionManager::windowWidthToHeightRatio;
    }

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                Long mostRecentTimestamp) {
        super(mostRecentTimestamp);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    protected NetColorShifts netColorShifts(List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                            ColorShiftStackAggregator colorShiftStackAggregator,
                                            long timestamp) {
        List<ColorShift> colorShifts = listOf();
        colorShiftProviders.forEach(provider ->
                colorShifts.add(provider.provide(timestamp)));

        return colorShiftStackAggregator.aggregate(colorShifts, timestamp);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                Color overrideColor) {
        render(renderingArea, snippet, matColor, overrideColor, null);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                NetColorShifts netColorShifts) {
        render(renderingArea, snippet, matColor, null, netColorShifts);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                Color overrideColor,
                NetColorShifts netColorShifts) {
        var snippetLeftX = (float) snippet.leftX() / snippet.image().width();
        var snippetTopY = (float) snippet.topY() / snippet.image().height();
        var snippetRightX = (float) snippet.rightX() / snippet.image().width();
        var snippetBottomY = (float) snippet.bottomY() / snippet.image().height();
        var textureId = snippet.image().textureId();
        render(renderingArea,
                snippetLeftX, snippetTopY, snippetRightX, snippetBottomY,
                textureId,
                matColor,
                overrideColor,
                netColorShifts);
    }

    void render(FloatBox renderingArea,
                float snippetLeftX, float snippetTopY, float snippetRightX, float snippetBottomY,
                int textureId,
                Color matColor) {
        render(renderingArea,
                snippetLeftX, snippetTopY, snippetRightX, snippetBottomY,
                textureId,
                matColor,
                null,
                null);
    }

    void render(FloatBox renderingArea,
                float snippetLeftX, float snippetTopY, float snippetRightX, float snippetBottomY,
                int textureId,
                Color matColor,
                Color overrideColor,
                NetColorShifts netColorShifts) {
        var windowPosition = intersection(renderingArea, RENDERING_BOUNDARIES.currentBoundaries());

        if (windowPosition == null) {
            return;
        }

        float snippetLeftXWithinBounds;
        float snippetTopYWithinBounds;
        float snippetRightXWithinBounds;
        float snippetBottomYWithinBounds;

        if (windowPosition.LEFT_X > renderingArea.LEFT_X) {
            var percentageOfSnippetToCutOnLeft =
                    ((windowPosition.LEFT_X - renderingArea.LEFT_X) / renderingArea.width());
            snippetLeftXWithinBounds = snippetLeftX +
                    (percentageOfSnippetToCutOnLeft * (snippetRightX - snippetLeftX));
        }
        else {
            snippetLeftXWithinBounds = snippetLeftX;
        }

        if (windowPosition.TOP_Y > renderingArea.TOP_Y) {
            var percentageOfSnippetToCutOnTop =
                    ((windowPosition.TOP_Y - renderingArea.TOP_Y) / renderingArea.height());
            snippetTopYWithinBounds = snippetTopY +
                    (percentageOfSnippetToCutOnTop * (snippetBottomY - snippetTopY));
        }
        else {
            snippetTopYWithinBounds = snippetTopY;
        }

        if (windowPosition.RIGHT_X < renderingArea.RIGHT_X) {
            var percentageOfSnippetToCutOnRight =
                    ((renderingArea.RIGHT_X - windowPosition.RIGHT_X) / renderingArea.width());
            snippetRightXWithinBounds = snippetRightX -
                    (percentageOfSnippetToCutOnRight * (snippetRightX - snippetLeftX));
        }
        else {
            snippetRightXWithinBounds = snippetRightX;
        }

        if (windowPosition.BOTTOM_Y < renderingArea.BOTTOM_Y) {
            var percentageOfSnippetToCutOnBottom =
                    ((renderingArea.BOTTOM_Y - windowPosition.BOTTOM_Y)
                            / renderingArea.height());
            snippetBottomYWithinBounds = snippetBottomY -
                    (percentageOfSnippetToCutOnBottom * (snippetBottomY - snippetTopY));
        }
        else {
            snippetBottomYWithinBounds = snippetBottomY;
        }

        var snippetBox = floatBoxOf(
                snippetLeftXWithinBounds,
                snippetTopYWithinBounds,
                snippetRightXWithinBounds,
                snippetBottomYWithinBounds
        );

        var colorRotationShift = 0f;
        var brightnessShift = 0f;
        var redIntensityShift = 0f;
        var greenIntensityShift = 0f;
        var blueIntensityShift = 0f;

        if (netColorShifts != null) {
            colorRotationShift = netColorShifts.colorRotationShift();

            colorRotationShift %= 1.0f;
            if (colorRotationShift < 0) {
                colorRotationShift += 1.0f;
            }

            brightnessShift = netColorShifts.brightnessShift();

            redIntensityShift = netColorShifts.redIntensityShift();
            greenIntensityShift = netColorShifts.greenIntensityShift();
            blueIntensityShift = netColorShifts.blueIntensityShift();
        }

        glBindTexture(GL_TEXTURE_2D, textureId);

        // offset:
        //     The first two values are the x and y locations within the image, where the
        //     upper-left corner of the image is (0f,0f), and the bottom-right corner of the image
        //     is (1f,1f).
        //     The second two values are the width and height of the snippet to take from the
        //     image, where the total width and height of the image are 1f.
        shader.setUniform("offset", snippetBox.LEFT_X, snippetBox.BOTTOM_Y,
                snippetBox.width(), -snippetBox.height());
        // dimensionsInWindow:
        //     The percentage of the window's total width and height taken up by the snippet, where
        //     the total width and height of the window are both 1f.
        shader.setUniform("dimensionsInWindow", windowPosition.width(), windowPosition.height());
        // windowPosition:
        //     The position to render the snippet, where the upper-left corner of the window is
        //     (0f,0f), and the lower-right corner of the window is (1f,1f).
        shader.setUniform("windowPosition", windowPosition.LEFT_X, windowPosition.TOP_Y);
//        // NB: This will almost undoubtedly change when ColorShifts are implemented
        // matColor:
        //     These values are the percentage of each channel (RGBA) to render, where 1f is 100%
        //     of that channel rendered, and 0f is 0% of that channel rendered
        shader.setUniform("matColor", matColor);
        // colorRotationShift:
        //     The degree to which the colors are rotated on the color wheel, ranging from [0,1]
        shader.setUniform("colorRotationShift", colorRotationShift);
        // colorRotationShift:
        //     The degree to which the colors are made brighter or darker, ranging from [-1,1]
        shader.setUniform("brightnessShift", brightnessShift);
        // redIntensityShift:
        //     The degree to which the reds are made brighter or darker, ranging from [-1,1]
        shader.setUniform("redIntensityShift", redIntensityShift);
        // greenIntensityShift:
        //     The degree to which the greens are made brighter or darker, ranging from [-1,1]
        shader.setUniform("greenIntensityShift", greenIntensityShift);
        // blueIntensityShift:
        //     The degree to which the blues are made brighter or darker, ranging from [-1,1]
        shader.setUniform("blueIntensityShift", blueIntensityShift);
        // overrideColor:
        //     This color entirely overrides the color of the actual object being rendered. This is
        //     intended for use in borders, shadows, etc. If the x value is less than 0, the shader
        //     ignores this value.
        if (overrideColor == null) {
            shader.setUniform("overrideColor", -1f, -1f, -1f, -1f);
        }
        else {
            shader.setUniform("overrideColor", overrideColor);
        }

        mesh.render();
    }

    protected void validateRenderableWithDimensionsMembers(FloatBox renderingDimensions,
                                                           List<ProviderAtTime<ColorShift>>
                                                                   colorShiftProviders,
                                                           UUID id,
                                                           String paramName) {
        Check.ifNull(renderingDimensions, paramName + " provided renderingDimensions");

        Check.ifNull(colorShiftProviders, paramName + ".colorShiftProviders()");

        Check.throwOnLteZero(renderingDimensions.width(),
                paramName + " provided renderingDimensions width");

        Check.throwOnLteZero(renderingDimensions.height(),
                paramName + " provided renderingDimensions height");

        Check.ifNull(id, paramName + " provided id()");
    }
}
