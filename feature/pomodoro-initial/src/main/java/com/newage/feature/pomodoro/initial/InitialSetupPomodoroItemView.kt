package com.newage.feature.pomodoro.initial

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.timers.stopwatch.feature.pomodoro.initial.R
import java.util.Locale

/**
 * Created by Janak.
 */
class InitialSetupPomodoroItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    MaterialCardView(context, attrs, defStyleAttr) {
    private var initialPomodoroHint: String? = ""
    private var initialPomodoroText: String? = ""

    init {
        this.radius = context.resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
        this.setCardBackgroundColor(
            context.resources
                .getColor(
                    com.timers.stopwatch.core.common.android.R.color.white_tint,
                ),)
        this.cardElevation = context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp)
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.pomodoro_setup_pomodoro_item_layout, this)

        val tvTitle = findViewById<MaterialTextView>(R.id.tv_title)
        val ibPlus = findViewById<ImageView>(R.id.ib_plus)
        val ibMinus = findViewById<ImageButton>(R.id.ib_minus)
        val etPomodoro = findViewById<TextInputEditText>(R.id.et_pomodoro)
        val tvPomodoro = findViewById<MaterialTextView>(R.id.tv_pomodoro)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.InitialSetupPomodoroItemView)
        try {
            val titleText = ta.getString(R.styleable.InitialSetupPomodoroItemView_pomodoroTitleText)
            tvTitle.text = titleText

            initialPomodoroHint =
                ta.getString(R.styleable.InitialSetupPomodoroItemView_pomodoroHintText)
            initialPomodoroText =
                ta.getString(R.styleable.InitialSetupPomodoroItemView_pomodoroText)
            etPomodoro.hint = initialPomodoroHint
            etPomodoro.setText(initialPomodoroText ?: "")
        } finally {
            ta.recycle()
        }

        etPomodoro.setOnFocusChangeListener { v, hasFocus ->
            tvPomodoro.isSelected = hasFocus
        }

        ibPlus.setOnClickListener {
            if (etPomodoro.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etPomodoro.text)) 0 else etPomodoro.text.toString().toInt()
                etPomodoro.setText(String.format(Locale.getDefault(), "%02d", existValue + 1))
            }
        }

        ibMinus.setOnClickListener {
            if (etPomodoro.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etPomodoro.text)) 0 else etPomodoro.text.toString().toInt()
                if (existValue > 1) {
                    etPomodoro.setText(String.format(Locale.getDefault(), "%02d", existValue - 1))
                }
            }
        }
    }

    fun reset() {
        val etPomodoro = findViewById<TextInputEditText>(R.id.et_pomodoro)
        etPomodoro.hint = initialPomodoroHint
        etPomodoro.setText(initialPomodoroText ?: "")
    }

    fun getText(): String {
        val etHour = findViewById<TextInputEditText>(R.id.et_pomodoro)
        val hourValue = if (!TextUtils.isEmpty(etHour.text)) etHour.text.toString().toInt() else 0

        val text = if (hourValue > 0) {
            "$hourValue"
        } else {
            "${etHour.hint}"
        }

        return text
    }
    fun validateInputs(): ValidationResult {
        val etHour = findViewById<TextInputEditText>(R.id.et_pomodoro)
        val hourValue = if (!TextUtils.isEmpty(etHour.text)) etHour.text.toString().toInt() else 0
        return if (!TextUtils.isEmpty(etHour.text) && hourValue <= 0) {
            ValidationResult.Invalid("Please enter pomodoro rounds")
        } else {
            val values = if (TextUtils.isEmpty(etHour.text)) {
                etHour.hint.toString().toInt()
            } else {
                hourValue
            }
            ValidationResult.ValidHourMinute(values, 0)
        }
    }
}
