package com.scy.bidirectionalseekbar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.SeekBar;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initSeekBar();
	}

	private void initSeekBar() {
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
		layerDrawable.setDrawableByLayerId(android.R.id.progress, newLevelListDrawable());
		seekBar.setProgressDrawable(layerDrawable);
		seekBar.setProgress(50);
	}

	private LevelListDrawable newLevelListDrawable() {
		LevelListDrawable levelListDrawable = new LevelListDrawable();
		levelListDrawable.addLevel(0, 5000, new ReverseClipDrawable(new ColorDrawable(Color.RED), ReverseClipDrawable.Orientation.HORIZONTAL));
		levelListDrawable.addLevel(5001, 10000, new ClipDrawable(new ColorDrawable(Color.BLUE), Gravity.LEFT, ClipDrawable.HORIZONTAL));
		return levelListDrawable;
	}
}
