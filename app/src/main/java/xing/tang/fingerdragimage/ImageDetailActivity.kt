package xing.tang.fingerdragimage

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_image_detail.*

/**
 * ImageDetailActivity
 *
 * @Description: 类作用描述
 * @Author: xing.tang
 * @CreateDate: 2020/7/18 9:04 PM
 */
class ImageDetailActivity : AppCompatActivity() {

    private var curPosition: Int = 0
    private var images: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        curPosition = intent.getIntExtra("position", 0)
        images = intent.getStringArrayListExtra("urls")
        var imagePagerAdapter = ImageDetailAdapter(images)
        imagePagerAdapter.setOnAlphaChangeListener {
            val colorId = convertPercentToBlackAlphaColor(it)
            rootView.setBackgroundColor(colorId)
        }
        imagePagerAdapter.setOnPageFinishListener {
            finish()
            overridePendingTransition(R.anim.fade_in_150, R.anim.fade_out_150)
        }
        viewPager.adapter = imagePagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                updateCurNum(position)
            }
        })
        viewPager.currentItem = curPosition
        updateCurNum(viewPager.currentItem)
    }

    private fun updateCurNum(currentItem: Int) {
        val pageNum = "${currentItem + 1}/${images?.size ?: 0}"
        tvPageNum.text = pageNum
    }

    private fun convertPercentToBlackAlphaColor(alpha: Float): Int {
        var percent = Math.min(1f, Math.max(0f, alpha))
        val intAlpha = (percent * 255).toInt()
        val stringAlpha = Integer.toHexString(intAlpha).toLowerCase()
        val color = "#" + (if (stringAlpha.length < 2) "0" else "") + stringAlpha + "000000"
        return Color.parseColor(color)
    }
}