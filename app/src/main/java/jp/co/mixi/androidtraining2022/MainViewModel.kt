package jp.co.mixi.androidtraining2022

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // 現在のタイマーの状態
    val state = MutableLiveData<State>(State.CLEAR)

    // 右側のボタンのアクションタイプ。タイマーの状態によって変化させる
    val primaryButtonType = state.map { state ->
        requireNotNull(state) { "LiveDataくんはJava実装なので明確なNotNull定義じゃない" }
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
                else -> {
                    throw IllegalStateException("LiveDataくんはJava実装なので明確なNotNull定義じゃない")
                }
            }
        }
    }

    // TextViewに表示する文字列。currentTimeを元に生成する。
    val currentTimeText = currentTime.map { currentTime ->
        LapTime.getTimeText(currentTime)
    }

    private val _lapList = MutableLiveData<List<LapTime>>(emptyList())
    val lapList: LiveData<List<LapTime>> = _lapList
    val lapListExist = _lapList.map { it.isNotEmpty() }
        .distinctUntilChanged()

    fun addLap() {
        val current = currentTime.value ?: return
        val oldList = (_lapList.value ?: emptyList()).toMutableList()
        val nowLap = LapTime(oldList.size + 1, current)

        _lapList.value = oldList + nowLap
    }

    fun resetLap() {
        _lapList.value = emptyList()
    }
}