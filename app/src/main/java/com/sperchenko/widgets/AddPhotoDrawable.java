package com.sperchenko.widgets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by stanislav.perchenko on 1/25/2016.
 */
public class AddPhotoDrawable extends Drawable {

    private boolean hasBounds;

    /**
     * These are actual dimensions of the drawing area of this drawable.
     * They are updated on drawable bounds changing.
     */
    private int mWidth, mHeight;

    /**
     * Left and Top offsets of the drawable's drawing area.
     */
    private int mDrawingAreaLeft, mDrawingAreaTop;

    private Paint mTextPaint;
    private Paint mCrossPaint;

    private CharSequence mText;
    private String mTextString;

    /**
     * Defines space between the left, right and bottom edges of the drawable
     * and a text.
     */
    private int mTextPadding;

    /**
     * this parameter is evaluated once in draw() method if its previous value
     * is invalid. Value 0 is invalid.
     * The value is invalidated if bounds, text and/or text padding is changed.
     */
    private float mTextSize;

    /**
     * Defines actual text padding for drawing, which corresponds currently set
     * mTextSize value.
     */
    private float mActualTextPadding;

    public AddPhotoDrawable() {
        init(null, 0, 0);
    }

    public AddPhotoDrawable(CharSequence text) {
        init(text, 0, 0);
    }

    public AddPhotoDrawable(CharSequence text, int textPadding) {
        init(text, textPadding, 0);
    }

    public AddPhotoDrawable(CharSequence text, int textPadding, int textColor) {
        init(text, textPadding, textColor);
    }

    private void init(CharSequence text, int textPadding, int textColor) {
        mText = text;
        mTextString = (text != null) ? text.toString() : null;
        mTextPadding = textPadding;
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(textColor);
        mCrossPaint = new Paint();
        mCrossPaint.setAntiAlias(false);
        mCrossPaint.setStyle(Paint.Style.STROKE);
        mCrossPaint.setStrokeWidth(11);
        mCrossPaint.setColor(textColor);
    }

    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        this.mText = text;
        mTextString = (text != null) ? text.toString() : null;
        mTextSize = 0;
        invalidateSelf();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int textPadding) {
        this.mTextPadding = textPadding;
        mTextSize = 0;
        invalidateSelf();
    }

    public void setColor(int color) {
        mTextPaint.setColor(color);
        mCrossPaint.setColor(color);
        invalidateSelf();
    }

    public void setCrossStrokeWidth(float strokeWidth) {
        mCrossPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        // When this callback happens, we can be sure the Drawable's bounds has been changed
        // So no need to compare with the previous bounds

        // Invalidate measurements
        mTextSize = 0;
        mWidth = bounds.width();
        mHeight = bounds.height();
        mDrawingAreaLeft = bounds.left;
        mDrawingAreaTop = bounds.top;
        hasBounds = true;
    }

    @Override
    public int getIntrinsicWidth() {
        return (hasBounds) ? mWidth : super.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return (hasBounds) ? mHeight : super.getIntrinsicHeight();
    }

    private static final float D_TEXT_SIZE = 0.25f;

    @Override
    public void draw(Canvas canvas) {
        if (!hasBounds) {
            return;
        }

        final float width = mWidth;
        final float height = mHeight;
        float textWidth = width - 2 * mTextPadding;
        float textHeight;

        //--- Evaluate test size parameter if not set to a valid value ---
        if (mTextSize == 0) {
            mTextSize = D_TEXT_SIZE;
            float currTextWidth;
            do {
                mTextSize += D_TEXT_SIZE;
                mTextPaint.setTextSize(mTextSize);
                currTextWidth = mTextPaint.measureText(mText, 0, mText.length());
            } while (currTextWidth < textWidth);
            if (currTextWidth > textWidth) {
                mTextSize -= D_TEXT_SIZE;
            }
            mTextPaint.setTextSize(mTextSize);


            textWidth = mTextPaint.measureText(mText, 0, mText.length());
            mTextPaint.getTextBounds(mTextString, 0, mText.length(), rectTextBounds);
            textHeight = rectTextBounds.height();

            final float p = (width - textWidth) / 2f;
            rectClipText.left = p;
            rectClipText.right = width - p;
            rectClipText.top = height - 2 * p - textHeight;
            rectClipText.bottom = height;
            mActualTextPadding = p;


            // Find cross icon size
            final float cSizeUpLimit = (height - textHeight - 2 * p) / 2f;
            final float cSizeBotLimit = (height - textHeight - 2 * p) / 3f;
            if (textHeight > cSizeUpLimit) {
                mCrossSize = cSizeUpLimit;
            } else if (textHeight < cSizeBotLimit) {
                mCrossSize = cSizeBotLimit;
            } else {
                mCrossSize = textHeight;
            }

            // Verify with the drawable width
            mCrossSize = Math.min(mCrossSize, width - 2 * p);

            mCrossHalfSize = mCrossSize / 2f;
            mCrossVertOffset = 0.7f * (height - textHeight - 2 * p - mCrossSize);
        }

        //--- Translate to the actual drawing area, if there is some offset
        if (mDrawingAreaLeft > 0 || mDrawingAreaTop > 0) {
            canvas.translate(mDrawingAreaLeft, mDrawingAreaTop);
        }

        //--- Draw text ---
        canvas.save();
        canvas.clipRect(rectClipText);
        canvas.drawText(mText, 0, mText.length(), width / 2f, height - 2 * mActualTextPadding, mTextPaint);
        canvas.restore();

        //--- Drawing the cross ---
        canvas.translate((width - mCrossSize) / 2f, mCrossVertOffset);
        canvas.clipRect(0, 0, mCrossSize, mCrossSize);
        canvas.drawLine(0, mCrossHalfSize, width, mCrossHalfSize, mCrossPaint);
        canvas.drawLine(mCrossHalfSize, 0, mCrossHalfSize, height, mCrossPaint);
    }

    private float mCrossSize, mCrossHalfSize;
    private float mCrossVertOffset;

    private RectF rectClipText = new RectF();
    private Rect rectTextBounds = new Rect();

    @Override
    public void setAlpha(int alpha) {
        mTextPaint.setAlpha(alpha);
        mCrossPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mTextPaint.setColorFilter(cf);
        mCrossPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}