package inaugural.soliloquy.graphics.api.dto;


public class AnimationFrameDefinitionDTO {
    public String imgLoc;
    public int ms;
    public int leftX;
    public int topY;
    public int rightX;
    public int bottomY;
    public float offsetX;
    public float offsetY;

    public AnimationFrameDefinitionDTO(String imgLoc, int ms,
                                       int leftX, int topY, int rightX, int bottomY,
                                       float offsetX, float offsetY) {
        this.imgLoc = imgLoc;
        this.ms = ms;
        this.leftX = leftX;
        this.topY = topY;
        this.rightX = rightX;
        this.bottomY = bottomY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
