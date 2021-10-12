package com.najed.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    lateinit var celebsList: Celeb
    lateinit var celebsRecyclerView: RecyclerView
    lateinit var addCelebButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        celebsList = Celeb()
        celebsRecyclerView = findViewById(R.id.celebs_rv)
        celebsRecyclerView.layoutManager = LinearLayoutManager(this)
        setCelebs()

        addCelebButton = findViewById(R.id.add_celeb_btn)
        addCelebButton.setOnClickListener {
            startActivity(Intent(this, AddCelebActivity::class.java))
            finish()
        }
    }

    private fun setCelebs(){
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
}