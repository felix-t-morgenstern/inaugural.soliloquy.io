package inaugural.soliloquy.graphics.api.dto;

public class SpriteSetDTO {
    public String id;
    public SpriteSetSnippetDTO[] snippets;

    public SpriteSetDTO(String id, SpriteSetSnippetDTO[] snippets) {
        this.id = id;
        this.snippets = snippets;
    }

    public static class SpriteSetSnippetDTO {
        public String imgLoc;
        public String type;
        public String direction;
        public int leftX;
        public int topY;
        public int rightX;
        public int bottomY;

        public SpriteSetSnippetDTO(String imgLoc, String type, String direction,
                                   int leftX, int topY, int rightX, int bottomY) {
            this.imgLoc = imgLoc;
            this.type = type;
            this.direction = direction;
            this.leftX = leftX;
            this.topY = topY;
            this.rightX = rightX;
            this.bottomY = bottomY;
        }
    }
}
