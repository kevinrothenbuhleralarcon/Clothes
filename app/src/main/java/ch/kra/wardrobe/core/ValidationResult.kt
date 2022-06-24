package ch.kra.wardrobe.core

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UIText? = null
)
