package com.example.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var rvAdapter: RVAdapter
    lateinit var recyclerView : RecyclerView


    lateinit var btnAdd: Button
    lateinit var btnSearch : Button

    lateinit var etCelebrity : EditText

    var list = ArrayList<CelebritiesItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.rvMain)
        rvAdapter = RVAdapter(list)
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdd = findViewById(R.id.btnAdd)
        btnSearch = findViewById(R.id.btnSearch)
        etCelebrity = findViewById(R.id.etCelebrity)

        getAllData()
        btnAdd.setOnClickListener { addData() }
        btnSearch.setOnClickListener { findData() }
    }

    private fun findData() {
        val text = etCelebrity.text.toString()
        if (text.isNotEmpty()) {
            for (item in list) {
                if (item.name.lowercase() == text.lowercase()) {
                    val celebrity = CelebritiesItem(
                        item.pk,
                        item.name,
                        item.taboo1,
                        item.taboo2,
                        item.taboo3
                    )

                    val intent = Intent(this@MainActivity, UpdateDeleteCelebrity::class.java)
                    intent.putExtra("displayData", celebrity)
                    startActivity(intent)
                }
            }
        }else {
            Toast.makeText(this, "please Enter celebrity name", Toast.LENGTH_LONG).show()
        }
    }

    fun getAllData() {
        val api = Client().getClient()?.create(API::class.java)

        api?.getData()?.enqueue(object : Callback<Celebrities> {
            override fun onResponse(
                call: Call<Celebrities>,
                response: Response<Celebrities>
            ) {
                for (item in response.body()!!) {
                    Log.d("item", "$item")
                    val pk = item.pk
                    val name = item.name
                    val taboo1 = item.taboo1
                    val taboo2 = item.taboo2
                    val taboo3 = item.taboo3
                    list.add(CelebritiesItem(pk,name,taboo1,taboo2,taboo3))
                    rvAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<Celebrities>, t: Throwable) {
                Log.d("error", "$t")
            }
        })
    }

    fun addData() {

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Enter the information.")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val Name_textbox = EditText(this)
        Name_textbox.hint = "Name"
        layout.addView(Name_textbox)

        val taboo1_textbox = EditText(this)
        taboo1_textbox.hint = "taboo1"
        layout.addView(taboo1_textbox)

        val taboo2_textbox = EditText(this)
        taboo2_textbox.hint = "taboo2"
        layout.addView(taboo2_textbox)

        val taboo3_textbox = EditText(this)
        taboo3_textbox.hint = "taboo3"
        layout.addView(taboo3_textbox)

        alert.setView(layout)

        alert.setPositiveButton(
            "Save"
        ) { dialog, whichButton ->
            val name = Name_textbox.text.toString()
            val taboo1 = taboo1_textbox.text.toString()
            val taboo2 = taboo2_textbox.text.toString()
            val taboo3 = taboo3_textbox.text.toString()
            val pk = 0
            if (name.isNotEmpty() && taboo1.isNotEmpty() && taboo2.isNotEmpty() && taboo3.isNotEmpty()){

                postData(pk,name,taboo1,taboo2,taboo3)
                Toast.makeText(this, "Saved Sucessfully", Toast.LENGTH_LONG).show()}
            else{
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton ->
            // what ever you want to do with No option.
        }

        alert.show()
    }

    fun postData(pk: Int, name: String, taboo1: String, taboo2: String, taboo3: String) {

        val api = Client().getClient()?.create(API::class.java)

        api?.postData(CelebritiesItem(pk,name,taboo1,taboo2,taboo3))?.enqueue(object : Callback<CelebritiesItem> {
            override fun onResponse(call: Call<CelebritiesItem>, response: Response<CelebritiesItem>) {
                getAllData()
                Toast.makeText(applicationContext, "Add Sucessfully!", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<CelebritiesItem>, t: Throwable) {
                Log.d("error", "$t")
            }
        })
    }
}