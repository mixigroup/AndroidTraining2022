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

        val LapList = mutableListOf<LapTime>()

        // FIXME 一時的にデータを入れておく
//        adapter.submitList(
//            List(10) {
//                LapTime(it + 1, 0)
//            }
//        )

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
                        LapList.clear()
                        binding.root.transitionToStart()
                    }
                }
            }
        }

        viewModel.secondaryButtonType.observe(this) { type ->
            when (type) {
                SecondaryButtonType.Enabled -> {
                    binding.secondaryButton.isEnabled = true
                }
                SecondaryButtonType.Disabled -> {
                    binding.secondaryButton.isEnabled = false
                }
            }
        }

        binding.secondaryButton.setOnClickListener {
            LapList.add(LapTime(LapList.size + 1, viewModel.currentTime.value!!))
            adapter.submitList(LapList)

            if (LapList.size > 0) {
                binding.root.transitionToEnd()
            } else {
                binding.root.transitionToStart()
            }
        }
    }
}