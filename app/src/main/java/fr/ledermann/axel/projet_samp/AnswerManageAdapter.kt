package fr.ledermann.axel.projet_samp

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnswerManageAdapter (var ama : AnswerManageActivity, var answerList : ArrayList<Answer>) : RecyclerView.Adapter<AnswerManageAdapter.ViewHolder>() {

    class ViewHolder (v : View) : RecyclerView.ViewHolder(v) {
        var text : TextView = v.findViewById(R.id.textAnswerEdit)
        var switch : Switch = v.findViewById(R.id.switchAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(ama)
        val v = inflater.inflate(R.layout.item_answer_edit, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = answerList[position].answer
        holder.text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                answerList[position].answer = p0.toString()
            }

            override fun afterTextChanged(editable: Editable?) {
                ama.db.updateAnswer(answerList[position], ama.currentQuestion!!.idQuestion!!)
            }
        })

        holder.switch.isChecked = answerList[position].isOk
        holder.switch.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            answerList[position].isOk = b
            ama.db.updateAnswer(answerList[position], ama.currentQuestion!!.idQuestion!!)
        }
    }

    override fun getItemCount(): Int {
        return answerList.size
    }
}