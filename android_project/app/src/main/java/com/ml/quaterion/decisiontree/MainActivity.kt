package com.ml.quaterion.decisiontree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tasteFeature = arrayOf( "Salty" , "Sweet" , "Spicy" )
    private val textureFeature = arrayOf( "Hard" , "Soft" )
    private val temperatureFeature = arrayOf( "Hot" , "Cold" )

    private var defaultTasteFeature = "Salty"
    private var defaultTextureFeature = "Hard"
    private var defaultTemperatureFeature = "Cold"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val decisionTree = loadData()

        ArrayAdapter.createFromResource(this,
            R.array.taste_feature ,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            taste_spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(this,
            R.array.texture_feature ,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            texture_spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(this,
            R.array.temperature_feature ,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            temperature_spinner.adapter = adapter
        }

        taste_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                defaultTasteFeature = tasteFeature[ position ]
            }
        }
        texture_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                defaultTextureFeature = textureFeature[ position ]
            }
        }
        temperature_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                defaultTemperatureFeature = temperatureFeature[ position ]
            }
        }

        predict.setOnClickListener {
            Log.e( "TEST" , "$defaultTemperatureFeature , $defaultTextureFeature , $defaultTasteFeature")
            val sample = HashMap<String,String>().apply {
                put( "Taste" , defaultTasteFeature )
                put( "Temperature" , defaultTemperatureFeature )
                put( "Texture" , defaultTextureFeature )
            }
            val label = decisionTree.predict( sample )
            predicted_label.text = "Prediction is $label"
        }

    }

    private fun loadData() : DecisionTree {
        val decisionTree = DecisionTree()
        val dataFrame = DataFrame()
        dataFrame.addColumn(
            arrayOf(
                "Salty",
                "Spicy",
                "Spicy",
                "Spicy",
                "Spicy",
                "Sweet",
                "Salty",
                "Sweet",
                "Spicy",
                "Salty"
            ).toList() as ArrayList<String>,
            "Taste"
        )
        dataFrame.addColumn(
            arrayOf("Hot", "Hot", "Hot", "Cold", "Hot", "Cold", "Cold", "Hot", "Cold", "Hot").toList() as ArrayList<String>,
            "Temperature"
        )
        dataFrame.addColumn(
            arrayOf(
                "Soft",
                "Soft",
                "Hard",
                "Hard",
                "Hard",
                "Soft",
                "Soft",
                "Soft",
                "Soft",
                "Hard"
            ).toList() as ArrayList<String>,
            "Texture"
        )
        dataFrame.addColumn(
            arrayOf("No", "No", "Yes", "No", "Yes", "Yes", "No", "Yes", "Yes", "Yes").toList() as ArrayList<String>,
            "Label"
        )
        decisionTree.setData( dataFrame )
        return decisionTree
    }

}
