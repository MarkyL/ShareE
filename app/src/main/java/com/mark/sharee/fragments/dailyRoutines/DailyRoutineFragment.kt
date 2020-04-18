package com.mark.sharee.fragments.dailyRoutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.network.model.responses.DailyActivity
import timber.log.Timber

class DailyRoutineFragment(private val activitiesList: MutableList<DailyActivity>) : ShareeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.i("DailyRoutineFragment onViewCreated, activitiesList = $activitiesList")
    }


}