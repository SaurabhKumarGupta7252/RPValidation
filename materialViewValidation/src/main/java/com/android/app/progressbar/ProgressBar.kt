package com.android.app.progressbar

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.app.material.view.validation.R
import java.util.*

/**
 * Created by Saurabh Gupta on 21/02/23.
 */
class ProgressBar : LinearLayout {
    private val id = 1
    private var radius = 0
    private var padding = 0
    private val isReverse = false
    private var max = 0f
    private var progressLayouts: MutableList<LinearLayout>? = null
    private var progress: MutableList<Progress>? = null
    private var gravity = 0
    private var textStyle = 0
    private var textSize = 0f
    private var textAppearance = 0
    private var sortProgress: ArrayList<Progress>? = null
    private var iconPadding = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
//        if (isInEditMode())
//            return;

//        setGravity(Gravity.RIGHT);
        progress = ArrayList()
        progressLayouts = ArrayList()
        sortProgress = ArrayList()
        var a = context.obtainStyledAttributes(attrs, R.styleable.progressAttr)
        progressValue = a.getFloat(R.styleable.progressAttr_sw_progressValue, 0f)
        setProgressText(a.getString(R.styleable.progressAttr_sw_progressText))
        setProgressTextStyle(a.getInt(R.styleable.progressAttr_sw_progressTextStyle, 0))
        setProgressTextAppearance(a.getInt(R.styleable.progressAttr_sw_progressTextAppearance, 0))
        setProgressTextSize(
            a.getDimension(
                R.styleable.progressAttr_sw_progressTextSize,
                resources.getDimension(R.dimen.progressTextSize)
            )
        )
        setProgressIcon(a.getDrawable(R.styleable.progressAttr_sw_progressIcon))
        setRadius(a.getDimensionPixelSize(R.styleable.progressAttr_sw_radius, 0))
        setPadding(a.getDimensionPixelSize(R.styleable.progressAttr_sw_progressPadding, 0))
        setProgressIconPadding(
            a.getDimensionPixelSize(
                R.styleable.progressAttr_sw_progressIconPadding,
                0
            )
        )
        setProgressColor(
            a.getColor(
                R.styleable.progressAttr_sw_progressColor,
                Color.rgb(255, 255, 255)
            )
        )
        setProgressBackground(
            a.getColor(
                R.styleable.progressAttr_sw_progressBackground,
                Color.argb(0, 0, 0, 0)
            )
        )
        refresh()
        if (!isInEditMode) {
            if (a.getBoolean(
                    R.styleable.progressAttr_sw_withAnimation,
                    false
                )
            ) withAnimation(a.getInteger(R.styleable.progressAttr_sw_duration, 1000).toLong())
        }
        a.recycle()
        a = context.obtainStyledAttributes(attrs, R.styleable.constraintAttr)
        setMaxValue(a.getFloat(R.styleable.constraintAttr_sw_maxValue, 0f))
        a.recycle()
    }

    private fun setProgressTextSize(textSize: Float) {
        this.textSize = textSize
    }

    fun setProgressBackground(color: Int) {
        createGradientDrawableWithCorner(this, color)
    }

    private fun refresh() {
        if (progressLayouts != null) {
            if (progressLayouts!!.size != sortProgress!!.size) {
                addView(context)
            }
        } else addView(context)
        if (progressLayouts != null && progress != null) {
            var previousValue = 0f
            for (i in progressLayouts!!.indices) {
                progressLayouts!![i].gravity = gravity
                progressLayouts!![i].weightSum = sortProgress!![i].value
                setProgress(progressLayouts!![i], sortProgress!![i], previousValue, i)
                previousValue = sortProgress!![i].value
            }
        }
    }

    var progressValue: Float
        get() = getProgressValue(0)
        set(progressValue) {
            if (progressValue > max) {
                setMax(progressValue)
            }
            if (progress!!.size == 0) progress!!.add(
                Progress(progressValue)
            ) else {
                progress!![0].value = progressValue
                progress = progress!!.subList(0, 1)
            }
            sort()
            refresh()
        }

    fun getProgressValue(index: Int): Float {
        return if (index < 0 || index >= progress!!.size) 0f else progress!![index].value
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun addView(context: Context) {
        if (progress == null) return
        removeAllViews()
        orientation = HORIZONTAL
        var progressLayout: LinearLayout? = null
        val isRightAlign = isRightAlign
        if (progressLayouts!!.size > 0) progressLayout =
            progressLayouts!![progressLayouts!!.size - 1]
        for (i in progressLayouts!!.size until progress!!.size) {
            progressLayouts!!.add(LinearLayout(context))
            progressLayouts!![i].orientation = HORIZONTAL
            val textView = TextView(getContext())
            textView.id = i
            val padding = Utils.convertDpToPixel(16f).toInt()
            textView.setPadding(padding, 0, padding, 0)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(Color.WHITE)
            val linearLayout = LinearLayout(getContext())
            linearLayout.id = 100 + i
            linearLayout.gravity = Gravity.CENTER
            linearLayout.layoutParams = LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            linearLayout.addView(textView)
            if (isRightAlign) progressLayouts!![i].addView(linearLayout)
            if (progressLayout != null) {
                progressLayouts!![i].addView(progressLayout)
            }
            if (!isRightAlign) progressLayouts!![i].addView(linearLayout)
            progressLayout = progressLayouts!![i]
        }
        progressLayout?.let { addView(it) }
        progressLayouts = progressLayouts!!.subList(0, progress!!.size)
    }

    fun setPadding(padding: Int) {
        this.padding = padding
    }

    private fun setRadius(dimension: Int) {
        radius = dimension
        refresh()
    }

    private fun setMax(maxValue: Float) {
        max = maxValue
        weightSum = max
    }

    fun setMaxValue(maxValue: Float) {
        var maxValue = maxValue
        if (progress!!.size > 0) if (sortProgress!![progress!!.size - 1].value > maxValue) maxValue =
            sortProgress!![progress!!.size - 1].value
        setMax(maxValue)
        refresh()
    }

    private fun setProgress(
        progressLayout: LinearLayout,
        progress: Progress,
        previousValue: Float,
        i: Int
    ) {
        createGradientDrawableWithCorner(progressLayout, progress.color)
        val params = LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, progress.value)
        progressLayout.layoutParams = params
        progressLayout.weightSum = progress.value
        val textView = progressLayout.findViewById<View>(i) as TextView
        val layout = progressLayout.findViewById<View>(100 + i) as LinearLayout
        if (textView != null) {
            textView.text = progress.text
            textView.setCompoundDrawablesWithIntrinsicBounds(
                progress.drawable,
                null,
                null,
                null
            )
            textView.compoundDrawablePadding = iconPadding
            //textView.setTextStyle(textStyle);
            textView.textSize = textSize
            //textView.setSupportTextAppearance(textAppearance);
            layout.layoutParams = LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                progress.value - previousValue
            )
        }
    }

    fun setProgressValues(vararg progressValues: Float) {
        progress!!.clear()
        for (i in progress!!.indices) {
            progress!![i].value = progressValues[i]
        }
        for (i in progress!!.size until progressValues.size) {
            progress!!.add(Progress(progressValues[i]))
        }
        progress = progress!!.subList(0, progressValues.size)
        if (progress!![progress!!.size - 1].value > max) setMax(progress!![progress!!.size - 1].value)
        sort()
        refresh()
    }

    fun addProgressValue(progressValue: Float) {
        progress!!.add(Progress(progressValue))
        sort()
        refresh()
    }

    fun addProgressValue(progressValue: Float, color: Int) {
        progress!!.add(Progress(progressValue, color))
        sort()
        refresh()
    }

    fun removeProgressValue(index: Int): Boolean {
        if (index < 0 || index >= progress!!.size) return false
        progress!!.removeAt(index)
        sort()
        refresh()
        return true
    }

    fun setProgressValue(index: Int, progressValue: Float) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].value = progressValue
        sort()
        refresh()
    }

    fun setProgressColor(progressColor: Int) {
        if (progress!!.size == 0) progress!!.add(
            Progress(progressColor)
        ) else {
            progress!![0].color = progressColor
            progress = progress!!.subList(0, 1)
        }
        refresh()
    }

    fun setProgressColor(index: Int, color: Int) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].color = color
        refresh()
    }

    fun setProgressColors(vararg colors: Int) {
        for (i in progress!!.indices) {
            progress!![i].color = colors[i]
        }
        for (i in progress!!.size until colors.size) {
            progress!!.add(Progress(colors[i]))
        }
        refresh()
    }

    fun setProgressValueWithColor(index: Int, progressValue: Int, color: Int) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].value = progressValue.toFloat()
        progress!![index].color = color
        sort()
        refresh()
    }

    fun setProgressText(resId: Int) {
        setProgressText(resources.getString(resId))
    }

    fun setProgressText(text: String?) {
        if (progress!!.size == 0) progress!!.add(
            Progress(text)
        ) else {
            progress!![0].text = text
            progress = progress!!.subList(0, 1)
        }
        refresh()
    }

    fun setProgressTexts(vararg resIds: Int) {
        for (i in progress!!.indices) {
            progress!![i].text = resources.getString(resIds[i])
        }
        for (i in progress!!.size until resIds.size) {
            progress!!.add(
                Progress(
                    resources.getString(
                        resIds[i]
                    )
                )
            )
        }
        refresh()
    }

    fun setProgressTexts(vararg texts: String?) {
        for (i in progress!!.indices) {
            progress!![i].text = texts[i]
        }
        for (i in progress!!.size until texts.size) {
            progress!!.add(Progress(texts[i]))
        }
        refresh()
    }

    fun setProgressText(index: Int, resId: Int) {
        setProgressText(index, resources.getString(resId))
    }

    fun setProgressText(index: Int, text: String?) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].text = text
        refresh()
    }

    fun setProgressValueAndText(index: Int, progressValue: Float, resId: Int) {
        setProgressValueAndText(index, progressValue, resId)
    }

    fun setProgressValueAndText(index: Int, progressValue: Float, text: String?) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].value = progressValue
        progress!![index].text = text
        sort()
        refresh()
    }

    fun setProgressIcon(resId: Int) {
        setProgressIcon(resources.getDrawable(resId))
    }

    fun setProgressIcon(drawable: Drawable?) {
        if (progress!!.size == 0) progress!!.add(
            Progress(drawable)
        ) else {
            progress!![0].drawable = drawable
            progress = progress!!.subList(0, 1)
        }
        refresh()
    }

    fun setProgressIcons(vararg drawables: Drawable?) {
        for (i in progress!!.indices) {
            progress!![i].drawable = drawables[i]
        }
        for (i in progress!!.size until drawables.size) {
            progress!!.add(Progress(drawables[i]))
        }
        refresh()
    }

    fun setProgressIcons(vararg resIds: Int) {
        for (i in progress!!.indices) {
            progress!![i].drawable = resources.getDrawable(resIds[i])
        }
        for (i in progress!!.size until resIds.size) {
            progress!!.add(
                Progress(
                    resources.getDrawable(
                        resIds[i]
                    )
                )
            )
        }
        refresh()
    }

    fun setProgressIcon(index: Int, resId: Int) {
        setProgressIcon(index, resources.getDrawable(resId))
    }

    fun setProgressIcon(index: Int, drawable: Drawable?) {
        if (index < 0 || index >= progress!!.size) return
        progress!![index].drawable = drawable
        refresh()
    }

    private fun sort() {
        sortProgress!!.clear()
        sortProgress!!.addAll(progress!!)
        sort(sortProgress)
        if (sortProgress!!.size > 0) if (sortProgress!![sortProgress!!.size - 1].value > max) {
            max = sortProgress!![sortProgress!!.size - 1].value
            weightSum = max
        }
    }

    private fun sort(progress: List<Progress>?) {
        Collections.sort(progress) { lhs, rhs -> if (lhs.value < rhs.value) -1 else if (lhs.value < rhs.value) 1 else 0 }
    }

    override fun setGravity(gravity: Int) {
        super.setGravity(gravity)
        this.gravity = gravity
        refresh()
    }

    fun withAnimation(duration: Long) {
        if (progressLayouts == null) return


        /*post(new Runnable() {
                 @Override
                 public void run() {
                     ObjectAnimator[] animators = new ObjectAnimator[progressLayouts.size() * 2];
                     int i = 0;
                     for (LinearLayout layout : progressLayouts) {
                         int start;
                         if (isRightAlign())
                             start = layout.getWidth() / 2;
                         else
                             start = -layout.getWidth() / 2;
                         animators[i++] =
                                 ObjectAnimator.ofFloat(
                                         layout, "translationX",
                                         start, 0);
                         animators[i++] = ObjectAnimator.ofFloat(layout, "scaleX", 0, 1f);
                     }

                     if (animators.length == 0)
                         return;
                     final AnimatorSet set = new AnimatorSet();
                     set.playTogether(animators);
                     set.setDuration(duration).start();
                 }
             }
        );*/
    }

    private val isRightAlign: Boolean
        private get() = Gravity.isHorizontal(gravity) &&
                gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.RIGHT

    private fun createGradientDrawableWithCorner(
        layout: LinearLayout,
        color: Int
    ): GradientDrawable {
        val backgroundDrawable = createGradientDrawable(color)
        val newRadius = radius
        backgroundDrawable.cornerRadii = floatArrayOf(
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat()
        )

//        backgroundDrawable.setCornerRadii(new float[]{0, 0, newRadius, newRadius, newRadius, newRadius, 0, 0});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layout.background = backgroundDrawable
        } else {
            layout.setBackgroundDrawable(backgroundDrawable)
        }
        return backgroundDrawable
    }

    // Create an empty color rectangle gradient drawable
    protected fun createGradientDrawable(color: Int): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(color)
        return gradientDrawable
    }

    fun setProgressTextStyle(progressTextStyle: Int) {
        textStyle = progressTextStyle
        refresh()
    }

    fun setProgressTextAppearance(progressTextAppearance: Int) {
        textAppearance = progressTextAppearance
    }

    fun setProgressIconPadding(progressIconPadding: Int) {
        iconPadding = progressIconPadding
    }

    private inner class Progress {
        var value = 0f
        var color = 0
        var text: String? = null
        var drawable: Drawable? = null

        constructor(progressValue: Float) {
            value = progressValue
            color = Color.rgb(
                (255 * Math.random()).toInt(),
                (255 * Math.random()).toInt(),
                (255 * Math.random()).toInt()
            )
        }

        constructor(color: Int) {
            this.color = color
        }

        constructor(progressValue: Float, color: Int) {
            value = progressValue
            this.color = color
        }

        constructor(text: String?) {
            this.text = text
        }

        constructor(drawable: Drawable?) {
            this.drawable = drawable
        }
    }

    companion object {
        private val TAG = ProgressBar::class.java.simpleName
        private val TEXTVIEW_TAG: Any = "TEXTVIEW"
    }
}