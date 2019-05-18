package com.example.mycounddowntimer

import android.icu.util.DateInterval
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    /*
    タイマーの作り方
      1. CountDownTimerを継承したクラスを作成する
      2. onTickメソッドとonFinishメソッドをoverride
      3. インスタンス作成
      4. startメソッドでカウントダウンを開始する
    */


    /*
    サウンドのつけかた
      1. SoundPoolクラスのインスタンスを作成する
      2. loadメソッドでサウンドファイルを読み込み、サウンドIDを取得する
      3. playメソッドにサウンドIDを渡して再生を開始する
      4. releaseメソッドでSoundPoolのりソースを解放する
     */

    private lateinit var soundPool: SoundPool
    private var soundResId = 0

    inner class MyCountDownTimer(millisInFuture: Long,
                                 countDownInterval: Long) :
                CountDownTimer(millisInFuture, countDownInterval) {

        var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            // %1 => 1つめの変数とリンク, 1d => 1桁の整数で表示
            // %2 => 2つめの変数とリンク, 02d => 2桁の整数で表示
            timerText.text = "%1d:%2$02d".format(minute, second)
        }

        override fun onFinish() {
            timerText.text = "0.00"
            soundPool.play(soundResId, 1.0f, 100f, 0, 0, 1.0f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "3:00"
        val timer = MyCountDownTimer(3 * 60 * 1000, 100)
        playStop.setOnClickListener {
            timer.isRunning = when (timer.isRunning) {
                true -> {
                    timer.cancel()
                    playStop.setImageResource(
                        R.drawable.ic_play_arrow_black_24dp
                    )
                    false
                }
                false -> {
                    timer.start()
                    playStop.setImageResource(
                        R.drawable.ic_stop_black_24dp
                    )
                    true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        soundPool = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // API 19以上、21未満の対応
            @Suppress("DEPRECATION") // 非推奨メソッドを使っているが、対応済なので検査不要
            SoundPool(2, AudioManager.STREAM_ALARM, 0)
        } else {
            // API 21以上の対応
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        }



        soundResId = soundPool.load(this, R.raw.bellsound, 1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }
}

