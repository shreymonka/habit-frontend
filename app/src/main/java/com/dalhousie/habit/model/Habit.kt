package com.dalhousie.habit.model

import android.content.Context
import android.os.Parcelable
import android.text.SpannedString
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.dalhousie.habit.R
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek

@Parcelize
data class Habit(
    val id: String,
    val userId: String,
    val name: String,
    val creationDate: String,
    val schedule: List<String>
) : Parcelable {

    fun getSpannedSchedule(context: Context): SpannedString = buildSpannedString {
        val upperSchedule = schedule.map { it.uppercase() }
        DayOfWeek.entries.forEach {
            bold {
                color(
                    ContextCompat.getColor(
                        context,
                        if (upperSchedule.contains(it.name)) R.color.app_text else R.color.app_secondary
                    )
                ) {
                    append(
                        when (it) {
                            DayOfWeek.THURSDAY -> "Tr"
                            DayOfWeek.SUNDAY -> "Su"
                            else -> it.name.first().toString()
                        }
                    )
                    append("  ")
                }
            }
        }
    }
}
