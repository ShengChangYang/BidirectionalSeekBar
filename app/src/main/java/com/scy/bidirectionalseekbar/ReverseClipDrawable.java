package com.scy.bidirectionalseekbar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.Gravity;

/**
 * Render the input drawable with {@link Gravity#RIGHT} and reverse level.
 *
 * @see ClipDrawable
 */
public class ReverseClipDrawable extends Drawable implements Drawable.Callback {
	private static final String TAG = "ReverseReverseClipDrawable";
	private static final int MAX_LEVEL = 10000;

	public enum Orientation {
		HORIZONTAL, VERTICAL
	}

	private final ClipState mClipState;
	private final Rect mTmpRect = new Rect();

	public ReverseClipDrawable(Drawable drawable, Orientation orientation) {
		this(null);

		mClipState.mDrawable = drawable;
		mClipState.mOrientation = orientation;

		if (drawable != null) {
			drawable.setCallback(this);
		}
	}

	@Override
	public void invalidateDrawable(@NonNull Drawable who) {
		final Drawable.Callback callback = getCallback();
		if (callback != null) {
			callback.invalidateDrawable(this);
		}
	}

	@Override
	public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
		final Drawable.Callback callback = getCallback();
		if (callback != null) {
			callback.scheduleDrawable(this, what, when);
		}
	}

	@Override
	public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
		final Drawable.Callback callback = getCallback();
		if (callback != null) {
			callback.unscheduleDrawable(this, what);
		}
	}

	@Override
	public int getChangingConfigurations() {
		return super.getChangingConfigurations()
			| mClipState.mChangingConfigurations
			| mClipState.mDrawable.getChangingConfigurations();
	}

	@Override
	public boolean getPadding(@NonNull Rect padding) {
		return mClipState.mDrawable.getPadding(padding);
	}

	@Override
	public boolean setVisible(boolean visible, boolean restart) {
		mClipState.mDrawable.setVisible(visible, restart);
		return super.setVisible(visible, restart);
	}

	@Override
	public void setAlpha(int alpha) {
		mClipState.mDrawable.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mClipState.mDrawable.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return mClipState.mDrawable.getOpacity();
	}

	@Override
	public boolean isStateful() {
		return mClipState.mDrawable.isStateful();
	}

	@Override
	protected boolean onStateChange(int[] state) {
		return mClipState.mDrawable.setState(state);
	}

	@Override
	protected boolean onLevelChange(int level) {
		mClipState.mDrawable.setLevel(MAX_LEVEL - level);
		invalidateSelf();
		return true;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		mClipState.mDrawable.setBounds(bounds);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		if (mClipState.mDrawable.getLevel() == 0) {
			return;
		}

		final Rect r = mTmpRect;
		final Rect bounds = getBounds();
		int level = MAX_LEVEL - getLevel();
		int w = bounds.width();
		if (mClipState.mOrientation == Orientation.HORIZONTAL) {
			w -= w * (MAX_LEVEL - level) / MAX_LEVEL;
		}
		int h = bounds.height();
		if (mClipState.mOrientation == Orientation.VERTICAL) {
			h -= h * (MAX_LEVEL - level) / MAX_LEVEL;
		}

		Gravity.apply(
			mClipState.mOrientation == Orientation.HORIZONTAL ? Gravity.RIGHT : Gravity.BOTTOM,
			w, h, bounds, 0, 0, r);

		if (w > 0 && h > 0) {
			canvas.save();
			canvas.clipRect(r);
			mClipState.mDrawable.draw(canvas);
			canvas.restore();
		}
	}

	@Override
	public int getIntrinsicWidth() {
		return mClipState.mDrawable.getIntrinsicWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		return mClipState.mDrawable.getIntrinsicHeight();
	}

	@Override
	public Drawable.ConstantState getConstantState() {
		if (mClipState.canConstantState()) {
			mClipState.mChangingConfigurations = getChangingConfigurations();
			return mClipState;
		}
		return null;
	}

	private final static class ClipState extends Drawable.ConstantState {
		Drawable mDrawable;
		int mChangingConfigurations;
		Orientation mOrientation;

		private boolean mCheckedConstantState;
		private boolean mCanConstantState;

		ClipState(ClipState orig, Drawable.Callback owner) {
			if (orig != null) {
				mDrawable = orig.mDrawable.getConstantState().newDrawable();
				mDrawable.setCallback(owner);
				mOrientation = orig.mOrientation;
				mCheckedConstantState = mCanConstantState = true;
			}
		}

		@NonNull
		@Override
		public Drawable newDrawable() {
			return new ReverseClipDrawable(this);
		}

		@NonNull
		@Override
		public Drawable newDrawable(Resources res) {
			return new ReverseClipDrawable(this);
		}

		@Override
		public int getChangingConfigurations() {
			return mChangingConfigurations;
		}

		boolean canConstantState() {
			if (!mCheckedConstantState) {
				mCanConstantState = mDrawable.getConstantState() != null;
				mCheckedConstantState = true;
			}

			return mCanConstantState;
		}
	}

	private ReverseClipDrawable(ClipState state) {
		mClipState = new ClipState(state, this);
	}
}
