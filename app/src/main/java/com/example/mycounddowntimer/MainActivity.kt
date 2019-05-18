package com.example.mycounddowntimer

import android.icu.util.DateInterval
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    // タイマーの作り方
    // 1. CountDownTimerを継承したクラスを作成する
    // 2. onTickメソッドとonFinishメソッドをoverride
    // 3. インスタンス作成
    // 4. startメソッドでカウントダウンを開始する

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
}

