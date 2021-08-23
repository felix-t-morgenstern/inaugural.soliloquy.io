package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TextLineRenderableFactoryImpl implements TextLineRenderableFactory {
    @Override
    public TextLineRenderable make(Font font, String lineText,
                                   ProviderAtTime<Float> lineHeightProvider,
                                   TextJustification justification, float paddingBetweenGlyphs,
                                   Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
                                   List<Integer> italicIndices, List<Integer> boldIndices,
                                   ProviderAtTime<Float> borderThicknessProvider,
                                   ProviderAtTime<Color> borderColorProvider,
                                   ProviderAtTime<Pair<Float,Float>> renderingLocationProvider,
                                   int z, EntityUuid uuid,
                                   Consumer<Renderable> updateZIndexInContainer,
                                   Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new TextLineRenderableImpl(font, lineText, lineHeightProvider, justification,
                paddingBetweenGlyphs, colorProviderIndices, italicIndices, boldIndices,
                borderThicknessProvider, borderColorProvider, renderingLocationProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return TextLineRenderableFactory.class.getCanonicalName();
    }
}
