package com.ml.quaterion.decisiontree

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// DecisionTree class which defines the whole tree. The tree is created by recursively calling a method.
// Hence, we use HashMap<String,Any> as the datatype of our tree.
class DecisionTree( private var data : DataFrame ) {

    // The name of column which contains the independent variable ( which needs to be predicted ).
    private val LABEL_COLUMN_NAME = "Label"

    // The tree which is used for inferencing.
    private var finalisedTree : HashMap<String,Any>? = null

    init {
        // We create the tree as soon as the data is fed. The tree is created and stored as `finalisedTree`.
        finalisedTree = createTree( data , null )
    }


    // Predict the label for the given sample
    fun predict ( x : HashMap<String,String> ) : String {
        return predictWithTree( x , finalisedTree!! )
    }

    // ------------- Internal Methods -------------

    // Generate a prediction for the given sample.
    private fun predictWithTree ( x : HashMap<String,String> , tree : HashMap<String,Any> ) : String {
        var prediction = ""
        var tree = tree
        for ( key in tree.keys ) {
            val value = x[ key ]
            val p = tree[ key ] as HashMap<String, Any>

            if ( p[ value ] is HashMap<*, *> ) {
                tree = p[ value ] as HashMap<String, Any>
                prediction = predictWithTree( x , tree )
            }
            else {
                if ( p[ value ] == null ) {
                    val k = p.keys.toList()[0]
                    prediction = p[ k ] as String
                }
                else {
                    prediction = p[value] as String
                }
                break
            }
        }
        return prediction
    }

    // Create a tree by recursively calling this method.
    private fun createTree( data : DataFrame , inputTree : HashMap<String,Any>? ) : HashMap<String,Any> {
        val highestIGFeatureName = findHighestIG( data )
        val attributes = data.getData()[ highestIGFeatureName ]!!.distinct()
        var tree = inputTree
        if ( tree == null ) {
            tree = HashMap()
            tree[ highestIGFeatureName ] = HashMap<String,Any>()
        }

        for ( attribute in attributes ) {
            val subTable = getSubTable( data , highestIGFeatureName , attribute )

            val clValueCountHashmap = uniqueAndReturnCounts( subTable[ LABEL_COLUMN_NAME ] as ArrayList<String> )
            val clValues = clValueCountHashmap.keys.toTypedArray()
            val counts = clValueCountHashmap.values.toIntArray()
            //println( Arrays.toString( subTable[ LABEL_COLUMN_NAME ]?.toTypedArray() ))
            //print( counts )
            if ( counts.count() == 1 ){
                val p = tree[ highestIGFeatureName ] as HashMap<String,Any>
                p[ attribute ] = clValues[ 0 ]
                break
            }
            else {
                val p = tree[ highestIGFeatureName ] as HashMap<String,Any>
                p[ attribute ] = createTree( getDataFrame( subTable ) , null )
            }
        }
        return tree
    }

    // Returns a subset of the dataframe sorted by `featureName` and `featureValue`.
    private fun getSubTable( data: DataFrame , featureName : String , featureValue : String ) : HashMap< String , ArrayList<String> > {
        val features = data.getData()[ featureName ]!!
        val outputHashmap = HashMap< String , ArrayList<String> >()
        for ( i in 0 until features.count() ) {
            if ( features[ i ] == featureValue ) {
                for ( x in data.getData().iterator() ){
                    val list = outputHashmap[ x.toPair().first ]
                    if ( list == null ) {
                        outputHashmap[ x.toPair().first ] = ArrayList<String>().apply {
                            add( x.toPair().second[ i ] )
                        }
                    }
                    else{
                        list.add( x.toPair().second[ i ] )
                        outputHashmap[ x.toPair().first ] = list
                    }
                }
            }
        }
        return outputHashmap
    }

    // Find the feature which gives us the maximum information gain score ( IG ).
    private fun findHighestIG( data : DataFrame ) : String {
        val featureNames = data.getFeatureColumnNames()
        val informationGain = ArrayList<Double>()
        for ( name in featureNames ){
            val labelEntropy = findEntropyForLabels( data )
            val featureEntropy = findEntropyForFeature( data , name )
            informationGain.add( labelEntropy - featureEntropy )
        }
        return featureNames[ argMax( informationGain ) ]
    }

    // Get the entropy for all labels.
    private fun findEntropyForLabels( data : DataFrame ) : Double {
        val labels = data.getData()[ LABEL_COLUMN_NAME ]?.toTypedArray()
        val totalLabelsCount = labels?.count()!!.toFloat()
        var entropy = 0.0
        for ( label in labels.distinct() ){
            val labelCounts = labels.count { it == label }.toFloat()
            val p = ( labelCounts / totalLabelsCount ).toDouble()
            entropy += -p * logbase2( p + 1e-19 )// To avoid division by zero.
        }
        return entropy
    }

    // Get the entropy for a specific feature in the dataset.
    private fun findEntropyForFeature( data : DataFrame, featureColumnName : String ) : Double {
        val labels = data.getData()[ LABEL_COLUMN_NAME ]!!.toTypedArray()
        val featureValues = data.getData()[ featureColumnName ]!!.toTypedArray()
        var featureEntropy = 0.0
        for ( featureValue in featureValues.distinct() ) {
            var entropy = 0.0
            var denCount = 0.0
            for (label in labels.distinct()) {
                var numCount = 0.0
                for (i in 0 until featureValues.count()) {
                    if (featureValues[i] == featureValue) {
                        if (labels[i] == label) {
                            numCount += 1
                        }
                    }
                }
                denCount = featureValues.count { it == featureValue }.toDouble()
                val p = numCount / (denCount + 1e-19) // To avoid division by zero.
                entropy += p * logbase2(p + 1e-19)// To avoid division by zero.
            }
            featureEntropy += -(denCount / labels.count()) * entropy
        }
        return Math.abs( featureEntropy )
    }

    // ------------- Secondary Methods -------------

    // Get a `DataFrame` object for the given `HashMap<String,ArrayList<String>>`.
    private fun getDataFrame( x : HashMap<String,ArrayList<String>> ) : DataFrame {
        val dataFrame = DataFrame()
        dataFrame.setData( x )
        return dataFrame
    }

    // Get the index of greatest element in this array.
    private fun argMax( x : ArrayList<Double> ) : Int {
        var y = Double.NEGATIVE_INFINITY
        var index = 0
        for ( i in 0 until x.count() ) {
            if ( x[ i ] > y ){
                y = x[ i ]
                index = i
            }
        }
        return index
    }

    override fun toString(): String {
        return finalisedTree.toString()
    }

    // Gets the number of occurrences ( frequency ) of all the elements.
    private fun uniqueAndReturnCounts(x: ArrayList<String>) : HashMap<String,Int> {
        val outputs = HashMap<String,Int>()
        for ( xi in x.distinct() ){
            outputs[ xi ] = x.count{ it == xi }
        }
        return outputs
    }

    // Calculates the logarithm with base 2 for the given `x`.
    private fun logbase2( x : Double ): Double {
        return Math.log10( x ) / Math.log10( 2.0 )
    }

}
