package fr.ledermann.axel.projet_samp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_manage_quizz.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener { _ ->

        }
        manageBtn.setOnClickListener { _ ->
            startActivity(Intent(this, QuizzManageActivty::class.java))
        }
        settingsBtn.setOnClickListener { _ ->

        }
    }
}
