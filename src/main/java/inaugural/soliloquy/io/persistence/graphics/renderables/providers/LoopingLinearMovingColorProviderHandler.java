package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class LoopingLinearMovingColorProviderHandler
        extends AbstractTypeHandler<LoopingLinearMovingColorProvider> {
    private final LoopingLinearMovingColorProviderFactory
            LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY;

    public LoopingLinearMovingColorProviderHandler(LoopingLinearMovingColorProviderFactory
                                                           factory) {
        LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY =
                Check.ifNull(factory,
                        "factory");
    }

    @Override
    public String typeHandled() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoopingLinearMovingColorProvider read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        var dto = JSON.fromJson(data, LoopingLinearMovingColorProviderDTO.class);

        Map<Integer, Color> valuesWithinPeriod = mapOf();
        for (var i = 0; i < dto.periodValues.length; i++) {
            valuesWithinPeriod.put(
                    dto.periodTimestamps[i],
                    new Color(
                            dto.periodValues[i].r,
                            dto.periodValues[i].g,
                            dto.periodValues[i].b,
                            dto.periodValues[i].a
                    )
            );
        }

        List<Boolean> hueMovementIsClockwise = listOf();
        for (var i = 0; i < dto.hueMovementIsClockwise.length; i++) {
            hueMovementIsClockwise.add(dto.hueMovementIsClockwise[i]);
        }

        return LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY.make(
                UUID.fromString(dto.uuid),
                valuesWithinPeriod,
                hueMovementIsClockwise,
                dto.periodDuration,
                dto.periodModuloOffset,
                dto.pausedTimestamp,
                dto.mostRecentTimestamp
        );
    }

    @Override
    public String write(LoopingLinearMovingColorProvider provider) {
        Check.ifNull(provider, "provider");

        var dto = new LoopingLinearMovingColorProviderDTO();

        dto.uuid = provider.uuid().toString();

        Map<Integer, Color> valuesWithinPeriod = provider.valuesWithinPeriod();
        dto.periodTimestamps = new int[valuesWithinPeriod.size()];
        dto.periodValues = new ColorDTO[valuesWithinPeriod.size()];
        var index = 0;
        for (Map.Entry<Integer, Color> valueWithinPeriod : valuesWithinPeriod.entrySet()) {
            dto.periodTimestamps[index] = valueWithinPeriod.getKey();
            Color value = valueWithinPeriod.getValue();
            dto.periodValues[index] = new ColorDTO(value.getRed(), value.getGreen(),
                    value.getBlue(), value.getAlpha());
            index++;
        }

        List<Boolean> hueMovementIsClockwise = provider.hueMovementIsClockwise();
        dto.hueMovementIsClockwise = new boolean[hueMovementIsClockwise.size()];
        for (var i = 0; i < hueMovementIsClockwise.size(); i++) {
            dto.hueMovementIsClockwise[i] = hueMovementIsClockwise.get(i);
        }

        dto.periodDuration = provider.periodDuration();
        dto.periodModuloOffset = provider.periodModuloOffset();
        dto.pausedTimestamp = provider.pausedTimestamp();
        dto.mostRecentTimestamp = provider.mostRecentTimestamp();

        return JSON.toJson(dto);
    }

    private static class LoopingLinearMovingColorProviderDTO {
        String uuid;
        int[] periodTimestamps;
        ColorDTO[] periodValues;
        boolean[] hueMovementIsClockwise;
        int periodDuration;
        int periodModuloOffset;
        Long pausedTimestamp;
        long mostRecentTimestamp;
    }

    private static class ColorDTO {
        ColorDTO(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        int r;
        int g;
        int b;
        int a;
    }
}
