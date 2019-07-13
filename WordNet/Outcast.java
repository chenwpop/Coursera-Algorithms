/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: 19-07-12
 *  Description: The solution to Coursera Algorithms II, Week 1 assignment
 **************************************************************************** */

/**
 * A functional class to detect outcast in a group of WordNet words
 */
public class Outcast {
    private final WordNet wordNet;

    /**
     * constructor take a WordNet object.
     *
     * @param aWordNet the WordNet object
     */
    public Outcast(WordNet aWordNet) {
        wordNet = aWordNet;
    }

    /**
     * Given an array of WordNet nouns, find out the outcast
     *
     * @param nouns a collection of WordNet nouns
     * @return the outcast
     */
    public String outcast(String[] nouns) {
        int dist = 0;
        int currentDist;
        String ans = nouns[0];
        for (int i = 0; i < nouns.length; ++i) {
            currentDist = 0;
            for (String noun : nouns) {
                if (nouns[i].equals(noun)) continue;
                currentDist += wordNet.distance(nouns[i], noun);
            }
            if (currentDist > dist) {
                dist = currentDist;
                ans = nouns[i];
            }
        }
        return ans;
    }

    /**
     * main function for unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
