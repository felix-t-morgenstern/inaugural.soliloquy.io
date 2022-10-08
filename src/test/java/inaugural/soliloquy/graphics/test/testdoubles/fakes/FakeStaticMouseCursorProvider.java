package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;

import java.util.UUID;

public class FakeStaticMouseCursorProvider implements StaticMouseCursorProvider {
    public String Id;
    public long ValueToProvide;

    public FakeStaticMouseCursorProvider(String id) {
        Id = id;
    }

    public FakeStaticMouseCursorProvider(String id, long valueToProvide) {
        Id = id;
        ValueToProvide = valueToProvide;
    }

    @Override
    public UUID uuid() throws UnsupportedOperationException {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public Long provide(long l) throws IllegalArgumentException {
        return ValueToProvide;
    }

    @Override
    public Object representation() {
        return null;
    }

    @Override
    public Long getArchetype() {
        return ValueToProvide;
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
        return null;
    }
}
