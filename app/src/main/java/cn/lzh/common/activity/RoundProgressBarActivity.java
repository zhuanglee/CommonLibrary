package cn.lzh.common.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.view.RingProgressBar;

public class RoundProgressBarActivity extends BaseWatermarkActivity {
	private RingProgressBar mRoundProgressBar2;
	private RingProgressBar mRoundProgressBar4;
	private int progress = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_round_progress_bar);
		initToolbar(true);
		mRoundProgressBar2 = (RingProgressBar) findViewById(R.id.roundProgressBar2);
		mRoundProgressBar4 = (RingProgressBar) findViewById(R.id.roundProgressBar4);
	}

	public void onClick(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				progress = 0;
				while(progress <= 100){
					progress += 3;

					System.out.println(progress);

					mRoundProgressBar2.setProgress(progress);
					mRoundProgressBar4.setProgress(progress);

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

}
