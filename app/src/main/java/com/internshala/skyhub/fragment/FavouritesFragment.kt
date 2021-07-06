package com.internshala.skyhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.skyhub.R
import com.internshala.skyhub.adapter.FavouriteRecyclerAdapter
import com.internshala.skyhub.database.BookDatabase
import com.internshala.skyhub.database.BookEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var favRecycler: RecyclerView
    lateinit var favProgressLayout: RelativeLayout
    lateinit var favProgressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter

    var dbBookList = listOf<BookEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        favRecycler = view.findViewById(R.id.favRecycler)
        favProgressLayout = view.findViewById(R.id.favProgressLayout)
        favProgressBar = view.findViewById(R.id.favProgressBar)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList = RetrieveFavouriteBooks(activity as Context).execute().get()

        if(activity != null) {
            favProgressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            favRecycler.adapter = recyclerAdapter
            favRecycler.layoutManager = layoutManager
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class RetrieveFavouriteBooks(val context: Context): AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
            return db.bookDao().getAllBooks()
        }

    }
}