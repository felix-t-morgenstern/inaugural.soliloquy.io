package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TextLineRenderableImpl extends AbstractRenderable implements TextLineRenderable {
    private final Font FONT;
    private final String LINE_TEXT;
    private final float LINE_HEIGHT;
    private final float PADDING_BETWEEN_GLYPHS;
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES;
    private final List<Integer> ITALIC_INDICES;
    private final List<Integer> BOLD_INDICES;

    public TextLineRenderableImpl(Font font, String lineText, float lineHeight,
                                  float paddingBetweenGlyphs,
                                  Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                  List<Integer> italicIndices, List<Integer> boldIndices,
                                  ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                  EntityUuid uuid,  Consumer<Renderable> removeFromContainer) {
        super(renderingAreaProvider, z, uuid, removeFromContainer);
        FONT = Check.ifNull(font, "font");
        LINE_TEXT = Check.ifNull(lineText, "lineText");
        LINE_HEIGHT = Check.throwOnLteZero(lineHeight, "lineHeight");
        PADDING_BETWEEN_GLYPHS = paddingBetweenGlyphs;
        COLOR_PROVIDER_INDICES = colorProviderIndices;
        ITALIC_INDICES = italicIndices;
        BOLD_INDICES = boldIndices;
    }

    @Override
    public Font font() {
        return FONT;
    }

    @Override
    public String lineText() {
        return LINE_TEXT;
    }

    @Override
    public float lineHeight() {
        return LINE_HEIGHT;
    }

    @Override
    public float paddingBetweenGlyphs() {
        return PADDING_BETWEEN_GLYPHS;
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
