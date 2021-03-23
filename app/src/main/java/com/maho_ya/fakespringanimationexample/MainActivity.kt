package com.maho_ya.fakespringanimationexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


class MainActivity : AppCompatActivity() {

    private companion object Params {
        val INITIAL_SCALE = 1f
        val STIFFNESS = SpringForce.STIFFNESS_MEDIUM
        val DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
    }

    lateinit var scaleXAnimation: SpringAnimation
    lateinit var scaleYAnimation: SpringAnimation
    lateinit var scaleGestureDetector: ScaleGestureDetector
    lateinit var img: View

    fun createSpringAnimation(view: View,
                              property: DynamicAnimation.ViewProperty,
                              finalPosition: Float,
                              stiffness: Float,
                              dampingRatio: Float): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }

    lateinit var xAnimation: SpringAnimation
    lateinit var yAnimation: SpringAnimation

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img = findViewById<View>(R.id.springImageView)

        // using DynamicAnimation
        // create scaleX and scaleY animations
        img.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                xAnimation = createSpringAnimation(
                        img, SpringAnimation.X, img.x, STIFFNESS, DAMPING_RATIO)
                yAnimation = createSpringAnimation(
                        img, SpringAnimation.Y, img.y, STIFFNESS, DAMPING_RATIO)
                img.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        // using android.view.animation
        img.setOnClickListener {
            startScaling()
        }
        var dX = 0f
        var dY = 0f
//        img.setOnTouchListener { view, event ->
//            when (event.actionMasked) {
//                MotionEvent.ACTION_DOWN -> {
//                    // capture the difference between view's top left corner and touch point
//                    dX = view.x - event.rawX
//                    dY = view.y - event.rawY
//                    Log.v("myon", view.x.toString() + " " + event.rawX)
//                    // cancel animations so we can grab the view during previous animation
//                    xAnimation.cancel()
//                    yAnimation.cancel()
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    //  a different approach would be to change the view's LayoutParams.
//                    img.animate()
//                            .x(event.rawX)//+ dX)
//                            .y(event.rawY + dY)
//                            .setDuration(0)
//                            .start()
//                }
//                MotionEvent.ACTION_UP -> {
//                    xAnimation.start()
//                    yAnimation.start()
//                }
//            }
//            true
//        }

//        val springAnim = findViewById<View>(R.id.springImageView).setOnClickListener { img ->
//            // Setting up a spring animation to animate the view’s translationY property with the final
//            // spring position at 0.
//            val animation = SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 500f)
//            animation.apply {
//                spring.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
//                spring.stiffness = SpringForce.STIFFNESS_LOW
//                start()
//            }
//
//        }
    }

    private fun startScaling() {
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        val scaleAnimation = ScaleAnimation(
                1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        // animation時間 msec
        scaleAnimation.duration = 200
        // 繰り返し回数
        scaleAnimation.repeatCount = 0
        // animationが終わったそのまま表示にする
        scaleAnimation.fillAfter = true


        Log.v("saya",scaleAnimation.hasStarted().toString())

        val scaleAnimation5 = scaleAnimation(
            0.9f, 1.0f,  100)

        val scaleAnimation4 = scaleAnimation(
            1.4f, 0.9f,  100)

        setAnimationListener(scaleAnimation4, scaleAnimation5)
        
        scaleAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                Log.v("saya onAnimationStart",scaleAnimation.hasStarted().toString())
            }
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                Log.v("saya onAnimationEnd",scaleAnimation.hasStarted().toString())

                val scaleAnimation2 = scaleAnimation(
                        2.0f, 0.8f,  100)

                scaleAnimation2.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        Log.v("saya onAnimationEnd",scaleAnimation.hasEnded().toString())
                        val scaleAnimation3 = scaleAnimation(
                                0.8f, 1.4f,  100)

                        scaleAnimation3.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationRepeat(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {

                                img.startAnimation(scaleAnimation4)
                            }
                        })

                        img.startAnimation(scaleAnimation3)
                    }
                })

                img.startAnimation(scaleAnimation2)
            }
        })
        //アニメーションの開始
        img.startAnimation(scaleAnimation)
    }

    private fun setAnimationListener(scaleAnimation: ScaleAnimation, runScaleAnimation: ScaleAnimation) {

        scaleAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                img.startAnimation(runScaleAnimation)
            }
        })

    }

    private fun scaleAnimation(fromX: Float, toX: Float, duration: Long): ScaleAnimation {

        val scaleAnimation = ScaleAnimation(
                fromX, toX, fromX, toX,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        // animation時間 msec
        scaleAnimation.duration = duration
        // 繰り返し回数
        scaleAnimation.repeatCount = 0
        // animationが終わったそのまま表示にする
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }



    private fun setupPinchToZoom() {
        var scaleFactor = 1f
        scaleGestureDetector = ScaleGestureDetector(this,
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        scaleFactor *= detector.scaleFactor
                        img.scaleX *= scaleFactor
                        img.scaleY *= scaleFactor
                        return true
                    }
                })
    }
}