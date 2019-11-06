package fr.ledermann.axel.projet_samp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuizzDBHelper(val context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS " + QuizzDBTable.NAME + ", " + QuestionDBTable.NAME + ", " + AnswerDBTable.NAME)
            onCreate(db)
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATE_QUIZZ)
        db.execSQL(DATABASE_CREATE_QUESTION)
        db.execSQL(DATABASE_CREATE_ANSWER)
    }

    fun getAllQuizz(): ArrayList<Quizz> {
        val quizzList = ArrayList<Quizz>()
        val selectQuizzQuery = "SELECT  * FROM " + QuizzDBTable.NAME
        val db = this.readableDatabase
        val cQuizz = db.rawQuery(selectQuizzQuery, null)

        if (cQuizz.moveToFirst()) {
            do {
                val quizz = Quizz(cQuizz.getString(cQuizz.getColumnIndex(QuizzDBTable.TITLE)))
                quizz.idQuizz = cQuizz.getLong(cQuizz.getColumnIndex(QuizzDBTable.ID))
                val questionList = ArrayList<Question>()
                val selectQuestionQuery = "SELECT  * FROM " + QuestionDBTable.NAME + " WHERE " + QuestionDBTable.ID_QUIZZ + " = " + quizz.idQuizz
                val cQuestion = db.rawQuery(selectQuestionQuery, null)

                if (cQuestion.moveToFirst()) {
                    do {
                        val question = Question(cQuestion.getString(cQuestion.getColumnIndex(QuestionDBTable.TEXT)))
                        question.idQuestion = cQuestion.getLong(cQuestion.getColumnIndex(QuizzDBTable.ID))
                        val answerList = ArrayList<Answer>()
                        val selectAnswerQuery = "SELECT  * FROM " + QuestionDBTable.NAME + " WHERE " + QuestionDBTable.ID_QUIZZ + " = " + question.idQuestion
                        val cAnswer = db.rawQuery(selectAnswerQuery, null)

                        if (cAnswer.moveToFirst()) {
                            do {
                                val answer = Answer(cAnswer.getString(cAnswer.getColumnIndex(AnswerDBTable.TEXT)), cAnswer.getInt(cAnswer.getColumnIndex(AnswerDBTable.IS_OK)) != 0)
                                answer.idAnswer = cAnswer.getLong(cAnswer.getColumnIndex(AnswerDBTable.ID))
                                answerList.add(answer)
                            } while (cAnswer.moveToNext())
                        }
                        cAnswer.close()

                        question.listAnswers = answerList
                        questionList.add(question)
                    } while (cQuestion.moveToNext())
                }
                cQuestion.close()

                quizz.listQuestions = questionList
                quizzList.add(quizz)
            } while (cQuizz.moveToNext())
        }
        cQuizz.close()

        return quizzList
    }

    fun deleteQuizz(id : Int) {
        val db = this.writableDatabase

    }

    fun newQuizz(quizz : Quizz): Long {
        val db = this.writableDatabase
        val quizzValues = ContentValues()
        quizzValues.put(QuizzDBTable.TITLE, quizz.titleQuizz)
        return db.insert(QuizzDBTable.NAME, null, quizzValues)
    }

    companion object {
        const val DATABASE_NAME = "quizz.db"
        const val DATABASE_VERSION = 1
        const val DATABASE_CREATE_QUIZZ = "CREATE TABLE " + QuizzDBTable.NAME + " (" + QuizzDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuizzDBTable.TITLE + " TEXT NOT NULL);"
        const val DATABASE_CREATE_QUESTION = "CREATE TABLE " + QuestionDBTable.NAME + " (" + QuestionDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuestionDBTable.TEXT + " TEXT NOT NULL, " + QuestionDBTable.ID_QUIZZ + " INTEGER NOT NULL);"
        const val DATABASE_CREATE_ANSWER = "CREATE TABLE " + AnswerDBTable.NAME + " (" + AnswerDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AnswerDBTable.TEXT + " TEXT NOT NULL, " + AnswerDBTable.IS_OK + " INTEGER NOT NULL, " + AnswerDBTable.ID_QUESTION + " INTEGER NOT NULL);"
    }
}