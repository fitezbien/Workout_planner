package com.bionic2.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bionic2.R
import com.bionic2.View.RecycleListAdapter
import com.bionic2.activities.CalendarActivity
import com.bionic2.entities.ListItems
import java.util.*
import kotlin.collections.ArrayList

//Popup Affichant les prochains évenements

class NextDates(listWorkouts: ArrayList<ListItems>) : DialogFragment(){

    val listWorkouts = listWorkouts

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        if (arguments != null) {
            if (arguments?.getBoolean("notAlertDialog")!!) {
                return super.onCreateDialog(savedInstanceState)
            }
        }

        val workoutsItems = arrayOfNulls<String>(listWorkouts.size)

        var i = 0

        for (workout in listWorkouts) {

            workoutsItems[i] = workout.name
            i++

        }

        var builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)

        return builder.create()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.calendar_recycler, container, false)
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listItems = view.findViewById(R.id.calendar_recycler) as RecyclerView
        listItems.layoutManager = LinearLayoutManager(context)

        var listNextWorkouts = ArrayList<ListItems>()
        var listWPLong = ArrayList<Long>()
        var WdateParsed : Date
        var WdateNumber : Long


            for(workout in listWorkouts){

                WdateParsed = CalendarActivity.dayTime.parse(workout.dayNumber+" "+workout.month+" "+workout.year)
                WdateNumber = CalendarActivity.shortDateNumber.format(WdateParsed).toLong()

                if(WdateNumber >= CalendarActivity.todayNumber){

                    listWPLong.add(WdateNumber)
                }
            }

            //Trie par date
            listWPLong.sort()

                for(wLong in listWPLong){

                    for(workout in listWorkouts){

                        WdateParsed = CalendarActivity.dayTime.parse(workout.dayNumber+" "+workout.month+" "+workout.year)
                        WdateNumber = CalendarActivity.shortDateNumber.format(WdateParsed).toLong()

                    if(WdateNumber == wLong){

                        listNextWorkouts.add( workout )
                    }
                }

            }

            val itemsAdapter = RecycleListAdapter(listNextWorkouts, context!!)
            listItems.adapter = itemsAdapter

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Hey", "onCreate")
        var  setFullScreen = false

        if (arguments != null) {

            setFullScreen = requireNotNull(arguments?.getBoolean("fullScreen"))
            setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme)
        }
        if (setFullScreen)
            setStyle(STYLE_NORMAL, android.R.style.Theme_Black)
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
    interface DialogListener {
        fun onFinishEditDialog(inputText:String)
    }

}
