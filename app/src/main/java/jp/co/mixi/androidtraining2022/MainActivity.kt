package jp.co.mixi.androidtraining2022

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import jp.co.mixi.androidtraining2022.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val list = mutableListOf<LapTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ラップタイムのAdapterを準備
        val lapTimeAdapter = LapTimeAdapter()
        binding.recyclerView.adapter = lapTimeAdapter

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
                        list.clear()
                        checkList(binding,list,lapTimeAdapter)
                    }
                }
            }
        }

        binding.secondaryButton.setOnClickListener {
            if (viewModel.state.value == State.START) {
                list.add(LapTime(list.size + 1, viewModel.currentTimeText.value!!))
                checkList(binding, list, lapTimeAdapter)
            }
        }
    }

    fun checkList(activity: ActivityMainBinding ,list:MutableList<LapTime>, adapter:LapTimeAdapter){
        if(list.size != 0){
            adapter.submitList(list)
            activity.root.transitionToEnd()
        }else{
            activity.root.transitionToStart()
        }
    }
}