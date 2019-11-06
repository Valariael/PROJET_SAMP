package fr.ledermann.axel.projet_samp

import android.provider.BaseColumns

class AnswerDBTable : BaseColumns {
    companion object {
        const val NAME = "answer"
        const val ID = "id"
        const val TEXT = "text"
        const val IS_OK = "is_ok"
        const val ID_QUESTION = "id_question"
    }
}