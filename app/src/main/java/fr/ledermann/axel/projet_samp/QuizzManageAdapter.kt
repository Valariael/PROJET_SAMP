package fr.ledermann.axel.projet_samp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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
        holder.text.text = quizzList[position].titleQuizz
        holder.text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                quizzList[position].titleQuizz = p0.toString()
            }

            override fun afterTextChanged(editable: Editable?) {
                qma.db.updateQuizz(quizzList[position])
            }
        })

        holder.button.setOnClickListener{
            val intent = Intent(qma, QuestionManageActivity::class.java)
            intent.putExtra("KEY_QUIZZ", quizzList[position])
            startActivity(qma, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return quizzList.size
    }
}