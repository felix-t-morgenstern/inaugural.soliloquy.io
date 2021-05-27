package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class FakeTextLineRenderable implements TextLineRenderable {
    public Font Font;
    public float LineHeight;
    public String LineText;
    public float PaddingBetweenGlyphs;
    public Map<Integer, ProviderAtTime<Color>> ColorProviderIndices;
    public List<Integer> ItalicIndices;
    public List<Integer> BoldIndices;
    public ProviderAtTime<FloatBox> RenderingAreaProvider;
    public EntityUuid Uuid;

    public FakeTextLineRenderable(Font font, float lineHeight, float paddingBetweenGlyphs,
                                  String lineText,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  EntityUuid id) {
        Font = font;
        LineHeight = lineHeight;
        PaddingBetweenGlyphs = paddingBetweenGlyphs;
        LineText = lineText;
        ColorProviderIndices = colorProviderIndices;
        ItalicIndices = italicIndices;
        BoldIndices = boldIndices;
        Uuid = id;
    }

    public FakeTextLineRenderable(Font font, float lineHeight, float paddingBetweenGlyphs,
                                  String lineText,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<FloatBox> renderingAreaProvider,
                                  EntityUuid uuid) {
        this(font, lineHeight, paddingBetweenGlyphs, lineText, colorProviderIndices, italicIndices,
                boldIndices, uuid);
        RenderingAreaProvider = renderingAreaProvider;
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
    public ProviderAtTime<FloatBox> getRenderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public void setRenderingAreaProvider(ProviderAtTime<FloatBox> providerAtTime)
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
}
