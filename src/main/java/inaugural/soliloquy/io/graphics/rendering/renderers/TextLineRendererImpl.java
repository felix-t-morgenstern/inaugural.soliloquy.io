package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.assets.FontStyleInfo;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TextLineRendererImpl extends CanRenderSnippets<TextLineRenderable>
        implements TextLineRenderer {
    private final Color DEFAULT_COLOR;

    public TextLineRendererImpl(RenderingBoundaries renderingBoundaries,
                                Color defaultColor,
                                Supplier<Float> getScreenWToHRatio,
                                TimestampValidator timestampValidator) {
        super(renderingBoundaries, getScreenWToHRatio, timestampValidator);
        DEFAULT_COLOR = Check.ifNull(defaultColor, "defaultColor");
    }

    @Override
    public void render(TextLineRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");
        Check.ifNull(renderable.getLineTextProvider(), "renderable.getLineTextProvider()");
        var lineText = renderable.getLineTextProvider().provide(timestamp);
        var lineHeight = validateTextLineRenderableAndGetLineHeight(renderable, lineText.length(),
                timestamp, "render");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        Float borderThickness = null;
        Color borderColor = null;


        if (renderable.getBorderThicknessProvider() != null) {
            borderThickness = renderable.getBorderThicknessProvider().provide(timestamp);
            if (borderThickness != null) {
                Check.throwOnLtValue(borderThickness, 0f, "provided border thickness");
                borderColor = Check.ifNull(renderable.getBorderColorProvider(),
                                "renderable.getBorderColorProvider()")
                        .provide(timestamp);
            }
        }

        Float dropShadowSize;
        Vertex dropShadowOffset = null;
        Color dropShadowColor = null;

        dropShadowSize = renderable.dropShadowSizeProvider().provide(timestamp);
        if (dropShadowSize != null) {
            if (dropShadowSize < 0f) {
                throw new IllegalArgumentException(
                        "TextLineRendererImpl.render: dropShadowSize cannot be less than 0");
            }
            dropShadowOffset = renderable.dropShadowOffsetProvider().provide(timestamp);
            Check.ifNull(dropShadowOffset, "dropShadowOffset provided by renderable");
            Check.ifNull(dropShadowOffset.X,
                    "dropShadowOffset's X offset provided by renderable");
            Check.ifNull(dropShadowOffset.Y,
                    "dropShadowOffset's Y offset provided by renderable");
            dropShadowColor = renderable.dropShadowColorProvider().provide(timestamp);
            Check.ifNull(dropShadowColor, "dropShadowColor provided by renderable");
        }

        var renderingLocation = renderable.getRenderingLocationProvider().provide(timestamp);
        float startX;
        float startY = renderingLocation.Y;

        if (renderable.getJustification() == TextJustification.LEFT) {
            startX = renderingLocation.X;
        }
        else {
            float lineLength = textLineLength(renderable, timestamp);
            if (renderable.getJustification() == TextJustification.CENTER) {
                startX = renderingLocation.X - (lineLength / 2f);
            }
            else {
                startX = renderingLocation.X - lineLength;
            }
        }
        renderAtLocation(renderable, timestamp, lineText, lineHeight, startX, startY,
                borderThickness, borderColor, dropShadowSize, dropShadowOffset, dropShadowColor);
    }

    private void renderAtLocation(TextLineRenderable renderable, Long timestamp, String lineText,
                                  float lineHeight, float startX, float startY,
                                  Float borderThickness, Color borderColor, Float dropShadowSize,
                                  Vertex dropShadowOffset, Color dropShadowColor) {
        if (dropShadowSize != null) {
            float xOffset = dropShadowOffset.X / getScreenWToHRatio.get();
            float yOffset = dropShadowOffset.Y;
            float sizeAdjustment = dropShadowSize / lineHeight;

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
                        var leftX = startX + xOffset + textLineLengthThusFar;
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
            var xThickness = yThickness / getScreenWToHRatio.get();

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
                        var leftX = startX - xThickness + textLineLengthThusFar;
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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

            iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
                    textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> _ -> {
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
        iterateOverTextLine(renderable, timestamp, lineText, lineHeight,
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
    public float textLineLength(TextLineRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");
        Check.ifNull(renderable.getLineTextProvider(), "renderable.getLineTextProvider()");
        var lineText = renderable.getLineTextProvider().provide(timestamp);
        var lineHeight = validateTextLineRenderableAndGetLineHeight(renderable, lineText.length(),
                timestamp, "textLineLength");
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        return iterateOverTextLine(renderable, timestamp, lineText, lineHeight, null);
    }

    @Override
    public float textLineLength(String text,
                                Font font,
                                float paddingBetweenGlyphs,
                                List<Integer> italicIndices,
                                List<Integer> boldIndices,
                                float lineHeight) throws IllegalArgumentException {
        return iterateOverTextLine(
                text,
                font,
                paddingBetweenGlyphs,
                italicIndices,
                boldIndices,
                null,
                lineHeight,
                null,
                null
        );
    }

    @Override
    public float getGlyphWidth(char aChar,
                               FontStyleInfo fontStyleInfo,
                               float lineHeight)
            throws IllegalArgumentException {
        return getGlyphInfo(aChar, fontStyleInfo, lineHeight).SECOND -
                rightPaddingAdjustment(aChar, fontStyleInfo, lineHeight);
    }

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't
    // elegant, but this is not front-facing code.
    private float iterateOverTextLine(
            TextLineRenderable renderable,
            Long timestamp,
            String lineText,
            float lineHeight,
            Function<Float, Function<Float, Function<Integer,
                    Function<FloatBox, Consumer<Color>>>>> renderingAction
    ) {
        return iterateOverTextLine(
                lineText,
                renderable.getFont(),
                renderable.getPaddingBetweenGlyphs(),
                renderable.italicIndices(),
                renderable.boldIndices(),
                timestamp,
                lineHeight,
                renderable.colorProviderIndices(),
                renderingAction
        );
    }

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't
    // elegant, but this is not front-facing code.
    private float iterateOverTextLine(
            String lineText,
            Font font,
            float paddingBetweenGlyphs,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            Long timestamp,
            float lineHeight,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            Function<Float, Function<Float, Function<Integer,
                    Function<FloatBox, Consumer<Color>>>>> renderingAction
    ) {
        var italic = false;
        var bold = false;
        var nextItalicIndex = 0;
        var nextBoldIndex = 0;
        var textLineLengthThusFar = 0f;
        var color = DEFAULT_COLOR;
        var paddingToRender = paddingBetweenGlyphs * lineHeight;
        FontStyleInfo fontStyleInfo;

        for (var i = 0; i < lineText.length(); i++) {
            if (renderingAction != null) {
                if (colorProviderIndices != null && colorProviderIndices.containsKey(i)) {
                    color = colorProviderIndices.get(i).provide(timestamp);
                }
            }
            if (italicIndices != null &&
                    italicIndices.size() > nextItalicIndex &&
                    italicIndices.get(nextItalicIndex) == i) {
                italic = !italic;
                nextItalicIndex++;
            }
            if (boldIndices != null &&
                    boldIndices.size() > nextBoldIndex &&
                    boldIndices.get(nextBoldIndex) == i) {
                bold = !bold;
                nextBoldIndex++;
            }

            if (italic) {
                if (bold) {
                    fontStyleInfo = font.boldItalic();
                }
                else {
                    fontStyleInfo = font.italic();
                }
            }
            else {
                if (bold) {
                    fontStyleInfo = font.bold();
                }
                else {
                    fontStyleInfo = font.plain();
                }
            }

            if (i > 0) {
                textLineLengthThusFar += paddingToRender;
            }

            var character = lineText.charAt(i);

            var glyphInfo = getGlyphInfo(character, fontStyleInfo, lineHeight);
            var glyphBox = glyphInfo.FIRST;
            float glyphLength = glyphInfo.SECOND;

            if (renderingAction != null) {
                renderingAction.apply(textLineLengthThusFar).apply(glyphLength)
                        .apply(fontStyleInfo.textureId()).apply(glyphBox).accept(color);
            }

            var lengthThusFarAddition =
                    glyphLength - rightPaddingAdjustment(character, fontStyleInfo, lineHeight);

            textLineLengthThusFar += lengthThusFarAddition;
        }

        return textLineLengthThusFar;
    }

    private Pair<FloatBox, Float> getGlyphInfo(char aChar,
                                               FontStyleInfo fontStyleInfo,
                                               float lineHeight) {
        var glyphBox = fontStyleInfo.getUvCoordinatesForGlyph(aChar);
        if (fontStyleInfo.glyphwiseWidthFactors().containsKey(aChar)) {
            var newWidth =
                    glyphBox.width() * fontStyleInfo.glyphwiseWidthFactors().get(aChar);
            glyphBox = floatBoxOf(
                    glyphBox.LEFT_X,
                    glyphBox.TOP_Y,
                    glyphBox.LEFT_X + newWidth,
                    glyphBox.BOTTOM_Y
            );
        }
        var glyphLength = glyphBox.width() * (lineHeight / glyphBox.height())
                * fontStyleInfo.textureWidthToHeightRatio();

        return pairOf(glyphBox, glyphLength);
    }

    private float rightPaddingAdjustment(char aChar,
                                         FontStyleInfo fontStyleInfo,
                                         float lineHeight) {
        var paddingPercentage = fontStyleInfo.additionalHorizontalTextureSpacing();
        if (fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing()
                .containsKey(aChar)) {
            paddingPercentage +=
                    fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing().get(aChar);
        }
        return paddingPercentage * lineHeight;
    }

    private float validateTextLineRenderableAndGetLineHeight(
            TextLineRenderable renderable,
            int lineTextLength,
            long timestamp,
            String methodName
    ) {
        Check.ifNull(renderable.getFont(), "renderable.getFont()");
        Check.ifNull(renderable.lineHeightProvider(), "renderable.lineHeightProvider()");
        Check.ifNull(renderable.getRenderingLocationProvider(),
                "renderable.getRenderingLocationProvider()");
        Check.ifNull(renderable.uuid(), "renderable.id()");
        if (renderable.colorProviderIndices() != null) {
            Set<Map.Entry<Integer, ProviderAtTime<Color>>> colorProviderIndicesEntries =
                    renderable.colorProviderIndices().entrySet();
            for (Map.Entry<Integer, ProviderAtTime<Color>> entry : colorProviderIndicesEntries) {
                validateIndex(entry.getKey(), lineTextLength, "renderable.colorIndices()",
                        methodName, null);
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": "
                            + "renderable.colorIndices cannot contain null value");
                }
            }
        }

        Check.ifNull(renderable.italicIndices(), "renderable.italicIndices()");
        Integer highestIndexThusFar = null;
        for (var index : renderable.italicIndices()) {
            validateIndex(index, lineTextLength, "renderable.italicIndices()", methodName,
                    highestIndexThusFar);
            highestIndexThusFar = index;
        }

        Check.ifNull(renderable.boldIndices(), "renderable.boldIndices()");
        highestIndexThusFar = null;
        for (var index : renderable.boldIndices()) {
            validateIndex(index, lineTextLength, "renderable.boldIndices()", methodName,
                    highestIndexThusFar);
            highestIndexThusFar = index;
        }

        if (Check.ifNull(renderable.getJustification(),
                "renderable.getJustification()") == TextJustification.UNKNOWN) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    "justification cannot be UNKNOWN");
        }

        Float lineHeight = renderable.lineHeightProvider().provide(timestamp);
        Check.ifNull(lineHeight, "value provided from renderable.lineHeightProvider()");
        if (lineHeight <= 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName +
                    ": value provided from renderable.lineHeightProvider() must be " +
                    "greater than 0");
        }

        Check.ifNull(renderable.dropShadowSizeProvider(),
                "renderable.dropShadowSizeProvider()");
        Check.ifNull(renderable.dropShadowOffsetProvider(),
                "renderable.dropShadowOffsetProvider()");
        Check.ifNull(renderable.dropShadowColorProvider(),
                "renderable.dropShadowColorProvider()");

        return lineHeight;
    }

    private void validateIndex(Integer index, int lineTextLength, String dataStructureName,
                               String methodName,
                               Integer highestIndexThusFar) {
        if (index == null) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain null key");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain negative key");
        }
        if (index > lineTextLength) {
            throw new IllegalArgumentException(
                    "TextLineRenderableImpl." + methodName + ": " + dataStructureName +
                            " cannot contain index above line length");
        }
        if (highestIndexThusFar != null && index <= highestIndexThusFar) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain an index out of ascending order");
        }
    }
}
