package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;

import java.util.UUID;

public class StaticMouseCursorProviderImpl
        extends StaticProviderImpl<Long>
        implements StaticMouseCursorProvider {
    private final String ID;

    public StaticMouseCursorProviderImpl(String id,
                                         long value,
                                         TimestampValidator timestampValidator) {
        super(UUID.randomUUID(), value, timestampValidator);
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
