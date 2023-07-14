/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package net.sevendays.android.code_check

import android.os.Bundle
import android.util.Log
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.ErrorResult
import net.sevendays.android.code_check.R
import net.sevendays.android.code_check.TopActivity.Companion.lastSearchDate
import net.sevendays.android.code_check.databinding.FragmentTwoBinding
import java.text.SimpleDateFormat
import java.util.*

class TwoFragment : Fragment(R.layout.fragment_two) {

    private val args: TwoFragmentArgs by navArgs()

    private var binding: FragmentTwoBinding? = null
    private val _binding get() = binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTwoBinding.bind(view)
        var item = args.item

        binding?.apply {
            ownerIconView.load(item.ownerIconUrl) { 
                listener(
                    onSuccess = { _, _ ->
                        // Called when the image is successfully loaded
                    },
                    onError = { _, _ ->
                        // Called when there is an error loading the image
                        ownerIconView.setImageResource(R.drawable.ic_launcher_background) // ideally, use a beautiful placeholder image
                    }
                )
            }  
            nameView.text = item.name
            languageView.text = item.language
            starsView.text = "${item.stargazersCount} stars"
            watchersView.text = "${item.watchersCount} watchers"
            forksView.text = "${item.forksCount} forks"
            openIssuesView.text = "${item.openIssuesCount} open issues"
        } 

        // Formatting the last search date
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val lastSearchDateString = dateFormat.format(lastSearchDate)
        val formattedLastSearchDate = DateUtils.getRelativeTimeSpanString(
            lastSearchDate.time,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        )
        println("Last Search Date: $lastSearchDateString ($formattedLastSearchDate)")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
