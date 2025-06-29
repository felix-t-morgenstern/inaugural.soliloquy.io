package inaugural.soliloquy.io.api.dto;

public class GlobalLoopingAnimationDefinitionDTO {
    public String id;
    public String animationId;
    public int periodModuloOffset;

    public GlobalLoopingAnimationDefinitionDTO(String id, String animationId,
                                               int periodModuloOffset) {
        this.id = id;
        this.animationId = animationId;
        this.periodModuloOffset = periodModuloOffset;
    }
}
