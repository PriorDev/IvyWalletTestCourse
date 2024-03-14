package com.ivy.core.domain.algorithm.calc

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.persistence.algorithm.calc.CalcTrn
import com.ivy.data.transaction.TransactionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawStatsTest {
    @Test
    fun `test raw stats`() {
        val transactions = listOf(
            CalcTrn(10.0, "MXN", TransactionType.Income, Instant.now()),
            CalcTrn(1000.0, "MXN", TransactionType.Income, Instant.now()),
            CalcTrn(20.0, "MXN", TransactionType.Expense, Instant.now()),
            CalcTrn(50.0, "MXN", TransactionType.Expense, Instant.now())
        )

        val result = rawStats(transactions)

        assertThat(result.expensesCount).isEqualTo(2)
        assertThat(result.incomesCount).isEqualTo(2)
    }
}