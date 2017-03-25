package io.skysail.server.ext.metrics;

import com.codahale.metrics.Snapshot;

import io.skysail.api.metrics.TimerDataProvider;
import lombok.Getter;

public class TimerSnapshot implements TimerDataProvider {

	//private double durationFactor = 1000 * 1000;

	@Getter
	private long count;

	@Getter
	private double max;

	@Getter
	private double mean;

	@Getter
	private double min;

	@Getter
	private String name;

	public TimerSnapshot(String name, long counter, Snapshot s) {
		
		this.name = name;
		//		 this.rateUnit = calculateRateUnit(rateUnit, "calls");
//         this.rateFactor = rateUnit.toSeconds(1);
//         this.durationUnit = durationUnit.toString().toLowerCase(Locale.US);
        double durationFactor = 1.0 / 1000000;//durationUnit.toNanos(1);
//         this.showSamples = showSamples;
		
        this.count = counter;
        this.max = s.getMax() * durationFactor;
        this.mean = s.getMean() * durationFactor;
        this.min = s.getMin() * durationFactor;

//        json.writeNumberField("p50", snapshot.getMedian() * durationFactor);
//        json.writeNumberField("p75", snapshot.get75thPercentile() * durationFactor);
//        json.writeNumberField("p95", snapshot.get95thPercentile() * durationFactor);
//        json.writeNumberField("p98", snapshot.get98thPercentile() * durationFactor);
//        json.writeNumberField("p99", snapshot.get99thPercentile() * durationFactor);
//        json.writeNumberField("p999", snapshot.get999thPercentile() * durationFactor);
//
//        if (showSamples) {
//            final long[] values = snapshot.getValues();
//            final double[] scaledValues = new double[values.length];
//            for (int i = 0; i < values.length; i++) {
//                scaledValues[i] = values[i] * durationFactor;
//            }
//            json.writeObjectField("values", scaledValues);
//        }
//
//        json.writeNumberField("stddev", snapshot.getStdDev() * durationFactor);
//        json.writeNumberField("m15_rate", timer.getFifteenMinuteRate() * rateFactor);
//        json.writeNumberField("m1_rate", timer.getOneMinuteRate() * rateFactor);
//        json.writeNumberField("m5_rate", timer.getFiveMinuteRate() * rateFactor);
//        json.writeNumberField("mean_rate", timer.getMeanRate() * rateFactor);
//        json.writeStringField("duration_units", durationUnit);
//json.writeStringField("rate_units", rateUnit);
	}

}
