package fr.ledermann.axel.projet_samp.controller

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Question

class QuestionManageAdapter (var quma : QuestionManageActivity, var questionList : ArrayList<Question>) : RecyclerView.Adapter<QuestionManageAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var text : TextView = v.findViewById(R.id.textQuestionEdit)
        var button : Button = v.findViewById(R.id.editBtnAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(quma)
        val v = inflater.inflate(R.layout.item_question_edit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = questionList[position].textQuestion
        holder.text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                questionList[position].textQuestion = p0.toString()
            }

            override fun afterTextChanged(editable: Editable?) {
                quma.db.updateQuestion(questionList[position], quma.currentQuizz.idQuizz!!)
            }
        })

        holder.button.setOnClickListener{
            val intent = Intent(quma, AnswerManageActivity::class.java)
            intent.putExtra("KEY_QUESTION", questionList[position])
            ContextCompat.startActivity(quma, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}