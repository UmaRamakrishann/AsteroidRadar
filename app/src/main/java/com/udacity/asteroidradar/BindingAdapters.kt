package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) = if (isHazardous) {
	imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
	imageView.setContentDescription("Potentially hazardous asteroid icon")
} else {
	imageView.setImageResource(R.drawable.ic_status_normal)
	imageView.setContentDescription("Not hazardous asteroid icon")
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) = if (isHazardous) {
	imageView.setImageResource(R.drawable.asteroid_hazardous)
	imageView.setContentDescription("Potentially hazardous asteroid image")
} else {
	imageView.setImageResource(R.drawable.asteroid_safe)
	imageView.setContentDescription("Not hazardous asteroid image")
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
	val context = textView.context
	textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
	val context = textView.context
	textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
	val context = textView.context
	textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
	view.visibility = if (it != null) View.GONE else View.VISIBLE
}
