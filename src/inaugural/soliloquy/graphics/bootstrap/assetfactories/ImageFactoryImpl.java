package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.assets.ImageImpl;
import inaugural.soliloquy.graphics.assets.ReadsCapturingMasks;
import inaugural.soliloquy.tools.Check;
import org.lwjgl.BufferUtils;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class ImageFactoryImpl extends ReadsCapturingMasks implements ImageFactory {
    private final static int DESIRED_CHANNELS = 4;
    private final static int LEVEL_OF_DETAIL = 0;
    private final static int BORDER = 0;
    private final static int MAX_ALPHA_VALUE = 256;

    private final float ALPHA_THRESHOLD;

    public ImageFactoryImpl(float alphaThreshold) {
        ALPHA_THRESHOLD = Check.throwOnGtValue(
                Check.throwOnLtValue(alphaThreshold, 0f, "alphaThreshold"),
                1f, "alphaThreshold");
    }

    @Override
    public Image make(String relativeLocation, boolean supportsEventCapturing)
            throws IllegalArgumentException {
        ImageData imageData = loadImageData(relativeLocation, supportsEventCapturing,
                ALPHA_THRESHOLD);
        if (imageData.CapturingMask != null) {
            return new ImageImpl(imageData.TextureId, relativeLocation, imageData.Width,
                    imageData.Height, imageData.CapturingMask);
        }
        else {
            return new ImageImpl(imageData.TextureId, relativeLocation, imageData.Width,
                    imageData.Height);
        }
    }

    private static ImageData loadImageData(String relativeLocation,
                                           boolean supportsEventCapturing,
                                           float alphaThreshold) {
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer imageBytes = stbi_load(relativeLocation, widthBuffer, heightBuffer,
                channelsBuffer, DESIRED_CHANNELS);
        assert imageBytes != null;

        int textureId = glGenTextures();

        int width = widthBuffer.get();
        int height = heightBuffer.get();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, LEVEL_OF_DETAIL, GL_RGBA, width, height, BORDER, GL_RGBA,
                GL_UNSIGNED_BYTE, imageBytes);

        ImageData imageData = new ImageData(textureId, width, height);

        if (supportsEventCapturing) {
            imageData.CapturingMask = loadCapturingMask(imageBytes, width, height, alphaThreshold);
        }

        stbi_image_free(imageBytes);

        return imageData;
    }

    private static long[][] loadCapturingMask(ByteBuffer imageBytes, int width, int height,
                                              float alphaThreshold) {
        long[][] capturingMask = new long[(width / BITS_PER_LONG) + 1][height];
        StringBuilder binaryBase;
        int captureMaskXIndex;
        for (int y = 0; y < height; y++) {
            binaryBase = newBinaryBase();
            captureMaskXIndex = 0;
            for (int x = 0; x < width; x++) {
                if (x % BITS_PER_LONG == 0 && x > 0) {
                    capturingMask[captureMaskXIndex][y] =
                            convertStringToLong(binaryBase.toString());
                    binaryBase = newBinaryBase();
                    captureMaskXIndex++;
                }

                int index = (DESIRED_CHANNELS * (x + (height * y))) + 3;
                byte value = imageBytes.get(index);
                int valueEquivalent = value & 0xFF;
                float alpha = (float) valueEquivalent / MAX_ALPHA_VALUE;
                if (alpha >= alphaThreshold) {
                    binaryBase.setCharAt(x % BITS_PER_LONG, '1');
                }
            }

            capturingMask[captureMaskXIndex][y] =
                    convertStringToLong(binaryBase.toString());
        }

        return capturingMask;
    }

    @Override
    public String getInterfaceName() {
        return ImageFactory.class.getCanonicalName();
    }

    private static class ImageData {
        int TextureId;
        int Width;
        int Height;
        long[][] CapturingMask;

        private ImageData(int textureId, int width, int height) {
            TextureId = textureId;
            Width = width;
            Height = height;
        }
    }
}
