package com.aaqibkhan.dottchallengeaaqib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.aaqibkhan.dottchallengeaaqib.R
import com.aaqibkhan.dottchallengeaaqib.databinding.FragmentRestaurantBinding
import com.aaqibkhan.dottchallengeaaqib.viewmodel.RestaurantViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RestaurantBottomDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RestaurantBottomDialog"
        private const val KEY_RESTAURANT_ID = "restaurant_id"

        fun newInstance(restaurantId: String) = RestaurantBottomDialog().apply {
            val args = Bundle()
            args.putString(KEY_RESTAURANT_ID, restaurantId)
            arguments = args
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRestaurantBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant, container, false)
        val model: RestaurantViewModel by viewModels()
        binding.restaurantViewModel = model
        binding.lifecycleOwner = viewLifecycleOwner
        model.loadRestaurant(getRestaurantId())
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getRestaurantId(): String? = arguments?.getString(KEY_RESTAURANT_ID)

}