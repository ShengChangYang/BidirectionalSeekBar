package com.scy.bidirectionalseekbar;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class HalfColorDrawable extends ColorDrawable {
	private static final String TAG = "HalfColorDrawable";

	public enum Align {
		LEFT, RIGHT
	}

	private final Rect tempRect = new Rect();
	private final Align align;

	public HalfColorDrawable(@ColorInt int color, @NonNull Align align) {
		super(color);
		this.align = align;
	}

	@Override
	public void draw(Canvas canvas) {
		tempRect.set(getBounds());

		switch (align) {
			case LEFT:
				tempRect.right = tempRect.right - Math.round(tempRect.width() * 0.5f);
				break;

			case RIGHT:
				tempRect.left = tempRect.left + Math.round(tempRect.width() * 0.5f);
				break;
		}

		int saveCount = canvas.save();
		canvas.clipRect(tempRect);
		super.draw(canvas);
		canvas.restoreToCount(saveCount);
	}
}
