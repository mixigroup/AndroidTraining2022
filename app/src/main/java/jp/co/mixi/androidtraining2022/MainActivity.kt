package jp.co.mixi.androidtraining2022

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.co.mixi.androidtraining2022.databinding.ActivityMainBinding
import java.sql.Time

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val lapTimeList = mutableListOf<LapTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ラップタイムのAdapterを準備
        val adapter = LapTimeAdapter()
        binding.recyclerView.adapter = adapter

        // 経過時間を変更
        viewModel.currentTimeText.observe(this) {
            binding.timeText.text = it
        }

        // 右側ボタンの設定を状況によって変更する
        viewModel.primaryButtonType.observe(this) { type ->
            when (type) {
                PrimaryButtonType.TIMER_START -> {
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
                        viewModel.state.value = State.CLEAR
                        lapTimeList.clear()
                    }
                }
            }
        }

        binding.secondaryButton.setOnClickListener {
            val time = viewModel.currentTime.value ?: 0L
            val lap = LapTime(lapTimeList.size + 1, time)
            lapTimeList.add(lap)

            if (lapTimeList.isEmpty()) {
                binding.root.transitionToStart()
            } else {
                adapter.submitList(lapTimeList)
                binding.root.transitionToEnd()
            }
        }
    }
}