package inaugural.soliloquy.graphics.assets;

public abstract class ReadsCapturingMasks {
    private final static String UNSIGNED_ZERO_LONG_REPRESENTATION =
            "0000000000000000000000000000000000000000000000000000000000000000";

    protected final static int BITS_PER_LONG = 64;

    protected static StringBuilder newBinaryBase() {
        return new StringBuilder(UNSIGNED_ZERO_LONG_REPRESENTATION);
    }

    protected static Long convertStringToLong(String binaryRepresentation) {
        return Long.parseUnsignedLong(binaryRepresentation, 2);
    }

    protected static String convertLongToString(long value) {
        return Long.toBinaryString(value);
    }
}
