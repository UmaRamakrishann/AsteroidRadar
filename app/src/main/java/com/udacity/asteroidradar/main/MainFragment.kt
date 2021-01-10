package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDisplayFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

	private val viewModel: MainViewModel by lazy {
		ViewModelProvider(this).get(MainViewModel::class.java)
	}
	private var viewModelAdapter: AsteroidAdapter? = null
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = FragmentMainBinding.inflate(inflater)
		binding.lifecycleOwner = this

		binding.viewModel = viewModel

		viewModelAdapter = AsteroidAdapter(AsteroidAdapter.AsteroidClick {
			viewModel.displayAsteroidDetails(it)
		})

		viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
			if ( null != it ) {
				this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
				viewModel.displayAsteroidDetailsComplete()
			}
		})

		viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
			if (it.mediaType == "image"){
				Picasso.get()
					.load(it.url)
					.placeholder(R.drawable.placeholder_picture_of_day)
					.error(R.drawable.placeholder_picture_of_day)
					.into(binding.activityMainImageOfTheDay)
			}
		})

		setHasOptionsMenu(true)
		binding.root.findViewById<RecyclerView>(R.id.asteroid_List).apply {
			layoutManager = LinearLayoutManager(context)
			adapter = viewModelAdapter
		}
		return binding.root
	}

	/**
	 * Called immediately after onCreateView() has returned, and fragment's
	 * view hierarchy has been created.  It can be used to do final
	 * initialization once these pieces are in place, such as retrieving
	 * views or restoring state.
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids->
			asteroids?.apply {
				viewModelAdapter?.asteroids = asteroids
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.main_overflow_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		viewModel.updateFilter(
			when (item.itemId) {
				R.id.show_week_menu -> AsteroidDisplayFilter.SHOW_WEEKLY
				R.id.show_today_menu -> AsteroidDisplayFilter.SHOW_TODAY
				else -> AsteroidDisplayFilter.SHOW_ALL
			}
		)
		return true
	}
}
