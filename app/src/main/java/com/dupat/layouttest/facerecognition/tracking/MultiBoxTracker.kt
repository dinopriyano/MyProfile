/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/
package com.dupat.layouttest.facerecognition.tracking

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.graphics.Paint.Join
import android.text.TextUtils
import android.util.Pair
import android.util.TypedValue
import com.dupat.layouttest.facerecognition.env.BorderedText
import com.dupat.layouttest.facerecognition.env.ImageUtils
import com.dupat.layouttest.facerecognition.env.Logger
import com.dupat.layouttest.facerecognition.tflite.SimilarityClassifier.Recognition
import java.util.*

class MultiBoxTracker(context: Context) {
    val screenRects: MutableList<Pair<Float?, RectF>> =
        LinkedList()
    private val logger =
        Logger()
    private val availableColors: Queue<Int> = LinkedList()
    private val trackedObjects: MutableList<TrackedRecognition> =
        LinkedList()
    private val boxPaint = Paint()
    private val textSizePx: Float
    private val borderedText: BorderedText
    private var frameToCanvasMatrix: Matrix? = null
    private var frameWidth = 0
    private var frameHeight = 0
    private var sensorOrientation = 0

    @Synchronized
    fun setFrameConfiguration(
        width: Int, height: Int, sensorOrientation: Int
    ) {
        frameWidth = width
        frameHeight = height
        this.sensorOrientation = sensorOrientation
    }

    @Synchronized
    fun drawDebug(canvas: Canvas) {
        val textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 60.0f
        val boxPaint = Paint()
        boxPaint.color = Color.RED
        boxPaint.alpha = 200
        boxPaint.style = Paint.Style.STROKE
        for (detection in screenRects) {
            val rect = detection.second
            canvas.drawRect(rect, boxPaint)
            canvas.drawText("" + detection.first, rect.left, rect.top, textPaint)
            borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + detection.first)
        }
    }

    @Synchronized
    fun trackResults(
        results: List<Recognition>,
        timestamp: Long
    ) {
        logger.i("Processing %d results from %d", results.size, timestamp)
        processResults(results)
    }

    @Synchronized
    fun draw(canvas: Canvas) {
        val rotated = sensorOrientation % 180 == 90
        val multiplier = Math.min(
            canvas.height / (if (rotated) frameWidth else frameHeight).toFloat(),
            canvas.width / (if (rotated) frameHeight else frameWidth).toFloat()
        )
        frameToCanvasMatrix =
            ImageUtils.getTransformationMatrix(
                frameWidth,
                frameHeight,
                (multiplier * if (rotated) frameHeight else frameWidth).toInt(),
                (multiplier * if (rotated) frameWidth else frameHeight).toInt(),
                sensorOrientation,
                false
            )
        for (recognition in trackedObjects) {
            val trackedPos = RectF(recognition.location)
            frameToCanvasMatrix!!.mapRect(trackedPos)
            boxPaint.color = recognition.color
            val cornerSize =
                Math.min(trackedPos.width(), trackedPos.height()) / 8.0f
            canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint)
            val strConfidence =
                if (recognition.detectionConfidence < 0) "" else String.format(
                    "%.2f",
                    recognition.detectionConfidence
                ) + ""
            val strPercen = if(strConfidence != ""){(strConfidence.replace(",",".").toFloat() * 100).toInt().toString()+"%"} else ""
            val labelString =
                if (!TextUtils.isEmpty(recognition.title)) String.format(
                    "%s %s",
                    recognition.title,
                    strPercen
                ) else strPercen
            borderedText.drawText(
                canvas, trackedPos.left + cornerSize, trackedPos.top, labelString, boxPaint
            )
        }
    }

    private fun processResults(results: List<Recognition>) {
        val rectsToTrack: MutableList<Pair<Float, Recognition>> =
            LinkedList()
        screenRects.clear()
        val rgbFrameToScreen =
            Matrix(frameToCanvasMatrix)
        for (result in results) {
            if (result.getLocation() == null) {
                continue
            }
            val detectionFrameRect = RectF(result.getLocation())
            val detectionScreenRect = RectF()
            rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect)
            logger.v(
                "Result! Frame: " + result.getLocation() + " mapped to screen:" + detectionScreenRect
            )
            screenRects.add(
                Pair(
                    result.distance,
                    detectionScreenRect
                )
            )
            if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
                logger.w("Degenerate rectangle! $detectionFrameRect")
                continue
            }
            rectsToTrack.add(Pair<Float, Recognition>(result.distance, result))
        }
        trackedObjects.clear()
        if (rectsToTrack.isEmpty()) {
            logger.v("Nothing to track, aborting.")
            return
        }
        for (potential in rectsToTrack) {
            val trackedRecognition = TrackedRecognition()
            trackedRecognition.detectionConfidence = potential.first
            trackedRecognition.location = RectF(potential.second.getLocation())
            trackedRecognition.title = potential.second.title
            if (potential.second.color != null) {
                trackedRecognition.color = potential.second.color!!
            } else {
                trackedRecognition.color = COLORS[trackedObjects.size]
            }
            trackedObjects.add(trackedRecognition)
            if (trackedObjects.size >= COLORS.size) {
                break
            }
        }
    }

    private class TrackedRecognition {
        var location: RectF? = null
        var detectionConfidence = 0f
        var color = 0
        var title: String? = null
    }

    companion object {
        private const val TEXT_SIZE_DIP = 18f
        private const val MIN_SIZE = 16.0f
        private val COLORS = intArrayOf(
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.WHITE,
            Color.parseColor("#55FF55"),
            Color.parseColor("#FFA500"),
            Color.parseColor("#FF8888"),
            Color.parseColor("#AAAAFF"),
            Color.parseColor("#FFFFAA"),
            Color.parseColor("#55AAAA"),
            Color.parseColor("#AA33AA"),
            Color.parseColor("#0D0068")
        )
    }

    init {
        for (color in COLORS) {
            availableColors.add(color)
        }
        boxPaint.color = Color.RED
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = 10.0f
        boxPaint.strokeCap = Cap.ROUND
        boxPaint.strokeJoin = Join.ROUND
        boxPaint.strokeMiter = 100f
        textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            TEXT_SIZE_DIP,
            context.resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
    }
}