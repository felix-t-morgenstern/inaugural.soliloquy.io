package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

public class ProgressiveStringProviderHandler extends AbstractTypeHandler<ProviderAtTime<String>> {
    private final ProgressiveStringProviderFactory FACTORY;

    public ProgressiveStringProviderHandler(ProgressiveStringProviderFactory factory) {
        super(new ProgressiveStringProviderArchetype());
        FACTORY = Check.ifNull(factory, "factory");
    }

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
        dto.string = representation.getItem1();
        dto.timeToComplete = representation.getItem2().getItem1();
        dto.startTimestamp = representation.getItem2().getItem2();
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

    private static final class ProgressiveStringProviderArchetype
            implements ProviderAtTime<String> {

        @Override
        public String provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Object representation() {
            return null;
        }

        @Override
        public String getArchetype() {
            return "";
        }

        @Override
        public UUID uuid() {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public Long pausedTimestamp() {
            return null;
        }

        @Override
        public Long mostRecentTimestamp() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return ProviderAtTime.class.getCanonicalName() + "<" + String.class.getCanonicalName() +
                    ">";
        }
    }
}
