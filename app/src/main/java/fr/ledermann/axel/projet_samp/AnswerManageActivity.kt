package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_manage_answer.*

class AnswerManageActivity : AppCompatActivity() {
    var currentQuestion : Question? = null
    private var listAnswers : ArrayList<Answer> = ArrayList()
    val db: QuizzDBHelper = QuizzDBHelper(this)

    private fun updateList() {
        recyclerManageAnswer.adapter?.notifyItemInserted(recyclerManageAnswer.adapter!!.itemCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_answer)

        currentQuestion = intent.getSerializableExtra("KEY_QUESTION") as Question?
        listAnswers = db.getAnswers(currentQuestion!!.idQuestion!!)

        recyclerManageAnswer.layoutManager = LinearLayoutManager(this)
        recyclerManageAnswer.adapter = AnswerManageAdapter(this, listAnswers)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabAnswer.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nouvelle Réponse")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = "Entrez une réponse"
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                addAnswer(Answer(editText.text.toString()))
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
        currentQuestion = savedInstanceState.getSerializable("SAVE_QUESTION") as Question?
        listAnswers = db.getAnswers(currentQuestion!!.idQuestion!!)
        updateList()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    fun addAnswer(a : Answer) {
        a.idAnswer = db.newAnswer(a)
        listAnswers.add(a)

        if(recyclerManageAnswer.adapter!!.hasObservers()) recyclerManageAnswer.adapter!!.notifyItemInserted(recyclerManageAnswer.adapter!!.itemCount)
    }

    fun removeAnswer(pos : Int) {
        db.deleteAnswer(listAnswers[pos])
        listAnswers.removeAt(pos)

        if(recyclerManageAnswer.adapter!!.hasObservers()) recyclerManageAnswer.adapter!!.notifyItemRemoved(pos)
    }

    fun moveAnswer(source : Int, target : Int) {
        val a = Answer(listAnswers[source].answer, listAnswers[source].isOk, listAnswers[source].idQuestion!!, listAnswers[source].idAnswer!!)

        listAnswers[source].idQuestion = listAnswers[target].idQuestion
        listAnswers[source].isOk = listAnswers[target].isOk
        listAnswers[source].answer = listAnswers[target].answer
        db.updateAnswer(listAnswers[source], currentQuestion!!.idQuestion!!)


        listAnswers[target].idQuestion = a.idQuestion
        listAnswers[target].isOk = a.isOk
        listAnswers[target].answer = a.answer
        db.updateAnswer(listAnswers[target], currentQuestion!!.idQuestion!!)

        if(recyclerManageAnswer.adapter!!.hasObservers()) recyclerManageAnswer.adapter!!.notifyItemMoved(source, target)
    }
}
