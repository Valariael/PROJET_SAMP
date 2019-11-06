package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manage_answer.*
import kotlinx.android.synthetic.main.activity_manage_question.*

class AnswerManageActivity : AppCompatActivity() {
    var answerList: ArrayList<Answer> = ArrayList()

    fun updateList() {
        recyclerAnswer.adapter?.notifyItemInserted(recyclerAnswer.adapter!!.getItemCount())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_answer)

        recyclerAnswer.layoutManager = LinearLayoutManager(this)
        recyclerAnswer.adapter = AnswerAdapter(this, answerList)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuestion.setOnClickListener { _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nouvelle Réponse")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = "Entrez une réponse"
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                answerList.add(Answer(editText.text.toString()))
                updateList()
            }
            builder.show()
        }
    }
}
