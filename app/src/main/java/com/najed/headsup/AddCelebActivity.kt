package com.najed.headsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCelebActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var addButton: Button
    private lateinit var celebEditTexts: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_celeb)

        celebEditTexts = listOf(
            findViewById(R.id.add_name_et),
            findViewById(R.id.add_taboo1_et),
            findViewById(R.id.add_taboo2_et),
            findViewById(R.id.add_taboo3_et)
        )

        addButton = findViewById(R.id.add_btn)
        addButton.setOnClickListener {
            addCeleb(
                CelebItem(
                    0,
                    celebEditTexts[0].text.toString(),
                    celebEditTexts[1].text.toString(),
                    celebEditTexts[2].text.toString(),
                    celebEditTexts[3].text.toString(),
                )
            )
            for (property in celebEditTexts)
                property.setText("")
        }

        backButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun addCeleb(celeb: CelebItem){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface!!.addCeleb(celeb)?.enqueue(object: Callback<CelebItem?>{
            override fun onResponse(call: Call<CelebItem?>, response: Response<CelebItem?>) {
                Toast.makeText(this@AddCelebActivity, "Celebrity added!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<CelebItem?>, t: Throwable) {
                Toast.makeText(this@AddCelebActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }
}