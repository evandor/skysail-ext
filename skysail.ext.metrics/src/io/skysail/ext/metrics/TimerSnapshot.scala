package io.skysail.ext.metrics

import io.skysail.api.metrics.TimerDataProvider
import com.codahale.metrics.Snapshot
import scala.beans.BeanProperty

case class TimerSnapshot(
  @BeanProperty val name: String,
  @BeanProperty val count: Long,
  s: Snapshot) extends TimerDataProvider {

  val durationFactor = 1.0 / 1000000

  @BeanProperty val max = s.getMax * durationFactor
  @BeanProperty val mean = s.getMean * durationFactor
  @BeanProperty val min = s.getMin * durationFactor

}