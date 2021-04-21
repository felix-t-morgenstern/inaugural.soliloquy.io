package inaugural.soliloquy.graphics.assets;

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

public class FontImpl implements Font {
    private final static int ASCII_CHAR_SPACE = 32;
    private final static int ASCII_CHAR_DELETE = 127;
    private final static int NUMBER_EXTENDED_ASCII_CHARS = 256;
    private final static int RGBA_BYTES = 4;
    private final static int RED_OFFSET = 16;
    private final static int GREEN_OFFSET = 8;
    private final static int BLUE_OFFSET = 0;
    private final static int ALPHA_OFFSET = 24;

    // NB: This field is static, since the GPU's maximum texture dimension size is unlikely to
    //     change from one font to the next
    @SuppressWarnings("FieldCanBeLocal")
    private static int MAXIMUM_TEXTURE_DIMENSION_SIZE = -1;

    private final String ID;
    private final Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING;
    private final int TEXTURE_ID;
    private final int TEXTURE_ID_ITALIC;
    private final int TEXTURE_ID_BOLD;
    private final int TEXTURE_ID_BOLD_ITALIC;
    private final Coordinate TEXTURE_DIMENSIONS;
    private final Coordinate TEXTURE_DIMENSIONS_ITALIC;
    private final Coordinate TEXTURE_DIMENSIONS_BOLD;
    private final Coordinate TEXTURE_DIMENSIONS_BOLD_ITALIC;
    private final float TEXTURE_WIDTH_TO_HEIGHT_RATIO;
    private final float TEXTURE_WIDTH_TO_HEIGHT_RATIO_ITALIC;
    private final float TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD;
    private final float TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD_ITALIC;
    private final Map<Character, FloatBox> GLYPHS;
    private final Map<Character, FloatBox> GLYPHS_ITALIC;
    private final Map<Character, FloatBox> GLYPHS_BOLD;
    private final Map<Character, FloatBox> GLYPHS_BOLD_ITALIC;

