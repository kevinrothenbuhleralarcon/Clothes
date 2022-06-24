package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.R
import ch.kra.wardrobe.core.UIText
import ch.kra.wardrobe.core.ValidationResult

class ValidateQuantity {
    operator fun invoke(quantity: Int): ValidationResult {
        if (quantity < 0) {
            return ValidationResult(
                successful = false,
                errorMessage = UIText.StringResource(R.string.quantity_error)
            )
        }
        return ValidationResult(successful = true)
    }
}