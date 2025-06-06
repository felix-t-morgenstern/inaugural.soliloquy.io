package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FakeImageAssetSetFactory
        implements AssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    public final Map<String, ImageAssetSetDefinition> INPUTS = mapOf();
    public final List<ImageAssetSet> OUTPUTS = listOf();

    @Override
    public ImageAssetSet make(ImageAssetSetDefinition imageAssetSetDefinition)
            throws IllegalArgumentException {
        INPUTS.put(imageAssetSetDefinition.id(), imageAssetSetDefinition);
        ImageAssetSet createdImageAssetSet = new FakeImageAssetSet(imageAssetSetDefinition.id());
        OUTPUTS.add(createdImageAssetSet);
        return createdImageAssetSet;
    }
}
