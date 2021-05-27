package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
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
                                FloatBoxFactory floatBoxFactory,
                                Color defaultColor) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
        DEFAULT_COLOR = Check.ifNull(defaultColor, "defaultColor");
    }

    @Override
    public void render(TextLineRenderable textLineRenderable, long timestamp)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "render");

        validateTimestamp(timestamp, "TextLineRendererImpl");

        Pair<Float,Float> renderingLocation =
                textLineRenderable.getRenderingLocationProvider().provide(timestamp);

        iterateOverTextLine(textLineRenderable, timestamp,
                textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                    float leftX = renderingLocation.getItem1() + textLineLengthThusFar;
                    FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                            leftX,
                            renderingLocation.getItem2(),
                            leftX + glyphLength,
                            renderingLocation.getItem2() + textLineRenderable.getLineHeight()
                    );

                    super.render(renderingArea,
                            glyphBox.leftX(), glyphBox.topY(),
                            glyphBox.rightX(), glyphBox.bottomY(),
                            textureId,
                            color);
        });
    }

    @Override
    public float textLineLength(TextLineRenderable textLineRenderable)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "textLineLength");

        return iterateOverTextLine(textLineRenderable, null, null);
    }

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't elegant, but this is not front-facing code.
    private float iterateOverTextLine(TextLineRenderable textLineRenderable, Long timestamp,
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
                textLineRenderable.getPaddingBetweenGlyphs() * textLineRenderable.getLineHeight();

        for (int i = 0; i < textLineRenderable.getLineText().length(); i++) {
            if (timestamp != null) {
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
                    glyphBox.width() * (textLineRenderable.getLineHeight() / glyphBox.height())
                    * fontStyleInfo.textureWidthToHeightRatio();

            if (renderingAction != null) {
                renderingAction.apply(textLineLengthThusFar).apply(glyphLength)
                        .apply(fontStyleInfo.textureId()).apply(glyphBox).accept(color);
            }

            float lengthThusFarAddition = glyphLength;
            float paddingPercentage = fontStyleInfo.additionalHorizontalTextureSpacing();
            if (fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing() != null &&
                    fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing()
                            .containsKey(character)) {
                paddingPercentage +=
                        fontStyleInfo.glyphwiseAdditionalHorizontalTextureSpacing().get(character);
            }
            lengthThusFarAddition -= paddingPercentage * textLineRenderable.getLineHeight();

            textLineLengthThusFar += lengthThusFarAddition;
        }

        return textLineLengthThusFar;
    }

    private void validateTextLineRenderable(TextLineRenderable textLineRenderable,
                                            String methodName) {
        Check.ifNull(textLineRenderable, "textLineRenderable");
        Check.ifNull(textLineRenderable.getFont(), "textLineRenderable.getFont()");
        Check.throwOnLteZero(textLineRenderable.getLineHeight(),
                "textLineRenderable.getLineHeight()");
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
        if (textLineRenderable.italicIndices() != null) {
            Integer highestIndexThusFar = null;
            for (Integer index : textLineRenderable.italicIndices()) {
                validateIndex(index, textLineRenderable.getLineText().length(),
                        "textLineRenderable.italicIndices()", methodName,
                        highestIndexThusFar);
                highestIndexThusFar = index;
            }
        }
        if (textLineRenderable.boldIndices() != null) {
            Integer highestIndexThusFar = null;
            for (Integer index : textLineRenderable.boldIndices()) {
                validateIndex(index, textLineRenderable.getLineText().length(),
                        "textLineRenderable.italicIndices()", methodName,
                        highestIndexThusFar);
                highestIndexThusFar = index;
            }
        }
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
        public int getZ() {
            return 0;
        }

        @Override
        public void setZ(int i) {

        }

        @Override
        public float getLineHeight() {
            return 0;
        }

        @Override
        public void setLineHeight(float v) throws IllegalArgumentException {

        }

        @Override
        public float getPaddingBetweenGlyphs() {
            return 0;
        }

        @Override
        public void setPaddingBetweenGlyphs(float v) {

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
        public String getInterfaceName() {
            return TextLineRenderable.class.getCanonicalName();
        }
    };
}
