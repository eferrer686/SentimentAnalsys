package com.example.sentimentanalysis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null
    private var goodFoods: ArrayList<String> = ArrayList<String>()
    private var badFoods: ArrayList<String> = ArrayList<String>()
    private var goodWords: ArrayList<String> = ArrayList<String>()
    private var badWords: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

        getGoodFood()
        getBadFood()
        getGoodWords()
        getBadWords()

    }

    private fun speakOut() {
        val text = editText!!.text.toString()

        var food:String = analizeMood(text)

        tts!!.speak(food, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


    private fun analizeMood(text:String):String{
        var word:String = ""
        var mood:Int = 0

        for (l in 0..(text.length-1)){
            if (text[l] == ' '){
                mood += analizeWord(word)
                word=""
            }
            else if ( l ==text.length-1){
                word += text[l]
                mood += analizeWord(word)
                word=""
            }
            else {
                word += text[l]
            }

        }
        Log.i("Mood",mood.toString())
        if (mood>=0){
            return goodFoods.random()
        }
        return badFoods.random()
    }
    private fun analizeWord(wordt:String):Int{
        var word:String = wordt.decapitalize()
        for (w:String in goodWords) {
            if (w.contains(word)) {
                return 1
            }
        }
        for (w:String in badWords) {
            if (w.contains(word)) {
                return -1
            }
        }

        return 0

    }

    private fun getGoodFood(){
        var inputStream: InputStream? = null;

        inputStream = getResources().openRawResource(R.raw.goodfood)

        var reader = BufferedReader( inputStream.bufferedReader())

        var allWords:String = reader.readText()



        var food:String = ""

        for (l in allWords){

            if (l=='\n'){

                goodFoods.add(food)
                food=""

            }
            else {
                food+=l
            }
        }

        Log.i("Filewords", goodFoods.toString())

    }
    private fun getBadFood(){
        var inputStream: InputStream? = null;

        inputStream = getResources().openRawResource(R.raw.badfood)

        var reader = BufferedReader( inputStream.bufferedReader())

        var allWords:String = reader.readText()



        var food:String = ""

        for (l in allWords){

            if (l=='\n'){

                badFoods.add(food)
                food=""

            }
            else {
                food+=l
            }
        }

        Log.i("Filewords", badFoods.toString())

    }
    private fun getGoodWords(){
        var inputStream: InputStream? = null;

        inputStream = getResources().openRawResource(R.raw.goodwords)

        var reader = BufferedReader( inputStream.bufferedReader())

        var allWords:String = reader.readText()



        var food:String = ""

        for (l in allWords){

            if (l=='\n'){

                goodWords.add(food)
                food=""

            }
            else {
                food+=l
            }
        }

        Log.i("Filewords", goodWords.toString())

    }
    private fun getBadWords(){
        var inputStream: InputStream? = null;

        inputStream = getResources().openRawResource(R.raw.badwords)

        var reader = BufferedReader( inputStream.bufferedReader())

        var allWords:String = reader.readText()



        var food:String = ""

        for (l in allWords){

            if (l=='\n'){

                badWords.add(food)
                food=""

            }
            else {
                food+=l
            }
        }

        Log.i("Filewords", badWords.toString())

    }


}
