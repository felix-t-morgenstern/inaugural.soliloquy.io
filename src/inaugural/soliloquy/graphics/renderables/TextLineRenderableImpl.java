package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TextLineRenderableImpl extends AbstractRenderable implements TextLineRenderable {
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES;
    private final List<Integer> ITALIC_INDICES;
    private final List<Integer> BOLD_INDICES;

    private Font _font;
    private String _lineText;
    private float _lineHeight;
    private ProviderAtTime<Pair<Float,Float>> _renderingLocationProvider;
    private float _paddingBetweenGlyphs;

    public TextLineRenderableImpl(Font font, String lineText, float lineHeight,
                                  float paddingBetweenGlyphs,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Pair<Float,Float>> renderingLocationProvider,
                                  int z, EntityUuid uuid,
                                  Consumer<Renderable> updateZIndexInContainer,
                                  Consumer<Renderable> removeFromContainer) {
        super(z, uuid, updateZIndexInContainer, removeFromContainer);
        setFont(font);
        setLineText(lineText);
        setLineHeight(lineHeight);
        setRenderingLocationProvider(renderingLocationProvider);
        setPaddingBetweenGlyphs(paddingBetweenGlyphs);
        COLOR_PROVIDER_INDICES = colorProviderIndices;
        ITALIC_INDICES = Check.ifNull(italicIndices, "italicIndices");
        BOLD_INDICES = Check.ifNull(boldIndices, "boldIndices");
    }

    @Override
    public Font getFont() {
        return _font;
    }

    @Override
    public void setFont(Font font) throws IllegalArgumentException {
        _font = Check.ifNull(font, "font");
    }

    @Override
    public String getLineText() {
        return _lineText;
    }

    @Override
    public void setLineText(String lineText) throws IllegalArgumentException {
        _lineText = Check.ifNull(lineText, "lineText");
    }

    @Override
    public ProviderAtTime<Pair<Float,Float>> getRenderingLocationProvider() {
        return _renderingLocationProvider;
    }

    @Override
    public void setRenderingLocationProvider(ProviderAtTime<Pair<Float,Float>>
                                                         renderingLocationProvider)
            throws IllegalArgumentException {
        _renderingLocationProvider = Check.ifNull(renderingLocationProvider,
                "renderingLocationProvider");
    }

    @Override
    public float getLineHeight() {
        return _lineHeight;
    }

    @Override
    public void setLineHeight(float lineHeight) throws IllegalArgumentException {
        _lineHeight = Check.throwOnLteZero(lineHeight, "lineHeight");
    }

    @Override
    public float getPaddingBetweenGlyphs() {
        return _paddingBetweenGlyphs;
    }

    @Override
    public void setPaddingBetweenGlyphs(float paddingBetweenGlyphs) {
        _paddingBetweenGlyphs = paddingBetweenGlyphs;
    }

    @Override
    public Map<Integer, ProviderAtTime<Color>> colorProviderIndices() {
        return COLOR_PROVIDER_INDICES;
    }

    @Override
    public List<Integer> italicIndices() {
        return ITALIC_INDICES;
    }

    @Override
    public List<Integer> boldIndices() {
        return BOLD_INDICES;
    }

    @Override
    public String getInterfaceName() {
        return TextLineRenderable.class.getCanonicalName();
    }
}
