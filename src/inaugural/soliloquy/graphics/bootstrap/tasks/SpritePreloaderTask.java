package inaugural.soliloquy.graphics.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.SpriteDefinitionDTO;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class SpritePreloaderTask implements Runnable {
    private final Function<String, Image> GET_IMAGE;
    private final AssetFactory<SpriteDefinition, Sprite> FACTORY;
    private final Collection<SpriteDefinitionDTO> SPRITE_DEFINITION_DTOS;
    private final Consumer<Sprite> PROCESS_RESULT;

    /** @noinspection ConstantConditions*/
    public SpritePreloaderTask(Function<String, Image> getImage,
                               Collection<SpriteDefinitionDTO> spriteDefinitionDTOs,
                               AssetFactory<SpriteDefinition, Sprite> factory,
                               Consumer<Sprite> processResult) {
        GET_IMAGE = Check.ifNull(getImage, "getImage");
        FACTORY = Check.ifNull(factory, "factory");
        Check.ifNull(spriteDefinitionDTOs, "spriteDefinitionDTOs");
        if (spriteDefinitionDTOs.isEmpty()) {
            throw new IllegalArgumentException(
                    "SpritePreloaderTask: spriteDefinitionDTOs is empty");
        }
        spriteDefinitionDTOs.forEach(spriteDefinitionDTO -> {
            Check.ifNull(spriteDefinitionDTO, "spriteDefinitionDTO within spriteDefinitionDTOs");
            Check.ifNullOrEmpty(spriteDefinitionDTO.id,
                    "spriteDefinitionDTO.id within spriteDefinitionDTOs");
            Check.ifNullOrEmpty(spriteDefinitionDTO.imgLoc,
                    "spriteDefinitionDTO.imgLoc within spriteDefinitionDTOs (" +
                    spriteDefinitionDTO.id + ")");
            Check.throwOnLteZero(spriteDefinitionDTO.leftX,
                    "spriteDefinitionDTO.leftX within spriteDefinitionDTOs (" +
                            spriteDefinitionDTO.id + ")");
            Check.throwOnLteZero(spriteDefinitionDTO.topY,
                    "spriteDefinitionDTO.topY within spriteDefinitionDTOs (" +
                            spriteDefinitionDTO.id + ")");
            Check.throwOnSecondLte(spriteDefinitionDTO.leftX, spriteDefinitionDTO.rightX,
                    "spriteDefinitionDTO.leftX",
                    "spriteDefinitionDTO.rightX within spriteDefinition DTOs (" +
                            spriteDefinitionDTO.id + ")");
            Check.throwOnSecondLte(spriteDefinitionDTO.topY, spriteDefinitionDTO.bottomY,
                    "spriteDefinitionDTO.topY",
                    "spriteDefinitionDTO.bottomY within spriteDefinition DTOs (" +
                            spriteDefinitionDTO.id + ")");
        });
        SPRITE_DEFINITION_DTOS = spriteDefinitionDTOs;
        PROCESS_RESULT = Check.ifNull(processResult, "processResult");
    }

    public void run() {
        SPRITE_DEFINITION_DTOS.forEach(dto -> PROCESS_RESULT.accept(FACTORY.make(
                new SpriteDefinition() {
                    @Override
                    public Image image() {
                        return GET_IMAGE.apply(dto.imgLoc);
                    }

                    @Override
                    public int leftX() {
                        return dto.leftX;
                    }

                    @Override
                    public int topY() {
                        return dto.topY;
                    }

                    @Override
                    public int rightX() {
                        return dto.rightX;
                    }

                    @Override
                    public int bottomY() {
                        return dto.bottomY;
                    }

                    @Override
                    public String id() {
                        return dto.id;
                    }

                    @Override
                    public String getInterfaceName() {
                        return SpriteDefinition.class.getCanonicalName();
                    }
                })
        ));
    }
}
