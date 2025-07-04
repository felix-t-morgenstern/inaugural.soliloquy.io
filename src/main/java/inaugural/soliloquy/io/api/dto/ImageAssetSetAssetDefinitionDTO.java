package inaugural.soliloquy.io.api.dto;

public class ImageAssetSetAssetDefinitionDTO {
    public int assetType;
    public String assetId;
    public DisplayParamDefinitionDTO[] displayParams;

    public ImageAssetSetAssetDefinitionDTO(int assetType, String assetId, DisplayParamDefinitionDTO... displayParams) {
        this.assetType = assetType;
        this.assetId = assetId;
        this.displayParams = displayParams;
    }

    public static class DisplayParamDefinitionDTO {
        public String name;
        public String val;

        public DisplayParamDefinitionDTO(String name, String val) {
            this.name = name;
            this.val = val;
        }
    }
}
