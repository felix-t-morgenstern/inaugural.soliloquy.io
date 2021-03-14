package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.*;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class SpriteRenderer implements Renderer<SpriteRenderable> {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    private Shader _shader;
    private Mesh _mesh;

    public SpriteRenderer(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        _mesh = Check.ifNull(mesh, "mesh");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        _shader = shader;
    }

    @Override
    public void render(SpriteRenderable spriteRenderable) throws IllegalArgumentException {
        Check.ifNull(spriteRenderable, "spriteRenderable");

        Check.ifNull(spriteRenderable.sprite(), "spriteRenderable.sprite()");

        Check.ifNull(spriteRenderable.colorShifts(), "spriteRenderable.colorShifts()");

        Check.throwOnLteZero(spriteRenderable.renderingArea().width(),
                "spriteRenderable.width()");

        Check.throwOnLteZero(spriteRenderable.renderingArea().height(),
                "spriteRenderable.height()");

        System.out.println("SpriteRenderer rendering!");

        FloatBox windowPosition = spriteRenderable.renderingArea()
                .intersection(RENDERING_BOUNDARIES.currentBoundaries());

        if (windowPosition == null) {
            return;
        }

        float snippetLeftXWithinBounds;
        float snippetTopYWithinBounds;
        float snippetRightXWithinBounds;
        float snippetBottomYWithinBounds;

        float snippetLeftX =
                (float)spriteRenderable.sprite().leftX() /
                        spriteRenderable.sprite().image().width();

        float snippetTopY =
                (float)spriteRenderable.sprite().topY() /
                        spriteRenderable.sprite().image().height();

        float snippetRightX =
                (float)spriteRenderable.sprite().rightX() /
                        spriteRenderable.sprite().image().width();

        float snippetBottomY =
                (float)spriteRenderable.sprite().bottomY() /
                        spriteRenderable.sprite().image().height();

        if (windowPosition.leftX() > spriteRenderable.renderingArea().leftX()) {
            float percentageOfSnippetToCutOnLeft =
                    ((windowPosition.leftX() - spriteRenderable.renderingArea().leftX())
                            / spriteRenderable.renderingArea().width());
            snippetLeftXWithinBounds = snippetLeftX +
                    (percentageOfSnippetToCutOnLeft * (snippetRightX - snippetLeftX));
        }
        else {
            snippetLeftXWithinBounds = snippetLeftX;
        }

        if (windowPosition.topY() > spriteRenderable.renderingArea().topY()) {
            float percentageOfSnippetToCutOnTop =
                    ((windowPosition.topY() - spriteRenderable.renderingArea().topY())
                            / spriteRenderable.renderingArea().height());
            snippetTopYWithinBounds = snippetTopY +
                    (percentageOfSnippetToCutOnTop * (snippetBottomY - snippetTopY));
        }
        else {
            snippetTopYWithinBounds = snippetTopY;
        }

        if (windowPosition.rightX() < spriteRenderable.renderingArea().rightX()) {
            float percentageOfSnippetToCutOnRight =
                    ((spriteRenderable.renderingArea().rightX() - windowPosition.rightX())
                            / spriteRenderable.renderingArea().width());
            snippetRightXWithinBounds = snippetRightX -
                    (percentageOfSnippetToCutOnRight * (snippetRightX - snippetLeftX));
        }
        else {
            snippetRightXWithinBounds = snippetRightX;
        }

        if (windowPosition.bottomY() < spriteRenderable.renderingArea().bottomY()) {
            float percentageOfSnippetToCutOnBottom =
                    ((spriteRenderable.renderingArea().bottomY() - windowPosition.bottomY())
                            / spriteRenderable.renderingArea().height());
            snippetBottomYWithinBounds = snippetBottomY -
                    (percentageOfSnippetToCutOnBottom * (snippetBottomY - snippetTopY));
        }
        else {
            snippetBottomYWithinBounds = snippetBottomY;
        }

        // TODO: Have a snippet provide this on demand; DEFINITELY decouple FloatBoxImpl
        FloatBox snippetBox = new FloatBoxImpl(
                snippetLeftXWithinBounds,
                snippetTopYWithinBounds,
                snippetRightXWithinBounds,
                snippetBottomYWithinBounds);

        glBindTexture(GL_TEXTURE_2D, spriteRenderable.sprite().image().textureId());

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
        _shader.setUniform("matColor", 1.0f, 1.0f, 1.0f, 1.0f);

        _mesh.render();
    }

    @Override
    public String getInterfaceName() {
        return Renderer.class.getCanonicalName() + "<" +
                SpriteRenderable.class.getCanonicalName() + ">";
    }
}
