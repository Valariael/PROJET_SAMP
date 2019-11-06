package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter (var qma : QuestionManageActivity, var questionList : ArrayList<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var text : TextView = v.findViewById(R.id.textQuestionEdit)
        var button : Button = v.findViewById(R.id.editBtnAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(qma)
        val v = inflater.inflate(R.layout.item_question_edit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val q = questionList.get(position)
        holder.text.setText(q.textQuestion)

        //TODO: add listener
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}