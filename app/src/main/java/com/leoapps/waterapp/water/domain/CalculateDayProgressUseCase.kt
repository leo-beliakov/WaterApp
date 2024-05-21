package com.leoapps.waterapp.water.domain

import com.leoapps.waterapp.water.domain.model.BalanceDayProgress
import com.leoapps.waterapp.water.domain.model.WaterData
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class CalculateDayProgressUseCase @Inject constructor() {

    operator fun invoke(waterData: WaterData): BalanceDayProgress {
        val goal = waterData.goal
        val records = waterData.records

        val now = ZonedDateTime.now(ZoneId.of("UTC"))
        val startOfDay = now.toLocalDate().atStartOfDay(ZoneId.of("UTC"))
        val endOfDay = startOfDay.plusDays(1)

        val currentDayRecords = records.filter { record ->
            val recordTime = record.timestamp.toDate().toInstant().atZone(ZoneId.of("UTC"))
            !recordTime.isBefore(startOfDay) && recordTime.isBefore(endOfDay)
        }

        val totalAmount = currentDayRecords.sumOf { it.drink.balanceImpact }
        val percentage = if (goal > 0) (totalAmount.toFloat() / goal) else 0f

        return BalanceDayProgress(balanceMl = totalAmount, progress = percentage)
    }
}
