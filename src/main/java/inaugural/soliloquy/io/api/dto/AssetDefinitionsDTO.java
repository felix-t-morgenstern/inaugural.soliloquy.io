package inaugural.soliloquy.io.api.dto;

public class AssetDefinitionsDTO {
    public ImageDefinitionDTO[] imageDefinitionDTOs;
    public FontDefinitionDTO[] fontDefinitionDTOs;
    public SpriteDefinitionDTO[] spriteDefinitionDTOs;
    public AnimationDefinitionDTO[] animationDefinitionDTOs;
    public GlobalLoopingAnimationDefinitionDTO[] globalLoopingAnimationDefinitionDTOs;
    public ImageAssetSetDefinitionDTO[] imageAssetSetDefinitionDTOs;
    public MouseCursorImageDefinitionDTO[] mouseCursorImageDefinitionDTOs;
    public AnimatedMouseCursorDefinitionDTO[] animatedMouseCursorDefinitionDTOs;
    public StaticMouseCursorDefinitionDTO[] staticMouseCursorDefinitionDTOs;

    public AssetDefinitionsDTO(ImageDefinitionDTO[] imageDefinitionDTOs,
                               FontDefinitionDTO[] fontDefinitionDTOs,
                               SpriteDefinitionDTO[] spriteDefinitionDTOs,
                               AnimationDefinitionDTO[] animationDefinitionDTOs,
                               GlobalLoopingAnimationDefinitionDTO[]
                                       globalLoopingAnimationDefinitionDTOs,
                               ImageAssetSetDefinitionDTO[] imageAssetSetDefinitionDTOs,
                               MouseCursorImageDefinitionDTO[] mouseCursorImageDefinitionDTOs,
                               AnimatedMouseCursorDefinitionDTO[]
                                       animatedMouseCursorDefinitionDTOs,
                               StaticMouseCursorDefinitionDTO[] staticMouseCursorDefinitionDTOs) {
        this.imageDefinitionDTOs = imageDefinitionDTOs;
        this.fontDefinitionDTOs = fontDefinitionDTOs;
        this.spriteDefinitionDTOs = spriteDefinitionDTOs;
        this.animationDefinitionDTOs = animationDefinitionDTOs;
        this.globalLoopingAnimationDefinitionDTOs = globalLoopingAnimationDefinitionDTOs;
        this.imageAssetSetDefinitionDTOs = imageAssetSetDefinitionDTOs;
        this.mouseCursorImageDefinitionDTOs = mouseCursorImageDefinitionDTOs;
        this.animatedMouseCursorDefinitionDTOs = animatedMouseCursorDefinitionDTOs;
        this.staticMouseCursorDefinitionDTOs = staticMouseCursorDefinitionDTOs;
    }
}
