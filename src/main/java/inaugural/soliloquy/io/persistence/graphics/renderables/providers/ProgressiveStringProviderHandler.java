package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

public class ProgressiveStringProviderHandler extends AbstractTypeHandler<ProviderAtTime<String>> {
    private final ProgressiveStringProviderFactory FACTORY;

    public ProgressiveStringProviderHandler(ProgressiveStringProviderFactory factory) {
        FACTORY = Check.ifNull(factory, "factory");
    }

    @Override
    public String typeHandled() {
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProviderAtTime<String> read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        ProgressiveStringProviderDTO dto = JSON.fromJson(data, ProgressiveStringProviderDTO.class);

        return FACTORY.make(UUID.fromString(dto.uuid), dto.string, dto.timeToComplete,
                dto.startTimestamp, dto.pausedTimestamp, dto.mostRecentTimestamp);
    }

    @Override
    public String write(ProviderAtTime<String> provider) {
        Check.ifNull(provider, "provider");

        ProgressiveStringProviderDTO dto = new ProgressiveStringProviderDTO();

        dto.uuid = provider.uuid().toString();
        //noinspection unchecked
        Pair<String, Pair<Long, Long>> representation =
                (Pair<String, Pair<Long, Long>>) provider.representation();
        dto.string = representation.FIRST;
        dto.timeToComplete = representation.SECOND.FIRST;
        dto.startTimestamp = representation.SECOND.SECOND;
        dto.pausedTimestamp = provider.pausedTimestamp();
        dto.mostRecentTimestamp = provider.mostRecentTimestamp();

        return JSON.toJson(dto);
    }

    private static class ProgressiveStringProviderDTO {
        private String uuid;
        private String string;
        private long timeToComplete;
        private long startTimestamp;
        private Long pausedTimestamp;
        private Long mostRecentTimestamp;
    }
}
