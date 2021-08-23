package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextJustification;
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
    private ProviderAtTime<Float> _lineHeightProvider;
    private TextJustification _justification;
    private ProviderAtTime<Pair<Float,Float>> _renderingLocationProvider;
    private float _paddingBetweenGlyphs;
    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;

    public TextLineRenderableImpl(Font font, String lineText,
                                  ProviderAtTime<Float> lineHeightProvider,
                                  TextJustification justification, float paddingBetweenGlyphs,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  ProviderAtTime<Pair<Float,Float>> renderingLocationProvider,
                                  int z, EntityUuid uuid,
                                  Consumer<Renderable> updateZIndexInContainer,
                                  Consumer<Renderable> removeFromContainer) {
        super(z, uuid, updateZIndexInContainer, removeFromContainer);
        setFont(font);
        setLineText(lineText);
        setJustification(justification);
        setLineHeightProvider(lineHeightProvider);
        setRenderingLocationProvider(renderingLocationProvider);
        setPaddingBetweenGlyphs(paddingBetweenGlyphs);
        setBorderColorProvider(borderColorProvider);
        setBorderThicknessProvider(borderThicknessProvider);
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
    public ProviderAtTime<Float> lineHeightProvider() {
        return _lineHeightProvider;
    }

    @Override
    public void setLineHeightProvider(ProviderAtTime<Float> lineHeightProvider)
            throws IllegalArgumentException {
        _lineHeightProvider = Check.ifNull(lineHeightProvider, "lineHeightProvider");
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
    public TextJustification getJustification() {
        return _justification;
    }

    @Override
    public void setJustification(TextJustification justification) throws IllegalArgumentException {
        Check.ifNull(justification, "justification");
        if (justification == TextJustification.UNKNOWN) {
            throw new IllegalArgumentException(
                    "TextLineRenderableImpl.setJustification: justification cannot be UNKNOWN");
        }
        _justification = justification;
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

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return _borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        if (borderThicknessProvider != null && _borderColorProvider == null) {
            throw new IllegalArgumentException("TextLineRenderableImpl.setBorderColorProvider: " +
                    "cannot set borderThicknessProvider to non-null while borderColorProvider " +
                    "is null");
        }
        _borderThicknessProvider = borderThicknessProvider;
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return _borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        if (_borderThicknessProvider != null && borderColorProvider == null) {
            throw new IllegalArgumentException("TextLineRenderableImpl.setBorderColorProvider: " +
                    "cannot set borderColorProvider to null while borderThicknessProvider is " +
                    "non-null");
        }
        _borderColorProvider = borderColorProvider;
    }
}
