package inaugural.soliloquy.graphics.api.dto;

public class FontDefinitionDTO {
    public String id;
    public String relativeLocation;
    public float maxLosslessFontSize;
    public float leadingAdjustment;
    public FontStyleDefinitionDTO plain;
    public FontStyleDefinitionDTO italic;
    public FontStyleDefinitionDTO bold;
    public FontStyleDefinitionDTO boldItalic;

    public FontDefinitionDTO(String id,
                             String relativeLocation,
                             float maxLosslessFontSize,
                             float leadingAdjustment,
                             FontStyleDefinitionDTO plain,
                             FontStyleDefinitionDTO italic,
                             FontStyleDefinitionDTO bold,
                             FontStyleDefinitionDTO boldItalic) {
        this.id = id;
        this.relativeLocation = relativeLocation;
        this.maxLosslessFontSize = maxLosslessFontSize;
        this.leadingAdjustment = leadingAdjustment;
        this.plain = plain;
        this.italic = italic;
        this.bold = bold;
        this.boldItalic = boldItalic;
    }
}
