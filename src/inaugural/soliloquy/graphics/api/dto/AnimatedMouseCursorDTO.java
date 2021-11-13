package inaugural.soliloquy.graphics.api.dto;

public class AnimatedMouseCursorDTO {
    public String Id;
    public AnimatedMouseCursorFrameDTO[] Frames;
    public int Duration;
    public int Offset;
    public Long Paused;
    public Long Timestamp;

    public static class AnimatedMouseCursorFrameDTO {
        public long Ms;
        public String Img;
    }
}
