package fr.ledermann.axel.projet_samp.controller

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.view.ContextThemeWrapper
import fr.ledermann.axel.projet_samp.R
import java.util.*

class LanguageHelper(base: Context) : ContextThemeWrapper(base,
    R.style.AppTheme
) {

    companion object {
        fun wrap(context: Context, language: String): ContextThemeWrapper {
            var newContext = context
            val config = context.resources.configuration
            if (language != "") {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.setLocale(locale)
                } else {
                    setSystemLocaleLegacy(
                        config,
                        locale
                    )
                }
                config.setLayoutDirection(locale)
                newContext = context.createConfigurationContext(config)
            }
            return LanguageHelper(newContext)
        }

        @SuppressWarnings("deprecation")
        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }
    }
}