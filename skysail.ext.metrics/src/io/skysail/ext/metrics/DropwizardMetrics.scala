package io.skysail.ext.metrics

import java.util.concurrent.{ ConcurrentHashMap, TimeUnit }

import com.codahale.metrics._
import io.skysail.api.metrics._
import org.osgi.service.component.annotations.{ Activate, Component }
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

@Component(immediate = true)
class DropwizardMetrics extends Metrics {

  private val log = LoggerFactory.getLogger(this.getClass())

  private val metrics = new MetricRegistry
  private val timers = new ConcurrentHashMap[String, Timer]
  private val meters = new ConcurrentHashMap[String, Meter]
  private val counters = new ConcurrentHashMap[String, Counter]
  private val histograms = new ConcurrentHashMap[String, Histogram]

  @Activate protected def activate(): Unit = {
    val reporter = ConsoleReporter.forRegistry(metrics).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build
    reporter.start(60, TimeUnit.MINUTES)
  }

  override def registerTimer(metric: TimerMetric): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val timer = metrics.timer(name)
    timers.put(name, timer)
  }

  override def time(timerMetric: TimerMetric): Stoppable = {
    val timer = timers.get(MetricRegistry.name(timerMetric.cls.getClass.getName, timerMetric.identifier))
    if (timer != null) {
      val time = timer.time
      return new Stoppable() {
        override def stop(): Unit = {
          time.stop
        }
      }
    }
    new Stoppable() {
      override def stop(): Unit = {
      }
    }
  }

  override def registerMeter(metric: MeterMetric): Unit = {
    val meter = metrics.meter(metric.identifier)
    meters.put(metric.identifier, meter)
  }

  override def meter(metric: MeterMetric): Unit = {
    metrics.meter(metric.identifier).mark()
  }

  override def registerCounter(metric: CounterMetric): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val counter = metrics.counter(name)
    counters.put(name, counter)
  }

  override def inc(metric: CounterMetric): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val counter = counters.get(name)
    if (counter != null) counter.inc()
    else log.warn("trying to increase counter metric which does not exist: {}", name)
  }

  override def dec(metric: CounterMetric): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val counter = counters.get(name)
    if (counter != null) counter.dec()
    else log.warn("trying to decrease counter metric which does not exist: {}", name)
  }

  override def registerHistogram(metric: HistogramMetric): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val histogram = metrics.histogram(name)
    histograms.put(name, histogram)
  }

  override def update(metric: HistogramMetric, value: Long): Unit = {
    val name = MetricRegistry.name(metric.cls.getClass.getName, metric.identifier)
    val histogram = histograms.get(name)
    if (histogram != null) histogram.update(value)
    else log.warn("trying to update histogram metric which does not exist: {}", name)
  }

  override def getTimers(): scala.collection.immutable.List[TimerDataProvider] = {
    metrics.getTimers().asScala.keySet
      .map { key => TimerSnapshot(key, metrics.getTimers().get(key).getCount(), metrics.getTimers().get(key).getSnapshot()) }
      .map { snapshot => snapshot.asInstanceOf[TimerDataProvider] }
      .toList
  }
}
