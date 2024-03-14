package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.parser.Parser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ExpressionParserTest {
    private lateinit var parser: Parser<TreeNode>
    @BeforeEach
    fun setUp() {
        parser = expressionParser()
    }

    @ParameterizedTest
    @CsvSource(
        "3+6+(3/3)+(10/2), 15.0",
        "26-(3*2), 20.0",
        "(3*3)+(2*4), 17",
        "-(1*8)+(15/3)+1, -2"
    )
    fun `Test evaluation expression`(expression: String, resultExpected: Double) {
        val result = parser(expression).first()

        val actual = result.value.eval()

        assertThat(actual).isEqualTo(resultExpected)
    }
}