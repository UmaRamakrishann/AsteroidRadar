package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapter(val callback: AsteroidClick) :
	RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {

	/**
	 * The asteroids that our Adapter will show
	 */
	var asteroids: List<Asteroid> = emptyList()
		set(value) {
			field = value
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
			it.asteroidCallback = callback
		}
		holder.viewDataBinding.clickableOverlay.contentDescription = "Asteroid ${position}: " +
				"${asteroids[position].codename}, with close approachDate of ${asteroids[position].closeApproachDate} "
	}

	class AsteroidViewHolder(val viewDataBinding: AsteroidItemBinding) :
		RecyclerView.ViewHolder(viewDataBinding.root) {

		companion object {
			@LayoutRes
			val LAYOUT = R.layout.asteroid_item
		}
	}

	class AsteroidClick(val block: (Asteroid) -> Unit) {
		/**
		 * Called when a asteroid is clicked
		 *
		 * @param asteroid the asteroid that was clicked
		 */
		fun onClick(asteroid: Asteroid) = block(asteroid)
	}

}