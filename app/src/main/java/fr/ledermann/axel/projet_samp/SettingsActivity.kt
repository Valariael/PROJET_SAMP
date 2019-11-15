package fr.ledermann.axel.projet_samp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.util.Log


const val SELECTED_LANGUAGE = "language"

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var languageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if(intent.data.toString() == "changed") languageChanged = true


        frenchBtn.setOnClickListener {
            changeApplicationLanguage("fr")
        }
        englishBtn.setOnClickListener {
            changeApplicationLanguage("en")
        }
        resetBtn.setOnClickListener {
            val db = QuizzDBHelper(this)
            db.onUpgrade(db.writableDatabase, 1, 2)
            Toast.makeText(this, getString(R.string.toast_reset), Toast.LENGTH_LONG).show()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(LanguageHelper.wrap(newBase!!, lang!!))
    }

    private fun changeApplicationLanguage(language:String){
        val sharedPreferencesEditor = sharedPreferences.edit()
        when (language) {
            "en" -> sharedPreferencesEditor?.putString(SELECTED_LANGUAGE, "en")
            "fr" -> sharedPreferencesEditor?.putString(SELECTED_LANGUAGE, "fr")
        }
        sharedPreferencesEditor.putBoolean("changed_language", true).apply()

        finish()
        overridePendingTransition(0, 0)
        val intent = intent
        intent.data = Uri.parse("changed")
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}
