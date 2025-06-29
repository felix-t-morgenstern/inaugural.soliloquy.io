package inaugural.soliloquy.io.api.dto;

public class MouseCursorImageDefinitionDTO {
    public String RelativeLocation;
    public int HotspotX;
    public int HotspotY;

    public MouseCursorImageDefinitionDTO(String relativeLocation, int hotspotX, int hotspotY) {
        RelativeLocation = relativeLocation;
        HotspotX = hotspotX;
        HotspotY = hotspotY;
    }
}
