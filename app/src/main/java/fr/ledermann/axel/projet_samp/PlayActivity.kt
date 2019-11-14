package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity() {
    private var questionIndex = 0
    private var doneQuestionsCount = 0
    private var correctAnswerCount = 0
    private var selectedAnswers : ArrayList<Int> = ArrayList()
    private var currentQuizz : Quizz? = null
    private var currentAnswers : ArrayList<Answer> = ArrayList()
    private var listAllAnswers : ArrayList<ArrayList<Answer>> = ArrayList()
    private var listQuestions : ArrayList<Question> = ArrayList()
    var db: QuizzDBHelper = QuizzDBHelper(this)

    private fun updateList() {
        recyclerAnswers.adapter?.notifyItemInserted(recyclerAnswers.adapter!!.itemCount)
    }

    private fun updateDisplay() {
        currentAnswers.clear()
        currentAnswers.addAll(listAllAnswers[questionIndex])
        textQuestion.text = listQuestions[questionIndex].textQuestion
    }

    private fun getData() {
        listQuestions = db.getQuestions(currentQuizz!!.idQuizz!!)
        for(q in listQuestions) {
            listAllAnswers.add(db.getAnswers(q.idQuestion!!))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        currentQuizz = intent.getSerializableExtra("KEY_QUIZZ_PLAY") as Quizz?
        getData()
        updateDisplay()

        recyclerAnswers.layoutManager = LinearLayoutManager(this)
        recyclerAnswers.adapter = AnswerAdapter(this, currentAnswers)

        setNextBtnListener()

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
        currentQuizz = savedInstanceState.getSerializable("SAVE_QUIZZ_PLAY") as Quizz?
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

    fun toggleAnswer(b : Button, index : Int) {
        if(selectedAnswers.contains(index)) {
            b.background = resources.getDrawable(R.drawable.button_background)
            selectedAnswers.remove(index)
        } else {
            b.background = resources.getDrawable(R.drawable.button_background_select)
            selectedAnswers.add(index)
        }
    }

    private fun setNextBtnListener() {
        btnNext.text = getString(R.string.action_validate)
        btnNext.setOnClickListener {
            //TODO delete if ???
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
                    Toast.makeText(this@PlayActivity, "Correct ! Bien joué !", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@PlayActivity, "Erreur ! Dommage..", Toast.LENGTH_LONG).show()
                }

                btnNext.text = getString(R.string.action_next)
                btnNext.setOnClickListener {
                    questionIndex++
                    //TODO : end quizz
                    (recyclerAnswers.adapter as AnswerAdapter).isDisplayingAnswers = false
                    selectedAnswers = ArrayList()
                    doneQuestionsCount++
                    updateDisplay()
                    (recyclerAnswers.adapter as AnswerAdapter).notifyDataSetChanged()
                    setNextBtnListener()
                }
            }
        }
    }
}
