package com.mark.sharee.fragments.dailyRoutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharee.R
import com.mark.sharee.adapters.DailyRoutineAdapter
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.network.model.responses.DailyActivity
import com.mark.sharee.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_poll.*

class DailyRoutineFragment(private val activitiesList: MutableList<DailyActivity>) : ShareeFragment() {

    private var dailyRoutineAdapter = DailyRoutineAdapter()

    init {
        dailyRoutineAdapter.submitList(activitiesList)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = dailyRoutineAdapter
        }


    }


}