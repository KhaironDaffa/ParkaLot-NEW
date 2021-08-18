package com.bangkit.parkalot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bangkit.parkalot.databinding.ActivityParkingBinding

class ParkingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var parkingBinding: ActivityParkingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parkingBinding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(parkingBinding.root)

        supportActionBar?.hide()

        parkingBinding.btnBack.setOnClickListener(this)

        val spotA1 = parkingBinding.A1
        val spotA2 = parkingBinding.A2
        val spotA3 = parkingBinding.A3
        val spotA4 = parkingBinding.A4
        val spotA5 = parkingBinding.A5
        val spotA6 = parkingBinding.A6
        val indicatorTrue = 1
        val indicatorFalse = 0

        indicator(indicatorFalse, spotA1)
        indicator(indicatorTrue, spotA2)
        indicator(indicatorFalse, spotA3)
        indicator(indicatorFalse, spotA4)
        indicator(indicatorFalse, spotA5)
        indicator(indicatorTrue, spotA6)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                val toBack = Intent(this@ParkingActivity, HomeActivity::class.java)
                startActivity(toBack)
            }
        }
    }

    private fun indicator(indicator: Int, spot: TextView) {
        if (indicator == 0){
            spot.setBackgroundResource(R.drawable.spot_layout_green)
        }

        else {
            spot.setBackgroundResource(R.drawable.spot_layout_red)
        }
    }
}