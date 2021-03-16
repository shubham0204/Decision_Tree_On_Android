package com.ml.quaterion.decisiontree


import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

// Class which creates a Random Forest with N Decision Trees ( N = NUM_TREES, see below ).
class RandomForest( private var data : DataFrame ) {

    // Number of trees in the forest.
    private val NUM_TREES = 5

    // Choose these many samples "with replacement" from the training dataset.
    private val BOOTSTRAPPED_DATASET_SIZE = 10

    // The forest represented as an array of DecisionTree objects.
    private var forest : ArrayList<DecisionTree> = ArrayList()

    init {
        // Create the bootstrapped datasets
        val datasets = createBootstrappedDatasets()
        println( "Bootstrapped datasets created." )
        // Initialize the forest
        createForest( datasets )
    }

    // Predict a class for the given sample using the Random Forest.
    fun predict( x : HashMap<String,String> ) : String {
        // Create an empty array to store class labels.
        val treeOutputs = Array( NUM_TREES ) { "" }
        for ( i in 0 until NUM_TREES ) {
            // Store the output of each DecisionTree in our forest.
            treeOutputs[ i ] = forest[i].predict( x )
            println( "Prediction ${i+1} DecisionTree is ${treeOutputs[i]}")
        }
        // Get the majority label, which is our final prediction for the given sample.
        val mostVotedLabel = treeOutputs.groupingBy{ it }.eachCount().maxBy{ entry -> entry.value }!!.key
        println( "Most voted label : $mostVotedLabel" )
        return mostVotedLabel
    }

    // Create bootstrapped datasets given training dataset ( which is given to the constructor of
    // this class ).
    private fun createBootstrappedDatasets() : Array<DataFrame> {
        // ArrayList to store the bootstrapped datasets ( which are DataFrame objects )
        val dataFrames = ArrayList<DataFrame>()
        for ( i in 0 until NUM_TREES ) {
            // Generate N random indices in the range [ 0 , num_samples_in_train_ds ).
            // Here N = BOOTSTRAPPED_DATASET_SIZE
            // Samples at these indices will constitute a bootstrapped dataset.
            val randomIndices = IntArray( BOOTSTRAPPED_DATASET_SIZE ){ Random.nextInt( data.getNumRows() ) }
            // Get all the entries ( samples ) present at the given indices and store them in dataFrames.
            dataFrames.add( data.getEntries( randomIndices ) )
        }
        // Return the datasets
        return dataFrames.toTypedArray()
    }

    // Create a forest given the bootstrapped datasets. Each tree will have one bootstrapped dataset.
    private fun createForest( dataFrames : Array<DataFrame> ) {
        for ( i in 0 until NUM_TREES ) {
            // Initialize a DecisionTree with ith bootstrapped dataset. Add to the forest.
            println( "Creating ${i+1} DecisionTree ..." )
            forest.add( DecisionTree( dataFrames[ i ] ) )
        }
    }

}
