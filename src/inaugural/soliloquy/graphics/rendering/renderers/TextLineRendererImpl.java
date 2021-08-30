package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class TextLineRendererImpl extends CanRenderSnippets<TextLineRenderable>
        implements TextLineRenderer {
    private final Color DEFAULT_COLOR;

    public TextLineRendererImpl(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory, Color defaultColor,
                                WindowResolutionManager windowResolutionManager,
                                Long mostRecentTimestamp) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE, windowResolutionManager,
                mostRecentTimestamp);
        DEFAULT_COLOR = Check.ifNull(defaultColor, "defaultColor");
    }

    @Override
    public void render(TextLineRenderable textLineRenderable, long timestamp)
            throws IllegalArgumentException {
        float lineHeight = validateTextLineRenderableAndGetLineHeight(textLineRenderable,
                timestamp, "render");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        Float borderThickness = null;
        Color borderColor = null;

        if (textLineRenderable.getBorderThicknessProvider() != null) {
            borderThickness = textLineRenderable.getBorderThicknessProvider().provide(timestamp);
            if (borderThickness != null) {
                Check.throwOnLtValue(borderThickness, 0f, "provided border thickness");
                borderColor = Check.ifNull(textLineRenderable.getBorderColorProvider(),
                        "textLineRenderable.getBorderColorProvider()")
                        .provide(timestamp);
            }
        }

        Float dropShadowSize;
        Pair<Float, Float> dropShadowOffset = null;
        Color dropShadowColor = null;

        dropShadowSize = textLineRenderable.dropShadowSizeProvider().provide(timestamp);
        if (dropShadowSize != null) {
            if (dropShadowSize < 0f) {
                throw new IllegalArgumentException(
                        "TextLineRendererImpl.render: dropShadowSize cannot be less than 0");
            }
            dropShadowOffset = textLineRenderable.dropShadowOffsetProvider().provide(timestamp);
            Check.ifNull(dropShadowOffset, "dropShadowOffset provided by textLineRenderable");
            Check.ifNull(dropShadowOffset.getItem1(),
                    "dropShadowOffset's x offset provided by textLineRenderable");
            Check.ifNull(dropShadowOffset.getItem2(),
                    "dropShadowOffset's y offset provided by textLineRenderable");
            dropShadowColor = textLineRenderable.dropShadowColorProvider().provide(timestamp);
            Check.ifNull(dropShadowColor, "dropShadowColor provided by textLineRenderable");
        }

        Pair<Float,Float> renderingLocation =
                textLineRenderable.getRenderingLocationProvider().provide(timestamp);
        float startX;
        float startY = renderingLocation.getItem2();

        if (textLineRenderable.getJustification() == TextJustification.LEFT) {
            startX = renderingLocation.getItem1();
        }
        else {
            float lineLength = textLineLength(textLineRenderable, timestamp);
            if (textLineRenderable.getJustification() == TextJustification.CENTER) {
                startX = renderingLocation.getItem1() - (lineLength / 2f);
            }
            else {
                startX = renderingLocation.getItem1() - lineLength;
            }
        }
        renderAtLocation(textLineRenderable, timestamp, lineHeight, startX, startY,
                borderThickness, borderColor, dropShadowSize, dropShadowOffset, dropShadowColor);
    }

    private void renderAtLocation(TextLineRenderable textLineRenderable, Long timestamp,
                                  float lineHeight, float startX, float startY,
                                  Float borderThickness, Color borderColor, Float dropShadowSize,
                                  Pair<Float, Float> dropShadowOffset, Color dropShadowColor) {
        if (dropShadowSize != null) {
            float xOffset = dropShadowOffset.getItem1() / _screenWidthToHeightRatio;
            float yOffset = dropShadowOffset.getItem2();
            float sizeAdjustment = dropShadowSize / lineHeight;

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xOffset + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY + yOffset,
                                leftX + (glyphLength * sizeAdjustment),
                                startY + yOffset + (lineHeight * sizeAdjustment)
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                dropShadowColor);
                    });
        }

        if (borderThickness != null) {
            float yThickness = borderThickness;
            float xThickness = yThickness / _screenWidthToHeightRatio;

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY - yThickness,
                                leftX + glyphLength,
                                startY - yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY - yThickness,
                                leftX + glyphLength,
                                startY - yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY - yThickness,
                                leftX + glyphLength,
                                startY - yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY,
                                leftX + glyphLength,
                                startY + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY + yThickness,
                                leftX + glyphLength,
                                startY + yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY + yThickness,
                                leftX + glyphLength,
                                startY + yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY + yThickness,
                                leftX + glyphLength,
                                startY + yThickness + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                                leftX,
                                startY,
                                leftX + glyphLength,
                                startY + lineHeight
                        );

                        super.render(renderingArea,
                                glyphBox.leftX(), glyphBox.topY(),
                                glyphBox.rightX(), glyphBox.bottomY(),
                                textureId,
                                borderColor);
                    });
        }
        iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                    float leftX = startX + textLineLengthThusFar;
                    FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                            leftX,
                            startY,
                            leftX + glyphLength,
                            startY + lineHeight
                    );

                    super.render(renderingArea,
                            glyphBox.leftX(), glyphBox.topY(),
                            glyphBox.rightX(), glyphBox.bottomY(),
                            textureId,
                            color);
                });
    }

    @Override
    public float textLineLength(TextLineRenderable textLineRenderable, long timestamp)
            throws IllegalArgumentException {
        float lineHeight = validateTextLineRenderableAndGetLineHeight(textLineRenderable,
                timestamp, "textLineLength");
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        return iterateOverTextLine(textLineRenderable, timestamp, lineHeight, null);
    }

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't elegant, but this is not front-facing code.
    private float iterateOverTextLine(TextLineRenderable textLineRenderable, Long timestamp,
                                      float lineHeight,
                                      Function<Float, Function<Float, Function<Integer,
                                              Function<FloatBox, Consumer<Color>>>>>
                                              renderingAction) {
        boolean italic = false;
        boolean bold = false;
        int nextItalicIndex = 0;
        int nextBoldIndex = 0;
        float textLineLengthThusFar = 0f;
        Color color = DEFAULT_COLOR;
        FontStyleInfo fontStyleInfo;
        float paddingBetweenGlyphs =
                textLineRenderable.getPaddingBetweenGlyphs() * lineHeight;

        for (int i = 0; i < textLineRenderable.getLineText().length(); i++) {
            if (renderingAction != null) {
                if (textLineRenderable.colorProviderIndices() != null &&
                        textLineRenderable.colorProviderIndices().containsKey(i)) {
                    color = textLineRenderable.colorProviderIndices().get(i).provide(timestamp);
                }
            }
            if (textLineRenderable.italicIndices() != null &&
                    textLineRenderable.italicIndices().size() > nextItalicIndex &&
                    textLineRenderable.italicIndices().get(nextItalicIndex) == i) {
                italic = !italic;
                nextItalicIndex++;
            }
            if (textLineRenderable.boldIndices() != null &&
                    textLineRenderable.boldIndices().size() > nextBoldIndex &&
                    textLineRenderable.boldIndices().get(nextBoldIndex) == i) {
                bold = !bold;
                nextBoldIndex++;
            }

            if (italic) {
                if (bold) {
                    fontStyleInfo = textLineRenderable.getFont().boldItalic();
                }
                else {
                    fontStyleInfo = textLineRenderable.getFont().italic();
                }
            }
            else {
                if (bold) {
                    fontStyleInfo = textLineRenderable.getFont().bold();
                }
                else {
                    fontStyleInfo = textLineRenderable.getFont().plain();
                }
            }

            if (i > 0) {
                textLineLengthThusFar += paddingBetweenGlyphs;
            }

            char character = textLineRenderable.getLineText().charAt(i);
            FloatBox glyphBox = fontStyleInfo.getUvCoordinatesForGlyph(character);
            float glyphLength =
                    glyphBox.width() * (lineHeight / glyphBox.height())
                    * fontStyleInfo.textureWidthToHeightRatio();

            if (renderingAction != null) {
                renderingAction.apply(textLineLengthThusFar).apply(glyphLength)
                        .apply(fontStyleInfo.textureId()).apply(glyphBox).accept(color);
            }

            float lengthThusFarAddition = glyphLength;
            float paddingPercentage = fontStyleInfo.additionalHorizontalTextureSpacing();
            if (fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing()
                    .containsKey(character)) {
                paddingPercentage +=
                        fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing().get(character);
            }
            lengthThusFarAddition -= paddingPercentage * lineHeight;

            textLineLengthThusFar += lengthThusFarAddition;
        }

        return textLineLengthThusFar;
    }

    private float validateTextLineRenderableAndGetLineHeight(TextLineRenderable textLineRenderable,
                                                             long timestamp, String methodName) {
        Check.ifNull(textLineRenderable, "textLineRenderable");
        Check.ifNull(textLineRenderable.getFont(), "textLineRenderable.getFont()");
        Check.ifNull(textLineRenderable.lineHeightProvider(),
                "textLineRenderable.lineHeightProvider()");
        Check.ifNull(textLineRenderable.getRenderingLocationProvider(),
                "textLineRenderable.getRenderingLocationProvider()");
        Check.ifNull(textLineRenderable.uuid(), "textLineRenderable.id()");
        if (textLineRenderable.colorProviderIndices() != null) {
            Integer highestIndexThusFar = null;
            Set<Map.Entry<Integer, ProviderAtTime<Color>>> colorProviderIndicesEntries =
                    textLineRenderable.colorProviderIndices().entrySet();
            for (Map.Entry<Integer, ProviderAtTime<Color>> entry : colorProviderIndicesEntries) {
                validateIndex(entry.getKey(), textLineRenderable.getLineText().length(),
                        "textLineRenderable.colorIndices()", methodName, highestIndexThusFar);
                highestIndexThusFar = entry.getKey();
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": "
                            + "textLineRenderable.colorIndices cannot contain null value");
                }
            }
        }

        Check.ifNull(textLineRenderable.italicIndices(), "textLineRenderable.italicIndices()");
        Integer highestIndexThusFar = null;
        for (Integer index : textLineRenderable.italicIndices()) {
            validateIndex(index, textLineRenderable.getLineText().length(),
                    "textLineRenderable.italicIndices()", methodName,
                    highestIndexThusFar);
            highestIndexThusFar = index;
        }

        Check.ifNull(textLineRenderable.boldIndices(), "textLineRenderable.boldIndices()");
        highestIndexThusFar = null;
        for (Integer index : textLineRenderable.boldIndices()) {
            validateIndex(index, textLineRenderable.getLineText().length(),
                    "textLineRenderable.italicIndices()", methodName,
                    highestIndexThusFar);
            highestIndexThusFar = index;
        }

        if (Check.ifNull(textLineRenderable.getJustification(),
                "textLineRenderable.getJustification()") == TextJustification.UNKNOWN) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    "justification cannot be UNKNOWN");
        }

        Float lineHeight = textLineRenderable.lineHeightProvider().provide(timestamp);
        Check.ifNull(lineHeight, "value provided from textLineRenderable.lineHeightProvider()");
        if (lineHeight <= 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName +
                    ": value provided from textLineRenderable.lineHeightProvider() must be " +
                    "greater than 0");
        }

        Check.ifNull(textLineRenderable.dropShadowSizeProvider(),
                "textLineRenderable.dropShadowSizeProvider()");
        Check.ifNull(textLineRenderable.dropShadowOffsetProvider(),
                "textLineRenderable.dropShadowOffsetProvider()");
        Check.ifNull(textLineRenderable.dropShadowColorProvider(),
                "textLineRenderable.dropShadowColorProvider()");

        return lineHeight;
    }

    private void validateIndex(Integer index, int textLineLength, String dataStructureName,
                               String methodName, Integer highestIndexThusFar) {
        if (index == null) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain null key");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain negative key");
        }
        if (index >= textLineLength) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain a key beyond lineText length");
        }
        if (highestIndexThusFar != null && index <= highestIndexThusFar) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain an index out of ascending order");
        }
    }

    private static final TextLineRenderable ARCHETYPE = new TextLineRenderable() {
        @Override
        public ProviderAtTime<Float> getBorderThicknessProvider() {
            return null;
        }

        @Override
        public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBorderColorProvider() {
            return null;
        }

        @Override
        public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public void delete() {

        }

        @Override
        public Font getFont() {
            return null;
        }

        @Override
        public void setFont(Font font) throws IllegalArgumentException {

        }

        @Override
        public String getLineText() {
            return null;
        }

        @Override
        public void setLineText(String s) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Pair<Float, Float>> getRenderingLocationProvider() {
            return null;
        }

        @Override
        public void setRenderingLocationProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Float> lineHeightProvider() {
            return null;
        }

        @Override
        public void setLineHeightProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public void setZ(int i) {

        }

        @Override
        public float getPaddingBetweenGlyphs() {
            return 0;
        }

        @Override
        public void setPaddingBetweenGlyphs(float v) {

        }

        @Override
        public TextJustification getJustification() {
            return null;
        }

        @Override
        public void setJustification(TextJustification textJustification) throws IllegalArgumentException {

        }

        @Override
        public Map<Integer, ProviderAtTime<Color>> colorProviderIndices() {
            return null;
        }

        @Override
        public List<Integer> italicIndices() {
            return null;
        }

        @Override
        public List<Integer> boldIndices() {
            return null;
        }

        @Override
        public ProviderAtTime<Float> dropShadowSizeProvider() {
            return null;
        }

        @Override
        public void setDropShadowSizeProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Pair<Float, Float>> dropShadowOffsetProvider() {
            return null;
        }

        @Override
        public void setDropShadowOffsetProvider(ProviderAtTime<Pair<Float, Float>> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> dropShadowColorProvider() {
            return null;
        }

        @Override
        public void setDropShadowColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

        }

        @Override
        public String getInterfaceName() {
            return TextLineRenderable.class.getCanonicalName();
        }
    };
}
