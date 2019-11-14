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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper



class QuizzManageActivity : AppCompatActivity() {
    var quizzList: ArrayList<Quizz> = ArrayList()
    var db: QuizzDBHelper = QuizzDBHelper(this)

    fun updateList() {
        recyclerManageQuizz.adapter?.notifyItemInserted(recyclerManageQuizz.adapter!!.itemCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_quizz)
        setSupportActionBar(toolbar)

        recyclerManageQuizz.layoutManager = LinearLayoutManager(this)
        quizzList = db.getQuizzs()
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

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (source.itemViewType != target.itemViewType) return false
                moveQuizz(source.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeQuizz(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerManageQuizz)

        updateList()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        db = QuizzDBHelper(this)
        quizzList = db.getQuizzs()
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
                quizzList = db.getQuizzs()
                updateList()
                true
            }
            R.id.action_reset -> {
                db.reset()
                quizzList = ArrayList()
                updateList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    fun addQuizz(q : Quizz) {
        q.idQuizz = db.newQuizz(q)
        quizzList.add(q)

        if(recyclerManageQuizz.adapter!!.hasObservers()) recyclerManageQuizz.adapter!!.notifyItemInserted(recyclerManageQuizz.adapter!!.itemCount)
    }

    fun removeQuizz(pos : Int) {
        db.deleteQuizz(quizzList[pos])
        quizzList.removeAt(pos)

        if(recyclerManageQuizz.adapter!!.hasObservers()) recyclerManageQuizz.adapter!!.notifyItemRemoved(pos)
    }

    fun moveQuizz(source : Int, target : Int) {
        val s = Quizz(quizzList[source].titleQuizz, quizzList[source].idQuizz!!)

        quizzList[source].titleQuizz = quizzList[target].titleQuizz
        db.updateQuizz(quizzList[source])

        quizzList[target].titleQuizz = s.titleQuizz
        db.updateQuizz(quizzList[target])

        if(recyclerManageQuizz.adapter!!.hasObservers()) recyclerManageQuizz.adapter!!.notifyItemMoved(source, target)
    }
}
