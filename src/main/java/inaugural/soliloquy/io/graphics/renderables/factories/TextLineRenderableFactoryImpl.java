package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TextLineRenderableFactoryImpl implements TextLineRenderableFactory {
    @Override
    public TextLineRenderable make(Font font, ProviderAtTime<String> lineTextProvider,
                                   ProviderAtTime<Vertex> locationProvider,
                                   ProviderAtTime<Float> lineHeightProvider,
                                   HorizontalAlignment alignment, float paddingBetweenGlyphs,
                                   Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                   List<Integer> italicIndices, List<Integer> boldIndices,
                                   ProviderAtTime<Float> borderThicknessProvider,
                                   ProviderAtTime<Color> borderColorProvider,
                                   ProviderAtTime<Float> dropShadowSizeProvider,
                                   ProviderAtTime<Vertex> dropShadowOffsetProvider,
                                   ProviderAtTime<Color> dropShadowColorProvider,
                                   int z, UUID uuid,
                                   Component component)
            throws IllegalArgumentException {
        return new TextLineRenderableImpl(font, lineTextProvider, lineHeightProvider,
                alignment, paddingBetweenGlyphs, colorProviderIndices, italicIndices,
                boldIndices, borderThicknessProvider, borderColorProvider,
                locationProvider, dropShadowSizeProvider, dropShadowOffsetProvider,
                dropShadowColorProvider, z, uuid, component);
    }
}
