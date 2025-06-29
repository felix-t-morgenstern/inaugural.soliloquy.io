package inaugural.soliloquy.io.api.dto;

public class ImageAssetSetDefinitionDTO {
    public String id;
    public ImageAssetSetAssetDefinitionDTO[] assets;

    public ImageAssetSetDefinitionDTO(String id, ImageAssetSetAssetDefinitionDTO[] assets) {
        this.id = id;
        this.assets = assets;
    }
}
