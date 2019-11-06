package fr.ledermann.axel.projet_samp

import android.provider.BaseColumns

class QuizzDBTable : BaseColumns {
    companion object {
        const val NAME = "quizz"
        const val ID = "id"
        const val TITLE = "title"
    }
}