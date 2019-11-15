package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var currentScore : Score? = null
    private var quizzTitle : String? = null

    fun displayScore() {
        scoreQuizzTitleText.text = quizzTitle
        scoreText.text = "${currentScore!!.goodAnswers} / ${currentScore!!.totalAnswers}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentScore = intent.getSerializableExtra("KEY_SCORE") as Score?
        quizzTitle = intent.getStringExtra("KEY_QUIZZ_TITLE")

        displayScore()

        backToMenuBtn.setOnClickListener {
            setResult(RESULT_OK, null)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("SAVE_SCORE", currentScore)
        outState.putString("SAVE_QUIZZ_TITLE", quizzTitle)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentScore = savedInstanceState.getSerializable("SAVE_SCORE") as Score?
        quizzTitle = savedInstanceState.getString("SAVE_QUIZZ_TITLE")

        displayScore()
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }
}
