package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class QuizzAdapter (var qa : QuizzActivity, var quizzList : ArrayList<Quizz>) : RecyclerView.Adapter<QuizzAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var button : Button = v.findViewById(R.id.playBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(qa)
        val v = inflater.inflate(R.layout.item_quizz, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val q = quizzList.get(position)
        holder.button.setText(q.titleQuizz)

        // TODO : add listener
    }

    override fun getItemCount(): Int {
        return quizzList.size
    }
}