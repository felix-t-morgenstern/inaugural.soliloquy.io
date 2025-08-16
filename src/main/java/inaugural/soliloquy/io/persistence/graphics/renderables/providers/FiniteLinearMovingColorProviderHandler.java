package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FiniteLinearMovingColorProviderHandler
        extends AbstractTypeHandler<FiniteLinearMovingColorProvider> {
    private final FiniteLinearMovingColorProviderFactory
            FACTORY;

    public FiniteLinearMovingColorProviderHandler(FiniteLinearMovingColorProviderFactory factory) {
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public FiniteLinearMovingColorProvider read(String writtenValue)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"), DTO.class);

        var uuid = UUID.fromString(dto.uuid);

        Map<Long, Color> colorsAtTimestamps = mapOf();
        List<Boolean> hueMovementIsClockwise = listOf();

        for (var i = 0; i < dto.colors.length; i++) {
            colorsAtTimestamps.put(dto.colors[i].timestamp,
                    new Color(dto.colors[i].r, dto.colors[i].g, dto.colors[i].b, dto.colors[i].a));
            hueMovementIsClockwise.add(dto.movementIsClockwise[i]);
        }

        return FACTORY.make(uuid, colorsAtTimestamps, hueMovementIsClockwise, dto.pausedTimestamp);
    }

    @Override
    public String write(FiniteLinearMovingColorProvider provider) {
        Check.ifNull(provider, "provider");

        var dto = new DTO();

        dto.uuid = provider.uuid().toString();

        var colorsAtTimestamps = provider.valuesAtTimestampsRepresentation();
        int colorsAtTimestampsSize = colorsAtTimestamps.size();
        dto.colors = new ColorAtTimestampDTO[colorsAtTimestampsSize];
        var hueMovementIsClockwise = provider.hueMovementIsClockwise();
        // NB: I am assuming here that colorsAtTimestamps and hueMovementIsClockwise have the same
        //     cardinality, since any implementation should enforce this.
        dto.movementIsClockwise = new boolean[colorsAtTimestampsSize];
        var index = 0;
        var timestamps = listOf(colorsAtTimestamps.keySet());
        Collections.sort(timestamps);
        for (var timestamp : timestamps) {
            var colorAtTimestampDto = new ColorAtTimestampDTO();
            var color = colorsAtTimestamps.get(timestamp);
            colorAtTimestampDto.timestamp = timestamp;
            colorAtTimestampDto.r = color.getRed();
            colorAtTimestampDto.g = color.getGreen();
            colorAtTimestampDto.b = color.getBlue();
            colorAtTimestampDto.a = color.getAlpha();
            dto.colors[index] = colorAtTimestampDto;
            dto.movementIsClockwise[index] = hueMovementIsClockwise.get(index);
            index++;
        }

        dto.pausedTimestamp = provider.pausedTimestamp();

        return JSON.toJson(dto);
    }

    private static class DTO {
        String uuid;
        ColorAtTimestampDTO[] colors;
        boolean[] movementIsClockwise;
        Long pausedTimestamp;
    }

    private static class ColorAtTimestampDTO {
        long timestamp;
        int r;
        int g;
        int b;
        int a;
    }
}
