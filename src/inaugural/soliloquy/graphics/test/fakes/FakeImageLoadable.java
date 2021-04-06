package inaugural.soliloquy.graphics.test.fakes;

import org.lwjgl.BufferUtils;
import soliloquy.specs.graphics.assets.Image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class FakeImageLoadable implements Image {
    public String RelativeLocation;

    public int _width;
    public int _height;
    public int _textureId;

    private final static int DESIRED_CHANNELS = 4;
    private final static int LEVEL_OF_DETAIL = 0;
    private final static int BORDER = 0;

    public FakeImageLoadable(String relativeLocation) {
        RelativeLocation = relativeLocation;
    }

    public void load() {
        _textureId = loadTexture();
    }

    private int loadTexture() {
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer imageData = stbi_load(RelativeLocation, widthBuffer, heightBuffer,
                channelsBuffer, DESIRED_CHANNELS);
        assert imageData != null;

        int textureId = glGenTextures();

        _width = widthBuffer.get();
        _height = heightBuffer.get();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, LEVEL_OF_DETAIL, GL_RGBA, _width, _height, BORDER, GL_RGBA,
                GL_UNSIGNED_BYTE, imageData);

        stbi_image_free(imageData);

        return textureId;
    }

    @Override
    public int textureId() {
        return _textureId;
    }

    @Override
    public String relativeLocation() {
        return RelativeLocation;
    }

    @Override
    public int width() {
        return _width;
    }

    @Override
    public int height() {
        return _height;
    }

    @Override
    public boolean supportsMouseEventCapturing() {
        return false;
    }

    @Override
    public boolean capturesMouseEventsAtPixel(int i, int i1) throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
