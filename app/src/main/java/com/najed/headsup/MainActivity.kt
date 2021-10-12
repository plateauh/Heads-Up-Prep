package com.najed.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var celebsList: Celeb
    private lateinit var celebsRecyclerView: RecyclerView
    private lateinit var addCelebButton: Button
    private lateinit var submitButton: Button
    private lateinit var celebNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        celebsList = Celeb()
        celebNameEditText = findViewById(R.id.celeb_name_et)

        celebsRecyclerView = findViewById(R.id.celebs_rv)
        celebsRecyclerView.layoutManager = LinearLayoutManager(this)
        setCelebs()

        addCelebButton = findViewById(R.id.add_celeb_btn)
        addCelebButton.setOnClickListener {
            startActivity(Intent(this, AddCelebActivity::class.java))
            finish()
        }

        submitButton = findViewById(R.id.submit_btn)
        submitButton.setOnClickListener {
            searchCeleb()
        }
    }

    private fun setCelebs() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<Celeb?>? = apiInterface!!.getCelebs()
        call?.enqueue(object: Callback<Celeb?>{
            override fun onResponse(call: Call<Celeb?>, response: Response<Celeb?>) {
                celebsList = response.body()!!
                celebsRecyclerView.adapter = RecyclerViewAdapter(celebsList)
            }
            override fun onFailure(call: Call<Celeb?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }

    private fun searchCeleb() {
        var found = false
        for (item in celebsList){
            if (celebNameEditText.text.toString().equals(item.name, true)){
                found = true
                val intent = Intent(this, UpdateDeleteActivity::class.java)
                intent.putExtra("celeb_id", item.pk)
                intent.putExtra("celeb_name", item.name)
                intent.putExtra("celeb_taboo1", item.taboo1)
                intent.putExtra("celeb_taboo2", item.taboo2)
                intent.putExtra("celeb_taboo3", item.taboo3)
                startActivity(intent)
            }
        }
        if (!found)
            Toast.makeText(this, "No such celebrity", Toast.LENGTH_SHORT).show()
    }
}