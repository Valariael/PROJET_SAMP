package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_quizz.*

class QuizzActivity : AppCompatActivity() {
    var quizzList: ArrayList<Quizz> = ArrayList()
    val db: QuizzDBHelper = QuizzDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        recyclerPlayQuizz.layoutManager = LinearLayoutManager(this)
        recyclerPlayQuizz.adapter = QuizzAdapter(this, quizzList)

        quizzList.addAll(db.getQuizzs())
        recyclerPlayQuizz.adapter?.notifyItemInserted(recyclerPlayQuizz.adapter!!.getItemCount())
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
