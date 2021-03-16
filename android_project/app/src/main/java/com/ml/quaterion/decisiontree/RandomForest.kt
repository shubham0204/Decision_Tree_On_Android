package com.ml.quaterion.decisiontree

import android.util.Log
import kotlin.random.Random

class RandomForest( private var data : DataFrame ) {

    private val BOOTSTRAPPED_DATASET_SIZE = 10
    private val NUM_TREES = 2
    private val rawData = data.getData()
    private var forest : ArrayList<DecisionTree> = ArrayList<DecisionTree>()

    init {
        createForest( createBootstrappedDatasets() )
    }

    fun predict( x : HashMap<String,String> ) : String {
        val treeOutputs = Array( NUM_TREES ) { "" }
        for ( i in 0 until NUM_TREES ) {
            treeOutputs[ i ] = forest[i].predict( x )
        }
        return treeOutputs.groupingBy{ it }.eachCount().maxBy{ entry -> entry.value }!!.key
    }

    private fun createBootstrappedDatasets() : Array<DataFrame> {
        val dataFrames = ArrayList<DataFrame>()
        for ( i in 0 until NUM_TREES ) {
            val randomIndices = IntArray( BOOTSTRAPPED_DATASET_SIZE ){ Random.nextInt( rawData.size ) }
            dataFrames.add( data.getEntries( randomIndices ) )
        }
        return dataFrames.toTypedArray()
    }


    private fun createForest( dataFrames : Array<DataFrame> ) {
        Log.e( "App" , dataFrames.size.toString() )
        for ( i in 0 until NUM_TREES ) {
            forest.add( DecisionTree( dataFrames[ i ] ) )
            println( forest[i] )
        }
    }

}