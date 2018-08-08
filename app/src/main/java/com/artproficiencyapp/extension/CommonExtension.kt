package com.artproficiencyapp.extension


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Admin on 9-11-2017.
 */
private var toast: Toast? = null

@SuppressLint("ShowToast")
fun Toast(msg: Any?, isShort: Boolean = true, app: Context) {
    msg?.let {
        if (toast == null) {
            toast = Toast.makeText(app, msg.toString(), Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg.toString())
        }
        toast!!.duration = if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        toast!!.show()
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}
fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        view.requestLayout()
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}


fun ImageView.setPadding(padding: Float) {
    val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, resources.displayMetrics).toInt()
    this.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
}

fun LoggE(tag: String, message: Any) {
    val isLog = true
    try {
        Log.e(tag, "==>" + message)
    } catch (e: Exception) {
    }

}

val EditText.value
    get() = text.toString()

val RadioButton.value
    get() = text.toString()

val TextView.value
    get() = text.toString()

//fun ImageView.loadImageCircle(context: Activity, imageUrl: String) {
//    Glide.with(context).load(imageUrl)
//            .crossFade()
//            .thumbnail(0.5f)
//            .bitmapTransform(CircleTransform(context))
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(this)
//}

//fun ImageView.loadImageCircle(context: Context, imageUrl: Int) {
//    Glide.with(context).load(imageUrl)
//            .crossFade()
//            .thumbnail(0.5f)
//            .bitmapTransform(CircleTransform(context))
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(this)
//}

/*fun ImageView.loadImage(context: Context, imageUrl: String) {
    Glide.with(context).load(imageUrl)
            .crossFade()
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
}*/

fun Long.date(pattern: String = "yyyy-MM-dd HH:mm:ss"): String? = SimpleDateFormat(pattern, Locale.getDefault()).format(this)
fun <T : Any> T.TAG() = this::class.simpleName




