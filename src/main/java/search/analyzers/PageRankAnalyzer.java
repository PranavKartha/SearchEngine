package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        // returning this boi
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        
        // build the set with all legal links
        ISet<URI> allLinks = new ChainedHashSet<>();
        for(Webpage page: webpages) {
            IList<URI> links = page.getLinks();
            allLinks.add(page.getUri());
            if (links.size() > 0) {
                for(URI link: links) {
                    allLinks.add(link);
                }
            }
        }
        
        for(Webpage page: webpages) {
            if(!graph.containsKey(page.getUri())) {
                ISet<URI> links = new ChainedHashSet<>();
                for(URI link:page.getLinks()){
                    if(!link.equals(page.getUri()) && allLinks.contains(link)) {
                        links.add(link);
                    }
                }
                graph.put(page.getUri(), links);
            }
        }
        
        
        
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        
        // we make starting ranks
        double n = graph.size() * 1.0;
        double startValue = 1 / n;
        IDictionary<URI, Double> ranks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> p: graph) {
            URI key = p.getKey();
            ranks.put(key, startValue);
        }
        
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            
            // set all values to 0.0
            // store reference to old values in oldRanks
            IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<>();
            System.out.println(i);
            for (KVPair<URI, ISet<URI>> p: graph) {
                URI key = p.getKey();
                oldRanks.put(key, ranks.get(key));
                ranks.put(key, 0.0);
            }
            // recalculate ranks score
            for (KVPair<URI, ISet<URI>> p: graph) {
                double numLinks = graph.get(p.getKey()).size() * 1.0;
                double oldRank = oldRanks.get(p.getKey());
                double value = 0.0;
                if (numLinks != 0.0) {
                    double quotient = oldRank / numLinks;
                    value = decay * quotient;
//                    ranks.put(p.getKey(), value);
                    ISet<URI> links = graph.get(p.getKey());
                    for (URI link: links) {
                        double changeVal = ranks.get(link);
                        changeVal += value;
                        ranks.put(link, changeVal);
                    }
                } else {
                    for (KVPair<URI, ISet<URI>> pair: graph) {
                        URI key = pair.getKey();
                        double changeVal = ranks.get(key);
                        double quotient = oldRank / n;
                        value = decay * quotient;
                        changeVal += value;
                        ranks.put(key, changeVal);
                    }
                }
//                for (URI link: graph.get(p.getKey())) {
//                    double changeVal = oldRanks.get(link);
//                    changeVal += value;
//                    ranks.put(link, changeVal);
//                }
            }
            for (KVPair<URI, ISet<URI>> p: graph) {
                URI key = p.getKey();
                double value = ranks.get(key);
                double top = 1.0 - decay;
                double quotient = top / n;
                value += quotient;
                ranks.put(key, value);
            }
            
            
            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean stopLoop = true;
            for (KVPair<URI, Double> p: ranks) {
                URI key = p.getKey();
                Double bic = oldRanks.get(key);
                Double boi = ranks.get(key);
                if (Math.abs(boi - bic) >= epsilon) {
                    oldRanks = ranks;
                    stopLoop = false;
                }
            }
            
            if (stopLoop) {
                i = limit;
            }
        }
        return ranks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return this.pageRanks.get(pageUri);
    }
}
