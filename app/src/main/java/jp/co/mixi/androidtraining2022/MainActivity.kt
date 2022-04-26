package jp.co.mixi.androidtraining2022

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.co.mixi.androidtraining2022.databinding.ActivityMainBinding

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
        adapter.submitList(viewModel.lapTimes)

        // FIXME 一時的にデータを入れておく
        binding.secondaryButton.setOnClickListener {
            if (binding.root.currentState == R.id.start) {
                binding.root.transitionToEnd()
            }
            if (viewModel.lapTimes.isNotEmpty()) {
                val tailItem = viewModel.lapTimes[viewModel.lapTimes.size - 1]
                val newIndex = tailItem.number + 1
                viewModel.lapTimes.add(
                    LapTime(newIndex, viewModel.currentTime.value!!)
                )
            } else {
                if (viewModel.currentTime.value != null) {
                    viewModel.lapTimes.add(
                        LapTime(1, viewModel.currentTime.value!!)
                    )
                }
            }
        }

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
                    binding.root.transitionToStart()
                    viewModel.lapTimes.clear()
                    binding.secondaryButton.isEnabled = false
                }
                PrimaryButtonType.TIMER_STOP -> {
                    binding.primaryButton.setText(R.string.stop)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.accent)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.STOP
                    }
                    binding.secondaryButton.isEnabled = true
                }
                PrimaryButtonType.TIMER_CLEAR -> {
                    binding.primaryButton.setText(R.string.clear)
                    binding.primaryButton.backgroundTintList = getColorStateList(R.color.primary_variant)
                    binding.primaryButton.setOnClickListener {
                        viewModel.state.value = State.CLEAR
                    }
                    binding.secondaryButton.isEnabled = false
                }
            }
        }

    }
}