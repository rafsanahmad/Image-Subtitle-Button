package com.rafsan.image_subtitlebuttonlib.model

/**
 * Kind of shapes Custom Button
 */
enum class Shape(val shape: Int) {
    RECTANGLE(0),
    SQUARE(1),
    CIRCLE(2),
    OVAL(3);

    companion object {
        fun fromInt(value: Int) = Shape.values().first { it.shape == value }
    }
}
