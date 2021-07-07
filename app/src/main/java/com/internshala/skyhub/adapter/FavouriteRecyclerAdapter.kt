package com.internshala.skyhub.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.internshala.skyhub.R
import com.internshala.skyhub.activity.DescriptionActivity
import com.internshala.skyhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val favImage: ImageView = view.findViewById(R.id.favImage)
        val favTitle: TextView = view.findViewById(R.id.favTitle)
        val favAuthor: TextView = view.findViewById(R.id.favAuthor)
        val favPrice: TextView = view.findViewById(R.id.favPrice)
        val favRating: TextView = view.findViewById(R.id.favRating)
        val favLayout: LinearLayout = view.findViewById(R.id.favLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favorite_single_row, parent, false)

        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.favTitle.text = book.bookName
        holder.favAuthor.text = book.bookAuthor
        holder.favPrice.text = book.bookPrice
        holder.favRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.favImage)

        holder.favLayout.setOnClickListener {
            val intent = Intent(context as Activity, DescriptionActivity::class.java)
            intent.putExtra("bookId", book.book_id.toString())
            context.startActivity(intent)
            context.finish()
//            Toast.makeText(context as Activity, "Clicked on id: ${book.book_id}", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}