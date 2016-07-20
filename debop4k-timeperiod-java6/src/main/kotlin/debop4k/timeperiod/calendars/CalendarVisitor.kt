/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.timeranges.*
import org.slf4j.LoggerFactory

/**
 * 특정 기간에 대한 필터링 정보를 기반으로 기간들을 필터링 할 수 있도록 특정 기간을 탐색하는 Visitor입니다.
 * Created by debop
 */
abstract class CalendarVisitor<out F : ICalendarVisitorFilter, in C : ICalendarVisitorContext>(
    val filter: F,
    val limits: ITimePeriod,
    val seekDirection: SeekDirection = SeekDirection.Forward,
    val calendar: ITimeCalendar = DefaultTimeCalendar) {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    val MAX_PERIOD = TimeRange(MinPeriodTime, MaxPeriodTime.minusYears(1))
  }

  val isForward: Boolean
    get() = seekDirection == SeekDirection.Forward

  protected fun startPeriodVisit(context: C): Unit {
    startPeriodVisit(limits, context)
  }

  protected fun startPeriodVisit(period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitYears(yearsToVisit: MutableList<YearRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitMonths(monthsToVisit: MutableList<MonthRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitDays(daysToVisit: MutableList<DayRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitHours(hoursToVisit: MutableList<HourRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  open protected fun startYearVisit(year: YearRange, context: C, seekDirection: SeekDirection): YearRange {
    TODO()
  }

  open protected fun startMonthVisit(month: MonthRange, context: C, seekDirection: SeekDirection): MonthRange {
    TODO()
  }

  open protected fun startDayVisit(day: DayRange, context: C, seekDirection: SeekDirection): DayRange {
    TODO()
  }

  open protected fun startHourVisit(hour: HourRange, context: C, seekDirection: SeekDirection): HourRange {
    TODO()
  }

  open protected fun onVisitStart(): Unit {
  }

  open protected fun onVisitEnd(): Unit {
  }

  protected fun checkLimits(target: ITimePeriod): Boolean {
    return limits.hasInside(target)
  }

  protected fun checkExcludePeriods(target: ITimePeriod): Boolean {
    return filter.excludePeriods.isEmpty()
           || filter.excludePeriods.overlapPeriods(target).isEmpty()
  }

  open protected fun enterYears(years: YearRangeCollection, context: C): Boolean = true
  open protected fun enterMonths(year: YearRange, context: C): Boolean = true
  open protected fun enterDays(month: MonthRange, context: C): Boolean = true
  open protected fun enterHours(day: DayRange, context: C): Boolean = true
  open protected fun enterMinutes(hour: HourRange, context: C): Boolean = true

  open protected fun onVisitYears(years: YearRangeCollection, context: C): Boolean = true
  open protected fun onVisitYear(year: YearRange, context: C): Boolean = true
  open protected fun onVisitMonth(month: MonthRange, context: C): Boolean = true
  open protected fun onVisitDay(day: DayRange, context: C): Boolean = true
  open protected fun onVisitHour(hour: HourRange, context: C): Boolean = true
  open protected fun onVisitMinute(minute: MinuteRange, context: C): Boolean = true

  open protected fun isMatchingYear(yr: YearRange, context: C): Boolean {
    TODO()
  }

  open protected fun isMatchingMonth(mr: MonthRange, context: C): Boolean {
    TODO()
  }

  open protected fun isMatchingDay(dr: DayRange, context: C): Boolean {
    TODO()
  }

  open protected fun isMatchingHour(hr: HourRange, context: C): Boolean {
    TODO()
  }

  open protected fun isMatchingMinute(mr: MinuteRange, context: C): Boolean {
    TODO()
  }

}
