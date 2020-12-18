package com.example.bflorian.texttranslator

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_image_view.*
import java.util.*

class ImageViewActivity : AppCompatActivity() {
    companion object{
        public lateinit var image: Drawable

        public lateinit var inputTextView: TextView
        public lateinit var inputLanguageTextView: TextView

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        var imageView : ImageView = findViewById(R.id.imageViewDisplay)
        imageView.background = image

        // setup the textView to display text found
        inputTextView = findViewById(R.id.textViewContent)
        inputLanguageTextView = findViewById(R.id.textViewInputLanguage)

        val inputImage = InputImage.fromBitmap(image.toBitmap(), 0)

        // the on-device model for text recognition
        val recognizer = TextRecognition.getClient()

        // pass the image to the process
        val result = recognizer.process(inputImage)
            .addOnSuccessListener { visionText -> // Task completed successfully
                // Display the text found in the textView
                textViewInputLanguage.text = SettingsActivity.inputLanguage
                inputTextView.text = "${visionText.text}"
                translateText()
            }
            .addOnFailureListener { // Task failed with an exception
                // ...
                inputTextView.text = "task failed"
            }
    }

    fun onButtonReturn(view: View){
        finish()
    }

    //returns the name for a given language code
    fun getLocaleName(languageCode: String): String {
        val loc = Locale(languageCode)
        return loc.getDisplayLanguage(loc)
    }

    fun identifyLanguage(text: String): String{
        var inputLanguage: String = "en"
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    inputLanguage = "en"
                } else {
                    inputLanguage = languageCode
                }
            }
            .addOnFailureListener {exception ->
                // Model couldn’t be loaded or other internal error.
                // ...
                exception.message
                inputLanguage = "en"
            }
         return inputLanguage
    }

    fun translateText(){
        val modelManager = RemoteModelManager.getInstance()

        // Get translation models stored on the device.
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                //models.contains()
                // ...
            }
            .addOnFailureListener {
                // Error.
            }

        // Create an English-German translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(SettingsActivity.inputLanguageCode)
            .setTargetLanguage(SettingsActivity.outputLanguageCode)
            .build()
        val englishTagalogTranslator = Translation.getClient(options)


        var packDownloaded: Boolean = false

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishTagalogTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                Toast.makeText(this, "Language pack downloaded", Toast.LENGTH_SHORT)
                packDownloaded = true
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                Toast.makeText(this, "Language pack has NOT been downloaded", Toast.LENGTH_SHORT)
            }

        var textView: TextView = findViewById(R.id.textViewTranslation)
            //if(packDownloaded){
                englishTagalogTranslator.translate(inputTextView.text.toString())
                    .addOnSuccessListener { translatedText ->
                        // Translation successful.

                        textView.text = "${SettingsActivity.outputLanguage}: $translatedText"
                    }
                    .addOnFailureListener { exception ->
                        // Error.
                        // ...
                        textView.text = "Language not set in settings menu or still downloading language pack: ${SettingsActivity.outputLanguage}.\nGo back and try again"

                    }
            //}
    }
}