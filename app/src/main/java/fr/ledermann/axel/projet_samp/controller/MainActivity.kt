package fr.ledermann.axel.projet_samp.controller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.SELECTED_LANGUAGE
import kotlinx.android.synthetic.main.activity_main.*

/*
 * This class is the activity used to display the main menu of the application.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.edit().putBoolean("changed_language", false).apply()

        startBtn.setOnClickListener {
            startActivity(Intent(this, QuizzActivity::class.java))
        }
        highscoreBtn.setOnClickListener {
            startActivity(Intent(this, HighscoreActivity::class.java))
        }
        manageBtn.setOnClickListener {
            startActivity(Intent(this, QuizzManageActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivityForResult(Intent(this, SettingsActivity::class.java), 1)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(
            LanguageHelper.wrap(
                newBase!!,
                lang!!
            )
        )
    }

    override fun onResume() {
        super.onResume()

        val changedLanguage = sharedPreferences.getBoolean("changed_language", false)

        if(changedLanguage) {
            finish()
            overridePendingTransition(0, 0)
            val intent = intent
            intent.data = Uri.parse("reloaded")
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}
