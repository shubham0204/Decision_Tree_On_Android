package com.ml.quaterion.decisiontree

// Holds columnar data for our decision tree
class DataFrame {

    // The object which holds the data. The below `HashMap` takes in the column name as its key and an `ArrayList<String>` as the
    // data.
    private var data : HashMap<String,ArrayList<String>> = HashMap()

    fun setData( x : HashMap<String,ArrayList<String>> ) {
        data = x
    }

    fun addColumn( columnData : ArrayList<String> , columnName : String ) {
        data[columnName] = columnData
    }

    fun removeColumn( columnName : String ) {
        data.remove( columnName )
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