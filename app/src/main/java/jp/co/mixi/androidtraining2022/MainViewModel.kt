package jp.co.mixi.androidtraining2022

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // 現在のタイマーの状態
    val state = MutableLiveData<State>(State.CLEAR)

    // 右側のボタンのアクションタイプ。タイマーの状態によって変化させる
    val primaryButtonType = state.map { state ->
        when (state) {
            State.CLEAR -> PrimaryButtonType.TIMER_START
            State.START -> PrimaryButtonType.TIMER_STOP
            State.STOP -> PrimaryButtonType.TIMER_CLEAR
        }
    }

    // タイマーの開始時刻(ミリ秒)
    private var timerStartedAt: Long = 0L

    // 現在のタイマー経過時間(ミリ秒)
    val currentTime = state.switchMap { state ->
        liveData {
            when (state) {
                State.CLEAR -> {
                    timerStartedAt = 0L
                    emit(0L)
                }
                State.START -> {
                    if (timerStartedAt == 0L) {
                        timerStartedAt = System.currentTimeMillis()
                    }
                    while (true) {
                        emit(System.currentTimeMillis() - timerStartedAt)
                        delay(10L)
                    }
                }
                State.STOP -> {
                    // do nothing
                }
            }
        }
    }

    // TextViewに表示する文字列。currentTimeを元に生成する。
    val currentTimeText = currentTime.map { currentTime ->
        val minute = currentTime / (1000 * 60)
        val second = currentTime / 1000 % 60
        val milliSecond = currentTime % 1000
        String.format(
            "%02d:%02d.%02d",
            minute,
            second,
            milliSecond / 10 // 上位2桁を表示するため
        )
    }

    // ラップリスト
    val lapTimeList = MediatorLiveData<List<LapTime>>().apply {
        addSource(state) { state ->
            if (state == State.CLEAR) {
                value = emptyList()
            }
        }
    }

    // MotionLayoutの遷移状態
    val transitionType = lapTimeList.map { lapTimeList ->
        if (lapTimeList.isEmpty()) {
            TransitionType.START
        } else {
            TransitionType.END
        }
    }

    // 新しいラップタイムを追加します
    fun addLapTime(time: Long) {
        val currentLapTimeList = lapTimeList.value?.toMutableList() ?: return
        val lap = LapTime(
            currentLapTimeList.size + 1,
            time
        )
        currentLapTimeList.add(lap)
        lapTimeList.value = currentLapTimeList
    }
}