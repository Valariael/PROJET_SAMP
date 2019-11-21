package fr.ledermann.axel.projet_samp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import android.net.Uri

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var languageChanged = false
    private var isFrench = true
    private var isShowingAnswers = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isFrench = sharedPreferences.getString(SELECTED_LANGUAGE, "fr") == "fr"
        isShowingAnswers = sharedPreferences.getBoolean(IS_SHOWING_ANSWERS, true)

        setLanguageButtonsState()
        if(!isShowingAnswers) {
            Utils.changeGradientBtnColor(showAnswersBtn, SELECTED_BTN_COLOR_START, SELECTED_BTN_COLOR_END)
            showAnswersBtn.text = getString(R.string.action_show_answers_false)
        } else {
            Utils.changeGradientBtnColor(showAnswersBtn, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)
            showAnswersBtn.text = getString(R.string.action_show_answers_true)
        }

        if(intent.data.toString() == "changed") languageChanged = true

        showAnswersBtn.setOnClickListener {
            toggleIsShowingAnswersButton()
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
            "en" -> sharedPreferencesEditor.putString(SELECTED_LANGUAGE, "en")
            "fr" -> sharedPreferencesEditor.putString(SELECTED_LANGUAGE, "fr")
        }
        sharedPreferencesEditor.putBoolean("changed_language", true).apply()

        finish()
        overridePendingTransition(0, 0)
        val intent = intent
        intent.data = Uri.parse("changed")
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun attachFrenchListener() {
        frenchBtn.setOnClickListener {
            changeApplicationLanguage("fr")
        }
    }

    private fun attachEnglishListener() {
        englishBtn.setOnClickListener {
            changeApplicationLanguage("en")
        }
    }

    private fun setLanguageButtonsState() {
        if(isFrench) {
            attachEnglishListener()
            Utils.changeGradientBtnColor(frenchBtn, SELECTED_BTN_COLOR_START, SELECTED_BTN_COLOR_END)
            Utils.changeGradientBtnColor(englishBtn, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)
        } else {
            attachFrenchListener()
            Utils.changeGradientBtnColor(englishBtn, SELECTED_BTN_COLOR_START, SELECTED_BTN_COLOR_END)
            Utils.changeGradientBtnColor(frenchBtn, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)
        }
    }

    private fun toggleIsShowingAnswersButton() {
        if(isShowingAnswers) {
            isShowingAnswers = false
            Utils.changeGradientBtnColor(showAnswersBtn, SELECTED_BTN_COLOR_START, SELECTED_BTN_COLOR_END)
            showAnswersBtn.text = getString(R.string.action_show_answers_false)
        } else {
            isShowingAnswers = true
            Utils.changeGradientBtnColor(showAnswersBtn, BASE_BTN_COLOR_START, BASE_BTN_COLOR_END)
            showAnswersBtn.text = getString(R.string.action_show_answers_true)
        }
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.putBoolean(IS_SHOWING_ANSWERS, isShowingAnswers).apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
