package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

abstract class CanRenderSnippets<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable>
        implements Renderer<TRenderable> {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    protected final FloatBoxFactory FLOAT_BOX_FACTORY;

    protected Supplier<Float> _getScreenWidthToHeightRatio;

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                TRenderable archetype,
                                WindowResolutionManager windowResolutionManager,
                                Long mostRecentTimestamp) {
        this(renderingBoundaries, floatBoxFactory, archetype, mostRecentTimestamp);
        Check.ifNull(windowResolutionManager, "windowResolutionManager");
        _getScreenWidthToHeightRatio = windowResolutionManager::windowWidthToHeightRatio;
    }

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                TRenderable archetype, Long mostRecentTimestamp) {
        super(archetype, mostRecentTimestamp);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    protected NetColorShifts netColorShifts(List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                            ColorShiftStackAggregator colorShiftStackAggregator,
                                            long timestamp) {
        ArrayList<ColorShift> colorShifts = new ArrayList<>();
        colorShiftProviders.forEach(provider ->
                colorShifts.add(provider.provide(timestamp)));

        return colorShiftStackAggregator.aggregate(colorShifts, timestamp);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor) {
        render(renderingArea, snippet, matColor, null, null);
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
        float snippetLeftX = (float) snippet.leftX() / snippet.image().width();
        float snippetTopY = (float) snippet.topY() / snippet.image().height();
        float snippetRightX = (float) snippet.rightX() / snippet.image().width();
        float snippetBottomY = (float) snippet.bottomY() / snippet.image().height();
        int textureId = snippet.image().textureId();
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
        FloatBox windowPosition = renderingArea.intersection(
                RENDERING_BOUNDARIES.currentBoundaries());

        if (windowPosition == null) {
            return;
        }

        float snippetLeftXWithinBounds;
        float snippetTopYWithinBounds;
        float snippetRightXWithinBounds;
        float snippetBottomYWithinBounds;

        if (windowPosition.leftX() > renderingArea.leftX()) {
            float percentageOfSnippetToCutOnLeft =
                    ((windowPosition.leftX() - renderingArea.leftX()) / renderingArea.width());
            snippetLeftXWithinBounds = snippetLeftX +
                    (percentageOfSnippetToCutOnLeft * (snippetRightX - snippetLeftX));
        }
        else {
            snippetLeftXWithinBounds = snippetLeftX;
        }

        if (windowPosition.topY() > renderingArea.topY()) {
            float percentageOfSnippetToCutOnTop =
                    ((windowPosition.topY() - renderingArea.topY()) / renderingArea.height());
            snippetTopYWithinBounds = snippetTopY +
                    (percentageOfSnippetToCutOnTop * (snippetBottomY - snippetTopY));
        }
        else {
            snippetTopYWithinBounds = snippetTopY;
        }

        if (windowPosition.rightX() < renderingArea.rightX()) {
            float percentageOfSnippetToCutOnRight =
                    ((renderingArea.rightX() - windowPosition.rightX()) / renderingArea.width());
            snippetRightXWithinBounds = snippetRightX -
                    (percentageOfSnippetToCutOnRight * (snippetRightX - snippetLeftX));
        }
        else {
            snippetRightXWithinBounds = snippetRightX;
        }

        if (windowPosition.bottomY() < renderingArea.bottomY()) {
            float percentageOfSnippetToCutOnBottom =
                    ((renderingArea.bottomY() - windowPosition.bottomY())
                            / renderingArea.height());
            snippetBottomYWithinBounds = snippetBottomY -
                    (percentageOfSnippetToCutOnBottom * (snippetBottomY - snippetTopY));
        }
        else {
            snippetBottomYWithinBounds = snippetBottomY;
        }

        FloatBox snippetBox = FLOAT_BOX_FACTORY.make(
                snippetLeftXWithinBounds,
                snippetTopYWithinBounds,
                snippetRightXWithinBounds,
                snippetBottomYWithinBounds);

        float colorRotationShift = 0f;
        float brightnessShift = 0f;
        float redIntensityShift = 0f;
        float greenIntensityShift = 0f;
        float blueIntensityShift = 0f;

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
        _shader.setUniform("offset", snippetBox.leftX(), snippetBox.bottomY(),
                snippetBox.width(), -snippetBox.height());
        // dimensionsInWindow:
        //     The percentage of the window's total width and height taken up by the snippet, where
        //     the total width and height of the window are both 1f.
        _shader.setUniform("dimensionsInWindow", windowPosition.width(), windowPosition.height());
        // windowPosition:
        //     The position to render the snippet, where the upper-left corner of the window is
        //     (0f,0f), and the lower-right corner of the window is (1f,1f).
        _shader.setUniform("windowPosition", windowPosition.leftX(), windowPosition.topY());
//        // NB: This will almost undoubtedly change when ColorShifts are implemented
        // matColor:
        //     These values are the percentage of each channel (RGBA) to render, where 1f is 100%
        //     of that channel rendered, and 0f is 0% of that channel rendered
        _shader.setUniform("matColor", matColor);
        // colorRotationShift:
        //     The degree to which the colors are rotated on the color wheel, ranging from [0,1]
        _shader.setUniform("colorRotationShift", colorRotationShift);
        // colorRotationShift:
        //     The degree to which the colors are made brighter or darker, ranging from [-1,1]
        _shader.setUniform("brightnessShift", brightnessShift);
        // redIntensityShift:
        //     The degree to which the reds are made brighter or darker, ranging from [-1,1]
        _shader.setUniform("redIntensityShift", redIntensityShift);
        // greenIntensityShift:
        //     The degree to which the greens are made brighter or darker, ranging from [-1,1]
        _shader.setUniform("greenIntensityShift", greenIntensityShift);
        // blueIntensityShift:
        //     The degree to which the blues are made brighter or darker, ranging from [-1,1]
        _shader.setUniform("blueIntensityShift", blueIntensityShift);
        // overrideColor:
        //     This color entirely overrides the color of the actual object being rendered. This is
        //     intended for use in borders, shadows, etc. If the x value is less than 0, the shader
        //     ignores this value.
        if (overrideColor == null) {
            _shader.setUniform("overrideColor", -1f, -1f, -1f, -1f);
        }
        else {
            _shader.setUniform("overrideColor", overrideColor);
        }

        _mesh.render();
    }

    protected void validateRenderableWithDimensionsMembers(FloatBox renderingDimensions,
                                                           List<ProviderAtTime<ColorShift>>
                                                                   colorShiftProviders,
                                                           UUID id,
                                                           String paramName) {
        Check.ifNull(renderingDimensions, paramName + " provided renderingDimensions");

        Check.ifNull(colorShiftProviders, paramName + ".colorShiftProviders()");

        Check.throwOnLteZero(renderingDimensions.width(),
                paramName + " provided renderingDimensions.width()");

        Check.throwOnLteZero(renderingDimensions.height(),
                paramName + " provided renderingDimensions.height()");

        Check.ifNull(id, paramName + " provided id()");
    }
}