    public FontImpl(String id, String relativeLocation, float maxLosslessFontSize,
                    float additionalGlyphHorizontalPadding,
                    Map<Character, Float> glyphwiseAdditionalHorizontalPadding,
                    float additionalGlyphVerticalPadding,
                    float leadingAdjustment,
                    FloatBoxFactory floatBoxFactory,
                    CoordinateFactory coordinateFactory) {
        Check.ifNullOrEmpty(id, "id");
        Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        Check.throwOnLteZero(maxLosslessFontSize, "maxLosslessFontSize");
        Check.throwOnLtValue(additionalGlyphHorizontalPadding, 0f,
                "additionalGlyphHorizontalPadding");
        Check.throwOnLtValue(additionalGlyphVerticalPadding, 0f, "additionalGlyphVerticalPadding");
        Check.throwOnLtValue(leadingAdjustment, 0f, "leadingAdjustment");
        Check.throwOnGteValue(leadingAdjustment, 1f, "leadingAdjustment");
        Check.ifNull(floatBoxFactory, "floatBoxFactory");
        Check.ifNull(coordinateFactory, "coordinateFactory");

        if (MAXIMUM_TEXTURE_DIMENSION_SIZE < 0) {
            MAXIMUM_TEXTURE_DIMENSION_SIZE = glGetInteger(GL_MAX_TEXTURE_SIZE);
        }

        ID = id;
        GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING = glyphwiseAdditionalHorizontalPadding;
        GLYPHS = new HashMap<>();
        GLYPHS_ITALIC = new HashMap<>();
        GLYPHS_BOLD = new HashMap<>();
        GLYPHS_BOLD_ITALIC = new HashMap<>();



        java.awt.Font fontFromFile = loadFontFromFile(relativeLocation, maxLosslessFontSize);
        java.awt.Font fontFromFileItalic = fontFromFile.deriveFont(java.awt.Font.ITALIC);
        java.awt.Font fontFromFileBold = fontFromFile.deriveFont(java.awt.Font.BOLD);
        java.awt.Font fontFromFileBoldItalic = fontFromFile.deriveFont(
                java.awt.Font.ITALIC | java.awt.Font.BOLD);



        FontImageInfo textureInfo = generateFontAsset(fontFromFile,
                additionalGlyphHorizontalPadding, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                additionalGlyphVerticalPadding, leadingAdjustment, GLYPHS, floatBoxFactory,
                coordinateFactory);
        TEXTURE_ID = textureInfo.TextureId;
        TEXTURE_DIMENSIONS = textureInfo.ImageDimensions;
        TEXTURE_WIDTH_TO_HEIGHT_RATIO = textureInfo.ImageDimensions.getX() /
                (float)textureInfo.ImageDimensions.getY();

        FontImageInfo textureInfoItalic = generateFontAsset(fontFromFileItalic,
                additionalGlyphHorizontalPadding, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                additionalGlyphVerticalPadding, leadingAdjustment, GLYPHS_ITALIC, floatBoxFactory,
                coordinateFactory);
        TEXTURE_ID_ITALIC = textureInfoItalic.TextureId;
        TEXTURE_DIMENSIONS_ITALIC = textureInfoItalic.ImageDimensions;
        TEXTURE_WIDTH_TO_HEIGHT_RATIO_ITALIC = textureInfoItalic.ImageDimensions.getX() /
                (float)textureInfoItalic.ImageDimensions.getY();

        FontImageInfo textureInfoBold = generateFontAsset(fontFromFileBold,
                additionalGlyphHorizontalPadding, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                additionalGlyphVerticalPadding, leadingAdjustment, GLYPHS_BOLD, floatBoxFactory,
                coordinateFactory);
        TEXTURE_ID_BOLD = textureInfoBold.TextureId;
        TEXTURE_DIMENSIONS_BOLD = textureInfoBold.ImageDimensions;
        TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD = textureInfoBold.ImageDimensions.getX() /
                (float)textureInfoBold.ImageDimensions.getY();

        FontImageInfo textureInfoBoldItalic = generateFontAsset(fontFromFileBoldItalic,
                additionalGlyphHorizontalPadding, GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING,
                additionalGlyphVerticalPadding, leadingAdjustment, GLYPHS_BOLD_ITALIC,
                floatBoxFactory, coordinateFactory);
        TEXTURE_ID_BOLD_ITALIC = textureInfoBoldItalic.TextureId;
        TEXTURE_DIMENSIONS_BOLD_ITALIC = textureInfoBoldItalic.ImageDimensions;
        TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD_ITALIC = textureInfoBoldItalic.ImageDimensions.getX() /
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

    private static FontImpl.FontImageInfo generateFontAsset(java.awt.Font font,
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

        FontImpl.FontImageInfo fontImageInfo = loopOverCharacters(fontMetrics,
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

    private static FontImpl.FontImageInfo loopOverCharacters(
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

            if (widthThusFar + glyphWidthWithPadding > MAXIMUM_TEXTURE_DIMENSION_SIZE) {
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



        return new FontImpl.FontImageInfo(coordinateFactory.make(
                rowNumber > 0 ? MAXIMUM_TEXTURE_DIMENSION_SIZE : widthThusFar,
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
        return ID;
    }

    @Override
    public String getInterfaceName() {
        return Font.class.getCanonicalName();
    }

    @Override
    public FloatBox getUvCoordinatesForGlyph(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return GLYPHS.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphItalic(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return GLYPHS_ITALIC.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBold(char glyph) throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return GLYPHS_BOLD.get(glyph);
    }

    @Override
    public FloatBox getUvCoordinatesForGlyphBoldItalic(char glyph)
            throws IllegalArgumentException {
        Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
        Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
        Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
        return GLYPHS_BOLD_ITALIC.get(glyph);
    }

    @Override
    public Map<Character, Float> glyphwiseAdditionalHorizontalPadding() {
        return GLYPHWISE_ADDITIONAL_HORIZONTAL_PADDING;
    }

    @Override
    public Coordinate textureDimensions() {
        return TEXTURE_DIMENSIONS;
    }

    @Override
    public float textureWidthToHeightRatio() {
        return TEXTURE_WIDTH_TO_HEIGHT_RATIO;
    }

    @Override
    public int textureIdItalic() {
        return TEXTURE_ID_ITALIC;
    }

    @Override
    public Coordinate textureDimensionsItalic() {
        return TEXTURE_DIMENSIONS_ITALIC;
    }

    @Override
    public float textureWidthToHeightRatioItalic() {
        return TEXTURE_WIDTH_TO_HEIGHT_RATIO_ITALIC;
    }

    @Override
    public int textureIdBold() {
        return TEXTURE_ID_BOLD;
    }

    @Override
    public Coordinate textureDimensionsBold() {
        return TEXTURE_DIMENSIONS_BOLD;
    }

    @Override
    public float textureWidthToHeightRatioBold() {
        return TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD;
    }

    @Override
    public int textureIdBoldItalic() {
        return TEXTURE_ID_BOLD_ITALIC;
    }

    @Override
    public Coordinate textureDimensionsBoldItalic() {
        return TEXTURE_DIMENSIONS_BOLD_ITALIC;
    }

    @Override
    public float textureWidthToHeightRatioBoldItalic() {
        return TEXTURE_WIDTH_TO_HEIGHT_RATIO_BOLD_ITALIC;
    }

    @Override
    public int textureId() {
        return TEXTURE_ID;
    }
}
