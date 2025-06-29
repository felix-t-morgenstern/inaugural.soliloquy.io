package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FiniteLinearMovingColorProviderHandler
        extends AbstractTypeHandler<FiniteLinearMovingColorProvider> {
    private final TypeHandler<UUID> UUID_HANDLER;
    private final FiniteLinearMovingColorProviderFactory
            FINITE_LINEAR_MOVING_COLOR_PROVIDER_FACTORY;

    public FiniteLinearMovingColorProviderHandler(TypeHandler<UUID> uuidHandler,
                                                  FiniteLinearMovingColorProviderFactory
                                                          finiteLinearMovingColorProviderFactory) {
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
        FINITE_LINEAR_MOVING_COLOR_PROVIDER_FACTORY =
                Check.ifNull(finiteLinearMovingColorProviderFactory,
                        "finiteLinearMovingColorProviderFactory");
    }

    @Override
    public String typeHandled() {
        return FiniteLinearMovingColorProviderImpl.class.getCanonicalName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public FiniteLinearMovingColorProvider read(String writtenValue)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                FiniteLinearMovingColorProviderDTO.class);

        var uuid = UUID_HANDLER.read(dto.uuid);

        Map<Long, Color> colorsAtTimestamps = mapOf();
        List<Boolean> hueMovementIsClockwise = listOf();

        for (var i = 0; i < dto.colors.length; i++) {
            colorsAtTimestamps.put(dto.colors[i].timestamp,
                    new Color(dto.colors[i].r, dto.colors[i].g, dto.colors[i].b, dto.colors[i].a));
            hueMovementIsClockwise.add(dto.movementIsClockwise[i]);
        }

        return FINITE_LINEAR_MOVING_COLOR_PROVIDER_FACTORY.make(uuid, colorsAtTimestamps,
                hueMovementIsClockwise, dto.pausedTimestamp, dto.mostRecentTimestamp);
    }

    @Override
    public String write(FiniteLinearMovingColorProvider finiteLinearMovingColorProvider) {
        Check.ifNull(finiteLinearMovingColorProvider, "finiteLinearMovingColorProvider");

        var dto = new FiniteLinearMovingColorProviderDTO();

        dto.uuid = UUID_HANDLER.write(finiteLinearMovingColorProvider.uuid());

        var colorsAtTimestamps = finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation();
        int colorsAtTimestampsSize = colorsAtTimestamps.size();
        dto.colors = new FiniteLinearMovingColorProviderColorAtTimestampDTO[colorsAtTimestampsSize];
        var hueMovementIsClockwise = finiteLinearMovingColorProvider.hueMovementIsClockwise();
        // NB: I am assuming here that colorsAtTimestamps and hueMovementIsClockwise have the same
        //     cardinality, since any implementation should enforce this.
        dto.movementIsClockwise = new boolean[colorsAtTimestampsSize];
        var index = 0;
        var timestamps = listOf(colorsAtTimestamps.keySet());
        Collections.sort(timestamps);
        for (var timestamp : timestamps) {
            var colorAtTimestampDto = new FiniteLinearMovingColorProviderColorAtTimestampDTO();
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

        dto.pausedTimestamp = finiteLinearMovingColorProvider.pausedTimestamp();

        dto.mostRecentTimestamp = finiteLinearMovingColorProvider.mostRecentTimestamp();

        return JSON.toJson(dto);
    }

    private static class FiniteLinearMovingColorProviderDTO {
        String uuid;
        FiniteLinearMovingColorProviderColorAtTimestampDTO[] colors;
        boolean[] movementIsClockwise;
        Long pausedTimestamp;
        Long mostRecentTimestamp;
    }

    private static class FiniteLinearMovingColorProviderColorAtTimestampDTO {
        long timestamp;
        int r;
        int g;
        int b;
        int a;
    }
}
