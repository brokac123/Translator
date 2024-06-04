package com.example.translator


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.translator.uiScreens.baseLanguage
import com.example.translator.uiScreens.secondLanguage
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage.CROATIAN
import com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH
import com.google.mlkit.nl.translate.TranslateLanguage.FRENCH
import com.google.mlkit.nl.translate.TranslateLanguage.GERMAN
import com.google.mlkit.nl.translate.TranslateLanguage.SPANISH
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


var outputText = mutableStateOf("")
const val HR = CROATIAN
const val EN = ENGLISH
const val DE = GERMAN
const val ES = SPANISH
const val FR = FRENCH

class TranslatorClass {

    private val options =
        TranslatorOptions.Builder()
            .setSourceLanguage(baseLanguage.value)
            .setTargetLanguage(secondLanguage.value)
            .build()
    private val languageTranslator = Translation.getClient(options)


    fun onTranslateButtonClick(
        textForTranslating: String,
        context: Context
    ) {
        languageTranslator.translate(textForTranslating)
            .addOnSuccessListener { translatedText ->
                outputText.value = translatedText
            }
            .addOnFailureListener {
                downloadModelIfNotDownloaded(context)
                Toast.makeText(
                    context,
                    "Downloading started...",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun downloadModelIfNotDownloaded(
        context: Context
    )   {
        val conditions = DownloadConditions
            .Builder()
            .requireWifi()
            .build()
        languageTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Downloaded model successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener{
                Toast.makeText(
                    context,
                    "Error occurred while downloading language model",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }}


