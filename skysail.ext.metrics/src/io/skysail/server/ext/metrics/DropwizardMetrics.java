package io.skysail.server.ext.metrics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import io.skysail.api.metrics.CounterMetric;
import io.skysail.api.metrics.HistogramMetric;
import io.skysail.api.metrics.MeterMetric;
import io.skysail.api.metrics.MetricsImplementation;
import io.skysail.api.metrics.Stoppable;
import io.skysail.api.metrics.TimerDataProvider;
import io.skysail.api.metrics.TimerMetric;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class DropwizardMetrics implements MetricsImplementation {

    private MetricRegistry metrics = new MetricRegistry();

    private Map<String, Timer> timers = new ConcurrentHashMap<>();
    private Map<String, Meter> meters = new ConcurrentHashMap<>();
    private Map<String, Counter> counters = new ConcurrentHashMap<>();
    private Map<String, Histogram> histograms = new ConcurrentHashMap<>();

    @Activate
    protected void activate() {
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(60, TimeUnit.MINUTES);
    }

    @Override
    public void registerTimer(TimerMetric metric) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Timer timer = metrics.timer(name);
        timers.put(name, timer);
    }

    @Override
    public Stoppable time(TimerMetric timerMetric) {
        Timer timer = timers.get(MetricRegistry.name(timerMetric.getCls(), timerMetric.getIdentifier()));
        if (timer != null) {
            Timer.Context time = timer.time();
            return new Stoppable() {
                @Override
                public void stop() {
                    time.stop();
                }
            };
        }
        return new Stoppable() {
            @Override
            public void stop() {
            }
        };
    }

    @Override
    public void registerMeter(MeterMetric metric) {
        Meter meter = metrics.meter(metric.getIdentifier());
        meters.put(metric.getIdentifier(), meter);
    }

    @Override
    public void meter(MeterMetric metric) {
        metrics.meter(metric.getIdentifier()).mark();
    }

    @Override
    public void registerCounter(CounterMetric metric) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Counter counter = metrics.counter(name);
        counters.put(name, counter);
    }

    @Override
    public void inc(CounterMetric metric) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.inc();
        } else {
            log.warn("trying to increase counter metric which does not exist: {}", name);
        }
    }

    @Override
    public void dec(CounterMetric metric) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Counter counter = counters.get(name);
        if (counter != null) {
            counter.dec();
        } else {
            log.warn("trying to decrease counter metric which does not exist: {}", name);
        }
    }

    @Override
    public void registerHistogram(HistogramMetric metric) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Histogram histogram = metrics.histogram(name);
        histograms.put(name, histogram);
    }

    @Override
    public void update(HistogramMetric metric, long value) {
        String name = MetricRegistry.name(metric.getCls(), metric.getIdentifier());
        Histogram histogram = histograms.get(name);
        if (histogram != null) {
            histogram.update(value);
        } else {
            log.warn("trying to update histogram metric which does not exist: {}", name);
        }
    }

    @Override
    public List<TimerDataProvider> getTimers() {
    	return metrics.getTimers().entrySet().stream()
    		.map(entry -> new TimerSnapshot(entry.getKey(), entry.getValue().getCount(), entry.getValue().getSnapshot()))
    		.map(TimerDataProvider.class::cast)
    		.collect(Collectors.toList());

    }

}
