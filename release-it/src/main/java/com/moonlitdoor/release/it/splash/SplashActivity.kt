package com.moonlitdoor.release.it.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.extension.milliTime
import com.moonlitdoor.release.it.navigation.NavigationActivity
import com.moonlitdoor.release.it.util.Analytics

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    val start = milliTime()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    findViewById<MotionLayout>(R.id.motion_layout).also {
      it.setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) = NavigationActivity.start(this@SplashActivity).also {
          Analytics.splashActivityTimer(start, milliTime())
        }
      })
      it.transitionToEnd()
    }
  }
}
