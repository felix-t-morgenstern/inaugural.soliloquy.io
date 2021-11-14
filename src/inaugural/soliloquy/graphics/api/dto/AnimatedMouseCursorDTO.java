package inaugural.soliloquy.graphics.api.dto;

public class AnimatedMouseCursorDTO {
    public String Id;
    public AnimatedMouseCursorFrameDTO[] Frames;
    public int Duration;
    public int Offset;
    public Long Paused;
    public Long Timestamp;

    public AnimatedMouseCursorDTO(String id, AnimatedMouseCursorFrameDTO[] frames, int duration,
                                  int offset, Long paused, Long timestamp) {
        Id = id;
        Frames = frames;
        Duration = duration;
        Offset = offset;
        Paused = paused;
        Timestamp = timestamp;
    }

    public static class AnimatedMouseCursorFrameDTO {
        public int Ms;
        public String Img;

        public AnimatedMouseCursorFrameDTO(int ms, String img) {
            Ms = ms;
            Img = img;
        }
    }
}
