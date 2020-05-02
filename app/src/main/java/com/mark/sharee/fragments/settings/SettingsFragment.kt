package com.mark.sharee.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sharee.R
import com.mark.sharee.activities.MainActivity
import com.mark.sharee.core.Action
import com.mark.sharee.core.ShareeFragment
import com.mark.sharee.utils.FontStyle
import com.mark.sharee.utils.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.sharee_toolbar.view.*
import timber.log.Timber


class SettingsFragment : ShareeFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureScreen()
    }

    private fun configureToolbar() {
        toolbar.titleTextView.text = resources.getString(R.string.settings)
        toolbar.addActions(arrayOf(Action.BackBlack), this)
    }

    private fun configureScreen() {
        val prefs = Preferences(requireContext())


        val titlesList = arrayListOf<String>()
        FontStyle.values().forEach {
            titlesList.add(it.title)
        }

        with(fontsSpinner) {
            adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, titlesList)
            setSelection(prefs.fontStyle.ordinal)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.i("onNothingSelected")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Timber.i("onItemSelected")
                    prefs.fontStyle = FontStyle.values()[position]

                    val mainActivity = activity as MainActivity
                    mainActivity.theme.applyStyle(prefs.fontStyle.resId, true)
                }
            }
        }

    }
}