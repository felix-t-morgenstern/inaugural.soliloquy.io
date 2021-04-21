package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.factories.CoordinateFactory;
import soliloquy.specs.common.valueobjects.Coordinate;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.*;

public class FakeFontLoadable implements Font {
    private final static int ASCII_CHAR_SPACE = 32;
    private final static int ASCII_CHAR_DELETE = 127;
    private final static int NUMBER_EXTENDED_ASCII_CHARS = 256;
    private final static int RGBA_BYTES = 4;
    private final static int RED_OFFSET = 16;
    private final static int GREEN_OFFSET = 8;
    private final static int BLUE_OFFSET = 0;
    private final static int ALPHA_OFFSET = 24;

    private final String RELATIVE_LOCATION;
    private final float MAX_LOSSLESS_FONT_SIZE;
    private final float ADDITIONAL_GLYPH_HORIZONTAL_PADDING;
    private final Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING;
    private final float ADDITIONAL_GLYPH_VERTICAL_PADDING;
    private final float LEADING_ADJUSTMENT;
    private final FloatBoxFactory FLOAT_BOX_FACTORY;
    private final CoordinateFactory COORDINATE_FACTORY;

    private int _textureId;
    private Coordinate _textureDimensions;
    private float _textureWidthToHeightRatio;
    private int _textureIdItalic;
    private Coordinate _textureDimensionsItalic;
    private float _textureWidthToHeightRatioItalic;
    private int _textureIdBold;
    private Coordinate _textureDimensionsBold;
    private float _textureWidthToHeightRatioBold;
    private int _textureIdBoldItalic;
    private Coordinate _textureDimensionsBoldItalic;
    private float _textureWidthToHeightRatioBoldItalic;
    private Map<Character, FloatBox> _glyphs;
    private Map<Character, FloatBox> _glyphsItalic;
    private Map<Character, FloatBox> _glyphsBold;
    private Map<Character, FloatBox> _glyphsBoldItalic;

    private static int _maximumTextureDimensionSize = -1;

    public FakeFontLoadable(String relativeLocation, float maxLosslessFontSize,
                            float additionalGlyphHorizontalPadding,
                            Map<Character, Float> glyphwiseAdditionalHorizontalPadding,
                            float additionalGlyphVerticalPadding,
                            float leadingAdjustment,
                            FloatBoxFactory floatBoxFactory,
                            CoordinateFactory coordinateFactory) {
        RELATIVE_LOCATION = relativeLocation;
        MAX_LOSSLESS_FONT_SIZE = maxLosslessFontSize;
        ADDITIONAL_GLYPH_HORIZONTAL_PADDING = additionalGlyphHorizontalPadding;
        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING = glyphwiseAdditionalHorizontalPadding;
        ADDITIONAL_GLYPH_VERTICAL_PADDING = additionalGlyphVerticalPadding;
        LEADING_ADJUSTMENT = leadingAdjustment;
        FLOAT_BOX_FACTORY = floatBoxFactory;
        COORDINATE_FACTORY = coordinateFactory;
    }

