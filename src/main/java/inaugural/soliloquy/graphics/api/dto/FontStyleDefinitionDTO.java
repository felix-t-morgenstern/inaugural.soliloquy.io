package inaugural.soliloquy.graphics.api.dto;

public class FontStyleDefinitionDTO {
    public float additionalGlyphHorizontalTextureSpacing;
    public FontStyleDefinitionGlyphPropertyDTO[] glyphwiseAdditionalHorizontalTextureSpacing;
    public FontStyleDefinitionGlyphPropertyDTO[] glyphwiseAdditionalLeftBoundaryShift;
    public float additionalGlyphVerticalTextureSpacing;

    public FontStyleDefinitionDTO(float additionalGlyphHorizontalTextureSpacing,
                                  FontStyleDefinitionGlyphPropertyDTO[]
                                          glyphwiseAdditionalHorizontalTextureSpacing,
                                  FontStyleDefinitionGlyphPropertyDTO[]
                                          glyphwiseAdditionalLeftBoundaryShift,
                                  float additionalGlyphVerticalTextureSpacing) {
        this.additionalGlyphHorizontalTextureSpacing = additionalGlyphHorizontalTextureSpacing;
        this.glyphwiseAdditionalHorizontalTextureSpacing =
                glyphwiseAdditionalHorizontalTextureSpacing;
        this.glyphwiseAdditionalLeftBoundaryShift = glyphwiseAdditionalLeftBoundaryShift;
        this.additionalGlyphVerticalTextureSpacing = additionalGlyphVerticalTextureSpacing;
    }
}
