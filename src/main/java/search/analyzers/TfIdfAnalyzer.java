package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;


import java.util.Iterator;

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
    private IDictionary<URI, Double> norms;
    private IDictionary<URI, IDictionary<String, Double>> tfScores;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.tfScores = this.getAllTfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.norms = this.getNorms();
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

        for (Webpage p: pages) {
            ISet<String> words = new ChainedHashSet<>();
            IList<String> pageWords = p.getWords();
            for (String word: pageWords) {
                words.add(word);
            }
            for (String word: words) {
                idf.put(word, idf.getOrDefault(word, 0.0) + 1);
            }
        }

        // now calculate idf scores 
        Iterator<KVPair<String, Double>> iter = idf.iterator();
        while (iter.hasNext()) {
            KVPair<String, Double> p = iter.next();
            double thing = p.getValue();
            double inside = totalNumDocs / thing;
            double score = Math.log(inside);
            idf.put(p.getKey(), score);
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
                allWords.add(word);
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
    
    private IDictionary<URI, IDictionary<String, Double>> getAllTfScores(ISet<Webpage> pages){
        IDictionary<URI, IDictionary<String, Double>> result = new ChainedHashDictionary<>();
        
        for (Webpage page: pages) {
            URI key = page.getUri();
            IDictionary<String, Double> tfScores = this.computeTfScores(page.getWords());
            result.put(key, tfScores);
        }
        
        return result;
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
        
        // for each Webpage
        for (Webpage page: pages) {
            // store URI
            URI key = page.getUri();
            
            // get tf scores, and this will eventually have total scores
            IDictionary<String, Double> scores = this.tfScores.get(key);
            
            // make and fill an ISet with words that are relevant to this Webpage
            ISet<String> scoreWords = new ChainedHashSet<>();
            for (KVPair<String, Double> p: scores) {
                scoreWords.add(p.getKey());
            }
            
            // now calculate the tfidf scores for each word, store in scores
            for (String word: scoreWords) {
                double idf = this.idfScores.get(word);
                double tf = scores.get(word);
                double newScore = tf * idf;
                scores.put(word, newScore);
                // scores should now have the tfidf scores in them
            }
            
            // put tfidf version of scores as value to URI key in 
            // return
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
        IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
        
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        
        // tfidf vector for inputted page
        IDictionary<String, Double> relevantPage = this.documentTfIdfVectors.get(pageUri);
        
        // now for the query
        // first, get the tf scores
        IDictionary<String, Double> queryVector = this.computeTfScores(query);
        // make a set of all words in queryVector
        ISet<String> queryWords = new ChainedHashSet<>();
        for (KVPair<String, Double> p: queryVector) {
            queryWords.add(p.getKey());
        }
        // now the idf scores
        // if not in the idf dictionary, then the value becomes 0
        double denominator;
        double numerator = 0.0;
        for (String word: queryWords) {
            double tf = queryVector.get(word);
            // if we have the idf score, GETI-ESKETIIII
            // otherwise set it to 0
            double idf;
            if (this.idfScores.containsKey(word)) {
                idf = idfScores.get(word);
            } else {
                idf = 0.0;
            }
            double score = tf * idf;
            queryVector.put(word, score);
            
            double qTop = score;
            double pageTop;
            if (relevantPage.containsKey(word)) {
                pageTop = relevantPage.get(word);
            } else {
                pageTop = 0.0;
            }
            double top = pageTop * qTop;
            numerator += top;
        }
        // BOOM we made the tfidf vector for the query
        
//        //da Top
//        double numerator = 0.0;
//        for (String word: queryWords) {
//            double pageScore;
//            if (relevantPage.containsKey(word)) {
//                pageScore = relevantPage.get(word);
//            } else {
//                pageScore = 0.0;
//            }
//            double wordScore = queryVector.get(word);
//            double thisTop = wordScore * pageScore;
//            numerator += thisTop;
//        }
        // da Bottom
        double doc = this.norms.get(pageUri);
        double q = this.norm(queryVector);
        denominator = doc * q;
        if (denominator == 0.0) {
            return 0.0;
        } else {
            return numerator / denominator;
        }
    }
    
    private double norm(IDictionary<String, Double> vector) {
        double sum = 0.0;
        for (KVPair<String, Double> p: vector) {
            String key = p.getKey();
            double add = vector.get(key);
            add *= add;
            sum += add;
        }
        sum = Math.sqrt(sum);
        return sum;
    }
    
    private IDictionary<URI, Double> getNorms(){
        IDictionary<URI, Double> theNorms = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String,Double>> p: this.documentTfIdfVectors) {
            URI key = p.getKey();
            IDictionary<String, Double> vector = p.getValue();
            double value = this.norm(vector);
            theNorms.put(key, value);
        }
        return theNorms;
    }
}
