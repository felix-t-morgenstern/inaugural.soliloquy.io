package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class TextLineRenderableImpl extends AbstractRenderable implements TextLineRenderable {
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES;
    private final List<Integer> ITALIC_INDICES;
    private final List<Integer> BOLD_INDICES;

    private Font font;
    private ProviderAtTime<String> lineTextProvider;
    private ProviderAtTime<Float> lineHeightProvider;
    private TextJustification justification;
    private ProviderAtTime<Vertex> renderingLocationProvider;
    private float paddingBetweenGlyphs;
    private ProviderAtTime<Float> borderThicknessProvider;
    private ProviderAtTime<Color> borderColorProvider;
    private ProviderAtTime<Float> dropShadowSizeProvider;
    private ProviderAtTime<Vertex> dropShadowOffsetProvider;
    private ProviderAtTime<Color> dropShadowColorProvider;

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
        COLOR_PROVIDER_INDICES = mapOf(colorProviderIndices);
        ITALIC_INDICES = listOf(Check.ifNull(italicIndices, "italicIndices"));
        BOLD_INDICES = listOf(Check.ifNull(boldIndices, "boldIndices"));
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) throws IllegalArgumentException {
        this.font = Check.ifNull(font, "font");
    }

    @Override
    public ProviderAtTime<String> getLineTextProvider() {
        return lineTextProvider;
    }

    @Override
    public void setLineTextProvider(ProviderAtTime<String> lineTextProvider)
            throws IllegalArgumentException {
        this.lineTextProvider = Check.ifNull(lineTextProvider, "lineTextProvider");
    }

    @Override
    public ProviderAtTime<Vertex> getRenderingLocationProvider() {
        return renderingLocationProvider;
    }

    @Override
    public void setRenderingLocationProvider(ProviderAtTime<Vertex>
                                                     renderingLocationProvider)
            throws IllegalArgumentException {
        this.renderingLocationProvider = Check.ifNull(renderingLocationProvider,
                "renderingLocationProvider");
    }

    @Override
    public ProviderAtTime<Float> lineHeightProvider() {
        return lineHeightProvider;
    }

    @Override
    public void setLineHeightProvider(ProviderAtTime<Float> lineHeightProvider)
            throws IllegalArgumentException {
        this.lineHeightProvider = Check.ifNull(lineHeightProvider, "lineHeightProvider");
    }

    @Override
    public float getPaddingBetweenGlyphs() {
        return paddingBetweenGlyphs;
    }

    @Override
    public void setPaddingBetweenGlyphs(float paddingBetweenGlyphs) {
        this.paddingBetweenGlyphs = paddingBetweenGlyphs;
    }

    @Override
    public TextJustification getJustification() {
        return justification;
    }

    @Override
    public void setJustification(TextJustification justification) throws IllegalArgumentException {
        Check.ifNull(justification, "justification");
        if (justification == TextJustification.UNKNOWN) {
            throw new IllegalArgumentException(
                    "TextLineRenderableImpl.setJustification: justification cannot be UNKNOWN");
        }
        this.justification = justification;
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
        return dropShadowSizeProvider;
    }

    @Override
    public void setDropShadowSizeProvider(ProviderAtTime<Float> dropShadowSizeProvider)
            throws IllegalArgumentException {
        this.dropShadowSizeProvider = Check.ifNull(dropShadowSizeProvider, "dropShadowSizeProvider");
    }

    @Override
    public ProviderAtTime<Vertex> dropShadowOffsetProvider() {
        return dropShadowOffsetProvider;
    }

    @Override
    public void setDropShadowOffsetProvider(ProviderAtTime<Vertex>
                                                    dropShadowOffsetProvider)
            throws IllegalArgumentException {
        this.dropShadowOffsetProvider = Check.ifNull(dropShadowOffsetProvider,
                "dropShadowOffsetProvider");
    }

    @Override
    public ProviderAtTime<Color> dropShadowColorProvider() {
        return dropShadowColorProvider;
    }

    @Override
    public void setDropShadowColorProvider(ProviderAtTime<Color> dropShadowColorProvider)
            throws IllegalArgumentException {
        this.dropShadowColorProvider = Check.ifNull(dropShadowColorProvider,
                "dropShadowColorProvider");
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        if (borderThicknessProvider != null && borderColorProvider == null) {
            throw new IllegalArgumentException("TextLineRenderableImpl.setBorderColorProvider: " +
                    "cannot set borderThicknessProvider to non-null while borderColorProvider " +
                    "is null");
        }
        this.borderThicknessProvider = borderThicknessProvider;
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        if (borderThicknessProvider != null && borderColorProvider == null) {
            throw new IllegalArgumentException("TextLineRenderableImpl.setBorderColorProvider: " +
                    "cannot set borderColorProvider to null while borderThicknessProvider is " +
                    "non-null");
        }
        this.borderColorProvider = borderColorProvider;
    }
}
