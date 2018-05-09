/*
* WikiMID
* Giorgia Di Tommaso, Stefano Faralli, Giovanni Stilo, Paola Velardi
*
* 
* Project and Resources:
*  http://wikimid.tweets.di.uniroma1.it/wikimid/
*  https://figshare.com/articles/Wiki-MID_Dataset_LOD_TSV_/6231326/1
*  https://github.com/stefanofaralli/wikimid
* License  
*  https://creativecommons.org/licenses/by/4.0/
*
*  This is part of the pipiline used for the contruction of the WikiMID resource
*  There are several aspects of the project (source and documentation) we are improving. 
*  
*/

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
        bnm.onFlyScreenname2Wiki2(tp, outputfile);
    }

}
