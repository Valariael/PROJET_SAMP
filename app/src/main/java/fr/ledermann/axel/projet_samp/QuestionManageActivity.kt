package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_manage_question.*

class QuestionManageActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    var currentQuizz : Quizz? = null
    var db : QuizzDBHelper = QuizzDBHelper(this)
    private var listQuestions : ArrayList<Question> = ArrayList()

    private fun updateList() {
        recyclerManageQuestion.adapter?.notifyItemInserted(recyclerManageQuestion.adapter!!.itemCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_question)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentQuizz = intent.getSerializableExtra("KEY_QUIZZ") as Quizz?
        listQuestions = db.getQuestions(currentQuizz!!.idQuizz!!)

        manageQuestionQuizzTitle.text = currentQuizz!!.titleQuizz
        recyclerManageQuestion.layoutManager = LinearLayoutManager(this)
        recyclerManageQuestion.adapter = QuestionManageAdapter(this, listQuestions)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuestion.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert_dialog_title_question))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = getString(R.string.alert_dialog_title_answer)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                addQuestion(Question(editText.text.toString(), currentQuizz!!.idQuizz!!))
                updateList()
            }
            builder.show()
        }

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (source.itemViewType != target.itemViewType) return false
                moveQuestion(source.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeQuestion(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerManageQuestion)
        updateList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putSerializable("SAVE_QUIZZ", currentQuizz!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuizz = savedInstanceState.getSerializable("SAVE_QUIZZ") as Quizz?
        db = QuizzDBHelper(this)
        listQuestions = db.getQuestions(currentQuizz!!.idQuizz!!)
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

    fun addQuestion(q : Question) {
        q.idQuestion = db.newQuestion(q)
        listQuestions.add(q)

        updateList()
    }

    fun removeQuestion(pos : Int) {
        db.deleteQuestion(listQuestions[pos])
        listQuestions.removeAt(pos)

        if(recyclerManageQuestion.adapter!!.hasObservers()) recyclerManageQuestion.adapter!!.notifyItemRemoved(pos)
    }

    fun moveQuestion(source : Int, target : Int) {
        val q = Question(listQuestions[source].textQuestion, listQuestions[source].idQuizz!!, listQuestions[source].idQuestion!!)

        listQuestions[source].idQuizz = listQuestions[target].idQuizz
        listQuestions[source].textQuestion = listQuestions[target].textQuestion
        db.updateQuestion(listQuestions[source], currentQuizz!!.idQuizz!!)


        listQuestions[target].idQuizz = q.idQuizz
        listQuestions[target].textQuestion = q.textQuestion
        db.updateQuestion(listQuestions[target], currentQuizz!!.idQuizz!!)

        if(recyclerManageQuestion.adapter!!.hasObservers()) recyclerManageQuestion.adapter!!.notifyItemMoved(source, target)
    }
}
