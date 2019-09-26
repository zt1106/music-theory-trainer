package cc.zengtian.mtt.util

actual object Time {
    actual fun currentMillisecond() : Long {
        return System.currentTimeMillis()
    }
}