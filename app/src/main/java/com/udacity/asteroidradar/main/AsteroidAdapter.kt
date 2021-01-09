package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter(val onClickListener: OnClickListener) :
	RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

	/**
	 * The asteroids that our Adapter will show
	 */
	var asteroids: List<Asteroid> = emptyList()
		set(value) {
			field = value
			// For an extra challenge, update this to use the paging library.

			// Notify any registered observers that the data set has changed. This will cause every
			// element in our RecyclerView to be invalidated.
			notifyDataSetChanged()
		}

	/**
	 * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
	 * an item.
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
		val withDataBinding: AsteroidItemBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			AsteroidViewHolder.LAYOUT,
			parent,
			false
		)
		return AsteroidViewHolder(withDataBinding)
	}

	override fun getItemCount() = asteroids.size

	/**
	 * Called by RecyclerView to display the data at the specified position. This method should
	 * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
	 * position.
	 */
	override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
		holder.viewDataBinding.also {
			it.asteroid = asteroids[position]

		}
	}

	class AsteroidViewHolder(val viewDataBinding: AsteroidItemBinding) :
		RecyclerView.ViewHolder(viewDataBinding.root) {

		companion object {
			@LayoutRes
			val LAYOUT = R.layout.asteroid_item
		}
	}

	class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
		fun onClick(asteroid: Asteroid) = clickListener(asteroid)
	}

}