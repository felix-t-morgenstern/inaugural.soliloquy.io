package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

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
                                   Consumer<Renderable> updateZIndexInContainer,
                                   Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new TextLineRenderableImpl(font, lineTextProvider, lineHeightProvider,
                justification, paddingBetweenGlyphs, colorProviderIndices, italicIndices,
                boldIndices, borderThicknessProvider, borderColorProvider,
                renderingLocationProvider, dropShadowSizeProvider, dropShadowOffsetProvider,
                dropShadowColorProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return TextLineRenderableFactory.class.getCanonicalName();
    }
}
