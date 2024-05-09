package com.leoapps.waterapp.auth.signup.domain

import org.junit.Assert
import org.junit.Test


class ValidateEmailUseCaseTest {

    private val sut = ValidateEmailUseCase()

    @Test
    fun `null, blank or empty email should not be valid`() {
        //Given
        val emails = listOf(null, "", " ")

        //When
        val results = emails.map { email -> sut(email) }

        //Then
        results.forEach { result ->
            Assert.assertFalse(result)
        }
    }

    @Test
    fun `email cannot start with a dot`() {
        //Given
        val email = ".email@mail.com"

        //When
        val result = sut(email)

        //Then
        Assert.assertFalse(result)
    }

    @Test
    fun `emails may contain dots, underscores, plus signs, ampersands, asterisks, and hyphens`() {
        //Given
        val emails = listOf(
            "my.email@a.su",
            "my_email@a.su",
            "my+email@a.su",
            "my&email@a.su",
            "my*email@a.su",
            "my-email@a.su",
        )

        //When
        val results = emails.map { email -> sut(email) }

        //Then
        results.forEach { result ->
            Assert.assertTrue(result)
        }
    }

    @Test
    fun `emails should have @ symbol`() {
        //Given
        val emailWithoutSymbol = "emailmail.com"
        val emailWithSymbol = "email@mail.com"

        //When
        val resultForEmailWithoutSymbol = sut(emailWithoutSymbol)
        val resultForEmailWithSymbol = sut(emailWithSymbol)

        //Then
        Assert.assertFalse(resultForEmailWithoutSymbol)
        Assert.assertTrue(resultForEmailWithSymbol)
    }

    @Test
    fun `emails should have domain suffix of 2-7 letters with dots`() {
        //Given
        val emailsWithoutProperSuffix = listOf(
            "email@",
            "email@a.",
            "email@a.a",
        )
        val emailsWithSuffix = listOf(
            "email@mail.co",
            "email@mail.com",
            "email@mail.private.com",
            "email@mail.private.com.au",
        )

        //When
        val resultsForEmailWithoutProperSuffix = emailsWithoutProperSuffix.map { email ->
            sut(email)
        }
        val resultsForEmailWithSuffix = emailsWithSuffix.map { email ->
            sut(email)
        }

        //Then
        resultsForEmailWithoutProperSuffix.forEach { result ->
            Assert.assertFalse(result)
        }
        resultsForEmailWithSuffix.forEach { result ->
            Assert.assertTrue(result)
        }
    }
}