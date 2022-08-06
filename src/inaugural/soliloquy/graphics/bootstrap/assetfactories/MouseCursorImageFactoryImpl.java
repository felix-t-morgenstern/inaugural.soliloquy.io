package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import soliloquy.specs.graphics.bootstrap.assetfactories.MouseCursorImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.MouseCursorImageDefinition;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

// NB: This class only process one at a time, since all OpenGL tasks must take place on the main
//     Thread
public class MouseCursorImageFactoryImpl implements MouseCursorImageFactory {
    private final static int DESIRED_CHANNELS = 4;

    @Override
    public Output make(MouseCursorImageDefinition definition) throws IllegalArgumentException {
        Check.ifNull(definition, "definition");
        Check.ifNullOrEmpty(definition.relativeLocation(), "relativeLocation");
        Check.ifNonNegative(definition.hotspotX(), "definition.HotspotX");
        Check.ifNonNegative(definition.hotspotY(), "definition.HotspotY");

        // NB: Look for commonality to refactor with ImageFactoryImpl
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer imageBytes = stbi_load(definition.relativeLocation(), widthBuffer, heightBuffer,
                channelsBuffer, DESIRED_CHANNELS);
        if (imageBytes == null) {
            throw new IllegalArgumentException("definition.relativeLocation (" +
                    definition.relativeLocation() + ") does not correspond to a valid image");
        }

        int width = widthBuffer.get();
        int height = heightBuffer.get();

        if (definition.hotspotX() >= width) {
            throw new IllegalArgumentException("MouseCursorImagePreloaderTask.run: HOTSPOT_X (" +
                    definition.hotspotX() + ") is out of bounds, width = " + width);
        }
        if (definition.hotspotY() >= height) {
            throw new IllegalArgumentException("MouseCursorImagePreloaderTask.run: HOTSPOT_Y (" +
                    definition.hotspotY() + ") is out of bounds, height = " + height);
        }

        GLFWImage mouseCursorImage = GLFWImage.create();
        mouseCursorImage.width(width);
        mouseCursorImage.height(height);
        mouseCursorImage.pixels(imageBytes);

        long mouseCursorId = GLFW.glfwCreateCursor(mouseCursorImage, definition.hotspotX(),
                definition.hotspotY());

        // NB: Refactor this out too
        stbi_image_free(imageBytes);

        return new Output(definition.relativeLocation(), mouseCursorId);
    }
}
