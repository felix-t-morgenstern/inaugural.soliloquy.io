package inaugural.soliloquy.io.api.dto;

public class AnimatedMouseCursorDefinitionDTO {
    public String Id;
    public AnimatedMouseCursorFrameDefinitionDTO[] Frames;
    public int Duration;
    public int Offset;
    public Long Paused;

    public AnimatedMouseCursorDefinitionDTO(String id,
                                            AnimatedMouseCursorFrameDefinitionDTO[] frames,
                                            int duration,
                                            int offset,
                                            Long paused) {
        Id = id;
        Frames = frames;
        Duration = duration;
        Offset = offset;
        Paused = paused;
    }
}
