package com.mark.sharee.fragments.dailyRoutines

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.sharee.R
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.network.model.responses.DailyActivity
import timber.log.Timber
import java.lang.RuntimeException

class DailyRoutinesPagerAdapter(fm: FragmentManager, val resources: Resources,
                                weekdayActivities: MutableList<DailyActivity>,
                                weekendActivities: MutableList<DailyActivity>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    enum class TabItem {
        WEEKDAY, WEEKEND
    }

    companion object {
        const val NUM_PAGES = 2
    }

    private val weekendFragment: ShareeFragment
    private val weekdayFragment: ShareeFragment

    init {
        Timber.i("PagerAdapter init")
        weekdayFragment = DailyRoutineFragment(weekdayActivities)
        weekendFragment = DailyRoutineFragment(weekendActivities)
    }


    override fun getItem(item: Int): Fragment {
        return when (item) {
            TabItem.WEEKEND.ordinal -> weekendFragment
            TabItem.WEEKDAY.ordinal -> weekdayFragment
            else -> throw RuntimeException()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }


    override fun getPageTitle(position: Int): CharSequence? {

        if (position == 1) {
            return resources.getString(R.string.daily_routine_tab_weekday)
        }

        if (position == 0) {
            return resources.getString(R.string.daily_routine_tab_weekend)
        }

        return null
    }

}