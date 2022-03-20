package eg.iti.weather_app.view.fragments.favourite

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eg.iti.weather_app.R
import eg.iti.weather_app.databinding.FragmentFavouriteWeatherBinding
import eg.iti.weather_app.db.Repository
import eg.iti.weather_app.db.Resource
import eg.iti.weather_app.db.entity.FavouriteData
import eg.iti.weather_app.utils.getAddressGeocoder
import eg.iti.weather_app.view.fragments.home.HomeWeather
import eg.iti.weather_app.view.fragments.mapFragment.SettingWeather
import eg.iti.weather_app.viewmodel.FavViewModel
import eg.iti.weather_app.viewmodel.SettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteWeather : Fragment(R.layout.fragment_favourite_weather),
    FavouriteAdapter.OnItemClickListener {

    lateinit var binding: FragmentFavouriteWeatherBinding

    private var adapt: RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var favViewModel: FavViewModel
    lateinit var dailyAdapter: FavouriteAdapter
    private lateinit var settingViewModel: SettingViewModel
    lateinit var repo: Repository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        binding = FragmentFavouriteWeatherBinding.inflate(layoutInflater)
        repo = Repository() //delete
        val root = binding.root
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecyclerView)
        context?.let {
            favViewModel.getFavAPIData(it)
        }


        binding.btnFav.setOnClickListener {
            loadFragment(SettingWeather())
        }

        getFavDataFromRoom()

        return root
    }

    private fun getFavDataFromRoom() {
        favViewModel.favFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { it1 -> displayDailyWeatherToRecycleView(it1 as MutableList<FavouriteData>) }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })
    }


    private fun initUI(data: MutableList<FavouriteData>) {
        dailyAdapter = FavouriteAdapter(data, this)
        context?.let { dailyAdapter.setData(data, it) }
        binding.favRecyclerView.apply {
            layoutManag = LinearLayoutManager(context)
            layoutManager = layoutManag
            adapt = dailyAdapter
            adapter = adapt

        }
    }

    private fun displayDailyWeatherToRecycleView(data: MutableList<FavouriteData>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fm = fragmentManager
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction().addToBackStack(null)
        fragmentTransaction.replace(R.id.favContainer, fragment)
        fragmentTransaction.commit() // save the changes
    }

    private fun showProgressBar() {
        binding.favProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.favProgressBar.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        System.out.println("Error is  :" + message)
    }


    override fun onItemClick(favouriteData: FavouriteData) {
        val Add = getAddressGeocoder(favouriteData.lat, favouriteData.lon, context)
        context?.let {
            settingViewModel.writeDataWeatherInSharedPreference(
                favouriteData.lat.toString(),
                favouriteData.lon.toString(),
                Add!!,
                it
            )
            binding.btnFav.visibility = View.GONE
        }
        loadFragment(HomeWeather())
    }


    // swipe for delete
    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity).setMessage(R.string.favMsg)
                    .setPositiveButton(R.string.yes,
                        DialogInterface.OnClickListener { dialog, id -> //when delete item from tripDatabase, add tripHistoryDB
                            val alertItemDeleted = dailyAdapter.getFavByVH(viewHolder)

                            CoroutineScope(Dispatchers.IO).launch {
                                deleteFavItemFromDB(alertItemDeleted)
                            }
                            dailyAdapter.removeFavItem(viewHolder)
                        })
                    .setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->

                            getFavDataFromRoom()
                        }).show()

            }
        }

    // delete favorite item
    suspend fun deleteFavItemFromDB(favData: FavouriteData) {
        repo.deleteFav(favData, requireContext())
    }


}