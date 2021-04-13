package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.List;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.common.shared.HasId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class FakeRegistry<T extends HasId> implements Registry<T> {
    private final HashMap<String,T> REGISTRY = new HashMap<>();

    @Override
    public boolean contains(String s) {
        return REGISTRY.containsKey(s);
    }

    @Override
    public T get(String s) {
        return REGISTRY.get(s);
    }

    @Override
    public void add(T t) throws IllegalArgumentException {
        REGISTRY.put(t.id(), t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addAll(Object[] objects) throws IllegalArgumentException {
        for (Object object : objects) {
            add((T) object);
        }
    }

    @Override
    public void addAll(T[] ts) throws UnsupportedOperationException {
        for(T t : ts) {
            add(t);
        }
    }

    @Override
    public void addAll(Collection<T> collection) throws IllegalArgumentException {
        collection.forEach(this::add);
    }

    @Override
    public void clear() throws UnsupportedOperationException {

    }

    @Override
    public List<T> representation() {
        return null;
    }

    @Override
    public boolean remove(T t) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public boolean remove(String s) {
        return REGISTRY.remove(s) != null;
    }

    @Override
    public boolean contains(T t) {
        return false;
    }

    @Override
    public int size() {
        return REGISTRY.size();
    }

    @Override
    public Iterator<T> iterator() {
        return REGISTRY.values().iterator();
    }

    @Override
    public T getArchetype() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
