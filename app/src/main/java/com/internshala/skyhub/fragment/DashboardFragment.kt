package com.internshala.skyhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.skyhub.R
import com.internshala.skyhub.adapter.DashboardRecyclerAdapter
import com.internshala.skyhub.model.Boxer
import com.internshala.skyhub.utils.ConnectionManager
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var rlProgressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    val boxerInfoList = arrayListOf<Boxer>()

//    val boxerInfoList = arrayListOf<Boxer>(
//        Boxer("Marvelous Marvin Hagler", "62(W)-3(L)-2(T)", "1973-1987", "5.0", R.drawable.marvin2),
//        Boxer("Errol Spence Jr", "27(W)-0(L)-0(T)", "2012-present", "5.0", R.drawable.spence2),
//        Boxer("Deontay Wilder", "42(W)-1(L)-1(T)", "2008-present", "5.0", R.drawable.wilder1),
//        Boxer("Floyd Mayweather Jr", "50(W)-0(L)-0(T)", "1996-2017", "5.0", R.drawable.floyd1),
//        Boxer("Terence Crawford", "37(W)-0(L)-0(T)", "2008-present", "5.0", R.drawable.crawford1),
//        Boxer("Devin Haney", "26(W)-0(L)-0(T)", "2015-present", "5.0", R.drawable.devin1),
//        Boxer("Tyson Fury", "30(W)-0(L)-1(T)", "2008-present", "5.0", R.drawable.fury1),
//        Boxer("Canelo Alvarez", "56(W)-1(L)-2(T)", "2005-present", "5.0", R.drawable.canelo1),
//        Boxer("Anthony Joshua", "24(W)-1(L)-0(T)", "2013-present", "5.0", R.drawable.joshua1),
//        Boxer("Gervonta Davis", "25W)-0(L)-0(T)", "2013-present", "5.0", R.drawable.davis1),
//    )

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
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)


        rlProgressLayout = view.findViewById(R.id.rlProgressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        rlProgressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try {
                    rlProgressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        rlProgressLayout.visibility = View.GONE
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Boxer(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("image")
                            )
                            boxerInfoList.add(bookObject)

                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, boxerInfoList)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager

//                            recyclerDashboard.addItemDecoration(
//                                DividerItemDecoration(
//                                    recyclerDashboard.context,
//                                    (layoutManager as LinearLayoutManager).orientation
//                                )
//                            )
                        }
                    } else {
                        Toast.makeText(
                            activity as Context,
                            "An error was encountered!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                catch (e: Exception) {
                    Toast.makeText(activity as Context, "Some unexpected error has been encountered!", Toast.LENGTH_SHORT).show()
                }

                }, Response.ErrorListener {

                    Toast.makeText(activity as Context, "Volley Library error encountered!", Toast.LENGTH_SHORT).show()

                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["content-type"] = "application/json"
                        headers["token"] = "dd07c37cc77234"
                        return headers
                    }

                }

            queue.add(jsonObjectRequest)
        }
        else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not established on this device!")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                activity?.finish()
                //ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
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
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
            }
        }
    }
}