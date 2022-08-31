package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakeTextLineRenderable implements TextLineRenderable {
    public Font Font;
    public ProviderAtTime<Float> LineHeightProvider;
    public ProviderAtTime<String> LineTextProvider;
    public float PaddingBetweenGlyphs;
    public TextJustification Justification = TextJustification.LEFT;
    public Map<Integer, ProviderAtTime<Color>> ColorProviderIndices;
    public List<Integer> ItalicIndices;
    public List<Integer> BoldIndices;
    public ProviderAtTime<Float> BorderThicknessProvider;
    public ProviderAtTime<Color> BorderColorProvider;
    public ProviderAtTime<Vertex> RenderingLocationProvider;
    public ProviderAtTime<Float> DropShadowSizeProvider;
    public ProviderAtTime<Vertex> DropShadowOffsetProvider;
    public ProviderAtTime<Color> DropShadowColorProvider;
    public UUID Uuid;

    public FakeTextLineRenderable(Font font, ProviderAtTime<Float> lineHeightProvider,
                                  float paddingBetweenGlyphs,
                                  ProviderAtTime<String> lineTextProvider,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  UUID id) {
        Font = font;
        LineHeightProvider = lineHeightProvider;
        PaddingBetweenGlyphs = paddingBetweenGlyphs;
        LineTextProvider = lineTextProvider;
        BorderThicknessProvider = borderThicknessProvider;
        BorderColorProvider = borderColorProvider;
        ColorProviderIndices = colorProviderIndices;
        ItalicIndices = italicIndices == null ? new ArrayList<>() : italicIndices;
        BoldIndices = boldIndices == null ? new ArrayList<>() : boldIndices;
        DropShadowSizeProvider = new FakeStaticProvider<>(null);
        DropShadowOffsetProvider = new FakeStaticProvider<>(null);
        DropShadowColorProvider = new FakeStaticProvider<>(null);
        Uuid = id;
    }

    public FakeTextLineRenderable(Font font, ProviderAtTime<Float> lineHeightProvider,
                                  float paddingBetweenGlyphs, String lineText,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  UUID id) {
        this(font, lineHeightProvider, paddingBetweenGlyphs, new FakeStaticProvider<>(lineText),
                borderThicknessProvider, borderColorProvider, colorProviderIndices, italicIndices,
                boldIndices, id);
    }

    public FakeTextLineRenderable(Font font, ProviderAtTime<Float> lineHeightProvider,
                                  float paddingBetweenGlyphs, String lineText,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Vertex> renderingLocationProvider,
                                  UUID uuid) {
        this(font, lineHeightProvider, paddingBetweenGlyphs, lineText, borderThicknessProvider,
                borderColorProvider, colorProviderIndices, italicIndices, boldIndices, uuid);
        RenderingLocationProvider = renderingLocationProvider;
    }

    public FakeTextLineRenderable(Font font, ProviderAtTime<Float> lineHeightProvider,
                                  float paddingBetweenGlyphs,
                                  ProviderAtTime<String> lineTextProvider,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Vertex> renderingLocationProvider,
                                  UUID uuid) {
        this(font, lineHeightProvider, paddingBetweenGlyphs, lineTextProvider,
                borderThicknessProvider, borderColorProvider, colorProviderIndices, italicIndices,
                boldIndices, uuid);
        RenderingLocationProvider = renderingLocationProvider;
    }

    public FakeTextLineRenderable(Font font, ProviderAtTime<Float> lineHeightProvider,
                                  float paddingBetweenGlyphs, String lineText,
                                  ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Vertex> renderingLocationProvider,
                                  ProviderAtTime<Float> dropShadowSizeProvider,
                                  ProviderAtTime<Vertex> dropShadowOffsetProvider,
                                  ProviderAtTime<Color> dropShadowColorProvider,
                                  UUID uuid) {
        this(font, lineHeightProvider, paddingBetweenGlyphs, lineText, borderThicknessProvider,
                borderColorProvider, colorProviderIndices, italicIndices, boldIndices,
                renderingLocationProvider, uuid);
        DropShadowSizeProvider = dropShadowSizeProvider;
        DropShadowOffsetProvider = dropShadowOffsetProvider;
        DropShadowColorProvider = dropShadowColorProvider;
    }

    @Override
    public Font getFont() {
        return Font;
    }

    @Override
    public void setFont(Font font) throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Float> lineHeightProvider() {
        return LineHeightProvider;
    }

    @Override
    public void setLineHeightProvider(ProviderAtTime v) throws IllegalArgumentException {

    }

    @Override
    public float getPaddingBetweenGlyphs() {
        return PaddingBetweenGlyphs;
    }

    @Override
    public void setPaddingBetweenGlyphs(float v) {

    }

    @Override
    public TextJustification getJustification() {
        return Justification;
    }

    @Override
    public void setJustification(TextJustification justification) throws IllegalArgumentException {
        Justification = justification;
    }

    @Override
    public ProviderAtTime<String> getLineTextProvider() {
        return LineTextProvider;
    }

    @Override
    public void setLineTextProvider(ProviderAtTime<String> s) throws IllegalArgumentException {

    }

    @Override
    public Map<Integer, ProviderAtTime<Color>> colorProviderIndices() {
        return ColorProviderIndices;
    }

    @Override
    public List<Integer> italicIndices() {
        return ItalicIndices;
    }

    @Override
    public List<Integer> boldIndices() {
        return BoldIndices;
    }

    @Override
    public ProviderAtTime<Float> dropShadowSizeProvider() {
        return DropShadowSizeProvider;
    }

    @Override
    public void setDropShadowSizeProvider(ProviderAtTime<Float> dropShadowSizeProvider)
            throws IllegalArgumentException {
        DropShadowSizeProvider = dropShadowSizeProvider;
    }

    @Override
    public ProviderAtTime<Vertex> dropShadowOffsetProvider() {
        return DropShadowOffsetProvider;
    }

    @Override
    public void setDropShadowOffsetProvider(ProviderAtTime<Vertex>
                                                    dropShadowOffsetProvider)
            throws IllegalArgumentException {
        DropShadowOffsetProvider = dropShadowOffsetProvider;
    }

    @Override
    public ProviderAtTime<Color> dropShadowColorProvider() {
        return DropShadowColorProvider;
    }

    @Override
    public void setDropShadowColorProvider(ProviderAtTime<Color> dropShadowColorProvider)
            throws IllegalArgumentException {
        DropShadowColorProvider = dropShadowColorProvider;
    }

    @Override
    public ProviderAtTime<Vertex> getRenderingLocationProvider() {
        return RenderingLocationProvider;
    }

    @Override
    public void setRenderingLocationProvider(ProviderAtTime<Vertex>
                                                     renderingLocationProvider)
            throws IllegalArgumentException {

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
    public String getInterfaceName() {
        return null;
    }

    @Override
    public UUID uuid() {
        return Uuid;
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return BorderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime)
            throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return BorderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime)
            throws IllegalArgumentException {

    }
}
