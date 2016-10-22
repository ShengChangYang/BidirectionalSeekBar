package com.scy.bidirectionalseekbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
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

		initSeekBar1();
		initSeekBar2();
	}

	private void initSeekBar1() {
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
		LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
		layerDrawable.setDrawableByLayerId(android.R.id.progress, newLevelListDrawable1());
		seekBar.setProgressDrawable(layerDrawable);
		seekBar.setProgress(50);
	}

	private static LevelListDrawable newLevelListDrawable1() {
		LevelListDrawable levelListDrawable = new LevelListDrawable();
		levelListDrawable.addLevel(0, 5000, new ReverseClipDrawable(new HalfColorDrawable(Color.RED, HalfColorDrawable.Align.LEFT), ReverseClipDrawable.Orientation.HORIZONTAL));
		levelListDrawable.addLevel(5001, 10000, new ClipDrawable(new HalfColorDrawable(Color.BLUE, HalfColorDrawable.Align.RIGHT), Gravity.LEFT, ClipDrawable.HORIZONTAL));
		return levelListDrawable;
	}

	private void initSeekBar2() {
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);
		LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
		Resources resources = getResources();
		layerDrawable.setDrawableByLayerId(android.R.id.background, resources.getDrawable(R.drawable.progress_background));
		layerDrawable.setDrawableByLayerId(android.R.id.progress, newLevelListDrawable2(resources));
		seekBar.setProgressDrawable(layerDrawable);
		seekBar.setProgress(50);
	}

	private static LevelListDrawable newLevelListDrawable2(Resources resources) {
		LevelListDrawable levelListDrawable = new LevelListDrawable();
		levelListDrawable.addLevel(0, 5000, new ReverseClipDrawable(resources.getDrawable(R.drawable.progress_left), ReverseClipDrawable.Orientation.HORIZONTAL));
		levelListDrawable.addLevel(5001, 10000, new ClipDrawable(resources.getDrawable(R.drawable.progress_right), Gravity.LEFT, ClipDrawable.HORIZONTAL));
		return levelListDrawable;
	}
}
