package inaugural.soliloquy.graphics.test.unit.rendering.timing;

import inaugural.soliloquy.graphics.rendering.FrameRateReporterImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameRateReporterAggregateOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporter;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static inaugural.soliloquy.graphics.api.Constants.GMT;
import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;
import static org.junit.jupiter.api.Assertions.*;

class FrameRateReporterImplTests {
    private final String FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID =
            "frameRateReporterAggregateOutputId";
    private final FakeFrameRateReporterAggregateOutput FRAME_RATE_REPORTER_AGGREGATE_OUTPUT =
            new FakeFrameRateReporterAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);
    private final ArrayList<FrameRateReporterAggregateOutput> AGGREGATE_OUTPUTS =
            new ArrayList<FrameRateReporterAggregateOutput>() {{
                add(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT);
            }};
    private final int PERIODS_PER_AGGREGATE = 3;

    private long _startingDatetime;
    private FrameRateReporter _frameRateReporter;

    @BeforeEach
    void setUp () {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT));

        calendar.set(2021, Calendar.APRIL,8,0,4, 31);
        _startingDatetime = calendar.getTimeInMillis();

        _frameRateReporter = new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, AGGREGATE_OUTPUTS);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(0, AGGREGATE_OUTPUTS));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        new ArrayList<FrameRateReporterAggregateOutput>(){{
                            add(null);
                        }}));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        new ArrayList<FrameRateReporterAggregateOutput>(){{
                            add(new FakeFrameRateReporterAggregateOutput(null));
                        }}));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        new ArrayList<FrameRateReporterAggregateOutput>(){{
                            add(new FakeFrameRateReporterAggregateOutput(""));
                        }}));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameRateReporterImpl(PERIODS_PER_AGGREGATE,
                        new ArrayList<FrameRateReporterAggregateOutput>(){{
                            add(new FakeFrameRateReporterAggregateOutput(
                                    FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID));
                            add(new FakeFrameRateReporterAggregateOutput(
                                    FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID));
                        }}));
    }

    @Test
    void testReportFrameRateAndCurrentActualFps() {
        float actualFps = 123f;

        _frameRateReporter.reportFrameRate(_startingDatetime, null, actualFps);

        assertEquals((Float)actualFps, _frameRateReporter.currentActualFps());
    }

    @Test
    void testReportFrameRateWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportFrameRate(_startingDatetime, -0.0001f,
                        0f));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportFrameRate(_startingDatetime, null,
                        -0.0001f));
    }

    @Test
    void testReportFrameRateForInvalidDatetimes() {
        float actualFps = 123f;

        _frameRateReporter.reportFrameRate(_startingDatetime, null, actualFps);

        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportFrameRate(_startingDatetime + 999, null,
                        actualFps));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportFrameRate(_startingDatetime + 1001, null,
                        actualFps));
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    void testReportFrameRateSendToAggregate() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = 0.6822f;
        Float targetFps3 = 0.2221f;
        Float targetFps4 = 0.7172f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2,
                actualFps2);
        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3,
                actualFps3);
        _frameRateReporter.reportFrameRate(_startingDatetime + 3000, targetFps4,
                actualFps4);

        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());
        assertEquals(new Date(_startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertEquals((Float)((targetFps1 / 3f) + (targetFps2 / 3f) + (targetFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertEquals((Float)((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    void testReportFrameRateSendToAggregateWithSomeNullTargetFps() {
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
        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2,
                actualFps2);
        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3,
                actualFps3);
        _frameRateReporter.reportFrameRate(_startingDatetime + 3000, targetFps4,
                actualFps4);
        _frameRateReporter.reportFrameRate(_startingDatetime + 4000, targetFps5,
                actualFps5);
        _frameRateReporter.reportFrameRate(_startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(_startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertEquals((Float)((targetFps1 / 2f) + (targetFps3 / 2f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertEquals((Float)((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));

        assertEquals(new Date(_startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(1));
        assertEquals((Float)((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(1));
        assertEquals((Float)((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(1));
    }

    @Test
    void testReportFrameRateSendToAggregateWithAllNullTargetFps() {
        Float targetFps1 = null;
        Float targetFps2 = null;
        Float targetFps3 = null;
        Float targetFps4 = 0.7172f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        float actualFps4 = 0.5632f;
        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2,
                actualFps2);
        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3,
                actualFps3);
        _frameRateReporter.reportFrameRate(_startingDatetime + 3000, targetFps4,
                actualFps4);

        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());
        assertEquals(new Date(_startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertEquals((Float)((actualFps1 / 3f) + (actualFps2 / 3f) + (actualFps3 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    void testDeactivateAndActivateAggregateOutput() {
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
        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2,
                actualFps2);

        _frameRateReporter.deactivateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3,
                actualFps3);
        _frameRateReporter.reportFrameRate(_startingDatetime + 3000, targetFps4,
                actualFps4);
        _frameRateReporter.reportFrameRate(_startingDatetime + 4000, targetFps5,
                actualFps5);

        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);

        _frameRateReporter.reportFrameRate(_startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(_startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertEquals((Float)((targetFps4 / 3f) + (targetFps5 / 3f) + (targetFps6 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertEquals((Float)((actualFps4 / 3f) + (actualFps5 / 3f) + (actualFps6 / 3f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));
    }

    @Test
    void testDeactivateAndActivateAggregateOutputWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.deactivateAggregateOutput(null));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.deactivateAggregateOutput(""));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.deactivateAggregateOutput("Invalid Id"));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.activateAggregateOutput(null));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.activateAggregateOutput(""));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.activateAggregateOutput("Invalid Id"));
    }

    @Test
    void testPausedPeriodCausesCurrentFpsToReturnNull() {
        Float targetFps1 = 0.7269f;
        float actualFps1 = 0.2626f;

        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);

        assertNotNull(_frameRateReporter.currentActualFps());

        _frameRateReporter.reportPause(_startingDatetime + 1500);

        assertNull(_frameRateReporter.currentActualFps());

        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps1, actualFps1);

        assertNull(_frameRateReporter.currentActualFps());

        _frameRateReporter.reportUnpause(_startingDatetime + 2500);

        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps1, actualFps1);

        assertNotNull(_frameRateReporter.currentActualFps());
    }

    @SuppressWarnings("WrapperTypeMayBePrimitive")
    @Test
    void testReportPauseAndUnpauseSendsCurrentPeriodWeightedByPausePercentageToOutputs() {
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
        _frameRateReporter.activateAggregateOutput(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT_ID);



        // 100% of period 1
        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);

        _frameRateReporter.reportPause(_startingDatetime + 1800);

        // 80% of period 2
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2,
                actualFps2);

        // 0% of period 3
        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3,
                actualFps3);

        _frameRateReporter.reportUnpause(_startingDatetime + 3500);

        // 50% of period 4
        _frameRateReporter.reportFrameRate(_startingDatetime + 3000, targetFps4,
                actualFps4);

        _frameRateReporter.reportPause(_startingDatetime + 4100);

        _frameRateReporter.reportUnpause(_startingDatetime + 4400);

        // 70% of period 5
        _frameRateReporter.reportFrameRate(_startingDatetime + 4000, targetFps5,
                actualFps5);

        // 100% of period 6, but 0% of targetFps for period 6
        _frameRateReporter.reportFrameRate(_startingDatetime + 5000, targetFps6,
                actualFps6);



        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(2,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(_startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertEquals((Float)((targetFps1 / 1.8f) + ((targetFps2 * 0.8f) / 1.8f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertEquals((Float)((actualFps1 / 1.8f) + ((actualFps2 * 0.8f) / 1.8f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));

        assertEquals(new Date(_startingDatetime + 3000),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(1));
        assertEquals((Float)(((targetFps4 * 0.5f) / 1.2f) + ((targetFps5 * 0.7f) / 1.2f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(1));
        assertEquals((Float)(((actualFps4 * 0.5f) / 2.2f) + ((actualFps5 * 0.7f) / 2.2f) +
                        ((actualFps6 * 1.0f) / 2.2f)),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(1));
    }

    @Test
    void testReportEntirelyPausedPeriodSendsNullValues() {
        Float targetFps1 = 0.7269f;
        Float targetFps2 = 0.6822f;
        Float targetFps3 = 0.2221f;
        float actualFps1 = 0.2626f;
        float actualFps2 = 0.1266f;
        float actualFps3 = 0.5525f;
        _frameRateReporter.reportPause(_startingDatetime - MS_PER_SECOND);



        _frameRateReporter.reportFrameRate(_startingDatetime, targetFps1, actualFps1);
        _frameRateReporter.reportFrameRate(_startingDatetime + 1000, targetFps2, actualFps2);
        _frameRateReporter.reportFrameRate(_startingDatetime + 2000, targetFps3, actualFps3);



        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.size());
        assertEquals(1,
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.size());

        assertEquals(new Date(_startingDatetime),
                FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateDates.get(0));
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateTargetFps.get(0));
        assertNull(FRAME_RATE_REPORTER_AGGREGATE_OUTPUT.OutputtedAggregateActualFps.get(0));
    }

    @Test
    void testReportPauseAndUnpauseBeyondCurrentPeriod() {
        float actualFps = 123f;

        _frameRateReporter.reportFrameRate(_startingDatetime, null, actualFps);

        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportPause(_startingDatetime + MS_PER_SECOND - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportPause(_startingDatetime + (MS_PER_SECOND * 2)));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportUnpause(_startingDatetime + MS_PER_SECOND - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _frameRateReporter.reportUnpause(_startingDatetime + (MS_PER_SECOND * 2)));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FrameRateReporter.class.getCanonicalName(),
                _frameRateReporter.getInterfaceName());
    }
}
