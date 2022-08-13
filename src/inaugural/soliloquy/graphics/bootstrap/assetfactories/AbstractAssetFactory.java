package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import soliloquy.specs.graphics.assets.Asset;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AssetDefinition;

import static inaugural.soliloquy.tools.Check.*;

abstract class AbstractAssetFactory<TAssetDefinition extends AssetDefinition<TAsset>,
        TAsset extends Asset> implements AssetFactory<TAssetDefinition, TAsset> {
    void throwOnInvalidSnippetDefinition(AssetSnippet snippet, String paramName) {
        ifNull(snippet.image(), paramName + ".image()");

        ifNullOrEmpty(snippet.image().relativeLocation(),
                paramName + ".image().relativeLocation()");

        throwOnLteZero(snippet.image().width(), paramName + ".image().width()");
        throwOnLteZero(snippet.image().height(), paramName + ".image().height()");

        ifNonNegative(snippet.leftX(), paramName + ".leftX()");
        ifNonNegative(snippet.topY(), paramName + ".topY()");
        ifNonNegative(snippet.rightX(), paramName + ".rightX()");
        ifNonNegative(snippet.bottomY(), paramName + ".bottomY()");

        throwOnSecondLte(snippet.leftX(), snippet.rightX(),
                paramName + ".leftX()", paramName + ".rightX()");
        throwOnSecondLte(snippet.topY(), snippet.bottomY(),
                paramName + ".topY()", paramName + ".bottomY()");

        throwOnSecondGt(snippet.image().width(), snippet.leftX(),
                paramName + ".image().width()", paramName + ".leftX()");
        throwOnSecondGt(snippet.image().height(), snippet.topY(),
                paramName + ".image().height()", paramName + ".topY()");
        throwOnSecondGt(snippet.image().width(), snippet.rightX(),
                paramName + ".image().width()", paramName + ".rightX()");
        throwOnSecondGt(snippet.image().height(), snippet.bottomY(),
                paramName + ".image().height()", paramName + ".bottomY()");
    }
}
