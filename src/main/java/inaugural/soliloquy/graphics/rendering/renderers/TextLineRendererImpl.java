package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TextLineRendererImpl extends CanRenderSnippets<TextLineRenderable>
        implements TextLineRenderer {
    private final Color DEFAULT_COLOR;

    public TextLineRendererImpl(RenderingBoundaries renderingBoundaries,
                                Color defaultColor, WindowResolutionManager windowResolutionManager,
                                Long mostRecentTimestamp) {
        super(renderingBoundaries, windowResolutionManager, mostRecentTimestamp);
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
        Vertex dropShadowOffset = null;
        Color dropShadowColor = null;

        dropShadowSize = textLineRenderable.dropShadowSizeProvider().provide(timestamp);
        if (dropShadowSize != null) {
            if (dropShadowSize < 0f) {
                throw new IllegalArgumentException(
                        "TextLineRendererImpl.render: dropShadowSize cannot be less than 0");
            }
            dropShadowOffset = textLineRenderable.dropShadowOffsetProvider().provide(timestamp);
            Check.ifNull(dropShadowOffset, "dropShadowOffset provided by textLineRenderable");
            Check.ifNull(dropShadowOffset.X,
                    "dropShadowOffset's X offset provided by textLineRenderable");
            Check.ifNull(dropShadowOffset.Y,
                    "dropShadowOffset's Y offset provided by textLineRenderable");
            dropShadowColor = textLineRenderable.dropShadowColorProvider().provide(timestamp);
            Check.ifNull(dropShadowColor, "dropShadowColor provided by textLineRenderable");
        }

        Vertex renderingLocation =
                textLineRenderable.getRenderingLocationProvider().provide(timestamp);
        float startX;
        float startY = renderingLocation.Y;

        if (textLineRenderable.getJustification() == TextJustification.LEFT) {
            startX = renderingLocation.X;
        }
        else {
            float lineLength = textLineLength(textLineRenderable, timestamp);
            if (textLineRenderable.getJustification() == TextJustification.CENTER) {
                startX = renderingLocation.X - (lineLength / 2f);
            }
            else {
                startX = renderingLocation.X - lineLength;
            }
        }
        renderAtLocation(textLineRenderable, timestamp, lineHeight, startX, startY,
                borderThickness, borderColor, dropShadowSize, dropShadowOffset, dropShadowColor);
    }

    private void renderAtLocation(TextLineRenderable textLineRenderable, Long timestamp,
                                  float lineHeight, float startX, float startY,
                                  Float borderThickness, Color borderColor, Float dropShadowSize,
                                  Vertex dropShadowOffset, Color dropShadowColor) {
        if (dropShadowSize != null) {
            float xOffset = dropShadowOffset.X / getScreenWidthToHeightRatio.get();
            float yOffset = dropShadowOffset.Y;
            float sizeAdjustment = dropShadowSize / lineHeight;

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xOffset + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY + yOffset
                                ),
                                vertexOf(
                                        leftX + (glyphLength * sizeAdjustment),
                                        startY + yOffset + (lineHeight * sizeAdjustment
                                        )
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                dropShadowColor);
                    });
        }

        if (borderThickness != null) {
            float yThickness = borderThickness;
            float xThickness = yThickness / getScreenWidthToHeightRatio.get();

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY - yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY - yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY - yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY - yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY - yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY - yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY + yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY + yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY + yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY + yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY + yThickness
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY + yThickness + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });

            iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                        float leftX = startX - xThickness + textLineLengthThusFar;
                        var renderingArea = floatBoxOf(
                                vertexOf(
                                        leftX,
                                        startY
                                ),
                                vertexOf(
                                        leftX + glyphLength,
                                        startY + lineHeight
                                )
                        );

                        super.render(renderingArea,
                                glyphBox.LEFT_X, glyphBox.TOP_Y,
                                glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
                                textureId,
                                borderColor);
                    });
        }
        iterateOverTextLine(textLineRenderable, timestamp, lineHeight,
                textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                    float leftX = startX + textLineLengthThusFar;
                    var renderingArea = floatBoxOf(
                            vertexOf(
                                    leftX,
                                    startY
                            ),
                            vertexOf(
                                    leftX + glyphLength,
                                    startY + lineHeight
                            )
                    );

                    super.render(renderingArea,
                            glyphBox.LEFT_X, glyphBox.TOP_Y,
                            glyphBox.RIGHT_X, glyphBox.BOTTOM_Y,
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

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't
    // elegant, but this is not front-facing code.
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

        String lineText = textLineRenderable.getLineTextProvider().provide(timestamp);
        for (var i = 0; i < lineText.length(); i++) {
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

            char character = lineText.charAt(i);
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
                validateIndex(entry.getKey(), "textLineRenderable.colorIndices()", methodName,
                        highestIndexThusFar);
                highestIndexThusFar = entry.getKey();
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": "
                            + "textLineRenderable.colorIndices cannot contain null value");
                }
            }
        }

        Check.ifNull(textLineRenderable.italicIndices(), "textLineRenderable.italicIndices()");
        Integer highestIndexThusFar = null;
        for (var index : textLineRenderable.italicIndices()) {
            validateIndex(index, "textLineRenderable.italicIndices()", methodName,
                    highestIndexThusFar);
            highestIndexThusFar = index;
        }

        Check.ifNull(textLineRenderable.boldIndices(), "textLineRenderable.boldIndices()");
        highestIndexThusFar = null;
        for (var index : textLineRenderable.boldIndices()) {
            validateIndex(index, "textLineRenderable.boldIndices()", methodName,
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

    private void validateIndex(Integer index, String dataStructureName, String methodName,
                               Integer highestIndexThusFar) {
        if (index == null) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain null key");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain negative key");
        }
        if (highestIndexThusFar != null && index <= highestIndexThusFar) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain an index out of ascending order");
        }
    }

    private static final TextLineRenderable ARCHETYPE = new TextLineRenderable() {
        @Override
        public UUID uuid() {
            return null;
        }

        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public void setZ(int i) {

        }

        @Override
        public RenderableStack containingStack() {
            return null;
        }

        @Override
        public void delete() {

        }

        @Override
        public ProviderAtTime<Float> getBorderThicknessProvider() {
            return null;
        }

        @Override
        public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> getBorderColorProvider() {
            return null;
        }

        @Override
        public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public Font getFont() {
            return null;
        }

        @Override
        public void setFont(Font font) throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<String> getLineTextProvider() {
            return null;
        }

        @Override
        public void setLineTextProvider(ProviderAtTime<String> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Vertex> getRenderingLocationProvider() {
            return null;
        }

        @Override
        public void setRenderingLocationProvider(ProviderAtTime<Vertex> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Float> lineHeightProvider() {
            return null;
        }

        @Override
        public void setLineHeightProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

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
        public void setJustification(TextJustification textJustification)
                throws IllegalArgumentException {

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
        public void setDropShadowSizeProvider(ProviderAtTime<Float> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Vertex> dropShadowOffsetProvider() {
            return null;
        }

        @Override
        public void setDropShadowOffsetProvider(ProviderAtTime<Vertex> providerAtTime)
                throws IllegalArgumentException {

        }

        @Override
        public ProviderAtTime<Color> dropShadowColorProvider() {
            return null;
        }

        @Override
        public void setDropShadowColorProvider(ProviderAtTime<Color> providerAtTime)
                throws IllegalArgumentException {

        }
    };
}
