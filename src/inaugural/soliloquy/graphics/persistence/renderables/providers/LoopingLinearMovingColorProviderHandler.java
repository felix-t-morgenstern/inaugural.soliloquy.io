package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoopingLinearMovingColorProviderHandler
        extends AbstractTypeHandler<LoopingLinearMovingColorProvider> {
    private final LoopingLinearMovingColorProviderFactory
            LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY;
    private final TypeHandler<EntityUuid> UUID_HANDLER;

    public LoopingLinearMovingColorProviderHandler(LoopingLinearMovingColorProviderFactory
                                                           loopingLinearMovingColorProviderFactory,
                                                   TypeHandler<EntityUuid> uuidHandler) {
        super(new LoopingLinearMovingColorProviderArchetype());
        LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY =
                Check.ifNull(loopingLinearMovingColorProviderFactory,
                        "loopingLinearMovingColorProviderFactory");
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
    }

    @Override
    public LoopingLinearMovingColorProvider read(String data) throws IllegalArgumentException {
        Check.ifNullOrEmpty(data, "data");

        LoopingLinearMovingColorProviderDTO dto =
                GSON.fromJson(data, LoopingLinearMovingColorProviderDTO.class);

        HashMap<Integer, Color> valuesWithinPeriod = new HashMap<>();
        for (int i = 0; i < dto.periodValues.length; i++) {
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

        ArrayList<Boolean> hueMovementIsClockwise = new ArrayList<>();
        for (int i = 0; i < dto.hueMovementIsClockwise.length; i++) {
            hueMovementIsClockwise.add(dto.hueMovementIsClockwise[i]);
        }

        return LOOPING_LINEAR_MOVING_COLOR_PROVIDER_FACTORY.make(
                UUID_HANDLER.read(dto.uuid),
                valuesWithinPeriod,
                hueMovementIsClockwise,
                dto.periodDuration,
                dto.periodModuloOffset,
                dto.pausedTimestamp,
                dto.mostRecentTimestamp
        );
    }

    @Override
    public String write(LoopingLinearMovingColorProvider loopingLinearMovingColorProvider) {
        Check.ifNull(loopingLinearMovingColorProvider, "loopingLinearMovingColorProvider");

        LoopingLinearMovingColorProviderDTO dto = new LoopingLinearMovingColorProviderDTO();

        dto.uuid = UUID_HANDLER.write(loopingLinearMovingColorProvider.uuid());

        Map<Integer, Color> valuesWithinPeriod =
                loopingLinearMovingColorProvider.valuesWithinPeriod();
        dto.periodTimestamps = new int[valuesWithinPeriod.size()];
        dto.periodValues = new ColorDTO[valuesWithinPeriod.size()];
        int index = 0;
        for (Map.Entry<Integer, Color> valueWithinPeriod : valuesWithinPeriod.entrySet()) {
            dto.periodTimestamps[index] = valueWithinPeriod.getKey();
            Color value = valueWithinPeriod.getValue();
            dto.periodValues[index] = new ColorDTO(value.getRed(), value.getGreen(),
                    value.getBlue(), value.getAlpha());
            index++;
        }

        List<Boolean> hueMovementIsClockwise =
                loopingLinearMovingColorProvider.hueMovementIsClockwise();
        dto.hueMovementIsClockwise = new boolean[hueMovementIsClockwise.size()];
        for (int i = 0; i < hueMovementIsClockwise.size(); i++) {
            dto.hueMovementIsClockwise[i] = hueMovementIsClockwise.get(i);
        }

        dto.periodDuration = loopingLinearMovingColorProvider.periodDuration();
        dto.periodModuloOffset = loopingLinearMovingColorProvider.periodModuloOffset();
        dto.pausedTimestamp = loopingLinearMovingColorProvider.pausedTimestamp();
        dto.mostRecentTimestamp = loopingLinearMovingColorProvider.mostRecentTimestamp();
        
        return GSON.toJson(dto);
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

    private static class LoopingLinearMovingColorProviderArchetype
            implements LoopingLinearMovingColorProvider {

        @Override
        public List<Boolean> hueMovementIsClockwise() {
            return null;
        }

        @Override
        public Map<Integer, Color> valuesWithinPeriod() {
            return null;
        }

        @Override
        public int periodDuration() {
            return 0;
        }

        @Override
        public int periodModuloOffset() {
            return 0;
        }

        @Override
        public void reset(long l) throws IllegalArgumentException {

        }

        @Override
        public Color provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Color getArchetype() {
            return Color.BLACK;
        }

        @Override
        public EntityUuid uuid() {
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
            return LoopingLinearMovingColorProvider.class.getCanonicalName();
        }
    }
}
