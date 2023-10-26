package com.example.objectclassifier

import android.graphics.RectF
import org.tensorflow.lite.Interpreter

class ObjectDetectionHelper(private val tflite: Interpreter, private val labels: List<String>) {

    /** Abstraction object that wraps a prediction output in an easy way to parse */
    data class ObjectPrediction(val location: RectF, val label: String, val score: Float)

    private val locations = arrayOf(Array(OBJECT_COUNT) { FloatArray(4) })
    private val labelIndices = arrayOf(FloatArray(OBJECT_COUNT))
    private val scores = arrayOf(FloatArray(OBJECT_COUNT))

    private val outputBuffer = mapOf(
        0 to locations,
        1 to labelIndices,
        2 to scores,
        3 to FloatArray(1)
    )

    val predictions get() = (0 until OBJECT_COUNT).map {
        ObjectPrediction(

            // The locations are an array of [0, 1] floats for [top, left, bottom, right]
            location = locations[0][it].let {
                RectF(it[1], it[0], it[3], it[2])
            },

            // SSD Mobilenet V1 Model assumes class 0 is background class
            // in label file and class labels start from 1 to number_of_classes + 1,
            // while outputClasses correspond to class index from 0 to number_of_classes
        )
    }

}