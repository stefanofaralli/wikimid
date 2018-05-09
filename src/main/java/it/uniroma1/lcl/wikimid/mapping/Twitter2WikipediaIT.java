
package it.uniroma1.lcl.wikimid.mapping;

import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation2;
import it.uniroma1.lcl.wikimid.mapping.mapper.babelnet.BabelNetMapperIT;


public class Twitter2WikipediaIT {

  
    public static void main(String[] args) {
        String source = args[0];
        String outputfile = source + ".mapping.tsv";
        System.out.println("Loading the population....");
        TwitterPopulation2 tp = TwitterPopulation2.getInstance(source, false, null, 10000L);
        System.out.println("DONE");
        System.out.println("TWITTER USER:" + tp.getPopulation().size());
        BabelNetMapperIT bnm = BabelNetMapperIT.getInstance(source);
        bnm.onFlyScreenname2Wiki2(tp,outputfile);
    }

}
