package inaugural.soliloquy.graphics.assets;

import inaugural.soliloquy.tools.Check;
import org.lwjgl.BufferUtils;
import soliloquy.specs.graphics.assets.Image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class ImageImpl implements Image {
    private final int TEXTURE_ID;
    private final String RELATIVE_LOCATION;
    private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;
    private final Float ALPHA_THRESHOLD;

    private int _width;
    private int _height;

    private final static int DESIRED_CHANNELS = 4;
    private final static int LEVEL_OF_DETAIL = 0;
    private final static int BORDER = 0;
    private final static int BITS_PER_LONG = 64;
    private final static int MAX_ALPHA_VALUE = 256;
    private final static String UNSIGNED_ZERO_LONG_REPRESENTATION =
            "0000000000000000000000000000000000000000000000000000000000000000";

    // NB: _capturingMask is a 2d array of longs, where each long represents 64 pixels of the
    // Image, starting at ([0-63],0), and moving horizontally and vertically according to the array
    // dimensions. Each bit in each long represents whether the corresponding pixel in that pixel
    // range. So, for a long representing ([0-63],0), (0,0) would be represented by the 2^63 bit,
    // (1,0) would be represented by the 2^62 bit, (2,0) would be represented by the 2^61 bit, and
    // so-on. For each bit, a value of 1 implies that the corresponding pixel passes the alpha
    // threshold and does capture mouse events. (Longs are chosen here, since it will use the whole
    // word with 64-bit processors, whereas smaller-bit processors will need the same number of
    // words to store the same capturing mask, regardless of the basic data type used.)
    private long[][] _capturingMask;

    private static StringBuilder newBinaryBase() {
        return new StringBuilder(UNSIGNED_ZERO_LONG_REPRESENTATION);
    }

    private static Long convertStringToLong(String binaryRepresentation) {
        return Long.parseUnsignedLong(binaryRepresentation, 2);
    }

    private static String convertLongToString(long value) {
        return Long.toBinaryString(value);
    }

    public ImageImpl(String relativeLocation) {
        RELATIVE_LOCATION = relativeLocation;
        SUPPORTS_MOUSE_EVENT_CAPTURING = false;
        ALPHA_THRESHOLD = null;

        TEXTURE_ID = loadTexture();
    }

    public ImageImpl(String relativeLocation,
                     float alphaThreshold) {
        RELATIVE_LOCATION = relativeLocation;
        SUPPORTS_MOUSE_EVENT_CAPTURING = true;
        ALPHA_THRESHOLD = alphaThreshold;

        TEXTURE_ID = loadTexture();
    }

    private int loadTexture() {
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        ByteBuffer imageData = stbi_load(RELATIVE_LOCATION, widthBuffer, heightBuffer,
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

        if (SUPPORTS_MOUSE_EVENT_CAPTURING) {
            loadCapturingMask(imageData);
        }

        stbi_image_free(imageData);

        return textureId;
    }

    private void loadCapturingMask(ByteBuffer imageData) {
        _capturingMask = new long[(_width / BITS_PER_LONG) + 1][_height];
        StringBuilder binaryBase;
        int captureMaskXIndex;
        for (int y = 0; y < _height; y++) {
            binaryBase = newBinaryBase();
            captureMaskXIndex = 0;
            for (int x = 0; x < _width; x++) {
                if (x % BITS_PER_LONG == 0 && x > 0) {
                    _capturingMask[captureMaskXIndex][y] =
                            convertStringToLong(binaryBase.toString());
                    binaryBase = newBinaryBase();
                    captureMaskXIndex++;
                }

                int index = (DESIRED_CHANNELS * (x + (_height * y))) + 3;
                byte value = imageData.get(index);
                int valueEquivalent = value & 0xFF;
                float alpha = (float) valueEquivalent / MAX_ALPHA_VALUE;
                if (alpha >= ALPHA_THRESHOLD) {
                    binaryBase.setCharAt(x % BITS_PER_LONG, '1');
                }
            }

            _capturingMask[captureMaskXIndex][y] =
                    convertStringToLong(binaryBase.toString());
        }
    }

    @Override
    public int textureId() {
        return TEXTURE_ID;
    }

    @Override
    public String relativeLocation() {
        return RELATIVE_LOCATION;
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
        return SUPPORTS_MOUSE_EVENT_CAPTURING;
    }

    @Override
    public boolean capturesMouseEventsAtPixel(int x, int y)
            throws UnsupportedOperationException, IllegalArgumentException {
        if (!SUPPORTS_MOUSE_EVENT_CAPTURING) {
            throw new UnsupportedOperationException("ImageImpl.capturesMouseEventsAtPixel: " +
                    "image does not support mouse event capturing");
        }
        Check.ifNonNegative(x, "x");
        Check.ifNonNegative(y, "y");
        Check.throwOnSecondLte(x, _width, "x", "_width");
        Check.throwOnSecondLte(y, _height, "y", "_height");

        long pixelsRange = _capturingMask[x / BITS_PER_LONG][y];
        String binary = convertLongToString(pixelsRange);
        int index = x % BITS_PER_LONG;
        char bitAtPixel = binary.charAt(index);

        return bitAtPixel == '1';
    }

    @Override
    public String getInterfaceName() {
        return Image.class.getCanonicalName();
    }
}
