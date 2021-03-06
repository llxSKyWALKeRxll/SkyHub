package com.internshala.skyhub.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.internshala.skyhub.R
import com.internshala.skyhub.activity.DescriptionActivity
import com.internshala.skyhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgRowLayout1: ImageView = view.findViewById(R.id.imgRowLayout1)
        val txtRLname1: TextView = view.findViewById(R.id.txtRLname1)
        val txtRLrecord1: TextView = view.findViewById(R.id.txtRLrecord1)
        val txtRLyears1: TextView = view.findViewById(R.id.txtRLyears1)
        val txtRLrating1: TextView = view.findViewById(R.id.txtRLrating1)

        val rlContent: LinearLayout = view.findViewById(R.id.rlContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_row_layout, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        //holder.imgRowLayout1.setImageResource(boxer.boxerImage)
        holder.txtRLname1.text = book.bookName
        holder.txtRLrecord1.text = book.bookAuthor
        holder.txtRLyears1.text = book.bookPrice
        holder.txtRLrating1.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgRowLayout1)

        holder.rlContent.setOnClickListener {
            val intent = Intent(context as Activity, DescriptionActivity::class.java)
            intent.putExtra("bookId", book.bookId)
            context.startActivity(intent)
            context.finish()
//            Toast.makeText(context as Activity, "Clicked on id: ${boxer.boxerId}", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}