package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TextLineRenderableFactoryImpl implements TextLineRenderableFactory {
    @Override
    public TextLineRenderable make(Font font, ProviderAtTime<String> lineTextProvider,
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
                                   RenderableStack containingStack)
            throws IllegalArgumentException {
        return new TextLineRenderableImpl(font, lineTextProvider, lineHeightProvider,
                justification, paddingBetweenGlyphs, colorProviderIndices, italicIndices,
                boldIndices, borderThicknessProvider, borderColorProvider,
                renderingLocationProvider, dropShadowSizeProvider, dropShadowOffsetProvider,
                dropShadowColorProvider, z, uuid, containingStack);
    }
}
