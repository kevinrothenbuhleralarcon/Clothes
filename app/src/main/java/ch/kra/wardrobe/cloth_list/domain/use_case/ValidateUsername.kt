package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.R
import ch.kra.wardrobe.core.UIText
import ch.kra.wardrobe.core.ValidationResult

class ValidateUsername {
    operator fun invoke(username: String): ValidationResult {
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UIText.StringResource(R.string.username_empty_error)
            )
        }
        return ValidationResult(successful = true)
    }
}