package inaugural.soliloquy.io.api.dto;

public class ImageAssetSetAssetDefinitionDTO {
    public String type;
    public Integer direction;
    public int assetType;
    public String assetId;

    public ImageAssetSetAssetDefinitionDTO(String type, Integer direction, int assetType,
                                           String assetId) {
        this.type = type;
        this.direction = direction;
        this.assetType = assetType;
        this.assetId = assetId;
    }
}
