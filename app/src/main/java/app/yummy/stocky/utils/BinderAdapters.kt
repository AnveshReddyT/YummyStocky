package app.yummy.stocky.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade


@BindingAdapter("drawable")
fun setDrawable(view: ImageView, @DrawableRes resId: Int) {
    try {
        view.setImageResource(resId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("rightDrawable")
fun setLeftDrawable(view: AppCompatTextView, @DrawableRes resId: Int) {
    try {
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("gif")
fun setGifDrawable(view: ImageView, @DrawableRes resId: Int) {
    try {
        Glide.with(view.context)
            .asGif()
            .centerInside()
            .load(resId)
            .into(view)
    } catch (e: Exception) {

    }
}

@BindingAdapter("imageUrl")
fun imageUrl(view: AppCompatImageView, url: String?) {
    Glide.with(view.rootView.context)
        .load(url)
        .placeholder(android.R.color.transparent)
        .transition(withCrossFade())
        .into(view)

}

