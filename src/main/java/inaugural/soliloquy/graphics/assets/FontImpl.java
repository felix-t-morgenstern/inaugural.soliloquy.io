package inaugural.soliloquy.graphics.assets;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Coordinate2d;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.opengl.GL11.*;
import static soliloquy.specs.common.valueobjects.Coordinate2d.coordinate2dOf;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

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
    private final FontStyleInfoImpl PLAIN;
    private final FontStyleInfoImpl ITALIC;
    private final FontStyleInfoImpl BOLD;
    private final FontStyleInfoImpl BOLD_ITALIC;

    public FontImpl(FontDefinition fontDefinition) {
        validateFontDefinition(fontDefinition);

        if (MAXIMUM_TEXTURE_DIMENSION_SIZE < 0) {
            MAXIMUM_TEXTURE_DIMENSION_SIZE = glGetInteger(GL_MAX_TEXTURE_SIZE);
        }

        ID = fontDefinition.id();



        var fontFromFile = loadFontFromFile(fontDefinition.relativeLocation(),
                fontDefinition.maxLosslessFontSize());
        var fontFromFileItalic = fontFromFile.deriveFont(java.awt.Font.ITALIC);
        var fontFromFileBold = fontFromFile.deriveFont(java.awt.Font.BOLD);
        var fontFromFileBoldItalic =
                fontFromFile.deriveFont(java.awt.Font.ITALIC | java.awt.Font.BOLD);



        PLAIN = loadFontStyle(fontFromFile, fontDefinition.plain(),
                fontDefinition.leadingAdjustment());

        ITALIC = loadFontStyle(fontFromFileItalic, fontDefinition.italic(),
                fontDefinition.leadingAdjustment());

        BOLD = loadFontStyle(fontFromFileBold, fontDefinition.bold(),
                fontDefinition.leadingAdjustment());

        BOLD_ITALIC = loadFontStyle(fontFromFileBoldItalic, fontDefinition.boldItalic(),
                fontDefinition.leadingAdjustment());
    }

    // NB: Extremely similar to FontPreloaderTask::validateFontDefinitionDTO; logic is
    // duplicated, since classes are different
    private void validateFontDefinition(FontDefinition fontDefinition) {
        Check.ifNull(fontDefinition, "fontDefinition");
        Check.ifNullOrEmpty(fontDefinition.id(), "fontDefinition.id()");
        Check.ifNullOrEmpty(fontDefinition.relativeLocation(), "fontDefinition.relativeLocation()");
        Check.throwOnLteZero(fontDefinition.maxLosslessFontSize(),
                "fontDefinition.maxLosslessFontSize()");
        Check.throwOnLtValue(fontDefinition.leadingAdjustment(), 0f,
                "fontDefinition.leadingAdjustment()");
        Check.throwOnGteValue(fontDefinition.leadingAdjustment(), 1f,
                "fontDefinition.leadingAdjustment()");
        Check.ifNull(fontDefinition.plain(), "fontDefinition.plain()");
        validateFontStyleDefinition(fontDefinition.plain(), fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.italic(), "fontDefinition.italic()");
        validateFontStyleDefinition(fontDefinition.italic(), fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.bold(), "fontDefinition.bold()");
        validateFontStyleDefinition(fontDefinition.bold(), fontDefinition.leadingAdjustment());
        Check.ifNull(fontDefinition.boldItalic(), "fontDefinition.boldItalic()");
        validateFontStyleDefinition(fontDefinition.boldItalic(),
                fontDefinition.leadingAdjustment());
    }

    private void validateFontStyleDefinition(FontStyleDefinition fontStyleDefinition,
                                             float leadingAdjustment) {
        Check.throwOnLtValue(fontStyleDefinition.additionalGlyphHorizontalTextureSpacing(), 0f,
                "fontStyleDefinition.additionalGlyphHorizontalTextureSpacing()");
        Check.throwOnLtValue(fontStyleDefinition.additionalGlyphVerticalTextureSpacing(), 0f,
                "fontStyleDefinition.additionalGlyphVerticalTextureSpacing()");
        Check.ifNull(fontStyleDefinition.glyphwiseAdditionalHorizontalTextureSpacing(),
                "fontStyleDefinition.glyphwiseAdditionalHorizontalTextureSpacing()");
        Check.ifNull(fontStyleDefinition.glyphwiseAdditionalLeftBoundaryShift(),
                "fontStyleDefinition.glyphwiseAdditionalLeftBoundaryShift()");
        Check.throwOnGteValue(
                leadingAdjustment + fontStyleDefinition.additionalGlyphVerticalTextureSpacing(), 1f,
                "sum of leadingAdjustment and " +
                        "fontStyleDefinition.additionalGlyphVerticalTextureSpacing()");
    }

    private FontStyleInfoImpl loadFontStyle(java.awt.Font fontFromFile,
                                            FontStyleDefinition fontStyleDefinition,
                                            float leadingAdjustment) {
        Map<Character, FloatBox> glyphs = mapOf();

        var textureInfo = generateFontAsset(fontFromFile,
                fontStyleDefinition.additionalGlyphHorizontalTextureSpacing(),
                fontStyleDefinition.glyphwiseAdditionalHorizontalTextureSpacing(),
                fontStyleDefinition.glyphwiseAdditionalLeftBoundaryShift(),
                fontStyleDefinition.additionalGlyphVerticalTextureSpacing(), leadingAdjustment,
                glyphs);

        return new FontStyleInfoImpl(glyphs, textureInfo.ImageDimensions,
                textureInfo.ImageDimensions.X / (float) textureInfo.ImageDimensions.Y,
                fontStyleDefinition.additionalGlyphHorizontalTextureSpacing(),
                fontStyleDefinition.glyphwiseAdditionalHorizontalTextureSpacing(),
                textureInfo.TextureId
        );
    }

    private static java.awt.Font loadFontFromFile(String relativeLocation,
                                                  float maxLosslessFontSize) {
        try {
            return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(relativeLocation))
                    .deriveFont(maxLosslessFontSize);
        }
        catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static FontImageInfo generateFontAsset(java.awt.Font font,
                                                   float additionalGlyphHorizontalTextureSpacing,
                                                   Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing,
                                                   Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift,
                                                   float additionalGlyphVerticalTextureSpacing,
                                                   float leadingAdjustment,
                                                   Map<Character, FloatBox> glyphs) {
        var graphicsConfiguration =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                        .getDefaultConfiguration();

        var graphics2d = graphicsConfiguration.createCompatibleImage(1, 1, Transparency.TRANSLUCENT)
                .createGraphics();

        graphics2d.setFont(font);

        var fontMetrics = graphics2d.getFontMetrics();

        var fontImageInfo = loopOverCharacters(fontMetrics, additionalGlyphHorizontalTextureSpacing,
                glyphwiseAdditionalHorizontalTextureSpacing, glyphwiseAdditionalLeftBoundaryShift,
                additionalGlyphVerticalTextureSpacing, leadingAdjustment, null);

        var bufferedImage = graphics2d.getDeviceConfiguration()
                .createCompatibleImage(fontImageInfo.ImageDimensions.X,
                        fontImageInfo.ImageDimensions.Y, Transparency.TRANSLUCENT);

        var textureId = glGenTextures();

        var generatedImage =
                generateImage(bufferedImage, font, fontMetrics, fontImageInfo.ImageDimensions.X,
                        fontImageInfo.ImageDimensions.Y, additionalGlyphHorizontalTextureSpacing,
                        glyphwiseAdditionalHorizontalTextureSpacing,
                        glyphwiseAdditionalLeftBoundaryShift, additionalGlyphVerticalTextureSpacing,
                        leadingAdjustment, fontImageInfo.GlyphHeight, fontImageInfo.GlyphDescent,
                        glyphs);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, fontImageInfo.ImageDimensions.X,
                fontImageInfo.ImageDimensions.Y, 0, GL_RGBA, GL_UNSIGNED_BYTE, generatedImage);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        fontImageInfo.TextureId = textureId;

        return fontImageInfo;
    }

    private static FontImageInfo loopOverCharacters(
            FontMetrics fontMetrics, float additionalGlyphHorizontalTextureSpacing,
            Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing,
            Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift,
            float additionalGlyphVerticalTextureSpacing, float leadingAdjustment,
            Function<Character, Function<Integer, Function<Integer, Function<Float,
                    Function<Float, Consumer<Float>>>>>> glyphFunction) {
        var widthThusFar = 0;
        var rowNumber = 0;
        var leading = fontMetrics.getLeading() + (leadingAdjustment * fontMetrics.getHeight());
        var glyphHeight = fontMetrics.getHeight() - leading;
        var glyphDescent = fontMetrics.getMaxDescent();

        float charLeftShift;
        var nextCharLeftShift = 0f;

        for (var i = ASCII_CHAR_SPACE; i < NUMBER_EXTENDED_ASCII_CHARS; i++) {
            if (i == ASCII_CHAR_DELETE) {
                continue;
            }

            var character = (char) i;
            var nextCharacter = (char) (i + 1);

            charLeftShift = nextCharLeftShift;
            if (glyphwiseAdditionalLeftBoundaryShift.containsKey(nextCharacter) &&
                    glyphwiseAdditionalLeftBoundaryShift.get(nextCharacter) != null) {
                nextCharLeftShift = glyphwiseAdditionalLeftBoundaryShift.get(nextCharacter);
            }
            else {
                nextCharLeftShift = 0f;
            }

            var glyphWidth = fontMetrics.charWidth(character);
            if (glyphwiseAdditionalHorizontalTextureSpacing != null &&
                    glyphwiseAdditionalHorizontalTextureSpacing.containsKey(character)) {
                glyphWidth += (int) (glyphHeight *
                        glyphwiseAdditionalHorizontalTextureSpacing.get(character));
            }

            var glyphWidthWithTextureSpacing =
                    glyphWidth + (glyphHeight * additionalGlyphHorizontalTextureSpacing);

            if (widthThusFar + glyphWidthWithTextureSpacing > MAXIMUM_TEXTURE_DIMENSION_SIZE) {
                widthThusFar = 0;
                rowNumber++;
            }

            if (glyphFunction != null) {
                glyphFunction.apply(character).apply(widthThusFar).apply(rowNumber)
                        .apply(glyphWidthWithTextureSpacing).apply(charLeftShift)
                        .accept(nextCharLeftShift);
            }

            // NB: The 0.5f factor is to ensure that the casting rounds up, so no glyph pixels
            //     overlap
            widthThusFar += (int) (glyphWidthWithTextureSpacing + 0.5f);
        }
        // NB: The 0.5f factor is to ensure that the casting rounds up, so no glyph pixels are lost
        var imageHeight = (int) ((glyphHeight * (1f + additionalGlyphVerticalTextureSpacing) *
                (rowNumber + 1)) - (glyphHeight * additionalGlyphVerticalTextureSpacing) + 0.5f);



        return new FontImageInfo(
                coordinate2dOf(rowNumber > 0 ? MAXIMUM_TEXTURE_DIMENSION_SIZE : widthThusFar,
                        imageHeight), glyphHeight, glyphDescent);
    }

    private static ByteBuffer generateImage(BufferedImage bufferedImage, java.awt.Font font,
                                            FontMetrics fontMetrics, int imageWidth,
                                            int imageHeight,
                                            float additionalGlyphHorizontalTextureSpacing,
                                            Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing,
                                            Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift,
                                            float additionalGlyphVerticalTextureSpacing,
                                            float leadingAdjustment, float glyphHeight,
                                            float glyphDescent, Map<Character, FloatBox> glyphs) {
        var graphics2d = (Graphics2D) bufferedImage.getGraphics();
        graphics2d.setFont(font);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawCharacters(graphics2d, fontMetrics, imageWidth, imageHeight,
                additionalGlyphHorizontalTextureSpacing,
                glyphwiseAdditionalHorizontalTextureSpacing, glyphwiseAdditionalLeftBoundaryShift,
                additionalGlyphVerticalTextureSpacing, leadingAdjustment, glyphHeight, glyphDescent,
                glyphs);

        return createBuffer(bufferedImage, imageWidth, imageHeight);
    }

    private static void drawCharacters(Graphics2D graphics2d, FontMetrics fontMetrics,
                                       int imageWidth, int imageHeight,
                                       float additionalGlyphHorizontalTextureSpacing,
                                       Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing,
                                       Map<Character, Float> glyphwiseAdditionalLeftBoundaryShift,
                                       float additionalGlyphVerticalTextureSpacing,
                                       float leadingAdjustment, float glyphHeight,
                                       float glyphDescent, Map<Character, FloatBox> glyphs) {
        var imageWidthFloat = (float) imageWidth;
        var imageHeightFloat = (float) imageHeight;
        var rowHeightInclTextureSpacing =
                (glyphHeight * (1f + additionalGlyphVerticalTextureSpacing));
        var rowTextureSpacing = (additionalGlyphVerticalTextureSpacing * glyphHeight);
        var glyphHeightInImage = glyphHeight / imageHeightFloat;
        loopOverCharacters(fontMetrics, additionalGlyphHorizontalTextureSpacing,
                glyphwiseAdditionalHorizontalTextureSpacing, glyphwiseAdditionalLeftBoundaryShift,
                additionalGlyphVerticalTextureSpacing, leadingAdjustment,
                character -> widthThusFar -> rowNumber -> glyphWidth -> charLeftShift -> nextCharLeftShift -> {
                    float leftX =
                            (widthThusFar / imageWidthFloat) - (charLeftShift * glyphHeightInImage);
                    float topY = (rowHeightInclTextureSpacing * rowNumber) / imageHeightFloat;
                    float rightX = (glyphWidth / imageWidthFloat) -
                            (nextCharLeftShift * glyphHeightInImage) + leftX;
                    float bottomY = topY + glyphHeightInImage;
                    glyphs.put(character, floatBoxOf(leftX, topY, rightX, bottomY));
                    float glyphDrawTopY =
                            (rowHeightInclTextureSpacing * (rowNumber + 1)) - glyphDescent -
                                    rowTextureSpacing;
                    graphics2d.drawString(String.valueOf(character), widthThusFar, glyphDrawTopY);
                });
    }

    private static class FontImageInfo {
        Coordinate2d ImageDimensions;
        int TextureId;
        float GlyphHeight;
        float GlyphDescent;

        private FontImageInfo(Coordinate2d imageDimensions, float glyphHeight, float glyphDescent) {
            ImageDimensions = imageDimensions;
            GlyphHeight = glyphHeight;
            GlyphDescent = glyphDescent;
        }
    }

    private static ByteBuffer createBuffer(BufferedImage bufferedImage, int imageWidth,
                                           int imageHeight) {
        var pixels = new int[imageWidth * imageHeight];

        bufferedImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
        var byteBuffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * RGBA_BYTES);

        for (var pixel : pixels) {
            byteBuffer.put((byte) ((pixel >> RED_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> GREEN_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> BLUE_OFFSET) & 0xFF));
            byteBuffer.put((byte) ((pixel >> ALPHA_OFFSET) & 0xFF));
        }

        byteBuffer.flip();

        return byteBuffer;
    }

    private static class FontStyleInfoImpl implements FontStyleInfo {
        private final Map<Character, FloatBox> GLYPHS;
        private final Coordinate2d TEXTURE_DIMENSIONS;
        private final float TEXTURE_WIDTH_TO_HEIGHT_RATIO;
        private final float ADDITIONAL_HORIZONTAL_TEXTURE_SPACING;
        private final Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING;
        private final int TEXTURE_ID;

        private FontStyleInfoImpl(Map<Character, FloatBox> glyphs, Coordinate2d textureDimensions,
                                  float textureWidthToHeightRatio,
                                  float additionalHorizontalTextureSpacing,
                                  Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing,
                                  int textureId) {
            GLYPHS = glyphs;
            TEXTURE_DIMENSIONS = textureDimensions;
            TEXTURE_WIDTH_TO_HEIGHT_RATIO = textureWidthToHeightRatio;
            ADDITIONAL_HORIZONTAL_TEXTURE_SPACING = additionalHorizontalTextureSpacing;
            GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
                    glyphwiseAdditionalHorizontalTextureSpacing;
            TEXTURE_ID = textureId;
        }

        @Override
        public FloatBox getUvCoordinatesForGlyph(char glyph) throws IllegalArgumentException {
            Check.throwOnLtValue(glyph, ASCII_CHAR_SPACE, "glyph");
            Check.throwOnEqualsValue(glyph, ASCII_CHAR_DELETE, "glyph");
            Check.throwOnGteValue(glyph, NUMBER_EXTENDED_ASCII_CHARS, "glyph");
            return GLYPHS.get(glyph);
        }

        @Override
        public Coordinate2d textureDimensions() {
            return TEXTURE_DIMENSIONS;
        }

        @Override
        public float textureWidthToHeightRatio() {
            return TEXTURE_WIDTH_TO_HEIGHT_RATIO;
        }

        @Override
        public float additionalHorizontalTextureSpacing() {
            return ADDITIONAL_HORIZONTAL_TEXTURE_SPACING;
        }

        @Override
        public Map<Character, Float> glyphwiseAdditionalHorizontalTextureSpacing() {
            return GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING;
        }

        @Override
        public int textureId() {
            return TEXTURE_ID;
        }
    }

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }

    @Override
    public FontStyleInfo plain() {
        return PLAIN;
    }

    @Override
    public FontStyleInfo italic() {
        return ITALIC;
    }

    @Override
    public FontStyleInfo bold() {
        return BOLD;
    }

    @Override
    public FontStyleInfo boldItalic() {
        return BOLD_ITALIC;
    }
}
