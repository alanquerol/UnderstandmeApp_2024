package com.example.firedatabase_assis;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class Understandme : AppCompatActivity() {

    // Declaring a TextToSpeech object and an EditText for later use.
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var editText:EditText


    // Override the onCreate method, which is called when the activity is starting.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the user interface layout for this activity.
        setContentView(R.layout.activity_understandme)

        // Initialize the editText and buttons by finding them in the layout.
        editText = findViewById<EditText>(R.id.editText)
        val textToSpeechBtn = findViewById<Button>(R.id.textToSpeechBtn)

        // Initialize the TextToSpeech object.
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set the language for TextToSpeech.
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    // Show a toast message if the language data is missing or not supported.
                    Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show()
                }
            }
        }
        // Set an OnClickListener for the textToSpeech button.
        textToSpeechBtn.setOnClickListener {
            // Check if the editText is not empty and then speak out the text.
            if (editText.text.toString().trim().isNotEmpty()) {

                textToSpeech.speak(
                    editText.text.toString().trim(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null

                )
            } else {
                // Show a toast message if editText is empty.
                Toast.makeText(this, "Mensaje requerido", Toast.LENGTH_LONG).show()
            }
        }

        // Initialize the speechToText button and set an OnClickListener.
        val speechToTextBtn = findViewById<Button>(R.id.speechToTextBtn)
        speechToTextBtn.setOnClickListener {
            // Clear the editText content.
            editText.text = null
            try {
                // Create an intent for speech recognition.
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something")
                // Launch the intent for result.
                result.launch(intent)
            }catch (e:Exception){
                // Print the stack trace if there's an exception.
                e.printStackTrace()
            }
        }

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            // Start the login_form activity
            startActivity(Intent(this, login_form::class.java))
            // Optionally, finish this activity if you don't want it in the back stack
            finish()
        }
    }



    // Register for activity result for speech recognition.
    val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode == Activity.RESULT_OK){
            // Retrieve and set the recognized speech to editText.
            val results = result.data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            ) as ArrayList<String>

            editText.setText(results[0])
        }
    }
}
