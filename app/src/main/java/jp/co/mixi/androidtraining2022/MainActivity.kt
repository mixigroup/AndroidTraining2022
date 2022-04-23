package jp.co.mixi.androidtraining2022

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import jp.co.mixi.androidtraining2022.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 経過時間を変更
        viewModel.currentTimeText.observe(this) {
            binding.timeText.text = it
        }

        // 右側ボタンの設定を状況によって変更する
        viewModel.primaryButtonType.observe(this) { type ->
            when (type) {
                PrimaryButtonType.TIMER_START -> {
                    binding.primaryButton.setText(R.string.start)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.primary)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.START
                    }
                }
                PrimaryButtonType.TIMER_STOP -> {
                    binding.primaryButton.setText(R.string.stop)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.accent)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.STOP
                    }
                }
                PrimaryButtonType.TIMER_CLEAR -> {
                    binding.primaryButton.setText(R.string.clear)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.primary_variant)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.CLEAR
                    }
                }
            }
        }

        // 左側ボタンの設定を状況によって変更する
        viewModel.state.observe(this) { state ->
            binding.secondaryButton.isEnabled = state == State.START
        }

        // 左側ボタンをタップ時、ラップタイムを追加
        binding.secondaryButton.setOnClickListener {
            val time = viewModel.currentTime.value ?: 0L
            viewModel.addLapTime(time)
        }

        // MotionLayoutの遷移状態を更新する
        viewModel.transitionType.observe(this) { type ->
            when (type) {
                TransitionType.START -> {
                    binding.motionLayout.transitionToStart()
                }
                TransitionType.END -> {
                    binding.motionLayout.transitionToEnd()
                }
            }
        }

        // ラップタイムのAdapterを準備
        val adapter = LapTimeAdapter()
        binding.recyclerView.adapter = adapter

        // ラップタイムリストをAdapterに渡す
        viewModel.lapTimeList.observe(this) {
            adapter.submitList(it)
        }
    }
}