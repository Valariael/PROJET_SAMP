package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_quizz.*

class QuizzActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var quizzList: ArrayList<Quizz> = ArrayList()
    var db: QuizzDBHelper = QuizzDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        recyclerPlayQuizz.layoutManager = LinearLayoutManager(this)
        recyclerPlayQuizz.adapter = QuizzAdapter(this, quizzList)

        val getQuizz = db.getQuizzs()
        if(getQuizz.isEmpty()) {

        } else {
            for(q in getQuizz) {
                val getQuestions = db.getQuestions(q.idQuizz!!)
                if(getQuestions.isNotEmpty()) quizzList.add(q)
            }
        }
        recyclerPlayQuizz.adapter?.notifyItemInserted(recyclerPlayQuizz.adapter!!.itemCount)
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }
}
