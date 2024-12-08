package com.dalhousie.habit.data.remote.response

import com.dalhousie.habit.model.Habit
import java.time.LocalDate
import java.time.Month

data class HabitStatisticsResponse(
    override val resultType: String,
    override val data: Data,
    override val message: String
) : BaseResponse<HabitStatisticsResponse.Data> {

    data class Data(
        val data: List<MonthYearAndStatistics>
    ) {

        data class MonthYearAndStatistics(
            val monthYear: MonthYear,
            val habitStatistics: List<HabitStatistics>
        ) {

            data class MonthYear(
                val month: String,
                val year: String
            ) {

                val monthYearString: String
                    get() = "$month $year"

                val numberOfDaysInMonth: Int
                    get() = when (Month.valueOf(month.uppercase())) {
                        Month.JANUARY, Month.MARCH, Month.MAY, Month.JULY,
                        Month.AUGUST, Month.OCTOBER, Month.DECEMBER -> 31

                        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30

                        Month.FEBRUARY -> if (year.toInt() % 4 == 0) 28 else 29
                    }

                fun getDateForDayOfMonth(dayOfMonth: Int): LocalDate =
                    LocalDate.of(year.toInt(), Month.valueOf(month.uppercase()), dayOfMonth)
            }

            data class HabitStatistics(
                val habit: Habit,
                val completionDates: List<String>
            ) {
                val completionLocalDates: List<LocalDate>
                    get() = completionDates.map { LocalDate.parse(it) }
            }
        }
    }
}
