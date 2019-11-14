package fr.ledermann.axel.projet_samp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_highscore.*
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView


class HighscoreActivity : AppCompatActivity() {
    private var listScores : ArrayList<Score> = ArrayList()
    private var listQuizz : ArrayList<Quizz> = ArrayList()
    private var allScores = false
    private var idCurrentQuizz : Long? = null
    var db: QuizzDBHelper = QuizzDBHelper(this)

    private fun updateList() {
        recyclerScores.adapter?.notifyItemInserted(recyclerScores.adapter!!.itemCount)
    }

    private fun getData() {
        listQuizz.clear()
        listQuizz.addAll(db.getQuizzs())
        listScores.clear()
        if(idCurrentQuizz == null && listQuizz.isNotEmpty()) {
            idCurrentQuizz = listQuizz[0].idQuizz!!
            listScores.addAll(db.getScores(allScores, idCurrentQuizz!!))
        }
        else if(idCurrentQuizz == null) //TODO
        else listScores.addAll(db.getScores(allScores, idCurrentQuizz!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        getData()

        recyclerScores.layoutManager = LinearLayoutManager(this)
        recyclerScores.adapter = ScoreAdapter(this, listScores)

        switchHighscores.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            allScores = b
            listScores.clear()
            listScores.addAll(db.getScores(allScores, idCurrentQuizz!!))
            recyclerScores.adapter?.notifyDataSetChanged()
        }

        val adapter = ArrayAdapter<Quizz>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            listQuizz
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerQuizz.adapter = adapter
        spinnerQuizz.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                idCurrentQuizz = listQuizz[position].idQuizz
                listScores.clear()
                listScores.addAll(db.getScores(allScores, idCurrentQuizz!!))
                recyclerScores.adapter?.notifyDataSetChanged()
            }
        }

        updateList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putLong("SAVE_QUIZZ_INDEX", this.idCurrentQuizz!!)
        outState.putBoolean("SAVE_ALL_SCORES", allScores)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        idCurrentQuizz = savedInstanceState.getLong("SAVE_QUIZZ_INDEX")
        allScores = savedInstanceState.getBoolean("SAVE_ALL_SCORES")
        db = QuizzDBHelper(this)
        getData()
        updateList()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
