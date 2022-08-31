package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.*;

public class TextLineRenderableImpl extends AbstractRenderable implements TextLineRenderable {
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES;
    private final List<Integer> ITALIC_INDICES;
    private final List<Integer> BOLD_INDICES;

    private Font _font;
    private ProviderAtTime<String> _lineTextProvider;
    private ProviderAtTime<Float> _lineHeightProvider;
    private TextJustification _justification;
    private ProviderAtTime<Vertex> _renderingLocationProvider;
    private float _paddingBetweenGlyphs;
    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;
    private ProviderAtTime<Float> _dropShadowSizeProvider;
    private ProviderAtTime<Vertex> _dropShadowOffsetProvider;
    private ProviderAtTime<Color> _dropShadowColorProvider;

    /** @noinspection ConstantConditions */
    public TextLineRenderableImpl(Font font, ProviderAtTime<String> lineTextProvider,
                                  ProviderAtTime<Float> lineHeightProvider,
                                  TextJustification justification, float paddingBetweenGlyphs,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  ProviderAtTime<Vertex> renderingLocationProvider,
                                  ProviderAtTime<Float> dropShadowSizeProvider,
                                  ProviderAtTime<Vertex> dropShadowOffsetProvider,
                                  ProviderAtTime<Color> dropShadowColorProvider,
                                  int z, UUID uuid,
                                  RenderableStack containingStack) {
        super(z, uuid, containingStack);
        setFont(font);
        this.setLineTextProvider(lineTextProvider);
        setJustification(justification);
        setLineHeightProvider(lineHeightProvider);
        setRenderingLocationProvider(renderingLocationProvider);
        setPaddingBetweenGlyphs(paddingBetweenGlyphs);
        setBorderColorProvider(borderColorProvider);
        setBorderThicknessProvider(borderThicknessProvider);
        setDropShadowSizeProvider(dropShadowSizeProvider);
        setDropShadowOffsetProvider(dropShadowOffsetProvider);
        setDropShadowColorProvider(dropShadowColorProvider);
        COLOR_PROVIDER_INDICES = new HashMap<>(colorProviderIndices);
        ITALIC_INDICES = new ArrayList<>(Check.ifNull(italicIndices, "italicIndices"));
        BOLD_INDICES = new ArrayList<>(Check.ifNull(boldIndices, "boldIndices"));
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
    public ProviderAtTime<String> getLineTextProvider() {
        return _lineTextProvider;
    }

    @Override
    public void setLineTextProvider(ProviderAtTime<String> lineTextProvider)
            throws IllegalArgumentException {
        _lineTextProvider = Check.ifNull(lineTextProvider, "lineTextProvider");
    }

    @Override
    public ProviderAtTime<Vertex> getRenderingLocationProvider() {
        return _renderingLocationProvider;
    }

    @Override
    public void setRenderingLocationProvider(ProviderAtTime<Vertex>
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
    public ProviderAtTime<Float> dropShadowSizeProvider() {
        return _dropShadowSizeProvider;
    }

    @Override
    public void setDropShadowSizeProvider(ProviderAtTime<Float> dropShadowSizeProvider)
            throws IllegalArgumentException {
        _dropShadowSizeProvider = Check.ifNull(dropShadowSizeProvider, "dropShadowSizeProvider");
    }

    @Override
    public ProviderAtTime<Vertex> dropShadowOffsetProvider() {
        return _dropShadowOffsetProvider;
    }

    @Override
    public void setDropShadowOffsetProvider(ProviderAtTime<Vertex>
                                                    dropShadowOffsetProvider)
            throws IllegalArgumentException {
        _dropShadowOffsetProvider = Check.ifNull(dropShadowOffsetProvider,
                "dropShadowOffsetProvider");
    }

    @Override
    public ProviderAtTime<Color> dropShadowColorProvider() {
        return _dropShadowColorProvider;
    }

    @Override
    public void setDropShadowColorProvider(ProviderAtTime<Color> dropShadowColorProvider)
            throws IllegalArgumentException {
        _dropShadowColorProvider = Check.ifNull(dropShadowColorProvider,
                "dropShadowColorProvider");
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
