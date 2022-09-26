package inaugural.soliloquy.graphics.assets;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;

public class ImageImpl extends ReadsCapturingMasks implements Image {
    private final int TEXTURE_ID;
    private final String RELATIVE_LOCATION;
    private final int WIDTH;
    private final int HEIGHT;
    private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;

    // NB: _capturingMask is a 2d array of longs, where each long represents 64 pixels of the
    // Image, starting at ([0-63],0), and moving horizontally and vertically according to the array
    // dimensions. Each bit in each long represents whether the corresponding pixel in that pixel
    // range. So, for a long representing ([0-63],0), (0,0) would be represented by the 2^63 bit,
    // (1,0) would be represented by the 2^62 bit, (2,0) would be represented by the 2^61 bit, and
    // so-on. For each bit, a value of 1 implies that the corresponding pixel passes the alpha
    // threshold and does capture mouse events. (Longs are chosen here, since it will use the whole
    // word with 64-bit processors, whereas smaller-bit processors will need the same number of
    // words to store the same capturing mask, regardless of the basic data type used.)
    private final long[][] CAPTURING_MASK;

    public ImageImpl(int textureId, String relativeLocation, int width, int height) {
        TEXTURE_ID = Check.throwOnLteZero(textureId, "textureId");
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = false;
        WIDTH = Check.throwOnLteZero(width, "width");
        HEIGHT = Check.throwOnLteZero(height, "height");
        CAPTURING_MASK = null;
    }

    public ImageImpl(int textureId, String relativeLocation, int width, int height,
                     long[][] capturingMask) {
        TEXTURE_ID = Check.throwOnLteZero(textureId, "textureId");
        RELATIVE_LOCATION = Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        SUPPORTS_MOUSE_EVENT_CAPTURING = true;
        WIDTH = Check.throwOnLteZero(width, "width");
        HEIGHT = Check.throwOnLteZero(height, "height");
        Check.ifNull(capturingMask, "capturingMask");
        if (capturingMask.length == 0) {
            throw new IllegalArgumentException(
                    "ImageImpl: capturingMask first dimension cannot be 0");
        }
        if (capturingMask[0].length == 0) {
            throw new IllegalArgumentException(
                    "ImageImpl: capturingMask second dimension cannot be 0");
        }
        CAPTURING_MASK = capturingMask;
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
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
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
        Check.throwOnSecondLte(x, WIDTH, "x", "_width");
        Check.throwOnSecondLte(y, HEIGHT, "y", "_height");

        @SuppressWarnings("ConstantConditions")
        long pixelsRange = CAPTURING_MASK[x / BITS_PER_LONG][y];
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
