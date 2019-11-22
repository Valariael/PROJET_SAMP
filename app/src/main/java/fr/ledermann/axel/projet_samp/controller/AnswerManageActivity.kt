package fr.ledermann.axel.projet_samp.controller

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Answer
import fr.ledermann.axel.projet_samp.model.Question
import fr.ledermann.axel.projet_samp.model.QuizzDBHelper
import fr.ledermann.axel.projet_samp.model.SELECTED_LANGUAGE
import kotlinx.android.synthetic.main.activity_manage_answer.*

class AnswerManageActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var currentQuestion : Question
    private var listAnswers : ArrayList<Answer> = ArrayList()
    var db: QuizzDBHelper =
        QuizzDBHelper(this)

    private fun updateList() {
        manageAnswerEmptyText.isVisible = listAnswers.isEmpty()
        recyclerManageAnswer.adapter?.notifyItemInserted(recyclerManageAnswer.adapter!!.itemCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_answer)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentQuestion = intent.getSerializableExtra("KEY_QUESTION") as Question
        listAnswers = db.getAnswers(currentQuestion.idQuestion!!)

        manageAnswerQuestionTitle.text = currentQuestion.textQuestion
        recyclerManageAnswer.layoutManager = LinearLayoutManager(this)
        recyclerManageAnswer.adapter =
            AnswerManageAdapter(this, listAnswers)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabAnswer.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert_dialog_title_answer))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = getString(R.string.alert_dialog_hint_answer)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                addAnswer(
                    Answer(
                        editText.text.toString(),
                        false,
                        currentQuestion.idQuestion!!
                    )
                )
                updateList()
            }
            builder.show()
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (source.itemViewType != target.itemViewType) return false
                moveAnswer(source.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeAnswer(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerManageAnswer)
        updateList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("SAVE_QUESTION", currentQuestion)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestion = savedInstanceState.getSerializable("SAVE_QUESTION") as Question
        db = QuizzDBHelper(this)
        listAnswers = db.getAnswers(currentQuestion.idQuestion!!)
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

    private fun addAnswer(a : Answer) {
        a.idAnswer = db.newAnswer(a)
        listAnswers.add(a)

        updateList()
    }

    private fun removeAnswer(pos : Int) {
        db.deleteAnswer(listAnswers[pos])
        listAnswers.removeAt(pos)
        manageAnswerEmptyText.isVisible = listAnswers.isEmpty()

        if(recyclerManageAnswer.adapter!!.hasObservers()) recyclerManageAnswer.adapter!!.notifyItemRemoved(pos)
    }

    private fun moveAnswer(source : Int, target : Int) {
        val a = Answer(
            listAnswers[source].answer,
            listAnswers[source].isOk,
            listAnswers[source].idQuestion!!,
            listAnswers[source].idAnswer!!
        )

        listAnswers[source].idQuestion = listAnswers[target].idQuestion
        listAnswers[source].isOk = listAnswers[target].isOk
        listAnswers[source].answer = listAnswers[target].answer
        db.updateAnswer(listAnswers[source], currentQuestion.idQuestion!!)


        listAnswers[target].idQuestion = a.idQuestion
        listAnswers[target].isOk = a.isOk
        listAnswers[target].answer = a.answer
        db.updateAnswer(listAnswers[target], currentQuestion.idQuestion!!)

        if(recyclerManageAnswer.adapter!!.hasObservers()) recyclerManageAnswer.adapter!!.notifyItemMoved(source, target)
    }
}
