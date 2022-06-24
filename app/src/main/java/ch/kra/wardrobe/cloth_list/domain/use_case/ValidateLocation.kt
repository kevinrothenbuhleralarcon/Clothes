package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.R
import ch.kra.wardrobe.core.UIText
import ch.kra.wardrobe.core.ValidationResult

class ValidateLocation {
    operator fun invoke(location: String): ValidationResult {
        if (location.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UIText.StringResource(R.string.location_empty_error)
            )
        }
        return ValidationResult(successful = true)
    }
}