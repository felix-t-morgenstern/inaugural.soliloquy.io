package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.Tools.defaultIfNull;
import static soliloquy.specs.io.input.keyboard.KeyBinding.keyBinding;

public class ComponentHandler extends AbstractTypeHandler<Component> {
    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    @SuppressWarnings("rawtypes") private final TypeHandler<Map> DATA_HANDLER;
    private final PersistenceHandler PERSISTENCE_HANDLER;
    @SuppressWarnings("rawtypes") private final Function<String, Action> GET_ACTION;
    private final ComponentFactory FACTORY;

    public ComponentHandler(
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            @SuppressWarnings("rawtypes") TypeHandler<Map> dataHandler,
            PersistenceHandler persistenceHandler,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            ComponentFactory factory) {
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        DATA_HANDLER = Check.ifNull(dataHandler, "dataHandler");
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        GET_ACTION = Check.ifNull(getAction, "getAction");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component read(String writtenVal) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        var dimens = PROVIDER_HANDLER.read(dto.dimens);
        var data = DATA_HANDLER.read(dto.data);

        var bindings = Collections.<KeyBinding>setOf();
        Arrays.stream(dto.bindings).forEach(b -> {
            var onPress = defaultIfNull(b.onPress, null, GET_ACTION);
            var onRelease = defaultIfNull(b.onRelease, null, GET_ACTION);
            bindings.add(keyBinding(b.keys, onPress, onRelease));
        });

        var component = FACTORY.make(
                UUID.fromString(dto.uuid),
                dto.z,
                bindings,
                dto.overrides,
                dimens,
                null,
                data
        );

        Arrays.stream(dto.content).forEach(c -> {
            var handler = PERSISTENCE_HANDLER.getTypeHandler(c.type);
            var content = (Renderable) handler.read(c.content);
            component.add(content);
        });

        return component;
    }

    @Override
    public String write(Component component) {
        Check.ifNull(component, "component");

        var dto = new Dto();

        dto.uuid = component.uuid().toString();
        dto.z = component.getZ();

        dto.overrides = component.blocksLowerKeyBindings();
        dto.bindings = component.keyBindings().stream().map(b -> {
            var bindingDto = new Dto.BindingDto();
            bindingDto.keys = b.BOUND_KEYS;
            bindingDto.onPress = b.ON_PRESS.id();
            bindingDto.onRelease = b.ON_RELEASE.id();
            return bindingDto;
        }).toArray(Dto.BindingDto[]::new);

        dto.dimens = PROVIDER_HANDLER.write(component.getRenderingBoundariesProvider());

        dto.content = component.contentsRepresentation().stream().map(c -> {
            var contentDto = new Dto.ContentDto();
            contentDto.type = c.getClass().getCanonicalName();
            var contentHandler = PERSISTENCE_HANDLER.getTypeHandler(contentDto.type);
            contentDto.content = contentHandler.write(c);
            return contentDto;
        }).toArray(Dto.ContentDto[]::new);

        dto.data = DATA_HANDLER.write(component.data());

        return JSON.toJson(dto);
    }

    private final static class Dto {
        String uuid;
        BindingDto[] bindings;
        boolean overrides;
        String dimens;
        ContentDto[] content;
        String data;
        int z;

        private final static class ContentDto {
            String type;
            String content;
        }


        private final static class BindingDto {
            char[] keys;
            String onPress;
            String onRelease;
        }
    }
}
