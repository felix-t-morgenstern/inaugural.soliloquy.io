package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeImageAssetSetFactory
        implements AssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    public final Map<String, ImageAssetSetDefinition> INPUTS = new HashMap<>();
    public final List<ImageAssetSet> OUTPUTS = new ArrayList<>();

    @Override
    public ImageAssetSet make(ImageAssetSetDefinition imageAssetSetDefinition)
            throws IllegalArgumentException {
        INPUTS.put(imageAssetSetDefinition.id(), imageAssetSetDefinition);
        ImageAssetSet createdImageAssetSet = new FakeImageAssetSet(imageAssetSetDefinition.id());
        OUTPUTS.add(createdImageAssetSet);
        return createdImageAssetSet;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
