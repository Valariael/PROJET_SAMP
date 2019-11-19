package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var currentScore : Score? = null
    private var quizz : Quizz? = null

    fun displayScore() {
        scoreQuizzTitleText.text = quizz!!.titleQuizz
        scoreText.text = "${currentScore!!.goodAnswers} / ${currentScore!!.totalAnswers}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentScore = intent.getSerializableExtra("KEY_SCORE") as Score?
        quizz = intent.getSerializableExtra("KEY_QUIZZ_TO_SCORE") as Quizz?

        displayScore()

        backToMenuBtn.setOnClickListener {
            setResult(RESULT_OK, null)
            finish()
        }

        replayBtn.setOnClickListener {
            setResult(RESULT_OK, null)
            finish()
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("KEY_QUIZZ_PLAY", quizz)
            ContextCompat.startActivity(this, intent, null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("SAVE_SCORE", currentScore)
        outState.putSerializable("SAVE_QUIZZ_TO_SCORE", quizz)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentScore = savedInstanceState.getSerializable("SAVE_SCORE") as Score?
        quizz = savedInstanceState.getSerializable("SAVE_QUIZZ_TO_SCORE") as Quizz?

        displayScore()
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }
}
