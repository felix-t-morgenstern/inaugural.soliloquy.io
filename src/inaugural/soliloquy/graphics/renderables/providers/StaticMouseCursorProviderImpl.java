package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;

import java.util.UUID;

public class StaticMouseCursorProviderImpl
        extends StaticProviderImpl<Long>
        implements StaticMouseCursorProvider {
    private final String ID;

    public StaticMouseCursorProviderImpl(String id, long value, Long mostRecentTimestamp) {
        super(UUID.randomUUID(), value, mostRecentTimestamp);
        ID = Check.ifNullOrEmpty(id, "id");
    }

    @Override
    public UUID uuid() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }
}
