package com.example.bflorian.texttranslator

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import java.util.*

class SettingsActivity : AppCompatActivity() {

    companion object{
        public var inputLanguageCode: String = "en"
        public var inputLanguage: String = "English"
        public var outputLanguageCode: String = "fr"
        public var outputLanguage: String = "French"
        private lateinit var radioGroup: RadioGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var languages: List<String> = TranslateLanguage.getAllLanguages()

        radioGroup = findViewById(R.id.radioGroupLanguages)

        //add languages to radio group
        for (languageCode in languages){
            var radioButton = RadioButton(this)
            val name = getLocaleName(languageCode)
            radioButton.text = "$languageCode - $name"
            radioGroup.addView(radioButton)
        }
        //radio group listener
//        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            var rb: RadioButton = findViewById(checkedId)
//            var sub = rb.text.toString()
//            sub = sub.substring(0,2)
//            var language = TranslateLanguage.fromLanguageTag(sub)
//            setOutputLanguage(language)
//        }
    }

    //Sets the language for use in translation
    public fun setOutputLanguage(languageCode: String?){
        if (languageCode != null) {
            outputLanguageCode = languageCode
            outputLanguage = getLocaleName(languageCode)

            getModels(outputLanguageCode)

        }
    }

    //Sets the language for use in translation
    public fun setInputLanguage(languageCode: String?){
        if (languageCode != null) {
            inputLanguageCode = languageCode
            inputLanguage = getLocaleName(languageCode)

            getModels(inputLanguageCode)

        }
    }

    fun getModels(languageCode: String){
        val modelManager = RemoteModelManager.getInstance()

        // Check if language model is downloaded
        val currentModel = TranslateRemoteModel.Builder(languageCode).build()
        val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
        modelManager.download(currentModel, conditions)
                .addOnSuccessListener {
                    // Model downloaded.
                    Toast.makeText(this, "Downloading language models, this can take a moment", Toast.LENGTH_SHORT)
                }
                .addOnFailureListener {
                    // Error.
                    Toast.makeText(this, "Failed downloading language models", Toast.LENGTH_SHORT)
                }
    }

    //returns the name for a given language code
    fun getLocaleName(languageCode: String): String {
        val loc = Locale(languageCode)
        return loc.getDisplayLanguage(loc)
    }

    fun onBackButtonClick(view: View){
        finish()
    }

    //deletes all the stored language models except english
    fun deleteAllLanguageModels(view: View){
        val modelManager = RemoteModelManager.getInstance()

        // Get translation models stored on the device.
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                Toast.makeText(this, "Deleting language models, this can take a moment", Toast.LENGTH_SHORT)
                for (model in models){
                    if(model.language != "en") {
                        modelManager.deleteDownloadedModel(model)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error deleting", Toast.LENGTH_SHORT)
            }
    }

    fun onSetInputClick(view: View) {
        var id = findViewById<RadioGroup>(R.id.radioGroupLanguages).checkedRadioButtonId
        var rb: RadioButton = findViewById(id)
        var sub = rb.text.toString()
        sub = sub.substring(0,2)
        var language = TranslateLanguage.fromLanguageTag(sub)
        setInputLanguage(language)
    }
    fun onSetOutputClick(view: View) {
        var id = findViewById<RadioGroup>(R.id.radioGroupLanguages).checkedRadioButtonId
        var rb: RadioButton = findViewById(id)
        var sub = rb.text.toString()
        sub = sub.substring(0,2)
        var language = TranslateLanguage.fromLanguageTag(sub)
        setOutputLanguage(language)
    }

}