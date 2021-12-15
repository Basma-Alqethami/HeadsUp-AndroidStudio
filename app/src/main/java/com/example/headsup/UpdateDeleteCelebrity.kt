package com.example.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDeleteCelebrity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etTaboo1: EditText
    lateinit var etTaboo2: EditText
    lateinit var etTaboo3: EditText

    lateinit var btCancel: Button
    lateinit var btDelete: Button
    lateinit var btUpdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_celebrity)

        etName = findViewById(R.id.etName)
        etTaboo1 = findViewById(R.id.etTaboo1)
        etTaboo2 = findViewById(R.id.etTaboo2)
        etTaboo3 = findViewById(R.id.etTaboo3)

        btCancel = findViewById(R.id.btCancel)
        btDelete = findViewById(R.id.btDelete)
        btUpdate = findViewById(R.id.btUpdate)

        val disData = intent.getSerializableExtra("displayData") as CelebritiesItem

        Log.d("gg", "$disData")

        var id = disData.pk
        etName.setText(disData.name)
        etTaboo1.setText(disData.taboo1)
        etTaboo2.setText(disData.taboo2)
        etTaboo3.setText(disData.taboo3)

        btCancel.setOnClickListener{
            val intent = Intent(this@UpdateDeleteCelebrity, MainActivity::class.java)
            startActivity(intent)
        }

        btDelete.setOnClickListener { Delete(disData) }
        btUpdate.setOnClickListener { Update(disData) }
    }

    fun Update(disData: CelebritiesItem) {

        val api = Client().getClient()?.create(API::class.java)

        api?.updateData(
            disData.pk, CelebritiesItem(
                disData.pk,
                etName.text.toString(),
                etTaboo1.text.toString(),
                etTaboo2.text.toString(),
                etTaboo3.text.toString()
            ))?.enqueue(object: Callback<CelebritiesItem> {
            override fun onResponse(call: Call<CelebritiesItem>, response: Response<CelebritiesItem>) {
                Toast.makeText(this@UpdateDeleteCelebrity, "User Updated", Toast.LENGTH_LONG).show()
                val intent = Intent(this@UpdateDeleteCelebrity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<CelebritiesItem>, t: Throwable) {
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun Delete(disData: CelebritiesItem) {

        val api = Client().getClient()?.create(API::class.java)

        api?.deleteData(disData.pk)?.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@UpdateDeleteCelebrity, "User Deleted", Toast.LENGTH_LONG).show()
                val intent = Intent(this@UpdateDeleteCelebrity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateDeleteCelebrity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
    }
}