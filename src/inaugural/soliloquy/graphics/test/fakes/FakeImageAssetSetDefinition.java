package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.List;

public class FakeImageAssetSetDefinition implements ImageAssetSetDefinition {
    public List<ImageAssetSetAssetDefinition> ImageAssetSetAssetDefinitions;
    public String Id;

    public FakeImageAssetSetDefinition(List<ImageAssetSetAssetDefinition> imageAssetSetAssetDefinitions,
                                       String id) {
        ImageAssetSetAssetDefinitions = imageAssetSetAssetDefinitions;
        Id = id;
    }

    @Override
    public List<ImageAssetSetAssetDefinition> assetDefinitions() {
        return ImageAssetSetAssetDefinitions;
    }

    @Override
    public String id() {
        return Id;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
