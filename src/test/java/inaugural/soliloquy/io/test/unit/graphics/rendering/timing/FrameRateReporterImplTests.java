package inaugural.soliloquy.io.test.unit.graphics.rendering.timing;

import inaugural.soliloquy.io.graphics.rendering.FrameRateReporterImpl;
import inaugural.soliloquy.tools.Tools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporter;

import java.util.*;
import java.util.function.Consumer;

import static inaugural.soliloquy.io.api.Constants.GMT;
import static inaugural.soliloquy.io.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FrameRateReporterImplTests {
    private final String OUTPUT_ID = randomString();
    private final int PERIODS_PER_AGGREGATE = 3;

    @Mock private Consumer<FrameRateReporter.Aggregate> mockAggregateOutput;

    private long startingDatetime;
    private Map<String, Consumer<FrameRateReporter.Aggregate>> aggregateOutputs;

    private FrameRateReporter frameRateReporter;

    @BeforeEach
    public void setUp() {
        aggregateOutputs = mapOf(OUTPUT_ID, mockAggregateOutput);

        var calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT));

        calendar.set(2021, Calendar.APRIL, 8, 0, 4, 31);
        startingDatetime = calendar.getTimeInMillis();

        frameRateReporter = new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, aggregateOutputs);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(0, aggregateOutputs));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        mapOf(null, mockAggregateOutput)));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        mapOf("", mockAggregateOutput)));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        mapOf(OUTPUT_ID, null)));
    }

    @Test
    public void testReportFrameRateAndCurrentActualFps() {
        float actualFps = 123f;

        frameRateReporter.reportFrameRate(startingDatetime, null, actualFps);

        assertEquals((Float) actualFps, frameRateReporter.currentActualFps());
    }

    @Test
    public void testReportFrameRateWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportFrameRate(startingDatetime, -0.0001f,
                        0f));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportFrameRate(startingDatetime, null,
                        -0.0001f));
    }

    @Test
    public void testReportFrameRateForInvalidDatetimes() {
        float actualFps = 123f;

        frameRateReporter.reportFrameRate(startingDatetime, null, actualFps);

        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportFrameRate(startingDatetime + 999, null,
                        actualFps));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportFrameRate(startingDatetime + 1001, null,
                        actualFps));
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    public void testReportFrameRateSendToAggregate() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = 0.6822f;
        Float targetFps3 = 0.2221f;
        Float targetFps4 = 0.7172f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        frameRateReporter.activateAggregateOutput(OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);

        var aggregateCaptor = ArgumentCaptor.forClass(FrameRateReporter.Aggregate.class);
        verify(mockAggregateOutput, once()).accept(any());
        verify(mockAggregateOutput, once()).accept(aggregateCaptor.capture());
        var aggregate = aggregateCaptor.getValue();
        assertEquals(new Date(startingDatetime), aggregate.periodStart());
        assertEquals(Tools.round(((targetFps1 / 3f) + (targetFps2 / 3f) + (targetFps3 / 3f)), 3),
                Tools.round(aggregate.targetFps(), 3));
        assertEquals((Float) ((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                aggregate.actualFps());
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    public void testReportFrameRateSendToAggregateWithSomeNullTargetFps() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = null;
        Float targetFps3 = 0.2221f;
        Float targetFps4 = 0.7172f;
        Float targetFps5 = 0.8308f;
        Float targetFps6 = 0.5917f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        float actualFps5 = 0.0375f;
        float actualFps6 = 0.7415f;
        frameRateReporter.activateAggregateOutput(OUTPUT_ID);



        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2, actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3, actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4, actualFps4);
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5, actualFps5);
        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6, actualFps6);



        verify(mockAggregateOutput, times(2)).accept(any());
        var aggregateCaptor = ArgumentCaptor.forClass(FrameRateReporter.Aggregate.class);
        verify(mockAggregateOutput, times(2)).accept(aggregateCaptor.capture());
        var aggregates = aggregateCaptor.getAllValues();
        var aggregate1 = aggregates.getFirst();
        assertEquals(new FrameRateReporter.Aggregate(
                new Date(startingDatetime),
                (targetFps1 / 2f) + (targetFps3 / 2f),
                (actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)
        ), aggregate1);
        var aggregate2 = aggregates.get(1);
        assertEquals(new Date(startingDatetime + 3000), aggregate2.periodStart());
        assertEquals(
                Tools.round(((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)), 3),
                Tools.round(aggregate2.targetFps(), 3)
        );
        assertEquals(
                Tools.round(((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)), 3),
                Tools.round(aggregate2.actualFps(), 3)
        );
    }

    @Test
    public void testReportFrameRateSendToAggregateWithAllNullTargetFps() {
        Float targetFps1 = null;
        Float targetFps2 = null;
        Float targetFps3 = null;
        Float targetFps4 = 0.7172f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        frameRateReporter.activateAggregateOutput(OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2, actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3, actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4, actualFps4);

        verify(mockAggregateOutput, once()).accept(any());
        verify(mockAggregateOutput, once()).accept(new FrameRateReporter.Aggregate(
                new Date(startingDatetime),
                null,
                (actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)
        ));
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    public void testDeactivateAndActivateAggregateOutput() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = null;
        Float targetFps3 = 0.2221f;
        Float targetFps4 = 0.7172f;
        Float targetFps5 = 0.8308f;
        Float targetFps6 = 0.5917f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        float actualFps5 = 0.0375f;
        float actualFps6 = 0.7415f;
        frameRateReporter.activateAggregateOutput(OUTPUT_ID);



        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);

        frameRateReporter.deactivateAggregateOutput(OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3, actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4, actualFps4);
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5, actualFps5);

        frameRateReporter.activateAggregateOutput(OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6, actualFps6);



        verify(mockAggregateOutput, once()).accept(any());

        var aggregateCaptor = ArgumentCaptor.forClass(FrameRateReporter.Aggregate.class);
        verify(mockAggregateOutput, once()).accept(aggregateCaptor.capture());
        var aggregate = aggregateCaptor.getValue();
        assertEquals(new Date(startingDatetime + 3000), aggregate.periodStart());
        assertEquals(
                Tools.round(((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)), 3),
                Tools.round(aggregate.targetFps(), 3)
        );
        assertEquals(
                Tools.round(((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)), 3),
                Tools.round(aggregate.actualFps(), 3)
        );
    }

    @Test
    public void testDeactivateAndActivateAggregateOutputWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.deactivateAggregateOutput(null));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.deactivateAggregateOutput(""));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.deactivateAggregateOutput("Invalid Id"));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.activateAggregateOutput(null));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.activateAggregateOutput(""));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.activateAggregateOutput("Invalid Id"));
    }

    @Test
    public void testPausedPeriodCausesCurrentFpsToReturnNull() {
        Float targetFps1 = 0.7269f;
        float actualFps1 = 0.2626f;

        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);

        assertNotNull(frameRateReporter.currentActualFps());

        frameRateReporter.reportPause(startingDatetime + 1500);

        assertNull(frameRateReporter.currentActualFps());

        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps1, actualFps1);

        assertNull(frameRateReporter.currentActualFps());

        frameRateReporter.reportUnpause(startingDatetime + 2500);

        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps1, actualFps1);

        assertNotNull(frameRateReporter.currentActualFps());
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    public void testReportPauseAndUnpauseSendsCurrentPeriodWeightedByPausePercentageToOutputs() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = 0.6822f;
        Float targetFps3 = 0.2221f;
        Float targetFps4 = 0.7172f;
        Float targetFps5 = 0.8308f;
        Float targetFps6 = null;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        float actualFps5 = 0.0375f;
        float actualFps6 = 0.7415f;
        frameRateReporter.activateAggregateOutput(OUTPUT_ID);



        // 100% of period 1
        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);

        frameRateReporter.reportPause(startingDatetime + 1800);

        // 80% of period 2
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2, actualFps2);

        // 0% of period 3
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3, actualFps3);

        frameRateReporter.reportUnpause(startingDatetime + 3500);

        // 50% of period 4
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4, actualFps4);

        frameRateReporter.reportPause(startingDatetime + 4100);

        frameRateReporter.reportUnpause(startingDatetime + 4400);

        // 70% of period 5
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5, actualFps5);

        // 100% of period 6, but 0% of targetFps for period 6
        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6, actualFps6);



        verify(mockAggregateOutput, times(2)).accept(any());

        var aggregateCaptor = ArgumentCaptor.forClass(FrameRateReporter.Aggregate.class);
        verify(mockAggregateOutput, times(2)).accept(aggregateCaptor.capture());
        var aggregates = aggregateCaptor.getAllValues();

        var aggregate1 = aggregates.getFirst();
        assertEquals(new Date(startingDatetime), aggregate1.periodStart());
        assertEquals(
                Tools.round(((targetFps1 / 1.8f) + ((targetFps2 * 0.8f) / 1.8f)), 3),
                Tools.round(aggregate1.targetFps(), 3)
        );
        assertEquals((Float) ((actualFps1 / 1.8f) + ((actualFps2 * 0.8f) / 1.8f)),
                aggregate1.actualFps());

        var aggregate2 = aggregates.get(1);
        assertEquals(new Date(startingDatetime + 3000), aggregate2.periodStart());
        assertEquals((Float) (((targetFps4 * 0.5f) / 1.2f) + ((targetFps5 * 0.7f) / 1.2f)),
                aggregate2.targetFps());
        assertEquals(
                Tools.round((((actualFps4 * 0.5f) / 2.2f) + ((actualFps5 * 0.7f) / 2.2f) +
                        ((actualFps6) / 2.2f)), 3),
                Tools.round(aggregate2.actualFps(), 3)
        );
    }

    @Test
    public void testReportEntirelyPausedPeriodSendsNullValues() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = 0.6822f;
        Float targetFps3 = 0.2221f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        frameRateReporter.reportPause(startingDatetime - MS_PER_SECOND);



        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2, actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3, actualFps3);



        verify(mockAggregateOutput, once()).accept(any());
        verify(mockAggregateOutput, once()).accept(new FrameRateReporter.Aggregate(
                new Date(startingDatetime),
                null,
                null
        ));
    }

    @Test
    public void testReportPauseAndUnpauseBeyondCurrentPeriod() {
        float actualFps = 123f;

        frameRateReporter.reportFrameRate(startingDatetime, null, actualFps);

        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportPause(startingDatetime + MS_PER_SECOND - 1));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportPause(startingDatetime + (MS_PER_SECOND * 2)));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportUnpause(startingDatetime + MS_PER_SECOND - 1));
        assertThrows(IllegalArgumentException.class,
                () -> frameRateReporter.reportUnpause(startingDatetime + (MS_PER_SECOND * 2)));
    }

    @Test
    public void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class, frameRateReporter::pausedTimestamp);
    }
}
