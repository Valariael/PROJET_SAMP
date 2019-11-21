package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_play.*
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable



class PlayActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var questionIndex = 0
    private var doneQuestionsCount = 0
    private var correctAnswerCount = 0
    private var selectedAnswers : ArrayList<Int> = ArrayList()
    private lateinit var currentQuizz : Quizz
    private var currentAnswers : ArrayList<Answer> = ArrayList()
    private var listAllAnswers : ArrayList<ArrayList<Answer>> = ArrayList()
    private var listQuestions : ArrayList<Question> = ArrayList()
    var isShowingAnswers : Boolean = true
    var db: QuizzDBHelper = QuizzDBHelper(this)

    private fun updateList() {
        recyclerAnswers.adapter?.notifyItemInserted(recyclerAnswers.adapter!!.itemCount)
    }

    private fun updateDisplay() {
        currentAnswers.clear()
        currentAnswers.addAll(listAllAnswers[questionIndex])
        titleQuizzText.text = currentQuizz.titleQuizz
        textQuestion.text = listQuestions[questionIndex].textQuestion
    }

    private fun getData() {
        listQuestions = db.getQuestions(currentQuizz.idQuizz!!)
        for(q in listQuestions) {
            listAllAnswers.add(db.getAnswers(q.idQuestion!!))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isShowingAnswers = sharedPreferences.getBoolean(IS_SHOWING_ANSWERS, true)

        currentQuizz = intent.getSerializableExtra("KEY_QUIZZ_PLAY") as Quizz
        getData()
        updateDisplay()

        recyclerAnswers.layoutManager = LinearLayoutManager(this)
        recyclerAnswers.adapter = AnswerAdapter(this, currentAnswers)

        setNextBtnListener()
        btnSkip.setOnClickListener {
            endOrContinueQuizz()
        }

        updateList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("SAVE_QUIZZ_PLAY", currentQuizz)
        outState.putInt("SAVE_QUESTION_INDEX", questionIndex)
        outState.putInt("SAVE_DONE_QUESTION_COUNT", doneQuestionsCount)
        outState.putInt("SAVE_CORRECT_ANSWER_COUNT", correctAnswerCount)
        outState.putIntegerArrayList("SAVE_SELECTED_ANSWERS", selectedAnswers)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuizz = savedInstanceState.getSerializable("SAVE_QUIZZ_PLAY") as Quizz
        questionIndex = savedInstanceState.getInt("SAVE_QUESTION_INDEX")
        doneQuestionsCount = savedInstanceState.getInt("SAVE_DONE_QUESTION_COUNT")
        correctAnswerCount = savedInstanceState.getInt("SAVE_CORRECT_ANSWER_COUNT")
        selectedAnswers = savedInstanceState.getIntegerArrayList("SAVE_SELECTED_ANSWERS")!!
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
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }

    fun toggleAnswer(b : Button, index : Int) {
        if(selectedAnswers.contains(index)) {
            Utils.changeGradientBtnColor(b, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)
            selectedAnswers.remove(index)
        } else {
            Utils.changeGradientBtnColor(b, SELECTED_BTN_COLOR_START, SELECTED_BTN_COLOR_END)
            selectedAnswers.add(index)
        }
    }

    private fun setNextBtnListener() {
        btnSkip.isEnabled = true
        btnSkip.isClickable = true
        Utils.changeGradientBtnColor(btnSkip, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)

        btnNext.text = getString(R.string.action_validate)
        btnNext.setOnClickListener {
            if(selectedAnswers.isEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.alert_dialog_title_noselect))
                builder.setMessage(getString(R.string.alert_dialog_msg_noselect))
                builder.setPositiveButton("OK", null)
                builder.show()
            } else {
                (recyclerAnswers.adapter as AnswerAdapter).isDisplayingAnswers = true

                var wellPlayed = true
                for(a in currentAnswers) {
                    if(a.isOk && !selectedAnswers.contains(currentAnswers.indexOf(a))) {
                        wellPlayed = false
                        (recyclerAnswers.adapter as AnswerAdapter).notifyItemChanged(currentAnswers.indexOf(a))
                    } else if(a.isOk) {
                        selectedAnswers.remove(currentAnswers.indexOf(a))
                        (recyclerAnswers.adapter as AnswerAdapter).notifyItemChanged(currentAnswers.indexOf(a))
                    }
                }

                if(selectedAnswers.isEmpty() && wellPlayed) {
                    correctAnswerCount++
                    Toast.makeText(this@PlayActivity, getString(R.string.toast_good), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@PlayActivity, getString(R.string.toast_bad), Toast.LENGTH_LONG).show()
                }

                btnSkip.isEnabled = false
                btnSkip.isClickable = false
                Utils.changeGradientBtnColor(btnSkip, DISABLED_BTN_COLOR_START, DISABLED_BTN_COLOR_END)

                btnNext.text = getString(R.string.action_next)
                btnNext.setOnClickListener {
                    doneQuestionsCount++

                    endOrContinueQuizz()
                }
            }
        }
    }

    private fun endOrContinueQuizz() {
        questionIndex++

        if(questionIndex == listQuestions.size) {
            val score = Score(correctAnswerCount, doneQuestionsCount, currentQuizz.idQuizz!!)
            db.newScore(score)

            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("KEY_QUIZZ_TO_SCORE", currentQuizz)
            intent.putExtra("KEY_SCORE", score)
            startActivityForResult(intent, 1)
        } else {
            (recyclerAnswers.adapter as AnswerAdapter).isDisplayingAnswers = false

            selectedAnswers = ArrayList()
            updateDisplay()
            (recyclerAnswers.adapter as AnswerAdapter).notifyDataSetChanged()

            setNextBtnListener()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.finish()
            }
        }
    }
}
