package fr.ledermann.axel.projet_samp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
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

        holder.button.setOnClickListener{
            val intent = Intent(qa, PlayActivity::class.java)
            intent.putExtra("KEY_QUIZZ_PLAY", quizzList[position])
            ContextCompat.startActivity(qa, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return quizzList.size
    }
}