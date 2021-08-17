package com.rafsan.image_subtitlebuttonlib

import android.animation.LayoutTransition
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rafsan.image_subtitlebuttonlib.model.Shape
import com.rafsan.image_subtitlebuttonlib.util.dpToPx


/**
 * A custom button that displays an image, a title, and a subtitle.
 */
class ImageSubtitleButton : LinearLayout {
    private lateinit var container: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var titleView: TextView
    private lateinit var subtitleView: TextView

    private var cornerRadius: Float = 0f
    private var rippleColor: Int = 0
    private var borderColor: Int = 0
    private var borderWidth: Float = 0f
    private var btnShape: Shape = Shape.RECTANGLE
    private var btnBackgroundColor: Int = Color.BLUE

    private var buttonListener: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context, null, null, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, null, null)
    }

    constructor(ctx: Context, attr: AttributeSet?, defStyle: Int) : super(ctx, attr, defStyle) {
        init(ctx, attr, defStyle, null)
    }

    @TargetApi(21)
    constructor(ctx: Context, attr: AttributeSet?, defStyle: Int, defStyleRes: Int)
            : super(ctx, attr, defStyle, defStyleRes) {
        init(ctx, attr, defStyle, defStyleRes)
    }


    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int?, defStyleRes: Int?) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ImageSubtitleButton,
            defStyle ?: 0,
            defStyleRes ?: 0
        )

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.button_layout, this, true)
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER

        /* Set android:animateLayoutChanges="true" programmatically */
        layoutTransition = LayoutTransition()

        container = findViewById(R.id.btn_container)
        imageView = findViewById(R.id.btn_image)
        titleView = findViewById(R.id.btn_title)
        subtitleView = findViewById(R.id.btn_subtitle)

        val imageRes: Int
        val imageTint: Int
        val imageVisible: Boolean
        val imageSize: Int

        val title: String?
        val titleColor: Int
        val titleSize: Float?

        val subtitle: String?
        val subtitleColor: Int
        val subtitleSize: Float?

        val titleVisible: Boolean
        val subtitleVisible: Boolean

        try {
            imageRes =
                a.getResourceId(R.styleable.ImageSubtitleButton_btn_image, R.drawable.info_icon)
            imageTint = a.getColor(R.styleable.ImageSubtitleButton_btn_imageTint, 0)
            imageVisible = a.getBoolean(R.styleable.ImageSubtitleButton_btn_imageVisible, false)
            imageSize = a.getDimensionPixelSize(R.styleable.ImageSubtitleButton_btn_imageSize, 0)
            title = a.getString(R.styleable.ImageSubtitleButton_btn_title)
            titleColor = a.getColor(
                R.styleable.ImageSubtitleButton_btn_titleColor,
                ContextCompat.getColor(context, R.color.view_title)
            )
            titleVisible = a.getBoolean(R.styleable.ImageSubtitleButton_btn_titleVisible, true)
            subtitle = a.getString(R.styleable.ImageSubtitleButton_btn_subtitle)
            subtitleColor = a.getColor(
                R.styleable.ImageSubtitleButton_btn_subtitleColor,
                ContextCompat.getColor(context, R.color.view_subtitle)
            )
            subtitleVisible =
                a.getBoolean(R.styleable.ImageSubtitleButton_btn_subtitleVisible, true)
            titleSize = a.getDimension(R.styleable.ImageSubtitleButton_btn_titleSize, 20f)
            subtitleSize = a.getDimension(R.styleable.ImageSubtitleButton_btn_subtitleSize, 12f)

            cornerRadius = a.getDimension(R.styleable.ImageSubtitleButton_btn_cornerRadius, 0f)
            rippleColor = a.getColor(
                R.styleable.ImageSubtitleButton_btn_rippleColor,
                Color.TRANSPARENT
            )
            btnBackgroundColor = a.getColor(
                R.styleable.ImageSubtitleButton_btn_backgroundColor,
                Color.BLUE
            )
            borderColor = a.getColor(
                R.styleable.ImageSubtitleButton_btn_borderColor,
                Color.TRANSPARENT
            )
            borderWidth = a.getDimension(R.styleable.ImageSubtitleButton_btn_borderWidth, 0f)
            btnShape =
                Shape.fromInt(
                    a.getInt(
                        R.styleable.ImageSubtitleButton_btn_shape,
                        Shape.RECTANGLE.shape
                    )
                )

            if (imageRes != 0) {
                setImage(imageRes)
            }
            if (imageTint != 0) {
                setImageTint(imageTint)
            }
            setImageVisible(imageVisible)
            if (imageSize != 0) {
                setImageSize(imageSize)
            }

            if (title != null) {
                setTitle(title)
            }

            if (subtitle != null) {
                setSubtitle(subtitle)
                subtitleView.visibility = View.VISIBLE
            }

            if (!titleVisible) {
                titleView.visibility = View.GONE
            }

            if (!subtitleVisible) {
                subtitleView.visibility = View.GONE
            }
            titleView.setTextColor(titleColor)
            subtitleView.setTextColor(subtitleColor)
            setTitleSize(titleSize)
            setSubtitleSize(subtitleSize)

            setRippleColor(rippleColor)

            if (cornerRadius != 0f) {
                setCornerRadius(cornerRadius)
            }

            setBtnBackgroundColor(btnBackgroundColor)

            if (borderWidth != 0f) {
                setButtonBorder(borderWidth, borderColor, btnBackgroundColor)
            }

            if (btnShape != Shape.RECTANGLE) {
                setButtonShape(btnShape)
            }

        } finally {
            a.recycle()
        }
    }

    private fun setTitleSize(titleSize: Float): ImageSubtitleButton {
        titleView.textSize = titleSize
        return this
    }

    private fun setSubtitleSize(subtitleSize: Float): ImageSubtitleButton {
        subtitleView.textSize = subtitleSize
        return this
    }

    /**
     * Indicates whether the title is currently visible.
     */
    var isImageVisible: Boolean
        get() = imageView.visibility == View.VISIBLE
        set(value) = setImageVisible(value).unitify()

    /**
     * Returns the current title string.
     */
    var title: CharSequence
        get() = titleView.text
        set(value) = setTitle(value.toString()).unitify()

    /**
     * Indicates whether the title is currently visible.
     */
    var isTitleVisible: Boolean
        get() = titleView.visibility == View.VISIBLE
        set(value) = setTitleVisible(value).unitify()

    /**
     * Returns the current subtitle.
     */
    var subtitle: CharSequence
        get() = subtitleView.text
        set(value) = setSubtitle(value.toString()).unitify()

    /**
     * Indicates whether the subtitle is currently visible.
     */
    var isSubtitleVisible: Boolean
        get() = subtitleView.visibility == View.VISIBLE
        set(value) = setSubtitleVisible(value).unitify()

    /**
     * Sets button image to a given drawable resource.
     */
    fun setImage(res: Int): ImageSubtitleButton {
        imageView.setImageResource(res)
        return this
    }

    /**
     * Sets the button image to a given [android.graphics.drawable.Drawable].
     */
    fun setImage(drawable: Drawable): ImageSubtitleButton {
        imageView.setImageDrawable(drawable)
        return this
    }

    /**
     * Sets the image button to a given [android.graphics.Bitmap].
     */
    fun setImage(bitmap: Bitmap): ImageSubtitleButton {
        imageView.setImageBitmap(bitmap)
        return this
    }

    /**
     * Tints the Button image with given color.
     */
    fun setImageTint(color: Int): ImageSubtitleButton {
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return this
    }

    /**
     * Shows or hides the Button image.
     */
    fun setImageVisible(visible: Boolean): ImageSubtitleButton {
        imageView.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * Sets Button image width to given pixel value. The ImageView adjusts its bounds to preserve the
     * aspect ratio of its drawable.
     */
    fun setImageSize(width: Int): ImageSubtitleButton {
        imageView.layoutParams.width = width
        return this
    }

    /**
     * Sets the Button title to a given [java.lang.String].
     */
    fun setTitle(text: String?): ImageSubtitleButton {
        setTitleVisible(text != null)
        titleView.text = text
        return this
    }

    /**
     * Sets the Button title to a given string resource.
     */
    fun setTitle(res: Int): ImageSubtitleButton {
        // An exception will be thrown if the res isn't found anyways so it's safe to just go ahead
        // and make the title visible.
        setTitleVisible(true)
        titleView.setText(res)
        return this
    }

    /**
     * Sets the Button title text to a given color.
     */
    fun setTitleColor(color: Int): ImageSubtitleButton {
        titleView.setTextColor(color)
        return this
    }

    /**
     * Sets the Button background to a given color.
     */
    fun setBtnBackgroundColor(color: Int): ImageSubtitleButton {
        container.setBackgroundColor(color)
        return this
    }

    /**
     * Shows or hides the Button title
     */
    fun setTitleVisible(visible: Boolean): ImageSubtitleButton {
        titleView.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * Sets the Button subtitle to a given [java.lang.String].
     */
    fun setSubtitle(subtitle: String?): ImageSubtitleButton {
        setSubtitleVisible(subtitle != null)
        subtitleView.text = subtitle
        return this
    }

    /**
     * Sets the Button subtitle to a given string resource.
     */
    fun setSubtitle(res: Int): ImageSubtitleButton {
        // An exception will be thrown if the res isn't found anyways so it's safe to just go ahead
        // and make the title visible.
        setSubtitleVisible(true)
        subtitleView.setText(res)
        return this
    }

    /**
     * Sets the Button subtitle text to a given color
     */
    fun setSubtitleColor(color: Int): ImageSubtitleButton {
        subtitleView.setTextColor(color)
        return this
    }

    /**
     * Shows or hides Button subtitle.
     */
    fun setSubtitleVisible(visible: Boolean): ImageSubtitleButton {
        subtitleView.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }


    /**
     * Get the button corner radius
     * @return button corner radius [Float]
     */
    fun getCornerRadius(): Float = getButton().cornerRadius

    /**
     * Set the button corner radius
     * @param cornerRadius [Float]
     */
    fun setCornerRadius(cornerRadius: Float): ImageSubtitleButton {
        getButton().cornerRadius = cornerRadius
        setButtonCornerRadius(cornerRadius)
        return this
    }

    /**
     * Get the button ripple effect color
     * @return ripple effect color [Int]
     */
    fun getRippleColor(): Int = getButton().rippleColor

    /**
     * Set the button ripple effect color
     * @param rippleColor [Int]
     */
    fun setRippleColor(rippleColor: Int): ImageSubtitleButton {
        getButton().rippleColor = rippleColor
        val drawable = GradientDrawable()
        drawable.setColor(btnBackgroundColor)
        addRipple(drawable)
        return this
    }

    /**
     * Get the button shape. By default uses [Shape.RECTANGLE]
     * @return button shape [Shape]
     */
    fun getButtonShape(): Shape = getButton().btnShape

    /**
     * Set the button shape
     * @param btnShape [Shape]
     */
    fun setButtonShape(btnShape: Shape): ImageSubtitleButton {
        getButton().btnShape = btnShape
        drawShape()
        return this
    }

    /**
     * Get the button border color
     * @return border color [Int]
     */
    fun getBorderColor(): Int = getButton().borderColor

    /**
     * Set the button border color
     * @param borderColor [Int]
     */
    fun setBorderColor(borderColor: Int): ImageSubtitleButton {
        getButton().borderColor = borderColor
        setButtonBorder(
            getButton().borderWidth,
            getButton().borderColor,
            getButton().btnBackgroundColor
        )
        return this
    }

    /**
     * Get the button border width
     * @return border width [Float]
     */
    fun getBorderWidth(): Float = getButton().borderWidth

    /**
     * Set the button border width
     * @param borderWidth [Float]
     */
    fun setBorderWidth(borderWidth: Float): ImageSubtitleButton {
        getButton().borderWidth = dpToPx(borderWidth)
        setButtonBorder(
            getButton().borderWidth,
            getButton().borderColor,
            getButton().btnBackgroundColor
        )
        return this
    }

    fun getButton(): ImageSubtitleButton {
        return this
    }

    fun setButtonBorder(borderWidth: Float, borderColor: Int, backgroundColor: Int) {
        val border = GradientDrawable()
        border.setColor(backgroundColor)
        border.setStroke(borderWidth.toInt(), borderColor)
        if (cornerRadius != 0f) {
            border.cornerRadius = getButton().cornerRadius
        }
        if (rippleColor != 0) {
            addRipple(border)
        } else {
            container.background = border
        }
    }

    fun setButtonCornerRadius(radius: Float) {
        val drawable = GradientDrawable()
        drawable.cornerRadius = radius
        if (rippleColor != 0) {
            addRipple(drawable)
        } else {
            container.background = drawable
        }
    }

    fun addRipple(drawable: GradientDrawable) {
        val colorStateList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(rippleColor))
        val rippleDrawable = RippleDrawable(colorStateList, drawable, null)
        container.background = rippleDrawable
    }

    // Draw button shape
    private fun drawShape() {
        val drawable = GradientDrawable()
        drawable.setColor(btnBackgroundColor)
        drawable.shape = when (getButton().btnShape) {
            Shape.RECTANGLE -> GradientDrawable.RECTANGLE
            Shape.OVAL -> GradientDrawable.OVAL
            Shape.SQUARE -> alignSides(GradientDrawable.RECTANGLE)
            Shape.CIRCLE -> alignSides(GradientDrawable.OVAL)
        }
        container.background = drawable
    }

    // Align shape sides
    private fun alignSides(shape: Int): Int {
        val dimension = if (container.layoutParams != null) {
            defineFitSide(container.layoutParams.width, container.layoutParams.height)
        } else {
            defineFitSide(container.measuredWidth, container.measuredHeight)
        }
        if (container.layoutParams != null) {
            container.layoutParams.width = dimension
            container.layoutParams.height = dimension
        }
        return shape
    }

    // Get a min side or a max side if anyone side equal zero or less
    private fun defineFitSide(w: Int, h: Int): Int {
        return if (w <= 0 || h <= 0) {
            Math.max(w, h)
        } else {
            Math.min(w, h)
        }
    }

    private fun Any.unitify(): Unit {}
}

