package com.bionic2.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bionic2.entities.ListItems
import com.bionic2.R
import kotlinx.android.synthetic.main.list_popup.view.*


class RecycleListAdapter(var items : ArrayList<ListItems>, var context: Context) : RecyclerView.Adapter<ViewHolder>() {


    var listWorkouts = items

    override fun getItemCount(): Int {

        return listWorkouts.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_popup, parent, false))

    }


    // Binds each animal in the ArrayList to a veiew
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.item_wkname?.text = items.get(position).name


        if(position == 0){

            holder?.item_redline?.visibility = View.GONE
        }

        holder?.item_dateTime?.text = items.get(position).day+" "+items.get(position).dayNumber+" "+items.get(position).month+" "+items.get(position).hour+":"+items.get(position).minute

        holder?.item_dateTime?.setTextAppearance(R.style.fontForListNextWorkouts)


    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    // Holds the TextView that will add each animal to
    val item_wkname = view.workoutName
    var item_dateTime = view.dateTime
    val item_redline = view.redline

}