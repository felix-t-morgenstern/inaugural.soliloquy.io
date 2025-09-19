package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

public class ProgressiveStringProviderHandler extends AbstractTypeHandler<ProviderAtTime<String>> {
    private final ProgressiveStringProviderFactory FACTORY;

    public ProgressiveStringProviderHandler(ProgressiveStringProviderFactory factory) {
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProviderAtTime<String> read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        var dto = JSON.fromJson(data, Dto.class);

        return FACTORY.make(UUID.fromString(dto.uuid), dto.string, dto.timeToComplete,
                dto.startTimestamp, dto.pausedTimestamp);
    }

    @Override
    public String write(ProviderAtTime<String> provider) {
        Check.ifNull(provider, "provider");

        var dto = new Dto();

        dto.uuid = provider.uuid().toString();
        var representation =
                (ProgressiveStringProvider.Representation) provider.representation();
        dto.string = representation.text();
        dto.timeToComplete = representation.timeToComplete();
        dto.startTimestamp = representation.anchorTime();
        dto.pausedTimestamp = provider.pausedTimestamp();

        return JSON.toJson(dto);
    }

    private static class Dto {
        private String uuid;
        private String string;
        private long timeToComplete;
        private long startTimestamp;
        private Long pausedTimestamp;
    }
}
