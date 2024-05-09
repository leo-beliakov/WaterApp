package com.leoapps.waterapp.auth.signup.domain

import org.junit.Assert
import org.junit.Test


class ValidateNameUseCaseTest {

    private val sut = ValidateNameUseCase()

    @Test
    fun `null, blank or empty name should not be valid`() {
        //Given
        val names = listOf(null, "", " ")

        //When
        val results = names.map { name -> sut(name) }

        //Then
        results.forEach { result ->
            Assert.assertFalse(result)
        }
    }

    @Test
    fun `single word name should not be valid`() {
        //Given
        val name = "Ivan"

        //When
        val result = sut(name)

        //Then
        Assert.assertFalse(result)
    }

    @Test
    fun `name with digits should not be valid! Sorry, Ilon`() {
        //Given
        val name = "Ivan123 Sandrik123"

        //When
        val result = sut(name)

        //Then
        Assert.assertFalse(result)
    }

    @Test
    fun `name with hyphens should be valid`() {
        //Given
        val name = "Chloe-Jasmine"

        //When
        val result = sut(name)

        //Then
        Assert.assertTrue(result)
    }

    @Test
    fun `name with spaces should be valid`() {
        //Given
        val name = "Chloe Jasmine"

        //When
        val result = sut(name)

        //Then
        Assert.assertTrue(result)
    }

    @Test
    fun `name with more than 2 words should be valid`() {
        //Given
        val name = "Chloe-ibn al Jasmine"

        //When
        val result = sut(name)

        //Then
        Assert.assertTrue(result)
    }
}