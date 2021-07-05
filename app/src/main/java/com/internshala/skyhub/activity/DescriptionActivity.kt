package com.internshala.skyhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.skyhub.R
import com.internshala.skyhub.utils.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var descImage: ImageView
    lateinit var descName: TextView
    lateinit var descAuthor: TextView
    lateinit var descPrice: TextView
    lateinit var descRating: TextView
    lateinit var mainDescription: TextView
    lateinit var btnAddFavourites: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var descProgressBar: ProgressBar

    lateinit var descToolbar: Toolbar

    var book_id: String? = "16"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        descImage = findViewById(R.id.descImage)
        descName = findViewById(R.id.descName)
        descAuthor = findViewById(R.id.descAuthor)
        descPrice = findViewById(R.id.descPrice)
        descRating = findViewById(R.id.descRating)
        mainDescription = findViewById(R.id.mainDescription)
        btnAddFavourites = findViewById(R.id.btnAddFavourites)
        progressLayout = findViewById(R.id.progressLayout)
        descProgressBar = findViewById(R.id.descProgressBar)

        descToolbar = findViewById(R.id.descToolbar)
        setSupportActionBar(descToolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Book Details"

        progressLayout.visibility = View.VISIBLE
        descProgressBar.visibility = View.VISIBLE

        if(intent != null) {
            book_id = intent.getStringExtra("bookId")
        }
        else {
            finish()
            Toast.makeText(this@DescriptionActivity, "An unexpected error was encountered!", Toast.LENGTH_SHORT).show()
        }
        if(book_id == "16") {
            finish()
            Toast.makeText(this@DescriptionActivity, "An unexpected error was encountered!", Toast.LENGTH_SHORT).show()
        }

        println("book_id is $book_id")

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", book_id)

        println("BAWSS")

        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    println("ANNOYING")
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            println("DEBUG 1")
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            descProgressBar.visibility = View.GONE
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(descImage)
                            descName.text = bookJsonObject.getString("name")
                            descAuthor.text = bookJsonObject.getString("author")
                            descPrice.text = bookJsonObject.getString("price")
                            descRating.text = bookJsonObject.getString("rating")
                            mainDescription.text = bookJsonObject.getString("description")

                        } else {
                            println("DEBUG 2")
                            Toast.makeText(
                                this@DescriptionActivity,
                                "An unexpected error has been encountered ELSE BLOCK!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "An unexpected error has been encountered EXCEPTION $e!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley Library Error: $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "dd07c37cc77234"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }
        else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not established on this device!")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
//                this.finish()
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                intent = Intent(this@DescriptionActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}