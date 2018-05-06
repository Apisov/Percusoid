package com.apisov.percusoid.ui.instrumentdetails

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.apisov.percusoid.R

const val SENSOR_MAX_VALUE = 2
const val GRAPH_BUFFER_SIZE = 80

data class Hit(var x: Float, val velocityAsRadius: Float)

class SensorGraph : View {

    private var measuresInited: Boolean = false

    private var valueSectionWidth = 0f
    private var scaleCoef = 0f

    private val hitsCircles = Path()

    private val samplesXPoints: FloatArray = FloatArray(GRAPH_BUFFER_SIZE) { 0f }
    private val graphPoints: FloatArray = FloatArray((GRAPH_BUFFER_SIZE - 1) * 4) { 0f }
    private val paintStroke = Paint()
    private val hitPaint = Paint()
    private val thresholdPaint = Paint()
    private val instrumentMaxValuePaint = Paint()

    private val hits: MutableList<Hit> = mutableListOf()

    var sensorMaxValuePercentage = 50
        set(value) {
            field = value
            updateCoef(value)
        }

    private var instrumentMaxValue = 0f
    private var instrumentMaxValuePercentage = 0
    private var instrumentThreshold = 0f
    private var instrumentThresholdPercentage = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_HARDWARE, null)

        with(paintStroke) {
            flags = Paint.ANTI_ALIAS_FLAG
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = context.resources.getDimension(R.dimen.graph_threshold_stroke)
        }
        with(hitPaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = context.resources.getDimension(R.dimen.graph_threshold_stroke) * 4
            color = Color.YELLOW
        }

        with(thresholdPaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            strokeWidth = context.resources.getDimension(R.dimen.graph_threshold_stroke)
            color = ContextCompat.getColor(context, R.color.instrumentThreshold)
        }

        with(instrumentMaxValuePaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            strokeWidth = context.resources.getDimension(R.dimen.graph_threshold_stroke)
            color = ContextCompat.getColor(context, R.color.sensorMax)
        }
    }

    fun toYPoint(value: Float): Float {
        val yPoint = height - scaleCoef * value
        return if (yPoint < 0) 0f else yPoint
    }

    private fun initMeasures() {
        if (!measuresInited) {
            valueSectionWidth = width.toFloat() / (GRAPH_BUFFER_SIZE)

            for (index in 0 until GRAPH_BUFFER_SIZE) {
                samplesXPoints[index] = (index * valueSectionWidth) + valueSectionWidth
            }
            samplesXPoints[0] = 0f

            updateCoef(sensorMaxValuePercentage)
            instrumentMaxValuePercentage(instrumentMaxValuePercentage)
            instrumentThresholdPercentage(instrumentThresholdPercentage)
            measuresInited = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initMeasures()

        canvas.drawLines(graphPoints, paintStroke)

        canvas.drawLine(
            0f,
            instrumentMaxValue,
            width.toFloat(),
            instrumentMaxValue,
            instrumentMaxValuePaint
        )

        canvas.drawLine(
            0f,
            instrumentThreshold,
            width.toFloat(),
            instrumentThreshold,
            thresholdPaint
        )
        // TODO: Rework it with [canvas.drawPoints] as more efficient way
        hits.forEach({
            hitPaint.strokeWidth = it.velocityAsRadius
            canvas.drawPoint(it.x, instrumentThreshold, hitPaint)
        })
    }

    private fun updateCoef(sensorMaxValue: Int) {
        val maxValue = (SENSOR_MAX_VALUE / 100f) * sensorMaxValue
        scaleCoef = height / (Math.round(maxValue * 100f) / 100f)
    }

    fun instrumentMaxValue() = (height - instrumentMaxValue) / scaleCoef

    fun instrumentThreshold() = (height - instrumentThreshold) / scaleCoef

    fun instrumentMaxValuePercentage(progress: Int) {
        instrumentMaxValuePercentage = progress
        instrumentMaxValue = height - progress / 100f * height

        paintStroke.shader = LinearGradient(
            0f,
            height.toFloat(),
            0f,
            instrumentMaxValue,
            ContextCompat.getColor(context, R.color.sensorMin),
            ContextCompat.getColor(context, R.color.sensorMax),
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun addHit(velocity: Int) {
        if (samples.isNotEmpty()) {
            hits.add(Hit(x = 0f, velocityAsRadius = velocity / 127f * 40))
        }
    }

    fun instrumentThresholdPercentage(progress: Int) {
        instrumentThresholdPercentage = progress
        instrumentThreshold = height - progress / 100f * height
        invalidate()
    }

    private var samples: List<Float> = emptyList()

    fun addValues(samples: List<Float>) {
        this.samples = samples
        for (index in 0 until GRAPH_BUFFER_SIZE - 1) {
            graphPoints[index * 4] = samplesXPoints[GRAPH_BUFFER_SIZE - 1 - index]
            graphPoints[index * 4 + 1] = samples[index]
            graphPoints[index * 4 + 2] = samplesXPoints[GRAPH_BUFFER_SIZE - index - 2]
            graphPoints[index * 4 + 3] = samples[index + 1]
        }
        hitsCircles.rewind()
        hits.removeAll { it.x + valueSectionWidth > width }
        hits.forEach({ it.x += valueSectionWidth })

        invalidate()
    }
}
