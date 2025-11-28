package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

@SuppressWarnings("rawtypes")
public class ProviderHandler extends AbstractTypeHandler<ProviderAtTime> {
    @SuppressWarnings("rawtypes") private final Map<String, TypeHandler>
            SUBHANDLERS;

    public ProviderHandler() {
        SUBHANDLERS = mapOf();
    }

    public void add(String type, TypeHandler handler) {
        SUBHANDLERS.put(type, handler);
    }

    public <TInstance extends ProviderAtTime> TInstance read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(writtenVal, ProviderDTO.class);

        @SuppressWarnings("unchecked") var subhandler = (TypeHandler<ProviderAtTime>)SUBHANDLERS.get(dto.type);

        return subhandler.read(writtenVal);
    }

    public String write(ProviderAtTime providerAtTime) {
        var subhandler = SUBHANDLERS.get(providerAtTime.getClass().getCanonicalName());

        //noinspection unchecked
        return subhandler.write(providerAtTime);
    }

    public static class ProviderDTO {
        public String type;
    }
}
