package com.faircorp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.model.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getLongExtra(ROOM_NAME_PARAM, 0)

        val recyclerView = findViewById<RecyclerView>(R.id.list_rooms)

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().roomsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        if(it.body() != null){
                            findViewById<TextView>(R.id.activity_room_name).text = it.body()?.name
                            findViewById<TextView>(R.id.activity_room_current).text = it.body()?.currentTemperature.toString()
                            findViewById<TextView>(R.id.activity_room_target).text = it.body()?.targetTemperature.toString()
                        }
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on room loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }

    fun showWindows(view: View) {

        val intent = Intent(this, windowsForARoom::class.java).putExtra(
            ROOM_NAME_PARAM,
            intent.getLongExtra(ROOM_NAME_PARAM, 0))
        startActivity(intent)
    }
}