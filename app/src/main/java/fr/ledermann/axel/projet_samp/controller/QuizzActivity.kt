package fr.ledermann.axel.projet_samp.controller

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Quizz
import fr.ledermann.axel.projet_samp.model.QuizzDBHelper
import fr.ledermann.axel.projet_samp.model.SELECTED_LANGUAGE
import kotlinx.android.synthetic.main.activity_quizz.*

/*
 * This class is the activity used to display all the playable and valid quizzes as well as select
 * the desired quiz to play.
 * A quiz is valid if it has at least one "true" answer to a question.
 */
class QuizzActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var quizzList: ArrayList<Quizz> = ArrayList()
    var db: QuizzDBHelper =
        QuizzDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        recyclerPlayQuizz.layoutManager = LinearLayoutManager(this)
        recyclerPlayQuizz.adapter =
            QuizzAdapter(this, quizzList)

        val getQuizz = db.getValidQuizzs()
        if(getQuizz.isEmpty()) {
            quizzEmptyText.isVisible = true
        } else {
            quizzEmptyText.isVisible = false
            quizzList.addAll(getQuizz)
            recyclerPlayQuizz.adapter?.notifyItemInserted(recyclerPlayQuizz.adapter!!.itemCount)
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(
            LanguageHelper.wrap(
                newBase!!,
                lang!!
            )
        )
    }
}
