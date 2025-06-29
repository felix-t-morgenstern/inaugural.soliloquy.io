package inaugural.soliloquy.io.api.dto;

public class FontStyleDefinitionGlyphPropertyDTO {
    public char glyph;
    public float amount;

    public FontStyleDefinitionGlyphPropertyDTO(char glyph, float amount) {
        this.glyph = glyph;
        this.amount = amount;
    }
}
