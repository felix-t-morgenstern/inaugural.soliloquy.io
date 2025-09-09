package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static soliloquy.specs.io.input.keyboard.entities.KeyBinding.keyBinding;

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

        var component = FACTORY.make(
                UUID.fromString(dto.uuid),
                dto.z,
                dimens,
                null,
                data
        );

        Arrays.stream(dto.content).forEach(c -> {
            var handler = PERSISTENCE_HANDLER.getTypeHandler(c.type);
            var content = (Renderable) handler.read(c.content);
            //content.
            component.add(content);
        });

        component.keyBindingContext().BLOCKS_LOWER_BINDINGS = dto.bindingContext.overrides;
        Arrays.stream(dto.bindingContext.bindings).forEach(b -> {
            var onPress = b.onPress == null ? null : GET_ACTION.apply(b.onPress);
            var onRelease = b.onRelease == null ? null : GET_ACTION.apply(b.onRelease);
            component.keyBindingContext().BINDINGS.add(keyBinding(b.keys, onPress, onRelease));
        });

        return component;
    }

    @Override
    public String write(Component component) {
        Check.ifNull(component, "component");

        var dto = new Dto();

        dto.uuid = component.uuid().toString();
        dto.z = component.getZ();

        var context = component.keyBindingContext();
        var contextDto = new Dto.BindingContextDto();
        contextDto.overrides = context.BLOCKS_LOWER_BINDINGS;
        contextDto.bindings = context.BINDINGS.stream().map(b -> {
            var bindingDto = new Dto.BindingContextDto.BindingDto();
            bindingDto.keys = b.BOUND_KEYS;
            bindingDto.onPress = b.ON_PRESS.id();
            bindingDto.onRelease = b.ON_RELEASE.id();
            return bindingDto;
        }).toArray(Dto.BindingContextDto.BindingDto[]::new);
        dto.bindingContext = contextDto;

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
        BindingContextDto bindingContext;
        String dimens;
        ContentDto[] content;
        String data;
        int z;

        private final static class ContentDto {
            String type;
            String content;
        }

        private final static class BindingContextDto {
            BindingDto[] bindings;
            boolean overrides;

            private final static class BindingDto {
                char[] keys;
                String onPress;
                String onRelease;
            }
        }
    }
}
