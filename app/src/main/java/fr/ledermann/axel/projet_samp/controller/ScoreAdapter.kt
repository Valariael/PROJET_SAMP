package fr.ledermann.axel.projet_samp.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Score

/*
 * This class is a recycler view adapter used to display the top scores or all the scores
 * in the "Best scores" section of the application.
 */
class ScoreAdapter (var ha : HighscoreActivity, var scoreList : ArrayList<Score>) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {
    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var indexText : TextView = v.findViewById(R.id.highscoreIndexText)
        var scoreText : TextView = v.findViewById(R.id.highscoreScoreText)
        var dateText : TextView = v.findViewById(R.id.highscoreDateText)
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