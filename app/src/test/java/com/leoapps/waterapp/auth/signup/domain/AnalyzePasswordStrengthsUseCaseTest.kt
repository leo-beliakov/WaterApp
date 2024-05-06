package com.leoapps.waterapp.auth.signup.domain

import org.junit.Assert
import org.junit.Test

class AnalyzePasswordStrengthsUseCaseTest {

    private val sut = ValidatePasswordUseCase()

    @Test
    fun `null password should return no strengths`() {
        //Given
        val password = null

        //When
        val result = sut(password)

        //Then
        Assert.assertTrue(result.none { (strength, isPresent) -> isPresent })
    }

    @Test
    fun `blank password should return no strengths`() {
        //Given
        val password = "   "

        //When
        val result = sut(password)

        //Then
        Assert.assertTrue(result.none { (strength, isPresent) -> isPresent })
    }

    @Test
    fun `WORST password should return no strengths`() {
        //Given
        val password = ". ;"

        //When
        val result = sut(password)

        //Then
        Assert.assertTrue(result.none { (strength, isPresent) -> isPresent })
    }

    @Test
    fun `only digits password should return corresponding strength`() {
        //Given
        val password = "12345"

        //When
        val result = sut(password)

        //Then
        val digitStrength = result[PasswordStrength.DIGITS_PRESENT]
        Assert.assertTrue(digitStrength != null)
        Assert.assertTrue(digitStrength == true)
    }

    @Test
    fun `only lowercase password should return corresponding strength`() {
        //Given
        val password = "qwerty"

        //When
        val result = sut(password)

        //Then
        val lowercaseStrength = result[PasswordStrength.LOWERCASE_LETTERS_PRESENT]
        Assert.assertTrue(lowercaseStrength != null)
        Assert.assertTrue(lowercaseStrength == true)
    }

    @Test
    fun `only uppercase password should return corresponding strength`() {
        //Given
        val password = "QWERTY"

        //When
        val result = sut(password)

        //Then
        val uppercaseStrength = result[PasswordStrength.UPPERCASE_LETTERS_PRESENT]
        Assert.assertTrue(uppercaseStrength != null)
        Assert.assertTrue(uppercaseStrength == true)
    }

    @Test
    fun `only special chars password should return corresponding strength`() {
        //Given
        val password = "@!%#"

        //When
        val result = sut(password)

        //Then
        val specialCharStrength = result[PasswordStrength.SPECIAL_CHAR_PRESENT]
        Assert.assertTrue(specialCharStrength != null)
        Assert.assertTrue(specialCharStrength == true)
    }

    @Test
    fun `password with white spaces shouldn't return corresponding strength`() {
        //Given
        val password = "one two"

        //When
        val result = sut(password)

        //Then
        val noWhiteSpacesStrength = result[PasswordStrength.NO_WHITE_SPACES]
        Assert.assertTrue(noWhiteSpacesStrength != null)
        Assert.assertTrue(noWhiteSpacesStrength == false)
    }

    @Test
    fun `long password should return corresponding strength`() {
        //Given
        val password = "longPassword"

        //When
        val result = sut(password)

        //Then
        val lengthStrength = result[PasswordStrength.VALID_LENGTH]
        Assert.assertTrue(lengthStrength != null)
        Assert.assertTrue(lengthStrength == true)
    }

    @Test
    fun `IDEAL password should return ALL strengths`() {
        //Given
        val password = "12qwER!$%word"

        //When
        val result = sut(password)

        //Then
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertTrue(result.all { (strength, isPresent) -> isPresent })
    }
}