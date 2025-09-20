package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FunctionalProviderFactory;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings("rawtypes")
public class FunctionalProviderHandler extends AbstractTypeHandler<FunctionalProvider> {
    @SuppressWarnings("rawtypes") private final TypeHandler<Map> DATA_HANDLER;
    private final FunctionalProviderFactory FACTORY;

    public FunctionalProviderHandler(
            @SuppressWarnings("rawtypes") TypeHandler<Map> dataHandler,
            FunctionalProviderFactory factory) {
        DATA_HANDLER = Check.ifNull(dataHandler, "dataHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public FunctionalProvider read(String writtenVal) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        Map<String, Object> data = DATA_HANDLER.read(dto.data);

        return FACTORY.make(
                UUID.fromString(dto.uuid),
                dto.provideId,
                dto.pauseId,
                dto.unpauseId,
                dto.pause,
                data
        );
    }

    @Override
    public String write(FunctionalProvider provider) {
        Check.ifNull(provider, "provider");

        var dto = new Dto();

        var representation = (FunctionalProvider.Representation) provider.representation();

        dto.uuid = provider.uuid().toString();
        dto.provideId = representation.provideId();
        dto.pauseId = representation.pauseId();
        dto.unpauseId = representation.unpauseId();
        dto.pause = representation.pauseTimestamp();
        dto.data = DATA_HANDLER.write(representation.data());

        return JSON.toJson(dto);
    }

    private static class Dto {
        String uuid;
        String provideId;
        String pauseId;
        String unpauseId;
        Long pause;
        String data;
    }
}
