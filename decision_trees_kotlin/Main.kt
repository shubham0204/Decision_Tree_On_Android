
import kotlin.collections.ArrayList

/*
* The method which inputs the data to the class DecisionTree.
* */
fun main( ) {

    val decisionTree = DecisionTree()

    // Get some sample data
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

    // Set the data in the tree. Soon, the tree is created.
    decisionTree.setData( dataFrame )
    val sample = HashMap<String,String>().apply {
        put( "Taste" , "Salty" )
        put( "Temperature" , "Cold" )
        put( "Texture" , "Hard" )
    }

    // Print the tree as a `HashMap`.
    println( decisionTree.predict( sample ) )

}