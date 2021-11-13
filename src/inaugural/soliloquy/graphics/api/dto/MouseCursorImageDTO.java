package inaugural.soliloquy.graphics.api.dto;

public class MouseCursorImageDTO {
    public String RelativeLocation;
    public int HotspotX;
    public int HotspotY;

    public MouseCursorImageDTO(String relativeLocation, int hotspotX, int hotspotY) {
        RelativeLocation = relativeLocation;
        HotspotX = hotspotX;
        HotspotY = hotspotY;
    }
}
