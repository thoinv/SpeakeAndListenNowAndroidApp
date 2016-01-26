package vn.eway.englishpronunciationpractice;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

import vn.english.tools.speak.instance.R;
import vn.eway.englishpronunciationpractice.utils.LogUtils;
import vn.eway.englishpronunciationpractice.utils.NotificationUtils;
import vn.eway.englishpronunciationpractice.utils.StorageUtils;
import vn.eway.englishpronunciationpractice.utils.helper.CircleImageView;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.audio.AudioPlaybackManager;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.audio.AudioRecordingHandler;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.audio.AudioRecordingThread;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.video.PlaybackHandler;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.visualizer.VisualizerView;
import vn.eway.englishpronunciationpractice.utils.helper.recorder.visualizer.renderer.BarGraphRenderer;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private CircleImageView btnRecord;
    private TextView txtRecord;
    private AdView adView;
    private AudioRecordingThread recordingThread;
    private String fileName;
    private VisualizerView visualizerView;
    private Animation shakeAnimation;
    private Timer timer;
    private TimerTask animTimerTask;
    private AudioPlaybackManager playbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
        btnRecord = (CircleImageView)findViewById(R.id.btnRecord);
        btnRecord.setClickable(true);
        btnRecord.setOnTouchListener(this);

        txtRecord = (TextView)findViewById(R.id.txtRecord);
        txtRecord.setClickable(true);
        txtRecord.setOnTouchListener(this);

        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        visualizerView = (VisualizerView) findViewById(R.id.visualizeView);
        setupVisualizer();

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        timer = new Timer();

        animTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(recordingThread != null){
                    return;
                }

                if(playbackManager == null){
                    return;
                }

                if(playbackManager.isPlaying())
                    return;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(btnRecord != null)
                            btnRecord.startAnimation(shakeAnimation);
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(animTimerTask, 500, 15000);
    }

    private void setupVisualizer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);                     //set bar width
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 227, 69, 53)); //set bar color
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(2, paint, false);
        visualizerView.addRenderer(barGraphRendererBottom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                btnRecord.setImageDrawable(new ColorDrawable(Color.RED));
                txtRecord.setText(getString(R.string.txt_recording));
                recordSound();
                break;

            case MotionEvent.ACTION_UP:
                btnRecord.setImageDrawable(new ColorDrawable(Color.parseColor("#FF4081")));
                txtRecord.setText(getString(R.string.txt_record));
                stopRecording();
                break;
        }
        return false;
    }

    private void stopRecording() {
        LogUtils.log("@stopRecording");
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread.interrupt();
            recordingThread = null;
        }

    }

    private void recordSound() {
        LogUtils.log("@recordSound");
        if (!StorageUtils.checkExternalStorageAvailable()) {
            NotificationUtils.showInfoDialog(this, getString(R.string.noExtStorageAvailable));
            return;
        }

        fileName = StorageUtils.getFileName(true);
        recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() { //pass file name where to store the recorded audio
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes); //update VisualizerView with new audio portion
                        }
                    }
                });
            }

            @Override
            public void onRecordSuccess() {
                LogUtils.log("@onRecordSuccess");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playbackManager = new AudioPlaybackManager(MainActivity.this, visualizerView, new PlaybackHandler() {
                            @Override
                            public void onPreparePlayback() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }

                            @Override
                            public void onFinish() {
                            }
                        });

                        playbackManager.setupPlayback(fileName);
                        playbackManager.start();
                    }
                });
            }

            @Override
            public void onRecordingError() {
                LogUtils.log("@onRecordingError");
                NotificationUtils.showInfoDialog(MainActivity.this, getString(R.string.recordingError));
            }

            @Override
            public void onRecordSaveError() {
                LogUtils.log("@onRecordSaveError");
                NotificationUtils.showInfoDialog(MainActivity.this, getString(R.string.saveRecordError));
            }
        });
        recordingThread.start();
    }

}
