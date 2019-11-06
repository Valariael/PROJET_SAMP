package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizzManageAdapter (var qma : QuizzManageActivity, var quizzList : ArrayList<Quizz>) : RecyclerView.Adapter<QuizzManageAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var text : TextView = v.findViewById(R.id.titleQuizzEdit)
        var button : Button = v.findViewById(R.id.editBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(qma)
        val v = inflater.inflate(R.layout.item_quizz_edit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val q = quizzList.get(position)
        holder.text.setText(q.titleQuizz)

        // TODO : add listener
    }

    override fun getItemCount(): Int {
        return quizzList.size
    }
}