package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.RenderableWithArea;
import soliloquy.specs.graphics.rendering.*;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

abstract class CanRenderSnippets<TRenderable extends Renderable>
        extends AbstractRenderer<TRenderable>
        implements Renderer<TRenderable> {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    protected final FloatBoxFactory FLOAT_BOX_FACTORY;

    private Shader _shader;
    private Mesh _mesh;

    protected CanRenderSnippets(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                TRenderable archetype) {
        super(archetype);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
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
                float red, float green, float blue, float alpha) {
        float snippetLeftX = (float)snippet.leftX() / snippet.image().width();
        float snippetTopY = (float)snippet.topY() / snippet.image().height();
        float snippetRightX = (float)snippet.rightX() / snippet.image().width();
        float snippetBottomY = (float)snippet.bottomY() / snippet.image().height();
        int textureId = snippet.image().textureId();
        render(renderingArea,
                snippetLeftX, snippetTopY, snippetRightX, snippetBottomY,
                textureId,
                red, green, blue, alpha);
    }

    void render(FloatBox renderingArea,
                float snippetLeftX, float snippetTopY, float snippetRightX, float snippetBottomY,
                int textureId,
                float red, float green, float blue, float alpha) {
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
        _shader.setUniform("matColor", red, green, blue, alpha);

        _mesh.render();
    }

    protected void validateRenderableWithArea(RenderableWithArea renderableWithArea,
                                              String paramName) {
        Check.ifNull(renderableWithArea, "spriteRenderable");

        Check.ifNull(renderableWithArea.colorShifts(), paramName + ".colorShifts()");

        Check.throwOnLteZero(renderableWithArea.renderingArea().width(),
                paramName + "renderingArea.width()");

        Check.throwOnLteZero(renderableWithArea.renderingArea().height(),
                paramName + "renderingArea.height()");
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }
}
