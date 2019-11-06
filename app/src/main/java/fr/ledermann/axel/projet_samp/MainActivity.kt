package fr.ledermann.axel.projet_samp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener {
            startActivity(Intent(this, QuizzActivity::class.java))
        }
        manageBtn.setOnClickListener {
            startActivity(Intent(this, QuizzManageActivity::class.java))
        }
        settingsBtn.setOnClickListener { _ ->

        }
    }
}
