package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AnswerAdapter (var pa : PlayActivity, var answerList : ArrayList<Answer>) : RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
    var isDisplayingAnswers = false

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var button : Button = v.findViewById(R.id.answerBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(pa)
        val v = inflater.inflate(R.layout.item_answer, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = answerList[position].answer
        holder.button.setOnClickListener{
            pa.toggleAnswer(holder.button, position)
        }
        if(isDisplayingAnswers && answerList[position].isOk) holder.button.background = pa.resources.getDrawable(R.drawable.button_background_correct)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }
}