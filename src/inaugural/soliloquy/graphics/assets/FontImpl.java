package inaugural.soliloquy.graphics.assets;

import inaugural.soliloquy.tools.Check;
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

    private final String ID;
    private final int TEXTURE_ID;
    private final int TEXTURE_ID_ITALIC;
    private final int TEXTURE_ID_BOLD;
    private final int TEXTURE_ID_BOLD_ITALIC;
    private final Map<Character, FloatBox> GLYPHS;
    private final Map<Character, FloatBox> GLYPHS_ITALIC;
    private final Map<Character, FloatBox> GLYPHS_BOLD;
    private final Map<Character, FloatBox> GLYPHS_BOLD_ITALIC;

    public FontImpl(String id, String relativeLocation, float maxLosslessFontSize,
                    float additionalGlyphHorizontalPadding, float additionalGlyphVerticalPadding,
                    float leadingAdjustment,
                    int imageWidth, int imageHeight,
                    FloatBoxFactory floatBoxFactory) {
        Check.ifNullOrEmpty(id, "id");
        Check.ifNullOrEmpty(relativeLocation, "relativeLocation");
        Check.throwOnLteZero(maxLosslessFontSize, "maxLosslessFontSize");
        Check.throwOnLtValue(additionalGlyphHorizontalPadding, 0f,
                "additionalGlyphHorizontalPadding");
        Check.throwOnLtValue(additionalGlyphVerticalPadding, 0f, "additionalGlyphVerticalPadding");
        Check.throwOnLtValue(leadingAdjustment, 0f, "leadingAdjustment");
        Check.throwOnGteValue(leadingAdjustment, 1f, "leadingAdjustment");
        Check.throwOnLteZero(imageWidth, "imageWidth");
        Check.throwOnLteZero(imageHeight, "imageHeight");
        Check.ifNull(floatBoxFactory, "floatBoxFactory");

        ID = id;
        GLYPHS = new HashMap<>();
        GLYPHS_ITALIC = new HashMap<>();
        GLYPHS_BOLD = new HashMap<>();
        GLYPHS_BOLD_ITALIC = new HashMap<>();

        java.awt.Font fontFromFile = loadFontFromFile(relativeLocation, maxLosslessFontSize);
        java.awt.Font fontFromFileItalic = fontFromFile.deriveFont(java.awt.Font.ITALIC);
        java.awt.Font fontFromFileBold = fontFromFile.deriveFont(java.awt.Font.BOLD);
        java.awt.Font fontFromFileBoldItalic = fontFromFile.deriveFont(
                java.awt.Font.ITALIC | java.awt.Font.BOLD);

        TEXTURE_ID = generateFontAsset(fontFromFile, imageWidth, imageHeight,
                additionalGlyphHorizontalPadding, leadingAdjustment, GLYPHS, floatBoxFactory);
        TEXTURE_ID_ITALIC = generateFontAsset(fontFromFileItalic, imageWidth, imageHeight,
                additionalGlyphHorizontalPadding, leadingAdjustment, GLYPHS_ITALIC, floatBoxFactory);
        TEXTURE_ID_BOLD = generateFontAsset(fontFromFileBold, imageWidth, imageHeight,
                additionalGlyphHorizontalPadding, leadingAdjustment, GLYPHS_BOLD, floatBoxFactory);
        TEXTURE_ID_BOLD_ITALIC = generateFontAsset(fontFromFileBoldItalic, imageWidth, imageHeight,
                additionalGlyphHorizontalPadding, leadingAdjustment, GLYPHS_BOLD_ITALIC, floatBoxFactory);
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

    private static int generateFontAsset(java.awt.Font font, int imageWidth, int imageHeight,
                                         float additionalGlyphPadding, float leadingAdjustment,
                                         Map<Character, FloatBox> glyphs,
                                         FloatBoxFactory floatBoxFactory) {
        GraphicsConfiguration graphicsConfiguration =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                        .getDefaultConfiguration();

        Graphics2D graphics2d = graphicsConfiguration.createCompatibleImage(1, 1,
                Transparency.TRANSLUCENT).createGraphics();

        graphics2d.setFont(font);

        FontMetrics fontMetrics = graphics2d.getFontMetrics();

        BufferedImage bufferedImage = graphics2d.getDeviceConfiguration()
                .createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);

        int textureId = glGenTextures();

        ByteBuffer generatedImage = generateImage(bufferedImage, font, fontMetrics, imageWidth,
                imageHeight, additionalGlyphPadding, leadingAdjustment, glyphs, floatBoxFactory);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imageWidth, imageHeight, 0, GL_RGBA,
                GL_UNSIGNED_BYTE, generatedImage);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        return textureId;
    }

    private static ByteBuffer generateImage(BufferedImage bufferedImage, java.awt.Font font,
                                            FontMetrics fontMetrics,
                                            int imageWidth, int imageHeight,
                                            float additionalGlyphPadding, float leadingAdjustment,
                                            Map<Character, FloatBox> glyphs,
                                            FloatBoxFactory floatBoxFactory) {
        Graphics2D graphics2d = (Graphics2D)bufferedImage.getGraphics();
        graphics2d.setFont(font);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCharacters(graphics2d, fontMetrics, imageWidth, imageHeight, additionalGlyphPadding,
                leadingAdjustment, glyphs, floatBoxFactory);

        return createBuffer(bufferedImage, imageWidth, imageHeight);
    }

    private static void drawCharacters(Graphics2D graphics2d, FontMetrics fontMetrics,
                                       int imageWidth, int imageHeight,
                                       float additionalGlyphPadding, float leadingAdjustment,
                                       Map<Character, FloatBox> glyphs,
                                       FloatBoxFactory floatBoxFactory) {
        int tempX = 0;
        int tempY = 0;
        float leading = fontMetrics.getLeading() + (leadingAdjustment * fontMetrics.getHeight());
        float glyphHeight = fontMetrics.getHeight() - leading;
        float descent = fontMetrics.getMaxDescent();

        for (int i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }

            char glyph = (char)i;

            float glyphWidth = fontMetrics.charWidth(glyph);

            float glyphWidthWithPadding = glyphWidth * (1f + additionalGlyphPadding);

            if (tempX + glyphWidthWithPadding > imageWidth) {
                tempX = 0;
                tempY++;
            }

            float leftX = tempX / (float) imageWidth;
            float topY = (glyphHeight * tempY) / imageHeight;
            float rightX = (glyphWidth / (float) imageWidth) + leftX;
            float bottomY = topY + (glyphHeight / imageHeight);
            glyphs.put(glyph, floatBoxFactory.make(leftX, topY, rightX, bottomY));

            float glyphDrawTopY = (glyphHeight * (tempY + 1)) - descent;
            graphics2d.drawString(String.valueOf(glyph), tempX,
                    glyphDrawTopY);

            tempX += glyphWidthWithPadding;
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
    public int textureIdItalic() {
        return TEXTURE_ID_ITALIC;
    }

    @Override
    public int textureIdBold() {
        return TEXTURE_ID_BOLD;
    }

    @Override
    public int textureIdBoldItalic() {
        return TEXTURE_ID_BOLD_ITALIC;
    }

    @Override
    public int textureId() {
        return TEXTURE_ID;
    }
}
