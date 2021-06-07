package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeTextLineRenderable implements TextLineRenderable {
    public Font Font;
    public float LineHeight;
    public String LineText;
    public float PaddingBetweenGlyphs;
    public TextJustification Justification = TextJustification.LEFT;
    public Map<Integer, ProviderAtTime<Color>> ColorProviderIndices;
    public List<Integer> ItalicIndices;
    public List<Integer> BoldIndices;
    public ProviderAtTime<Float> BorderThicknessProvider;
    public ProviderAtTime<Color> BorderColorProvider;
    public ProviderAtTime<Pair<Float,Float>> RenderingLocationProvider;
    public EntityUuid Uuid;

    public FakeTextLineRenderable(Font font, float lineHeight, float paddingBetweenGlyphs,
                                  String lineText, ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  EntityUuid id) {
        Font = font;
        LineHeight = lineHeight;
        PaddingBetweenGlyphs = paddingBetweenGlyphs;
        LineText = lineText;
        BorderThicknessProvider = borderThicknessProvider;
        BorderColorProvider = borderColorProvider;
        ColorProviderIndices = colorProviderIndices;
        ItalicIndices = italicIndices == null ? new ArrayList<>() : italicIndices;
        BoldIndices = boldIndices == null ? new ArrayList<>() : boldIndices;
        Uuid = id;
    }

    public FakeTextLineRenderable(Font font, float lineHeight, float paddingBetweenGlyphs,
                                  String lineText, ProviderAtTime<Float> borderThicknessProvider,
                                  ProviderAtTime<Color> borderColorProvider,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<Pair<Float,Float>> renderingLocationProvider,
                                  EntityUuid uuid) {
        this(font, lineHeight, paddingBetweenGlyphs, lineText, borderThicknessProvider,
                borderColorProvider, colorProviderIndices, italicIndices, boldIndices, uuid);
        RenderingLocationProvider = renderingLocationProvider;
    }

    @Override
    public Font getFont() {
        return Font;
    }

    @Override
    public void setFont(Font font) throws IllegalArgumentException {

    }

    @Override
    public float getLineHeight() {
        return LineHeight;
    }

    @Override
    public void setLineHeight(float v) throws IllegalArgumentException {

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
    public String getLineText() {
        return LineText;
    }

    @Override
    public void setLineText(String s) throws IllegalArgumentException {

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
    public ProviderAtTime<Pair<Float,Float>> getRenderingLocationProvider() {
        return RenderingLocationProvider;
    }

    @Override
    public void setRenderingLocationProvider(ProviderAtTime<Pair<Float,Float>>
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
    public void delete() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public EntityUuid uuid() {
        return Uuid;
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return BorderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> providerAtTime) throws IllegalArgumentException {

    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return BorderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> providerAtTime) throws IllegalArgumentException {

    }
}
