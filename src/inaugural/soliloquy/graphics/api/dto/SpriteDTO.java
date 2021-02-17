package inaugural.soliloquy.graphics.api.dto;

public class SpriteDTO {
    public SpriteDTO (String id, String imgLoc, int leftX, int topY, int rightX, int bottomY) {
        this.id = id;
        this.imgLoc = imgLoc;
        this.leftX = leftX;
        this.topY = topY;
        this.rightX = rightX;
        this.bottomY = bottomY;
    }

    public String id;
    public String imgLoc;
    public int leftX;
    public int topY;
    public int rightX;
    public int bottomY;
}
