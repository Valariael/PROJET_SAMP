package fr.ledermann.axel.projet_samp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

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

    fun reset() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${QuizzDBTable.NAME}")
        db.execSQL("DELETE FROM ${QuestionDBTable.NAME}")
        db.execSQL("DELETE FROM ${AnswerDBTable.NAME}")
    }

    fun getQuizzs(): ArrayList<Quizz> {
        val quizzList = ArrayList<Quizz>()
        val selectQuizzQuery = "SELECT  * FROM ${QuizzDBTable.NAME}"
        val db = this.readableDatabase
        val cQuizz = db.rawQuery(selectQuizzQuery, null)

        if (cQuizz.moveToFirst()) {
            do {
                val quizz = Quizz(cQuizz.getString(cQuizz.getColumnIndex(QuizzDBTable.TITLE)))
                quizz.idQuizz = cQuizz.getLong(cQuizz.getColumnIndex(QuizzDBTable.ID))
                quizzList.add(quizz)
            } while (cQuizz.moveToNext())
        }
        cQuizz.close()

        return quizzList
    }

    fun getQuestions(idQuizz: Long): ArrayList<Question> {
        val questionList = ArrayList<Question>()
        val selectQuestionQuery = "SELECT  * FROM ${QuestionDBTable.NAME} WHERE ${QuestionDBTable.ID_QUIZZ} = ?"
        val db = this.readableDatabase
        val cQuestion = db.rawQuery(selectQuestionQuery, arrayOf(idQuizz.toString()))

        if (cQuestion.moveToFirst()) {
            do {
                val question = Question(cQuestion.getString(cQuestion.getColumnIndex(QuestionDBTable.TEXT)))
                question.idQuizz = cQuestion.getLong(cQuestion.getColumnIndex(QuestionDBTable.ID_QUIZZ))
                question.idQuestion = cQuestion.getLong(cQuestion.getColumnIndex(QuestionDBTable.ID))
                questionList.add(question)
            } while (cQuestion.moveToNext())
        }
        cQuestion.close()

        return questionList
    }

    fun getAnswers(idQuestion: Long): ArrayList<Answer> {
        val answerList = ArrayList<Answer>()
        val selectAnswerQuery = "SELECT  * FROM ${AnswerDBTable.NAME} WHERE ${AnswerDBTable.ID_QUESTION} = ?"
        val db = this.readableDatabase
        val cAnswer = db.rawQuery(selectAnswerQuery, arrayOf(idQuestion.toString()))

        if (cAnswer.moveToFirst()) {
            do {
                val answer = Answer(cAnswer.getString(cAnswer.getColumnIndex(AnswerDBTable.TEXT)))
                answer.isOk = cAnswer.getInt(cAnswer.getColumnIndex(AnswerDBTable.IS_OK))>0
                Log.d("looooooooooooooogxx: ", answer.isOk.toString() + " : " + cAnswer.getInt(cAnswer.getColumnIndex(AnswerDBTable.IS_OK)))
                answer.idAnswer = cAnswer.getLong(cAnswer.getColumnIndex(AnswerDBTable.ID))
                answer.idQuestion = cAnswer.getLong(cAnswer.getColumnIndex(AnswerDBTable.ID_QUESTION))
                answerList.add(answer)
            } while (cAnswer.moveToNext())
        }
        cAnswer.close()

        return answerList
    }

    fun deleteQuizz(quizz : Quizz) {
        val db = this.writableDatabase
        db.delete(QuizzDBTable.NAME, "${QuizzDBTable.ID} = ?", arrayOf(quizz.idQuizz.toString()))

        val questions = db.query(QuestionDBTable.NAME, arrayOf(QuestionDBTable.ID), "${QuestionDBTable.ID_QUIZZ} = ?", arrayOf(quizz.idQuizz.toString()), null, null, null)
        val deleted = db.delete(QuestionDBTable.NAME, "${QuestionDBTable.ID_QUIZZ} = ?", arrayOf(quizz.idQuizz.toString()))
        if(deleted > 0) {
            if (questions.moveToFirst()) {
                do {
                    db.delete(AnswerDBTable.NAME, "${AnswerDBTable.ID_QUESTION} = ?", arrayOf(questions.getString(questions.getColumnIndex(QuestionDBTable.ID))))
                } while (questions.moveToNext())
            }
            questions.close()
        }
    }

    fun deleteQuestion(question : Question) {
        val db = this.writableDatabase
        db.delete(QuestionDBTable.NAME, "${QuestionDBTable.ID} = ?", arrayOf(question.idQuestion.toString()))

        db.delete(AnswerDBTable.NAME, "${AnswerDBTable.ID_QUESTION} = ?", arrayOf(question.idQuestion.toString()))
    }

    fun deleteAnswer(answer : Answer) {
        val db = this.writableDatabase
        db.delete(AnswerDBTable.NAME, "${AnswerDBTable.ID} = ?", arrayOf(answer.idAnswer.toString()))
    }

    fun newQuizz(quizz : Quizz): Long {
        val db = this.writableDatabase
        val quizzValues = ContentValues()
        quizzValues.put(QuizzDBTable.TITLE, quizz.titleQuizz)
        return db.insert(QuizzDBTable.NAME, null, quizzValues)
    }

    fun newQuestion(question : Question): Long {
        val db = this.writableDatabase
        val questionValues = ContentValues()
        questionValues.put(QuestionDBTable.TEXT, question.textQuestion)
        questionValues.put(QuestionDBTable.ID_QUIZZ, question.idQuizz)
        return db.insert(QuestionDBTable.NAME, null, questionValues)
    }

    fun newAnswer(answer : Answer): Long {
        val db = this.writableDatabase
        val answerValues = ContentValues()
        answerValues.put(AnswerDBTable.ID_QUESTION, answer.idQuestion)
        answerValues.put(AnswerDBTable.TEXT, answer.answer)
        if(answer.isOk) answerValues.put(AnswerDBTable.IS_OK, 1)
        else answerValues.put(AnswerDBTable.IS_OK, 0)
        return db.insert(AnswerDBTable.NAME, null, answerValues)
    }

    fun updateQuizz(quizz : Quizz) {
        val db = this.writableDatabase
        val quizzValues = ContentValues()
        quizzValues.put(QuizzDBTable.TITLE, quizz.titleQuizz)
        db.update(QuizzDBTable.NAME, quizzValues, "${QuizzDBTable.ID} = ?", arrayOf(quizz.idQuizz.toString()))
    }

    fun updateQuestion(question : Question, idQuizz: Long) {
        val db = this.writableDatabase
        val questionValues = ContentValues()
        questionValues.put(QuestionDBTable.ID_QUIZZ, idQuizz)
        questionValues.put(QuestionDBTable.TEXT, question.textQuestion)
        db.update(QuestionDBTable.NAME, questionValues, "${QuestionDBTable.ID} = ?", arrayOf(question.idQuestion.toString()))
    }

    fun updateAnswer(answer : Answer, idQuestion: Long) {
        val db = this.writableDatabase
        val answerValues = ContentValues()
        var isok = 0
        if(answer.isOk) isok = 1
        answerValues.put(AnswerDBTable.ID_QUESTION, idQuestion)
        answerValues.put(AnswerDBTable.IS_OK, isok)
        answerValues.put(AnswerDBTable.TEXT, answer.answer)
        db.update(AnswerDBTable.NAME, answerValues, "${AnswerDBTable.ID} = ?", arrayOf(answer.idAnswer.toString()))
    }

    companion object {
        const val DATABASE_NAME = "quizz.db"
        const val DATABASE_VERSION = 1
        const val DATABASE_CREATE_QUIZZ = "CREATE TABLE " + QuizzDBTable.NAME + " (" + QuizzDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuizzDBTable.TITLE + " TEXT NOT NULL);"
        const val DATABASE_CREATE_QUESTION = "CREATE TABLE " + QuestionDBTable.NAME + " (" + QuestionDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuestionDBTable.TEXT + " TEXT NOT NULL, " + QuestionDBTable.ID_QUIZZ + " INTEGER NOT NULL);"
        const val DATABASE_CREATE_ANSWER = "CREATE TABLE " + AnswerDBTable.NAME + " (" + AnswerDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AnswerDBTable.TEXT + " TEXT NOT NULL, " + AnswerDBTable.IS_OK + " INTEGER NOT NULL, " + AnswerDBTable.ID_QUESTION + " INTEGER NOT NULL);"
    }
}