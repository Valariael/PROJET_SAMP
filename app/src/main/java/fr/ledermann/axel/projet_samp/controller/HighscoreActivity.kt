package fr.ledermann.axel.projet_samp.controller

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_highscore.*
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Quizz
import fr.ledermann.axel.projet_samp.model.QuizzDBHelper
import fr.ledermann.axel.projet_samp.model.SELECTED_LANGUAGE
import fr.ledermann.axel.projet_samp.model.Score

/*
 * This class is the activity used to display the top scores or all of them.
 * For each one of them, there is a ranking, the score and the date.
 */
class HighscoreActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var listScores : ArrayList<Score> = ArrayList()
    private var listQuizz : ArrayList<Quizz> = ArrayList()
    private var isHighscores = true
    private var idCurrentQuizz : Long? = null
    var db: QuizzDBHelper =
        QuizzDBHelper(this)

    private fun updateList() {
        recyclerScores.adapter?.notifyItemInserted(recyclerScores.adapter!!.itemCount)
    }

    private fun getData() {
        listQuizz.clear()
        listQuizz.addAll(db.getQuizzs())
        listScores.clear()
        if(idCurrentQuizz == null && listQuizz.isNotEmpty()) {
            idCurrentQuizz = listQuizz[0].idQuizz!!
            listScores.addAll(db.getScores(isHighscores, idCurrentQuizz!!))
        }
        else listScores.addAll(db.getScores(isHighscores, idCurrentQuizz!!))

        highscoreEmptyText.isVisible = listScores.isEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        getData()

        recyclerScores.layoutManager = LinearLayoutManager(this)
        recyclerScores.adapter =
            ScoreAdapter(this, listScores)

        switchHighscores.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            isHighscores = !b
            listScores.clear()
            listScores.addAll(db.getScores(isHighscores, idCurrentQuizz!!))
            recyclerScores.adapter?.notifyDataSetChanged()
        }

        val adapter = ArrayAdapter<Quizz>(
            applicationContext,
            R.layout.item_spinner_quizz,
            listQuizz
        )
        adapter.setDropDownViewResource(R.layout.item_spinner_quizz)

        spinnerQuizz.adapter = adapter
        spinnerQuizz.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                idCurrentQuizz = listQuizz[position].idQuizz
                listScores.clear()
                listScores.addAll(db.getScores(isHighscores, idCurrentQuizz!!))
                recyclerScores.adapter?.notifyDataSetChanged()
            }
        }

        updateList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putLong("SAVE_QUIZZ_INDEX", this.idCurrentQuizz!!)
        outState.putBoolean("SAVE_ALL_SCORES", isHighscores)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        idCurrentQuizz = savedInstanceState.getLong("SAVE_QUIZZ_INDEX")
        isHighscores = savedInstanceState.getBoolean("SAVE_ALL_SCORES")
        db = QuizzDBHelper(this)
        getData()
        updateList()
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
