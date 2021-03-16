package com.ml.quaterion.decisiontree

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// Holds columnar data for our decision tree
class DataFrame {

    // The object which holds the data. The below `HashMap` takes in the column name as its key and an `ArrayList<String>` as the
    // data.
    private var data : HashMap<String,ArrayList<String>> = HashMap()
    private var numRows = 0

    fun setData( x : HashMap<String,ArrayList<String>> ) {
        data = x
    }

    fun addColumn( columnData : ArrayList<String> , columnName : String ) {
        data[columnName] = columnData
        numRows = columnData.size
    }

    fun removeColumn( columnName : String ) {
        data.remove( columnName )
    }


    fun getNumCols() : Int {
        return data.size
    }

    fun getNumRows() : Int {
        return numRows
    }

    // Return samples ( in form of DataFrame object ) given their indices.
    fun getEntries( indices : IntArray ) : DataFrame {
        val dataFrame = DataFrame()
        data.map { column ->
            // `column` represent a Map -> ( String , ArrayList<String> )
            // column.key -> Name of the column as in the training datasets.
            // column.value -> ArrayList<String> containing the column's data.
            val columnData = ArrayList<String>()
            val values = column.value
            // Add the feature values corresponding to each index in `indices`.
            for ( index in indices ) {
                columnData.add( values[ index ] )
            }
            // Append the column to the data frame.
            dataFrame.addColumn( columnData , column.key )
        }
        return dataFrame
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for ( ( columnName , data ) in data ) {
            stringBuilder.append( "\n $columnName ${Arrays.toString( data.toTypedArray() )}" )
        }
        return stringBuilder.toString()
    }

    // Get a list of all the feature column names. Exclude the column which contains the labels.
    fun getFeatureColumnNames() : List<String> {
        val pKeys = data.keys.toList()
        return pKeys.filter { it != "Label" }
    }

    fun getData() : HashMap< String , ArrayList<String> > {
        return data
    }
}
