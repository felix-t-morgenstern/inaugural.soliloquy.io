package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

public class FiniteLinearMovingColorProviderHandler
        extends AbstractTypeHandler<FiniteLinearMovingColorProvider> {
    private final TypeHandler<UUID> UUID_HANDLER;
    private final FiniteLinearMovingColorProviderFactory
            FINITE_LINEAR_MOVING_COLOR_PROVIDER_FACTORY;

    private static final FiniteLinearMovingColorProvider ARCHETYPE =
            new FiniteLinearMovingColorProviderArchetype();

    public FiniteLinearMovingColorProviderHandler(TypeHandler<UUID> uuidHandler,
                                                  FiniteLinearMovingColorProviderFactory
                                                          finiteLinearMovingColorProviderFactory) {
        super(ARCHETYPE);
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
        FINITE_LINEAR_MOVING_COLOR_PROVIDER_FACTORY =
                Check.ifNull(finiteLinearMovingColorProviderFactory,
                        "finiteLinearMovingColorProviderFactory");
    }

    @Override
    public FiniteLinearMovingColorProvider read(String writtenValue)
            throws IllegalArgumentException {
        FiniteLinearMovingColorProviderDTO dto = JSON.fromJson(
                Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                FiniteLinearMovingColorProviderDTO.class);

        UUID uuid = UUID_HANDLER.read(dto.uuid);

        HashMap<Long, Color> colorsAtTimestamps = new HashMap<>();
        ArrayList<Boolean> hueMovementIsClockwise = new ArrayList<>();

        for (int i = 0; i < dto.colors.length; i++) {
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

        FiniteLinearMovingColorProviderDTO dto = new FiniteLinearMovingColorProviderDTO();

        dto.uuid = UUID_HANDLER.write(finiteLinearMovingColorProvider.uuid());

        Map<Long, Color> colorsAtTimestamps =
                finiteLinearMovingColorProvider.valuesAtTimestampsRepresentation();
        int colorsAtTimestampsSize = colorsAtTimestamps.size();
        dto.colors =
                new FiniteLinearMovingColorProviderColorAtTimestampDTO[colorsAtTimestampsSize];
        List<Boolean> hueMovementIsClockwise =
                finiteLinearMovingColorProvider.hueMovementIsClockwise();
        // NB: I am assuming here that colorsAtTimestamps and hueMovementIsClockwise have the same
        //     cardinality, since any implementation should enforce this.
        dto.movementIsClockwise = new boolean[colorsAtTimestampsSize];
        int index = 0;
        ArrayList<Long> timestamps = new ArrayList<>(colorsAtTimestamps.keySet());
        Collections.sort(timestamps);
        for (Long timestamp : timestamps) {
            FiniteLinearMovingColorProviderColorAtTimestampDTO colorAtTimestampDto =
                    new FiniteLinearMovingColorProviderColorAtTimestampDTO();
            Color color = colorsAtTimestamps.get(timestamp);
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

    private static class FiniteLinearMovingColorProviderArchetype
            implements FiniteLinearMovingColorProvider {

        @Override
        public List<Boolean> hueMovementIsClockwise() {
            return null;
        }

        @Override
        public Map<Long, Color> valuesAtTimestampsRepresentation() {
            return null;
        }

        @Override
        public Color provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Object representation() {
            return null;
        }

        // NB: An archetype is needed to pass validation checks for parent classes, but it is not
        //     in fact used to generate the interface name
        @Override
        public Color getArchetype() {
            return Color.BLACK;
        }

        @Override
        public UUID uuid() {
            return null;
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
            return FiniteLinearMovingColorProvider.class.getCanonicalName();
        }
    }
}
