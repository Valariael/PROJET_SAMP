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

class QuizzManageActivty : AppCompatActivity() {
    var quizzList: ArrayList<Quizz> = ArrayList()

    fun updateList() {
        recyclerQuizz.adapter?.notifyItemInserted(recyclerQuizz.adapter!!.getItemCount())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_quizz)
        setSupportActionBar(toolbar)

        recyclerQuizz.layoutManager = LinearLayoutManager(this)
        recyclerQuizz.adapter = QuizzAdapter(this, quizzList)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuizz.setOnClickListener { _ ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nouveau Quizz")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                quizzList.add(Quizz(editText.text.toString()))
                updateList()
            }
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_manage_quizz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_quizz -> {
                val http = XmlHttpQuizz(this)
                http.execute()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
