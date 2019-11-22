package fr.ledermann.axel.projet_samp.model

import android.provider.BaseColumns

/*
 * This class is the database model for the answer table.
 */
class AnswerDBTable : BaseColumns {
    companion object {
        const val NAME = "answer"
        const val ID = "id"
        const val TEXT = "text"
        const val IS_OK = "is_ok"
        const val ID_QUESTION = "id_question"
    }
}