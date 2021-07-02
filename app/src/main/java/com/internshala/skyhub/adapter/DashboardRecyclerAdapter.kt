package com.internshala.skyhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.internshala.skyhub.R
import com.internshala.skyhub.model.Boxer

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Boxer>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgRowLayout1: ImageView = view.findViewById(R.id.imgRowLayout1)
        val txtRLname1: TextView = view.findViewById(R.id.txtRLname1)
        val txtRLrecord1: TextView = view.findViewById(R.id.txtRLrecord1)
        val txtRLyears1: TextView = view.findViewById(R.id.txtRLyears1)
        val txtRLrating1: TextView = view.findViewById(R.id.txtRLrating1)

        val rlContent: RelativeLayout = view.findViewById(R.id.rlContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_row_layout, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val boxer = itemList[position]
        holder.imgRowLayout1.setImageResource(boxer.boxerImage)
        holder.txtRLname1.text = boxer.boxerName
        holder.txtRLrecord1.text = boxer.boxerRecord
        holder.txtRLyears1.text = boxer.yearsActive
        holder.txtRLrating1.text = boxer.rating

        holder.rlContent.setOnClickListener {
            Toast.makeText(context, "Clicked on ${holder.txtRLname1.text}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}