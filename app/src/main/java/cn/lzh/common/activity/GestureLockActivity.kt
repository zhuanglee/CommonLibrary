package cn.lzh.common.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import cn.lzh.common.R
import cn.lzh.common.base.BaseActivity
import cn.lzh.ui.widget.gesturelock.GestureLockPreview
import cn.lzh.ui.widget.gesturelock.GestureLockView

/**
 * 手势锁
 */
class GestureLockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_lock)
        initToolbar(true)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val gestureLockPreview = findViewById<GestureLockPreview>(R.id.gestureLockPreview)
        val gestureLockView = findViewById<GestureLockView>(R.id.gestureLockView)
        gestureLockView.setGestureLockViewListener(object :
            GestureLockView.GestureLockViewListener {
            override fun onReset() {
                gestureLockPreview.reset()
            }

            override fun onBlockSelected(position: Int) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createOneShot(
                        30,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                    vibrator.vibrate(effect)
                } else {
                    vibrator.vibrate(30)
                }
                gestureLockPreview.addSelectedPoint(position)
            }

            override fun validate(password: List<Int>): Boolean {
                return password.size == 9
            }
        })
    }
}
