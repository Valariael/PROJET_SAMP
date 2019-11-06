package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnswerManageAdapter (var ama : AnswerManageActivity, var answerList : ArrayList<Answer>) : RecyclerView.Adapter<AnswerManageAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var text : TextView = v.findViewById(R.id.textQuestionEdit)
        var switch : Switch = v.findViewById(R.id.switchAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ama)
        val v = inflater.inflate(R.layout.item_answer_edit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val a = answerList.get(position)
        holder.text.setText(a.answer)
        holder.switch.isChecked = a.isOk
    }

    override fun getItemCount(): Int {
        return answerList.size
    }
}