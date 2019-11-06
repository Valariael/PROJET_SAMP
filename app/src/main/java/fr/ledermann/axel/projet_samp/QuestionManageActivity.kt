package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manage_question.*

class QuestionManageActivity : AppCompatActivity() {
    var questionList: ArrayList<Question> = ArrayList()

    fun updateList() {
        recyclerManageQuestion.adapter?.notifyItemInserted(recyclerManageQuestion.adapter!!.getItemCount())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_question)

        recyclerManageQuestion.layoutManager = LinearLayoutManager(this)
        recyclerManageQuestion.adapter = QuestionManageAdapter(this, questionList)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuestion.setOnClickListener { _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nouvelle Question")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = "Entrez une question"
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                questionList.add(Question(editText.text.toString()))
                updateList()
            }
            builder.show()
        }
    }
}
