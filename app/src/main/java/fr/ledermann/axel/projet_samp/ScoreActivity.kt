package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {
    private var currentScore : Score? = null
    private var quizzTitle : String? = null

    fun displayScore() {
        scoreQuizzTitleText.text = quizzTitle
        scoreText.text = "${currentScore!!.goodAnswers} / ${currentScore!!.totalAnswers}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        currentScore = intent.getSerializableExtra("KEY_SCORE") as Score?
        quizzTitle = intent.getStringExtra("KEY_QUIZZ_TITLE")

        displayScore()

        backToMenuBtn.setOnClickListener {
            setResult(RESULT_OK, null);
            finish();
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
}
