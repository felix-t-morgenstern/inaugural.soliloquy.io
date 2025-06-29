package inaugural.soliloquy.io.api.dto;

public class AnimationDefinitionDTO {
    public String id;
    public int msDur;
    public AnimationFrameDefinitionDTO[] frames;

    public AnimationDefinitionDTO(String id, int msDur, AnimationFrameDefinitionDTO[] frames) {
        this.id = id;
        this.msDur = msDur;
        this.frames = frames;
    }
}
