package inaugural.soliloquy.graphics.api.dto;

public class ImageDefinitionDTO {
    public String RelativeLocation;
    public boolean SupportsMouseEvents;

    public ImageDefinitionDTO(String relativeLocation, boolean supportsMouseEvents) {
        RelativeLocation = relativeLocation;
        SupportsMouseEvents = supportsMouseEvents;
    }
}
