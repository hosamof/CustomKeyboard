package com.husam.customkeyboard

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LiveData
import com.husam.customkeyboard.MainActivity.Companion.FOR_QR_CODE
import com.husam.customkeyboard.MainActivity.Companion.textModel

// https://developer.android.com/develop/ui/views/touch-and-input/creating-input-method

class FastInputIME : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    private val MY_TAG = "HusamDebug";
    private var keyboardView: KeyboardView? = null


    override fun onCreate() {
        super.onCreate()
        val liveData: LiveData<String> = textModel
        liveData.observeForever { str ->
            //update main activity view based on state changed
            if (currentInputConnection != null) {
                currentInputConnection.commitText(str, 1);
            }
        }
    }

    override fun onCreateInputView(): View {
        val latinKeyboardView = Keyboard(this, R.xml.keyboard)
        return LayoutInflater.from(this).inflate(R.layout.keyboard_view, null).apply {
            if (this is KeyboardView) {
                setOnKeyboardActionListener(this@FastInputIME)
                keyboard = latinKeyboardView
                keyboardView = this
            }
        }

    }

    override fun onKey(p0: Int, p1: IntArray?) {
        Log.e(MY_TAG, "onKey is: $p0")
        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            when (p0) {
                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
                // QR Code
                10 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(FOR_QR_CODE, true)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                else -> {
                    inputConnection.commitText("not valid input", 1)
                }
            }
        }
    }

    override fun onPress(p0: Int) {
    }

    override fun onRelease(p0: Int) {
    }


    override fun onText(p0: CharSequence?) {
    }

    override fun swipeLeft() {
    }

    override fun swipeRight() {
    }

    override fun swipeDown() {
    }

    override fun swipeUp() {
    }
}