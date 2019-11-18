package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manage_quizz.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper



class QuizzManageActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    var quizzList: ArrayList<Quizz> = ArrayList()
    var db: QuizzDBHelper = QuizzDBHelper(this)

    fun updateList() {
        recyclerManageQuizz.adapter?.notifyItemInserted(recyclerManageQuizz.adapter!!.itemCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_quizz)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        recyclerManageQuizz.layoutManager = LinearLayoutManager(this)
        quizzList = db.getQuizzs()
        recyclerManageQuizz.adapter = QuizzManageAdapter(this, quizzList)

        val inflater: LayoutInflater = LayoutInflater.from(this)

        fabQuizz.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert_dialog_title_quizz))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_gettext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.textAlertDialog)
            editText.hint = getString(R.string.alert_dialog_hint_quizz)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { _, _ ->
                addQuizz(Quizz(editText.text.toString()))
            }
            builder.show()
        }

        getQuizzBtn.setOnClickListener {
            val http = XmlHttpQuizz(this)
            http.execute()
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

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }

    fun addQuizz(q : Quizz) {
        q.idQuizz = db.newQuizz(q)
        quizzList.add(q)

        updateList()
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
