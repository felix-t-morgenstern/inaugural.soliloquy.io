package inaugural.soliloquy.graphics.api.dto;

public class ImageAssetSetAssetDefinitionDTO {
    public String type;
    public String direction;
    public int assetType;
    public String assetId;

    public ImageAssetSetAssetDefinitionDTO(String type, String direction, int assetType,
                                           String assetId) {
        this.type = type;
        this.direction = direction;
        this.assetType = assetType;
        this.assetId = assetId;
    }
}
