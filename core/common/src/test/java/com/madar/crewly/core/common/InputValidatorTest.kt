package com.madar.crewly.core.common

import com.madar.crewly.core.common.validation.Field
import com.madar.crewly.core.common.validation.InputValidator
import com.madar.crewly.core.common.validation.ValidationResult
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `validate with valid input should return all valid results`() {
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.NAME] is ValidationResult.Valid)
        assertTrue(result[Field.AGE] is ValidationResult.Valid)
        assertTrue(result[Field.JOB_TITLE] is ValidationResult.Valid)
        assertTrue(result[Field.GENDER] is ValidationResult.Valid)
    }

    @Test
    fun `validateName with empty name should return invalid`() {
        val result = InputValidator.validate(
            name = "",
            age = "30",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.NAME] is ValidationResult.Invalid)
    }

    @Test
    fun `validateName with blank name should return invalid`() {
        val result = InputValidator.validate(
            name = "   ",
            age = "30",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.NAME] is ValidationResult.Invalid)
    }

    @Test
    fun `validateName with name exceeding max length should return invalid`() {
        val longName = "A".repeat(51)
        val result = InputValidator.validate(
            name = longName,
            age = "30",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.NAME] is ValidationResult.Invalid)
    }

    @Test
    fun `validateAge with empty age should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.AGE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateAge with non-numeric age should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "abc",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.AGE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateAge with age below minimum should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "0",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.AGE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateAge with age above maximum should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "121",
            jobTitle = "Engineer",
            gender = "Male"
        )

        assertTrue(result[Field.AGE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateJobTitle with empty job title should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = "",
            gender = "Male"
        )

        assertTrue(result[Field.JOB_TITLE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateJobTitle with job title exceeding max length should return invalid`() {
        val longJobTitle = "A".repeat(101)
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = longJobTitle,
            gender = "Male"
        )

        assertTrue(result[Field.JOB_TITLE] is ValidationResult.Invalid)
    }

    @Test
    fun `validateGender with null gender should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = "Engineer",
            gender = null
        )

        assertTrue(result[Field.GENDER] is ValidationResult.Invalid)
    }

    @Test
    fun `validateGender with empty gender should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = "Engineer",
            gender = ""
        )

        assertTrue(result[Field.GENDER] is ValidationResult.Invalid)
    }

    @Test
    fun `validateGender with blank gender should return invalid`() {
        val result = InputValidator.validate(
            name = "John",
            age = "30",
            jobTitle = "Engineer",
            gender = "   "
        )

        assertTrue(result[Field.GENDER] is ValidationResult.Invalid)
    }
}
