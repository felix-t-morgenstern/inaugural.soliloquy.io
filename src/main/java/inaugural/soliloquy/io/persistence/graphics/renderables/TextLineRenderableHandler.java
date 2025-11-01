package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class TextLineRenderableHandler extends AbstractTypeHandler<TextLineRenderable> {
    private final Function<String, Font> GET_FONT;
    private final ProviderHandler PROVIDER_HANDLER;
    private final TextLineRenderableFactory FACTORY;

    public TextLineRenderableHandler(Function<String, Font> getFont,
                                     ProviderHandler providerHandler,
                                     TextLineRenderableFactory factory) {
        GET_FONT = Check.ifNull(getFont, "getFont");
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public TextLineRenderable read(String writtenVal)
            throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        var font = GET_FONT.apply(dto.fontId);
        var text = PROVIDER_HANDLER.read(dto.text);
        var loc = PROVIDER_HANDLER.read(dto.loc);
        var height = PROVIDER_HANDLER.read(dto.height);
        var alignment = HorizontalAlignment.fromValue(dto.just);
        var padding = dto.padding;
        @SuppressWarnings("unchecked") var colors = mapOf(Arrays.stream(dto.colors)
                .map(c -> pairOf(c.index, (ProviderAtTime<Color>) PROVIDER_HANDLER.read(c.color))));
        var italics = Arrays.stream(dto.italic).boxed().toList();
        var bolds = Arrays.stream(dto.bold).boxed().toList();
        var borderThickness = PROVIDER_HANDLER.read(dto.borderThickness);
        var borderColor = PROVIDER_HANDLER.read(dto.borderColor);
        var shadowSize = PROVIDER_HANDLER.read(dto.shadowSize);
        var shadowOffset = PROVIDER_HANDLER.read(dto.shadowOffset);
        var shadowColor = PROVIDER_HANDLER.read(dto.shadowColor);

        return FACTORY.make(
                font,
                text,
                loc,
                height,
                alignment,
                padding,
                colors,
                italics,
                bolds,
                borderThickness,
                borderColor,
                shadowSize,
                shadowOffset,
                shadowColor,
                dto.z,
                UUID.fromString(dto.uuid),
                null
        );
    }

    @Override
    public String write(TextLineRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new Dto();

        dto.fontId = renderable.getFont().id();
        dto.text = PROVIDER_HANDLER.write(renderable.getLineTextProvider());
        dto.loc = PROVIDER_HANDLER.write(renderable.getRenderingLocationProvider());
        dto.height = PROVIDER_HANDLER.write(renderable.lineHeightProvider());
        dto.just = renderable.getAlignment().getValue();
        dto.padding = renderable.getPaddingBetweenGlyphs();
        dto.colors = renderable.colorProviderIndices().entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey)).map(e -> {
                    var colorIndex = new Dto.ColorIndex();
                    colorIndex.index = e.getKey();
                    colorIndex.color = PROVIDER_HANDLER.write(e.getValue());
                    return colorIndex;
                }).toArray(Dto.ColorIndex[]::new);
        dto.italic = renderable.italicIndices().stream().mapToInt(i -> i).toArray();
        dto.bold = renderable.boldIndices().stream().mapToInt(i -> i).toArray();
        dto.borderThickness = PROVIDER_HANDLER.write(renderable.getBorderThicknessProvider());
        dto.borderColor = PROVIDER_HANDLER.write(renderable.getBorderColorProvider());
        dto.shadowSize = PROVIDER_HANDLER.write(renderable.dropShadowSizeProvider());
        dto.shadowOffset = PROVIDER_HANDLER.write(renderable.dropShadowOffsetProvider());
        dto.shadowColor = PROVIDER_HANDLER.write(renderable.dropShadowColorProvider());
        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();

        return JSON.toJson(dto);
    }

    private static class Dto {
        String fontId;
        String text;
        String loc;
        String height;
        int just;
        float padding;
        ColorIndex[] colors;
        int[] italic;
        int[] bold;
        String borderThickness;
        String borderColor;
        String shadowSize;
        String shadowOffset;
        String shadowColor;
        int z;
        String uuid;

        private static class ColorIndex {
            int index;
            String color;
        }
    }
}
