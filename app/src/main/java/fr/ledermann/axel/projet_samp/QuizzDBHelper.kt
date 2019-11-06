package fr.ledermann.axel.projet_samp

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
        db.execSQL(DATABASE_CREATE)
    }

    fun getAllQuizz() {
        /*val quizzList = ArrayList<Quizz>()
        var name = ""
        val selectQuery = "SELECT  * FROM " +  + ";"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            do {
                name = c.getString(c.getColumnIndex(KEY_FIRSTNAME))
                studentsArrayList.add(name)
            } while (c.moveToNext())
            Log.d("array", studentsArrayList.toString())
        }
        return studentsArrayList*/
    }

    companion object {
        const val DATABASE_NAME = "quizz.db"
        const val DATABASE_VERSION = 1
        const val DATABASE_CREATE = "CREATE TABLE " + QuizzDBTable.NAME + " (" + QuizzDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuizzDBTable.TITLE + " TEXT NOT NULL);" +
                "CREATE TABLE " + QuestionDBTable.NAME + " (" + QuestionDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuestionDBTable.TEXT + " TEXT NOT NULL, " + QuestionDBTable.ID_QUIZZ + " INTEGER NOT NULL REFERENCES " + QuizzDBTable.NAME + "(" + QuizzDBTable.ID + ");" +
                "CREATE TABLE " + AnswerDBTable.NAME + " (" + AnswerDBTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AnswerDBTable.TEXT + " TEXT NOT NULL, " + AnswerDBTable.IS_OK + " BOOLEAN NOT NULL, " + AnswerDBTable.ID_QUESTION + " INTEGER NOT NULL REFERENCES " + QuestionDBTable.NAME + "(" + QuestionDBTable.ID + ");"
    }
}