    public void load() {
        if (_maximumTextureDimensionSize < 0) {
            _maximumTextureDimensionSize = glGetInteger(GL_MAX_TEXTURE_SIZE);
        }

        _glyphs = new HashMap<>();
        _glyphsItalic = new HashMap<>();
        _glyphsBold = new HashMap<>();
        _glyphsBoldItalic = new HashMap<>();


        java.awt.Font fontFromFile = loadFontFromFile(RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE);

        java.awt.Font fontFromFileItalic = fontFromFile.deriveFont(java.awt.Font.ITALIC);

        java.awt.Font fontFromFileBold = fontFromFile.deriveFont(java.awt.Font.BOLD);

        java.awt.Font fontFromFileBoldItalic = fontFromFile.deriveFont(
                java.awt.Font.ITALIC | java.awt.Font.BOLD);


        FontImageInfo textureInfo = generateFontAsset(fontFromFile,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT, _glyphs, FLOAT_BOX_FACTORY,
                COORDINATE_FACTORY);
        _textureId = textureInfo.TextureId;
        _textureDimensions = textureInfo.ImageDimensions;
        _textureWidthToHeightRatio =
                textureInfo.ImageDimensions.getX() / (float)textureInfo.ImageDimensions.getY();

        FontImageInfo textureInfoItalic = generateFontAsset(fontFromFileItalic,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT, _glyphsItalic,
                FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
        _textureIdItalic = textureInfoItalic.TextureId;
        _textureDimensionsItalic = textureInfoItalic.ImageDimensions;
        _textureWidthToHeightRatioItalic =  textureInfoItalic.ImageDimensions.getX() /
                (float)textureInfoItalic.ImageDimensions.getY();

        FontImageInfo textureInfoBold = generateFontAsset(fontFromFileBold,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT, _glyphsBold,
                FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
        _textureIdBold = textureInfoBold.TextureId;
        _textureDimensionsBold = textureInfoBold.ImageDimensions;
        _textureWidthToHeightRatioBold = textureInfoBold.ImageDimensions.getX() /
                (float)textureInfoBold.ImageDimensions.getY();

        FontImageInfo textureInfoBoldItalic = generateFontAsset(fontFromFileBoldItalic,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT, _glyphsBoldItalic,
                FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
        _textureIdBoldItalic = textureInfoBoldItalic.TextureId;
        _textureDimensionsBoldItalic = textureInfoBoldItalic.ImageDimensions;
        _textureWidthToHeightRatioBoldItalic = textureInfoBoldItalic.ImageDimensions.getX() /
                (float)textureInfoBoldItalic.ImageDimensions.getY();
    }

    private static java.awt.Font loadFontFromFile(String relativeLocation,
                                                  float maxLosslessFontSize) {
        try {
            return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                    new File(relativeLocation)).deriveFont(maxLosslessFontSize);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static FontImageInfo generateFontAsset(java.awt.Font font,
                                                   float additionalGlyphHorizontalPadding,
                                                   Map<Character, Float>
                                                           glyphwiseAdditionalHorizontalPadding,
                                                   float additionalGlyphVerticalPadding,
                                                   float leadingAdjustment,
                                                   Map<Character, FloatBox> glyphs,
                                                   FloatBoxFactory floatBoxFactory,
                                                   CoordinateFactory coordinateFactory) {
        GraphicsConfiguration graphicsConfiguration =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                        .getDefaultConfiguration();

        Graphics2D graphics2d = graphicsConfiguration.createCompatibleImage(1, 1,
                Transparency.TRANSLUCENT).createGraphics();

        graphics2d.setFont(font);

        FontMetrics fontMetrics = graphics2d.getFontMetrics();

        FontImageInfo fontImageInfo = loopOverCharacters(fontMetrics,
                additionalGlyphHorizontalPadding, glyphwiseAdditionalHorizontalPadding,
                additionalGlyphVerticalPadding, leadingAdjustment, null, coordinateFactory);

        BufferedImage bufferedImage = graphics2d.getDeviceConfiguration()
                .createCompatibleImage(fontImageInfo.ImageDimensions.getX(),
                        fontImageInfo.ImageDimensions.getY(),
                        Transparency.TRANSLUCENT);

        int textureId = glGenTextures();

        ByteBuffer generatedImage = generateImage(bufferedImage, font, fontMetrics,
                fontImageInfo.ImageDimensions.getX(), fontImageInfo.ImageDimensions.getY(),
                additionalGlyphHorizontalPadding, glyphwiseAdditionalHorizontalPadding,
                additionalGlyphVerticalPadding, leadingAdjustment, glyphs, floatBoxFactory,
                coordinateFactory);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, fontImageInfo.ImageDimensions.getX(),
                fontImageInfo.ImageDimensions.getY(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                generatedImage);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        fontImageInfo.TextureId = textureId;

        return fontImageInfo;
    }

    private static ByteBuffer generateImage(BufferedImage bufferedImage, java.awt.Font font,
                                            FontMetrics fontMetrics,
                                            int imageWidth, int imageHeight,
                                            float additionalGlyphHorizontalPadding,
                                            Map<Character, Float>
                                                    glyphwiseAdditionalHorizontalPadding,
                                            float additionalGlyphVerticalPadding,
                                            float leadingAdjustment,
                                            Map<Character, FloatBox> glyphs,
                                            FloatBoxFactory floatBoxFactory,
                                            CoordinateFactory coordinateFactory) {
        Graphics2D graphics2d = (Graphics2D)bufferedImage.getGraphics();
        graphics2d.setFont(font);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCharacters(graphics2d, fontMetrics, imageWidth, imageHeight,
                additionalGlyphHorizontalPadding, glyphwiseAdditionalHorizontalPadding,
                additionalGlyphVerticalPadding, leadingAdjustment, glyphs, floatBoxFactory,
                coordinateFactory);

        return createBuffer(bufferedImage, imageWidth, imageHeight);
    }

    private static void drawCharacters(Graphics2D graphics2d, FontMetrics fontMetrics,
                                       int imageWidth, int imageHeight,
                                       float additionalGlyphHorizontalPadding,
                                       Map<Character, Float> glyphwiseAdditionalHorizontalPadding,
                                       float additionalGlyphVerticalPadding,
                                       float leadingAdjustment,
                                       Map<Character, FloatBox> glyphs,
                                       FloatBoxFactory floatBoxFactory,
                                       CoordinateFactory coordinateFactory) {
        loopOverCharacters(fontMetrics, additionalGlyphHorizontalPadding,
                glyphwiseAdditionalHorizontalPadding, additionalGlyphVerticalPadding,
                leadingAdjustment,
                glyph -> widthThusFar -> rowNumber -> glyphWidth -> glyphHeight -> descent -> {
                    // NB: Consider eliminating all of these redundant casts
                    float leftX = (widthThusFar / (float)imageWidth);
                    float topY = ((glyphHeight * (1f + additionalGlyphVerticalPadding)) * rowNumber) / (float)imageHeight; // NB: Same as part of glyphDrawTopY
                    float rightX = (glyphWidth / (float)imageWidth) + leftX;
                    float bottomY = topY + (glyphHeight / (float)imageHeight);
                    glyphs.put(glyph, floatBoxFactory.make(leftX, topY, rightX, bottomY));

                    float glyphDrawTopY = ((glyphHeight * (1f + additionalGlyphVerticalPadding)) * (rowNumber + 1)) - descent;
                    graphics2d.drawString(String.valueOf(glyph), widthThusFar,
                            glyphDrawTopY);
                }, coordinateFactory);
    }

    private static FakeFontLoadable.FontImageInfo loopOverCharacters(
            FontMetrics fontMetrics, float additionalGlyphHorizontalPadding,
            Map<Character, Float> glyphwiseAdditionalHorizontalPadding,
            float additionalGlyphVerticalPadding, float leadingAdjustment,
            Function<Character, Function<Integer, Function<Integer, Function<Float, Function<Float,
                    Consumer<Float>>>>>> glyphFunction,
            CoordinateFactory coordinateFactory) {
        int widthThusFar = 0;
        int rowNumber = 0;
        float leading = fontMetrics.getLeading() + (leadingAdjustment * fontMetrics.getHeight());
        float glyphHeight = fontMetrics.getHeight() - leading;
        float descent = fontMetrics.getMaxDescent();

        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }

            char glyph = (char)i;

            float glyphWidth = fontMetrics.charWidth(glyph);
            if (glyphwiseAdditionalHorizontalPadding != null &&
                    glyphwiseAdditionalHorizontalPadding.containsKey(glyph)) {
                glyphWidth *= (1f + glyphwiseAdditionalHorizontalPadding.get(glyph));
            }

            float glyphWidthWithPadding = glyphWidth * (1f + additionalGlyphHorizontalPadding);

            if (widthThusFar + glyphWidthWithPadding > _maximumTextureDimensionSize) {
                widthThusFar = 0;
                rowNumber++;
            }

            if (glyphFunction != null) {
                glyphFunction.apply(glyph).apply(widthThusFar).apply(rowNumber).apply(glyphWidth)
                        .apply(glyphHeight).accept(descent);
            }

            widthThusFar += glyphWidthWithPadding;
        }
        // NB: The 0.5f factor is to ensure that the casting rounds up, so no glyph pixels are lost
        int imageHeight = (int)
                ((glyphHeight * (1f + additionalGlyphVerticalPadding) * (rowNumber + 1)) + 0.5f);



        return new FakeFontLoadable.FontImageInfo(coordinateFactory.make(
                rowNumber > 0 ? _maximumTextureDimensionSize : widthThusFar,
                imageHeight));
    }

    private static class FontImageInfo {
        Coordinate ImageDimensions;
        int TextureId;

        private FontImageInfo(Coordinate imageDimensions) {
            ImageDimensions = imageDimensions;
        }
    }

    private static ByteBuffer createBuffer(BufferedImage bufferedImage, int imageWidth,
                                           int imageHeight) {
        int[] pixels = new int[imageWidth * imageHeight];

        bufferedImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
        ByteBuffer byteBuffer =
                ByteBuffer.allocateDirect(imageWidth * imageHeight * RGBA_BYTES);

        for (int pixel : pixels) {
            byteBuffer.put((byte) ((pixel >> RED_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> GREEN_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> BLUE_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> ALPHA_OFFSET) & 0xFF));
        }

        byteBuffer.flip();

        return byteBuffer;
    }

    @Override
    public String id() throws IllegalStateException {
        return null;
    }

    @Override
    public FloatBox getUvCoordinatesForGlyph(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return _glyphs.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphItalic(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return _glyphsItalic.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBold(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return _glyphsBold.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBoldItalic(char glyph)
            throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return _glyphsBoldItalic.get(glyph);
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalPadding() {
        return GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING;
    }

    @Override
    public Coordinate textureDimensions() {
        return _textureDimensions;
    }

    @Override
    public float textureWidthToHeightRatio() {
        return _textureWidthToHeightRatio;
    }

    @Override
    public int textureIdItalic() {
        return _textureIdItalic;
    }

    @Override
    public Coordinate textureDimensionsItalic() {
        return _textureDimensionsItalic;
    }

    @Override
    public float textureWidthToHeightRatioItalic() {
        return _textureWidthToHeightRatioItalic;
    }

    @Override
    public int textureIdBold() {
        return _textureIdBold;
    }

    @Override
    public Coordinate textureDimensionsBold() {
        return _textureDimensionsBold;
    }

    @Override
    public float textureWidthToHeightRatioBold() {
        return _textureWidthToHeightRatioBold;
    }

    @Override
    public int textureIdBoldItalic() {
        return _textureIdBoldItalic;
    }

    @Override
    public Coordinate textureDimensionsBoldItalic() {
        return _textureDimensionsBoldItalic;
    }

    @Override
    public float textureWidthToHeightRatioBoldItalic() {
        return _textureWidthToHeightRatioBoldItalic;
    }

    @Override
    public int textureId() {
        return _textureId;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
