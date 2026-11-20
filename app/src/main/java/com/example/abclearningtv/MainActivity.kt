package com.example.abclearningtv

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import java.util.Locale

data class Entry(val word: String, val emoji: String, val hindi: String)

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private lateinit var root: LinearLayout
    private lateinit var letterView: TextView
    private lateinit var wordView: TextView
    private lateinit var emojiView: TextView
    private lateinit var hindiView: TextView
    private lateinit var hintView: TextView
    private var currentLetter = 'A'

    private val entries = mapOf(
        'A' to Entry("Apple", "🍎", "सेब"),
        'B' to Entry("Ball", "⚽", "गेंद"),
        'C' to Entry("Cat", "🐱", "बिल्ली"),
        'D' to Entry("Dog", "🐶", "कुत्ता"),
        'E' to Entry("Elephant", "🐘", "हाथी"),
        'F' to Entry("Fish", "🐟", "मछली"),
        'G' to Entry("Grapes", "🍇", "अंगूर"),
        'H' to Entry("House", "🏠", "घर"),
        'I' to Entry("Ice cream", "🍦", "आइसक्रीम"),
        'J' to Entry("Juice", "🧃", "जूस"),
        'K' to Entry("Kite", "🪁", "पतंग"),
        'L' to Entry("Lion", "🦁", "शेर"),
        'M' to Entry("Mango", "🥭", "आम"),
        'N' to Entry("Nest", "🪺", "घोंसला"),
        'O' to Entry("Orange", "🍊", "संतरा"),
        'P' to Entry("Parrot", "🦜", "तोता"),
        'Q' to Entry("Queen", "👑", "रानी"),
        'R' to Entry("Rabbit", "🐰", "खरगोश"),
        'S' to Entry("Sun", "☀️", "सूरज"),
        'T' to Entry("Tiger", "🐯", "बाघ"),
        'U' to Entry("Umbrella", "☂️", "छाता"),
        'V' to Entry("Van", "🚐", "वैन"),
        'W' to Entry("Watch", "⌚", "घड़ी"),
        'X' to Entry("Xylophone", "🎼", "वाद्य यंत्र"),
        'Y' to Entry("Yak", "🐂", "याक"),
        'Z' to Entry("Zebra", "🦓", "ज़ेब्रा")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color.rgb(16, 26, 58))
        tts = TextToSpeech(this, this)
        buildScreen()
        showLetter('A', false)
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false): TextView {
        return TextView(this).apply {
            text = value
            setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(8), dp(6), dp(8), dp(6))
        }
    }

    private fun buildScreen() {
        val scroll = ScrollView(this)
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(22), dp(12), dp(22), dp(24))
        }
        scroll.addView(root)
        setContentView(scroll)

        root.addView(text("🌈  ABC FUN LEARNING  🌈", 30f, Color.WHITE, true))
        root.addView(text("Press A–Z on your keyboard or choose a letter below", 17f, Color.rgb(190, 215, 255)))

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.rgb(36, 53, 100))
            setPadding(dp(18), dp(8), dp(18), dp(8))
        }
        val params = LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(dp(8), dp(12), dp(8), dp(12))
        }
        root.addView(card, params)

        letterView = text("A", 64f, Color.rgb(255, 211, 80), true)
        wordView = text("A for Apple", 34f, Color.WHITE, true)
        emojiView = text("🍎", 88f, Color.WHITE)
        hindiView = text("Apple = सेब", 25f, Color.rgb(176, 245, 205), true)
        hintView = text("Press OK / Enter to hear again", 15f, Color.LTGRAY)
        card.addView(letterView)
        card.addView(wordView)
        card.addView(emojiView)
        card.addView(hindiView)
        card.addView(hintView)

        root.addView(text("LETTERS", 20f, Color.WHITE, true))
        val letters = GridLayout(this).apply { columnCount = 7 }
        ('A'..'Z').forEach { ch ->
            val b = makeButton(ch.toString()) { showLetter(ch, true) }
            letters.addView(b, gridParams())
        }
        root.addView(letters)

        root.addView(text("NUMBERS  •  1 TO 100", 20f, Color.WHITE, true))
        val numbers = GridLayout(this).apply { columnCount = 10 }
        (1..100).forEach { n ->
            val b = makeButton(n.toString()) { showNumber(n, true) }
            numbers.addView(b, gridParams())
        }
        root.addView(numbers)
        root.addView(text("Keyboard: A–Z = letters  |  0–9 = numbers  |  Enter = repeat  |  Back = exit", 14f, Color.LTGRAY))
    }

    private fun gridParams() = GridLayout.LayoutParams().apply {
        width = 0
        height = dp(52)
        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        setMargins(dp(3), dp(3), dp(3), dp(3))
    }

    private fun makeButton(label: String, action: () -> Unit): Button {
        return Button(this).apply {
            text = label
            textSize = 16f
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(54, 91, 165))
            setOnClickListener { action() }
            setOnFocusChangeListener { v, focused ->
                v.setBackgroundColor(if (focused) Color.rgb(235, 142, 40) else Color.rgb(54, 91, 165))
            }
        }
    }

    private fun showLetter(ch: Char, speak: Boolean) {
        currentLetter = ch
        val item = entries[ch] ?: return
        letterView.text = ch.toString()
        wordView.text = "$ch for ${item.word}"
        emojiView.text = item.emoji
        hindiView.text = "${item.word} = ${item.hindi}"
        if (speak) say("$ch. $ch for ${item.word}. ${item.word}. ${item.hindi}", Locale.ENGLISH)
    }

    private fun showNumber(n: Int, speak: Boolean) {
        letterView.text = n.toString()
        wordView.text = "Number $n"
        emojiView.text = numberEmoji(n)
        hindiView.text = "$n = ${hindiNumber(n)}"
        if (speak) {
            say("Number $n", Locale.ENGLISH)
            say(hindiNumber(n), Locale("hi", "IN"))
        }
    }

    private fun numberEmoji(n: Int): String = when (n) {
        1 -> "☝️"; 2 -> "👟👟"; 3 -> "⭐ ⭐ ⭐"; 4 -> "🦋 🦋 🦋 🦋"
        5 -> "🍎 🍎 🍎 🍎 🍎"; 10 -> "🎈 🎈 🎈 🎈 🎈"
        else -> "🔢"
    }

    private fun hindiNumber(n: Int): String {
        val units = listOf("", "एक", "दो", "तीन", "चार", "पाँच", "छह", "सात", "आठ", "नौ")
        val teens = listOf("दस","ग्यारह","बारह","तेरह","चौदह","पंद्रह","सोलह","सत्रह","अठारह","उन्नीस")
        val tens = listOf("", "", "बीस","तीस","चालीस","पचास","साठ","सत्तर","अस्सी","नब्बे")
        return when {
            n < 10 -> units[n]
            n < 20 -> teens[n - 10]
            n == 100 -> "एक सौ"
            n % 10 == 0 -> tens[n / 10]
            else -> "$n"
        }
    }

    private fun say(message: String, locale: Locale) {
        if (!ttsReady) return
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale.ENGLISH)
        }
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "abc-learning")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsReady = true
            tts?.setLanguage(Locale.ENGLISH)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> {
                val ch = ('A'.code + (keyCode - KeyEvent.KEYCODE_A)).toChar()
                showLetter(ch, true); return true
            }
            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                val n = if (keyCode == KeyEvent.KEYCODE_0) 10 else keyCode - KeyEvent.KEYCODE_0
                showNumber(n, true); return true
            }
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (letterView.text.toString().length == 1 && letterView.text[0] in 'A'..'Z') showLetter(letterView.text[0], true)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
