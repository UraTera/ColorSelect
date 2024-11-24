package com.tera.colorselect

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tera.colorselect.databinding.ActivityMainBinding
import yuku.ambilwarna.AmbilWarnaDialog

class MainActivity : AppCompatActivity() {

    companion object {
        const val RED = "red"
        const val GREEN = "green"
        const val BLUE = "blue"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var sp: SharedPreferences
    private var red = 0
    private var green = 0
    private var blue = 0
    private var colorDef = 255
    private var currentColor = Color.GRAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Orientation
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        sp = getSharedPreferences("settings", Context.MODE_PRIVATE)
        red = sp.getInt(RED, colorDef)
        green = sp.getInt(GREEN, colorDef)
        blue = sp.getInt(BLUE, colorDef)
        setSlider()

        val color = Color.rgb(red, green, blue)
        currentColor = color

        initButtons()
        initSlider()
    }

    private fun initButtons() = with(binding) {
        bnOpenDlg.setOnClickListener {
            openDialog()
        }
    }

    private fun initSlider() = with(binding) {
        slRed.addOnChangeListener { _, value, _ ->
            red = value.toInt()
            setColor()
        }
        slGreen.addOnChangeListener { _, value, _ ->
            green = value.toInt()
            setColor()
        }
        slBlue.addOnChangeListener { _, value, _ ->
            blue = value.toInt()
            setColor()
        }
    }

    private fun setSlider() = with(binding) {
        slRed.value = red.toFloat()
        slGreen.value = green.toFloat()
        slBlue.value = blue.toFloat()
        setColor()
    }

    private fun setColor() = with(binding){
        var str = red.toString()
        tvRed.text = str
        str = green.toString()
        tvGreen.text = str
        str = blue.toString()
        tvBlue.text = str

        val color = Color.rgb(red, green, blue)
        str = color.toString()
        tvInt.text = str
        tvColor.setBackgroundColor(color)
        currentColor = color

        val hexColor = String.format("#%06X", (0xFFFFFF and color))
        tvHex.text = hexColor
    }

    private fun convertIntToRGB(colorInt: Int) {
        // Convert Int to HEX
        val hexColor = String.format("#%06X", (0xFFFFFF and colorInt))
        // Get array of components
        val arrayColor = hexToRGB(hexColor)

        red = arrayColor[0]
        green = arrayColor[1]
        blue = arrayColor[2]
        setSlider()
    }
    // Convert HEX to RGB
    private fun hexToRGB(hex: String): IntArray {
        var hexColor = hex
        if (hex[0] == '#')
            hexColor = hex.substring(1) // Remove '#' character
        val red = hexColor.substring(0, 2).toInt(16)
        val green = hexColor.substring(2, 4).toInt(16)
        val blue = hexColor.substring(4, 6).toInt(16)
        return intArrayOf(red, green, blue)
    }


    // Color selection dialog
    private fun openDialog() {
        val dialog = AmbilWarnaDialog(this, currentColor, false, object :
            AmbilWarnaDialog.OnAmbilWarnaListener {
            // ОК
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                currentColor = color
                convertIntToRGB(color)
            }
            // Cancel
            override fun onCancel(dialog: AmbilWarnaDialog) {
                Toast.makeText(applicationContext, "Select cancel!", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show()
    }

    override fun onStop() {
        super.onStop()
        val editor = sp.edit()
        editor.putInt(RED, red)
        editor.putInt(GREEN, green)
        editor.putInt(BLUE, blue)
        editor.apply()

    }

}