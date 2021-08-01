package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Coordinate;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

abstract class CanRenderSnippets<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable>
        implements Renderer<TRenderable> {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    protected final FloatBoxFactory FLOAT_BOX_FACTORY;

    protected float _screenWidthToHeightRatio;
    private Shader _shader;
    private Mesh _mesh;

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                TRenderable archetype,
                                WindowResolutionManager windowResolutionManager,
                                Long mostRecentTimestamp) {
        this(renderingBoundaries, floatBoxFactory, archetype, mostRecentTimestamp);
        Check.ifNull(windowResolutionManager, "windowResolutionManager")
                .registerResolutionSubscriber(this::registerResolutionChange);
    }

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                TRenderable archetype, Long mostRecentTimestamp) {
        super(archetype, mostRecentTimestamp);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    private void registerResolutionChange(Coordinate resolution) {
        _screenWidthToHeightRatio = (float)resolution.getX() / resolution.getY();
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        _shader = Check.ifNull(shader, "shader");
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        _mesh = Check.ifNull(mesh, "mesh");
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor) {
        render(renderingArea, snippet, matColor, null, 0f);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                Color overrideColor) {
        render(renderingArea, snippet, matColor, overrideColor, 0f);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                float colorRotationShift) {
        render(renderingArea, snippet, matColor, null, colorRotationShift);
    }

    void render(FloatBox renderingArea,
                AssetSnippet snippet,
                Color matColor,
                Color overrideColor,
                float colorRotationShift) {
        float snippetLeftX = (float)snippet.leftX() / snippet.image().width();
        float snippetTopY = (float)snippet.topY() / snippet.image().height();
        float snippetRightX = (float)snippet.rightX() / snippet.image().width();
        float snippetBottomY = (float)snippet.bottomY() / snippet.image().height();
        int textureId = snippet.image().textureId();
        render(renderingArea,
                snippetLeftX, snippetTopY, snippetRightX, snippetBottomY,
                textureId,
                matColor,
                overrideColor,
                colorRotationShift);
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
                0f);
    }

    void render(FloatBox renderingArea,
                float snippetLeftX, float snippetTopY, float snippetRightX, float snippetBottomY,
                int textureId,
                Color matColor,
                Color overrideColor,
                float colorRotationShift) {
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

        colorRotationShift %= 1.0f;
        if (colorRotationShift < 0) {
            colorRotationShift += 1.0f;
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

    protected void validateRenderableWithAreaMembers(FloatBox renderingArea,
                                                     List<ColorShift> colorShifts,
                                                     EntityUuid id,
                                                     String paramName) {
        Check.ifNull(renderingArea, paramName + " provided renderingArea");

        Check.ifNull(colorShifts, paramName + ".colorShifts()");

        Check.throwOnLteZero(renderingArea.width(),
                paramName + " provided renderingArea.width()");

        Check.throwOnLteZero(renderingArea.height(),
                paramName + " provided renderingArea.height()");

        Check.ifNull(id, paramName + " provided id()");
    }
}
