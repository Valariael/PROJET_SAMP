package fr.ledermann.axel.projet_samp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreAdapter (var ha : HighscoreActivity, var scoreList : ArrayList<Score>) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {
    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var indexText : TextView = v.findViewById(R.id.highscoreIndexText)
        var scoreText : TextView = v.findViewById(R.id.highscoreDateText)
        var dateText : TextView = v.findViewById(R.id.highscoreScoreText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ha)
        val v = inflater.inflate(R.layout.item_highscore, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.indexText.text = (position+1).toString()
        holder.scoreText.text =
            "${scoreList[position].goodAnswers} / ${scoreList[position].totalAnswers}"
        holder.dateText.text = scoreList[position].date
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }
}