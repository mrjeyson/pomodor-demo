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
import com.timers.stopwatch.core.common.android.extensions.MAX_HOUR
import com.timers.stopwatch.core.common.android.extensions.MAX_MINUTE
import com.timers.stopwatch.feature.pomodoro.initial.R
import java.util.Locale

/**
 * Created by Janak.
 */
class InitialSetupHourMinuteItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    MaterialCardView(context, attrs, defStyleAttr) {

    var hourHintText: String? = ""
    var hourText: String? = ""
    var minuteHintText: String? = ""
    var minuteText: String? = ""

    init {
        this.radius = context.resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
        this.setCardBackgroundColor(
            context.resources.getColor(
                com.timers.stopwatch.core.common.android.R.color.white_tint,
            ),
        )
        this.cardElevation = context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp)
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.pomodoro_setup_hour_minute_item_layout, this)
        val tvTitle = findViewById<MaterialTextView>(R.id.tv_title)
        val etHour = findViewById<TextInputEditText>(R.id.et_hour)
        val etMinute = findViewById<TextInputEditText>(R.id.et_minute)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.InitialSetupHourMinuteItemView)
        try {
            val titleText = ta.getString(R.styleable.InitialSetupHourMinuteItemView_titleText)
            tvTitle.text = titleText

            hourHintText = ta.getString(R.styleable.InitialSetupHourMinuteItemView_hourHintText)
            hourText = ta.getString(R.styleable.InitialSetupHourMinuteItemView_hourText)
            minuteHintText = ta.getString(R.styleable.InitialSetupHourMinuteItemView_minuteHintText)
            minuteText = ta.getString(R.styleable.InitialSetupHourMinuteItemView_minuteText)

            etHour.hint = hourHintText
            etHour.setText(hourText ?: "")
            etMinute.hint = (minuteHintText)
            etMinute.setText(minuteText ?: "")
        } finally {
            ta.recycle()
        }

        setListener(etHour, etMinute)
    }

    fun setListener(etHour: TextInputEditText, etMinute: TextInputEditText) {
        val ibPlus = findViewById<ImageButton>(R.id.ib_plus)
        val ibMinus = findViewById<ImageButton>(R.id.ib_minus)
        val tvHour = findViewById<MaterialTextView>(R.id.tv_hour)
        val tvMinute = findViewById<MaterialTextView>(R.id.tv_minute)

        etHour.setOnFocusChangeListener { v, hasFocus ->
            tvHour.isSelected = hasFocus
        }
        etMinute.setOnFocusChangeListener { v, hasFocus ->
            tvMinute.isSelected = hasFocus
        }

        ibPlus.setOnClickListener {
            if (etHour.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etHour.text)) 0 else etHour.text.toString().toInt()
                if (existValue < MAX_HOUR) {
                    etHour.setText(String.format(Locale.getDefault(), "%02d", existValue + 1))
                }
            } else if (etMinute.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etMinute.text)) 0 else etMinute.text.toString().toInt()
                if (existValue < MAX_MINUTE) {
                    etMinute.setText(String.format(Locale.getDefault(), "%02d", existValue + 1))
                }
            }
        }

        ibMinus.setOnClickListener {
            if (etHour.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etHour.text)) 0 else etHour.text.toString().toInt()
                if (existValue > 1) {
                    etHour.setText(String.format(Locale.getDefault(), "%02d", existValue - 1))
                }
            } else if (etMinute.isFocused) {
                val existValue =
                    if (TextUtils.isEmpty(etMinute.text)) 0 else etMinute.text.toString().toInt()
                if (existValue > 1) {
                    etMinute.setText(String.format(Locale.getDefault(), "%02d", existValue - 1))
                }
            }
        }
    }

    fun reset() {
        val etHour = findViewById<TextInputEditText>(R.id.et_hour)
        val etMinute = findViewById<TextInputEditText>(R.id.et_minute)

        etHour.hint = hourHintText
        etHour.setText(hourText ?: "")
        etMinute.hint = (minuteHintText)
        etMinute.setText(minuteText ?: "")
    }

    fun getTimeText(): String {
        val etHour = findViewById<TextInputEditText>(R.id.et_hour)
        val etMinute = findViewById<TextInputEditText>(R.id.et_minute)

        val hourValue = if (!TextUtils.isEmpty(etHour.text)) etHour.text.toString().toInt() else 0

        val minuteValue =
            if (!TextUtils.isEmpty(etMinute.text)) etMinute.text.toString().toInt() else 0

        var minuteText = if (minuteValue > 0) {
            "$minuteValue"
        } else {
            if (!TextUtils.isEmpty(etMinute.text)) {
                "$minuteValue"
            } else {
                "${etMinute.hint}"
            }
        }

        return "$hourValue:$minuteText"
    }

    fun validateInputs(): ValidationResult {
        val etHour = findViewById<TextInputEditText>(R.id.et_hour)
        val etMinute = findViewById<TextInputEditText>(R.id.et_minute)

        val hourValue = if (!TextUtils.isEmpty(etHour.text)) etHour.text.toString().toInt() else 0
        val minuteValue =
            if (!TextUtils.isEmpty(etMinute.text)) {
                etMinute.text.toString().toInt()
            } else {
                0
            }

        return if (hourValue <= 0 && !TextUtils.isEmpty(etMinute.text) && minuteValue <= 0) {
            ValidationResult.Invalid(context.getString(R.string.please_enter_hours_and_minutes))
        } else {
            val hourHintValue =
                if (!TextUtils.isEmpty(etHour.text)) etHour.text.toString().toInt() else 0
            val minuteHintValue =
                if (!TextUtils.isEmpty(etMinute.text)) etMinute.text.toString().toInt() else 0

            val hValue = if (hourValue > 0) {
                etHour.text.toString().toInt()
            } else {
                hourHintValue
            }

            val mValue = if (minuteValue > 0) {
                etMinute.text.toString().toInt()
            } else {
                minuteHintValue
            }

            ValidationResult.ValidHourMinute(hValue, mValue)
        }
    }
}
