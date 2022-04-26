package jp.co.mixi.androidtraining2022

data class LapTime(
    val number: Int,
    val time: Long
) {
    companion object {
        fun getTimeText(time: Long): String {
            val minute = time / (1000 * 60)
            val second = time / 1000 % 60
            val milliSecond = time % 1000
            return String.format(
                "%02d:%02d.%02d",
                minute,
                second,
                milliSecond / 10 // 上位2桁を表示するため
            )
        }
    }
}