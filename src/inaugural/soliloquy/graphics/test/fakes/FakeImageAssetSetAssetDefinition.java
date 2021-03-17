package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;

public class FakeImageAssetSetAssetDefinition implements ImageAssetSetAssetDefinition {
    public String Type;
    public String Direction;
    public ImageAsset.ImageAssetType AssetType;
    public String AssetId;

    public FakeImageAssetSetAssetDefinition(String type, String direction,
                                            ImageAsset.ImageAssetType assetType, String assetId) {
        Type = type;
        Direction = direction;
        AssetType = assetType;
        AssetId = assetId;
    }

    @Override
    public String type() {
        return Type;
    }

    @Override
    public String direction() {
        return Direction;
    }

    @Override
    public ImageAsset.ImageAssetType assetType() {
        return AssetType;
    }

    @Override
    public String assetId() {
        return AssetId;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
