package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        // add all strings that aren't already in IDictionary to the field
        IDictionary<String, Double> idf = new ChainedHashDictionary<>();
        double totalNumDocs = pages.size() * 1.0;
        ISet<String> allWords = new ChainedHashSet<>();
        
        // nested loops subject to change
        for (Webpage p: pages) {
            for (String word: p.getWords()) {
                if (!idf.containsKey(word)) {
                    idf.put(word, 0.0);
                    allWords.add(word);
                }
            }
        }
        // now calculate idf scores 
        for (String word: allWords) {
            int count = 0;
            for (Webpage w: pages) {
                if (w.getWords().contains(word)) {
                    count++;
                }
            }
            
            if (count != 0) {
                double newCount = 1.0 * count;
                double inside = totalNumDocs / newCount;
                double score = Math.log(inside);
                idf.put(word, score);
            }
        }
        
        return idf;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        // this gets called in computeAllTfIdfVectors buy de wae
        // these words are all within a single WebPage
        IDictionary<String, Double> pageScores = new ChainedHashDictionary<String, Double>();
        double wordCount = 1.0 * words.size();
        ISet<String> allWords = new ChainedHashSet<>();
        
        for (String word: words) {
            if (!pageScores.containsKey(word)) {
                pageScores.put(word, 1.0);
            } else {
                double thisCount = pageScores.get(word);
                thisCount++;
                pageScores.put(word, thisCount);
            }
        }
        
        for (String key: allWords) {
            Double value = pageScores.get(key);
            Double Tf = value / wordCount;
            pageScores.put(key, Tf);
        }
        
        return pageScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        
        // IDF calculations first
        // done, call this.idfScores to access
        
        // setup return
        IDictionary<URI, IDictionary<String, Double>> mixScores = new ChainedHashDictionary<>();
        
        // for each page
        for (Webpage page: pages) {
            // store URI
            URI key = page.getUri();
            
            // get tf scores, and this will eventually have total scores
            IDictionary<String, Double> scores = this.computeTfScores(page.getWords());
            ISet<String> scoreWords = new ChainedHashSet<>();
            for (KVPair<String, Double> p: scores) {
                scoreWords.add(p.getKey());
            }
            for (String word: scoreWords) {
                double idf = this.idfScores.get(word);
                double tf = scores.get(word);
                double newScore = tf * idf;
                scores.put(word, newScore);
            }
            
            mixScores.put(key, scores);
        }
        
        
        return mixScores;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        return 0.0;
    }
}
