package com.najed.headsup

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

class UpdateDeleteActivity : AppCompatActivity() {

    private lateinit var celebEditTexts: List<EditText>
    private lateinit var deleteButton: Button
    private lateinit var updateButton: Button
    private lateinit var backButton: Button
    private lateinit var celebItem: CelebItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete)

        setCeleb()

        deleteButton = findViewById(R.id.delete_btn)
        deleteButton.setOnClickListener {
            deleteCeleb()
            toMainActivity()
        }

        updateButton = findViewById(R.id.update_btn)
        updateButton.setOnClickListener {
            updateCeleb()
            toMainActivity()
        }

        backButton = findViewById(R.id.back_btn2)
        backButton.setOnClickListener {
            toMainActivity()
        }
    }

    private fun toMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setCeleb(){
        celebEditTexts = listOf (
            findViewById(R.id.update_name_et),
            findViewById(R.id.update_taboo1_et),
            findViewById(R.id.update_taboo2_et),
            findViewById(R.id.update_taboo3_et)
        )
        celebItem = CelebItem (
            intent.extras!!.getInt("celeb_id", 0),
            intent.extras!!.getString("celeb_name", ""),
            intent.extras!!.getString("celeb_taboo1", ""),
            intent.extras!!.getString("celeb_taboo2", ""),
            intent.extras!!.getString("celeb_taboo3", ""),
        )
        celebEditTexts[0].setText(celebItem.name)
        celebEditTexts[1].setText(celebItem.taboo1)
        celebEditTexts[2].setText(celebItem.taboo2)
        celebEditTexts[3].setText(celebItem.taboo3)
    }

    private fun deleteCeleb() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.deleteCeleb(celebItem.pk)?.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(this@UpdateDeleteActivity, "Celebrity deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateDeleteActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }

    private fun updateCeleb() {
        celebItem = CelebItem(celebItem.pk, celebEditTexts[0].text.toString(),
                celebEditTexts[1].text.toString(), celebEditTexts[2].text.toString(), celebEditTexts[3].text.toString())

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.updateCeleb(celebItem.pk, celebItem)?.enqueue(object: Callback<CelebItem>{
            override fun onResponse(call: Call<CelebItem>, response: Response<CelebItem>) {
                Toast.makeText(this@UpdateDeleteActivity, "Celebrity updated", Toast.LENGTH_SHORT).show()
                Log.d("celebItem", "The response is ${response.body()}")
            }

            override fun onFailure(call: Call<CelebItem>, t: Throwable) {
                Toast.makeText(this@UpdateDeleteActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }
}