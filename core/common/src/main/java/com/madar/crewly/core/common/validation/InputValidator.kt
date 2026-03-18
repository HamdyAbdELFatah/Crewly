package com.madar.crewly.core.common.validation

import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.constants.AppConstants
import com.madar.crewly.core.common.ui.UiText

enum class Field {
    NAME, AGE, JOB_TITLE, GENDER
}

object InputValidator {
    fun validate(
        name: String,
        age: String,
        jobTitle: String,
        gender: String?
    ): Map<Field, ValidationResult> {
        return mapOf(
            Field.NAME to validateName(name),
            Field.AGE to validateAge(age),
            Field.JOB_TITLE to validateJobTitle(jobTitle),
            Field.GENDER to validateGender(gender)
        )
    }

    private fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_name_empty))
            name.length > AppConstants.NAME_MAX_LENGTH -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_name_too_long, arrayOf(AppConstants.NAME_MAX_LENGTH.toString())))
            else -> ValidationResult.Valid
        }
    }

    private fun validateAge(age: String): ValidationResult {
        val ageInt = age.toIntOrNull()
        return when {
            age.isBlank() -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_age_empty))
            ageInt == null -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_age_invalid))
            ageInt < AppConstants.AGE_MIN || ageInt > AppConstants.AGE_MAX -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_age_out_of_range, arrayOf(
                    AppConstants.AGE_MIN.toString(),
                    AppConstants.AGE_MAX.toString())))
            else -> ValidationResult.Valid
        }
    }

    private fun validateJobTitle(jobTitle: String): ValidationResult {
        return when {
            jobTitle.isBlank() -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_job_empty))
            jobTitle.length > AppConstants.JOB_MAX_LENGTH -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_job_too_long, arrayOf(AppConstants.JOB_MAX_LENGTH.toString())))
            else -> ValidationResult.Valid
        }
    }

    private fun validateGender(gender: String?): ValidationResult {
        return when {
            gender.isNullOrBlank() -> ValidationResult.Invalid(
                UiText.StringResource(R.string.error_gender_empty))
            else -> ValidationResult.Valid
        }
    }
}
