package jp.co.mixi.androidtraining2022

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.co.mixi.androidtraining2022.databinding.ActivityMainBinding
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ラップタイムのAdapterを準備
        val adapter = LapTimeAdapter()
        binding.recyclerView.adapter = adapter
        val lapTimeList = mutableListOf<LapTime>()

        // 経過時間を変更
        viewModel.currentTimeText.observe(this) {
            binding.timeText.text = it
        }

        // 右側ボタンの設定を状況によって変更する
        viewModel.primaryButtonType.observe(this) { type ->
            when (type) {
                PrimaryButtonType.TIMER_START -> {
                    lapTimeList.removeIf { true }
                    binding.secondaryButton.isEnabled = false
                    binding.primaryButton.setText(R.string.start)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.primary)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.START
                    }
                }
                PrimaryButtonType.TIMER_STOP -> {
                    binding.secondaryButton.isEnabled = true
                    binding.primaryButton.setText(R.string.stop)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.accent)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.STOP
                    }
                }
                PrimaryButtonType.TIMER_CLEAR -> {
                    binding.secondaryButton.isEnabled = false
                    binding.primaryButton.setText(R.string.clear)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.primary_variant)
                    binding.primaryButton.setOnClickListener {
                        binding.root.transitionToStart()
                        viewModel.state.value = State.CLEAR
                    }
                }
            }
        }

        binding.secondaryButton.setOnClickListener {
            if (binding.root.currentState == R.id.start) {
                if(viewModel.state.value == State.START)
                    binding.root.transitionToEnd()
            }
            val v = viewModel.currentTime.value!!
            val lapTime = LapTime(lapTimeList.size + 1, v)
            lapTimeList.add(lapTime)
            adapter.submitList(lapTimeList.toList())

        }
    }
}