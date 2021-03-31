package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class FontFactory implements AssetFactory<FontDefinition, Font> {
    private final int IMAGE_WIDTH;
    private final int IMAGE_HEIGHT;
    private final FloatBoxFactory FLOAT_BOX_FACTORY;

    public FontFactory(int imageWidth, int imageHeight, FloatBoxFactory floatBoxFactory) {
        IMAGE_WIDTH = Check.throwOnLteZero(imageWidth, "imageWidth");
        IMAGE_HEIGHT = Check.throwOnLteZero(imageHeight, "imageHeight");
        FLOAT_BOX_FACTORY = Check.ifNull(floatBoxFactory, "floatBoxFactory");
    }

    @Override
    public Font make(FontDefinition fontDefinition) throws IllegalArgumentException {
        return new FontImpl(fontDefinition.id(),
                fontDefinition.relativeLocation(),
                fontDefinition.maxLosslessFontSize(),
                fontDefinition.additionalGlyphPadding(),
                IMAGE_WIDTH,
                IMAGE_HEIGHT,
                FLOAT_BOX_FACTORY);
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                FontDefinition.class.getCanonicalName() + "," + Font.class.getCanonicalName() +
                ">";
    }
}
