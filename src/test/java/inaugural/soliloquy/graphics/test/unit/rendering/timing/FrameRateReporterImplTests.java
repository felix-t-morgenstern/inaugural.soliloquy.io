package inaugural.soliloquy.graphics.test.unit.rendering.timing;

import inaugural.soliloquy.graphics.rendering.FrameRateReporterImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFrameRateReporterAggregateOutput;
import inaugural.soliloquy.tools.Tools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporter;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static inaugural.soliloquy.graphics.api.Constants.GMT;
import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;

public class FrameRateReporterImplTests {
    private final String FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID =
            "frameRateReporterAggregateOutputId";
    private final FakeFrameRateReporterAggregateOutput FRAME_RATE_REPORTER_AGGREGATE_OUTPUT =
            new FakeFrameRateReporterAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);
    private final List<FrameRateReporterAggregateOutput> AGGREGATE_OUTPUTS =
            listOf(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT);
    private final int PERIODS_PER_AGGREGATE = 3;

    private long startingDatetime;
    private FrameRateReporter frameRateReporter;

    @BeforeEach
    public void setUp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT));

        calendar.set(2021, Calendar.APRIL, 8, 0, 4, 31);
        startingDatetime = calendar.getTimeInMillis();

        frameRateReporter = new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, AGGREGATE_OUTPUTS);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(0, AGGREGATE_OUTPUTS));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        listOf((FrameRateReporterAggregateOutput) null)));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        listOf(new FakeFrameRateReporterAggregateOutput(null))));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        listOf(new FakeFrameRateReporterAggregateOutput(""))));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        listOf(new FakeFrameRateReporterAggregateOutput(
                                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID),
                                new FakeFrameRateReporterAggregateOutput(
                                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID))));
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
        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);

        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());
        assertEquals(new Date(startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertEquals(Tools.round(((targetFps1 / 3f) + (targetFps2 / 3f) + (targetFps3 / 3f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst(),
                        3));
        assertEquals((Float) ((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst());
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
        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5,
                actualFps5);
        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertEquals((Float) ((targetFps1 / 2f) + (targetFps3 / 2f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst());
        assertEquals((Float) ((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst());

        assertEquals(new Date(startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(1));
        assertEquals(
                Tools.round(((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(1),
                        3));
        assertEquals(
                Tools.round(((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(1),
                        3));
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
        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);

        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());
        assertEquals(new Date(startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst());
        assertEquals((Float) ((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst());
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
        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);

        frameRateReporter.deactivateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5,
                actualFps5);

        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertEquals(
                Tools.round(((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst(),
                        3));
        assertEquals(
                Tools.round(((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst(),
                        3));
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
        frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        // 100% of period 1
        frameRateReporter.reportFrameRate(startingDatetime, targetFps1, actualFps1);

        frameRateReporter.reportPause(startingDatetime + 1800);

        // 80% of period 2
        frameRateReporter.reportFrameRate(startingDatetime + 1000, targetFps2,
                actualFps2);

        // 0% of period 3
        frameRateReporter.reportFrameRate(startingDatetime + 2000, targetFps3,
                actualFps3);

        frameRateReporter.reportUnpause(startingDatetime + 3500);

        // 50% of period 4
        frameRateReporter.reportFrameRate(startingDatetime + 3000, targetFps4,
                actualFps4);

        frameRateReporter.reportPause(startingDatetime + 4100);

        frameRateReporter.reportUnpause(startingDatetime + 4400);

        // 70% of period 5
        frameRateReporter.reportFrameRate(startingDatetime + 4000, targetFps5,
                actualFps5);

        // 100% of period 6, but 0% of targetFps for period 6
        frameRateReporter.reportFrameRate(startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertEquals(
                Tools.round(((targetFps1 / 1.8f) + ((targetFps2 * 0.8f) / 1.8f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst(),
                        3));
        assertEquals((Float) ((actualFps1 / 1.8f) + ((actualFps2 * 0.8f) / 1.8f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst());

        assertEquals(new Date(startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(1));
        assertEquals((Float) (((targetFps4 * 0.5f) / 1.2f) + ((targetFps5 * 0.7f) / 1.2f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(1));
        assertEquals(
                Tools.round((((actualFps4 * 0.5f) / 2.2f) + ((actualFps5 * 0.7f) / 2.2f) +
                        ((actualFps6 * 1.0f) / 2.2f)), 3),
                Tools.round(
                        FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(1),
                        3));
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



        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.getFirst());
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.getFirst());
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.getFirst());
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
