package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

class QuizzActivity : AppCompatActivity() {
    var quizzList: ArrayList<Quizz> = ArrayList()
    val db: QuizzDBHelper = QuizzDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        recyclerQuizz.layoutManager = LinearLayoutManager(this)
        recyclerQuizz.adapter = QuizzAdapter(this, quizzList)

        //val inflater: LayoutInflater = LayoutInflater.from(this)

        quizzList.addAll(db.getAllQuizz())
        recyclerQuizz.adapter?.notifyItemInserted(recyclerQuizz.adapter!!.getItemCount())
    }
}
