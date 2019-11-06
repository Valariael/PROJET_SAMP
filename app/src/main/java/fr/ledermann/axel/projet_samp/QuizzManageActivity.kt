package fr.ledermann.axel.projet_samp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_manage_quizz.*

class QuizzManageActivity : AppCompatActivity() {
    var quizzList: ArrayList<Quizz> = ArrayList()
    val db: QuizzDBHelper = QuizzDBHelper(this)

    fun updateList() {
        recyclerManageQuizz.adapter?.notifyItemInserted(recyclerManageQuizz.adapter!!.getItemCount())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_quizz)
        setSupportActionBar(toolbar)

        recyclerManageQuizz.layoutManager = LinearLayoutManager(this)
        recyclerManageQuizz.adapter = QuizzManageAdapter(this, quizzList)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuizz.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nouveau Quizz")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                addQuizz(Quizz(editText.text.toString()))
            }
            builder.show()
        }

        quizzList.addAll(db.getAllQuizz())
        updateList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_manage_quizz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_get_quizz -> {
                val http = XmlHttpQuizz(this)
                http.execute()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addQuizz(q : Quizz) {
        q.idQuizz = db.newQuizz(q)
        quizzList.add(q)
        if(recyclerManageQuizz.adapter!!.hasObservers()) recyclerManageQuizz.adapter!!.notifyItemInserted(recyclerManageQuizz.adapter!!.itemCount)
    }

    fun removeQuizz(pos : Int) {
        db.deleteQuizz(pos)
        quizzList.removeAt(pos)
        if(recyclerManageQuizz.adapter!!.hasObservers()) recyclerManageQuizz.adapter!!.notifyItemRemoved(pos)
    }
}
