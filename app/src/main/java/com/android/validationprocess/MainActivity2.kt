package com.android.validationprocess

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.android.app.progressbar.ProgressBar
import com.android.app.speedviewlib.components.Section
import com.anndroid.validationprocess.R
import com.anndroid.validationprocess.databinding.ActivityMain2Binding

class MainActivity2 : Activity() {

    private val TAG = "NUMBER_PICKER"
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        val colorMultiProgressBar = findViewById<View>(R.id.progressBar_multi_color) as ProgressBar
        colorMultiProgressBar.setProgressValues(
            25f,
            50f,
            100f,
            75f
        )

        colorMultiProgressBar.setProgressColors(
            Color.parseColor("#039BE5"),
            Color.parseColor("#8BC34A"),
            Color.parseColor("#FBC02D"),
            Color.parseColor("#f44336")
        )

        colorMultiProgressBar.setMaxValue(100f)
        colorMultiProgressBar.withAnimation(1000)

        /*val numberPicker = binding.numberPicker
        numberPicker.setFontResource(R.font.monsterrat_medium)
        numberPicker.setSelectedFontResource(R.font.montserrat_bold)
        // Set divider color

        // Set divider color
        numberPicker.dividerColor = ContextCompat.getColor(this, R.color.colorPrimary)
        numberPicker.setDividerColorResource(R.color.colorPrimary)

        // Set formatter

        // Set formatter
        numberPicker.setFormatter(getString(R.string.number_picker_formatter))
        numberPicker.setFormatter(R.string.number_picker_formatter)

        // Set selected text color

        // Set selected text color
        numberPicker.selectedTextColor = ContextCompat.getColor(this, R.color.colorPrimary)
        numberPicker.setSelectedTextColorResource(R.color.colorPrimary)

        // Set selected text size

        // Set selected text size
        numberPicker.selectedTextSize = resources.getDimension(R.dimen.selected_text_size)
        numberPicker.setSelectedTextSize(R.dimen.selected_text_size)

        // Set selected typeface

        // Set selected typeface
        numberPicker.setSelectedTypeface(
            Typeface.create(
                getString(R.string.roboto_light),
                Typeface.NORMAL
            )
        )
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light), Typeface.NORMAL)
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light))
        numberPicker.setSelectedTypeface(R.string.roboto_light, Typeface.NORMAL)
        numberPicker.setSelectedTypeface(R.string.roboto_light)

        // Set text color

        // Set text color
        numberPicker.textColor = ContextCompat.getColor(this, R.color.dark_grey)
        numberPicker.setTextColorResource(R.color.dark_grey)

        // Set text size

        // Set text size
        numberPicker.textSize = resources.getDimension(R.dimen.text_size)
        numberPicker.setTextSize(R.dimen.text_size)

        // Set typeface

        // Set typeface
        numberPicker.typeface = Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL)
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL)
        numberPicker.setTypeface(getString(R.string.roboto_light))
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL)
        numberPicker.setTypeface(R.string.roboto_light)

        // Set value

        // Set value
        numberPicker.maxValue = 59
        numberPicker.minValue = 0
        numberPicker.value = 3

        // Set string values
//        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(data.length);
//        numberPicker.setDisplayedValues(data);

        // Set fading edge enabled

        // Set string values
//        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(data.length);
//        numberPicker.setDisplayedValues(data);

        // Set fading edge enabled
        numberPicker.isFadingEdgeEnabled = true

        // Set scroller enabled

        // Set scroller enabled
        numberPicker.isScrollerEnabled = true

        // Set wrap selector wheel

        // Set wrap selector wheel
        numberPicker.wrapSelectorWheel = true

        // Set accessibility description enabled

        // Set accessibility description enabled
        numberPicker.isAccessibilityDescriptionEnabled = true

        // OnClickListener

        // OnClickListener
        numberPicker.setOnClickListener(View.OnClickListener {
            Log.d(
                TAG,
                "Click on current value"
            )
        })

        // OnValueChangeListener

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener { _, oldVal, newVal ->
            Log.d(
                TAG,
                String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal)
            )
        }

        // OnScrollListener

        // OnScrollListener
        numberPicker.setOnScrollListener { picker, scrollState ->
            if (scrollState == SCROLL_STATE_IDLE) {
                Log.d(
                    TAG,
                    java.lang.String.format(Locale.US, "newVal: %d", picker.getValue())
                )
            }
        }*/

        binding.speedView.apply {
            speedPercentTo(30, 1000)
            clearSections()
            val strokeWidth = 50f
            addSections(
                Section(0.toFloat(), 20f / 100, Color.LTGRAY, dpTOpx(strokeWidth)),
                Section(20f / 100, 40f / 100, Color.GRAY, dpTOpx(strokeWidth)),
                Section(40f / 100, 60f / 100, Color.DKGRAY, dpTOpx(strokeWidth)),
                Section(60f / 100, 80f / 100, Color.BLACK, dpTOpx(strokeWidth)),
                Section(80f / 100, 100f / 100, Color.CYAN, dpTOpx(strokeWidth))
            )
        }
    }
}