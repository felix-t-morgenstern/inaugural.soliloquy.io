package inaugural.soliloquy.graphics.api.dto;

public class AnimationDTO {
    public String id;
    public int msDur;
    public AnimationFrameDTO[] frames;

    public AnimationDTO(String id, int msDur, AnimationFrameDTO[] frames) {
        this.id = id;
        this.msDur = msDur;
        this.frames = frames;
    }

    public static class AnimationFrameDTO {
        public String imgLoc;
        public int ms;
        public int leftX;
        public int topY;
        public int rightX;
        public int bottomY;
        public float offsetX;
        public float offsetY;

        public AnimationFrameDTO (String imgLoc, int ms,
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
}
