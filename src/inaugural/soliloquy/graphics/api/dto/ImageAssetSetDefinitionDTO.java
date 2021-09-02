package inaugural.soliloquy.graphics.api.dto;

public class ImageAssetSetDefinitionDTO {
    public String id;
    public ImageAssetSetAssetDTO[] assets;

    public ImageAssetSetDefinitionDTO(String id, ImageAssetSetAssetDTO[] assets) {
        this.id = id;
        this.assets = assets;
    }

    public static class ImageAssetSetAssetDTO {
        public String type;
        public String direction;
        public int assetType;
        public String assetId;

        public ImageAssetSetAssetDTO(String type, String direction, int assetType, String assetId) {
            this.type = type;
            this.direction = direction;
            this.assetType = assetType;
            this.assetId = assetId;
        }
    }
}
