package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.R
import ch.kra.wardrobe.core.UIText
import ch.kra.wardrobe.core.ValidationResult

class ValidateClothe {
    operator fun invoke(clothe: String): ValidationResult {
        if (clothe.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UIText.StringResource(R.string.clothe_empty_error)
            )
        }
        return ValidationResult(successful = true)
    }
}