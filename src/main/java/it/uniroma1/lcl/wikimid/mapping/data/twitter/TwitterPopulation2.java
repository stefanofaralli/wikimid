package it.uniroma1.lcl.wikimid.mapping.data.twitter;

import it.uniroma1.lcl.jlt.util.Files;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sfaralli
 */
public class TwitterPopulation2 {

    private static HashMap<String, TwitterPopulation2> instances = null;
    private static BufferedReader itbr=null;
    public static TwitterPopulation2 getIterator(String source) 
    {
        if (instances == null) {
            instances = new HashMap<String, TwitterPopulation2>();
        }
        if (instances.containsKey(source)) {
            return instances.get(source);
        } else {
            instances.put(source, new TwitterPopulation2(source));
            return instances.get(source);
        }
    }
    private List<TwitterUser2> population = null;

    private TwitterPopulation2(String source,boolean verifiedorpopular,Set<String> languages,Long minfollowers) {
        population = new ArrayList<TwitterUser2>();
        BufferedReader br;
        try {
            br = Files.getBufferedReader(source);
            while (br.ready()) {
                String line = br.readLine();
                TwitterUser2 tu=TwitterUser2.fromLine(line,verifiedorpopular,languages,minfollowers);
                if (tu!=null)
                population.add(tu);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(TwitterPopulation2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   private TwitterPopulation2(String source) {
        
       
        try {
            //itbr = Files.getBufferedReader(source);
          itbr = new BufferedReader(new InputStreamReader(new FileInputStream(source)/*, "UTF-8"*/)); 

        } catch (IOException ex) {
            Logger.getLogger(TwitterPopulation2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public List<TwitterUser2> getPopulation() {
        return population;
    }

    public static TwitterPopulation2 getInstance(String source,boolean verifiedorpopular,Set<String> languages,Long minfollowers) {
        if (instances == null) {
            instances = new HashMap<String, TwitterPopulation2>();
        }
        if (instances.containsKey(source)) {
            return instances.get(source);
        } else {
            instances.put(source, new TwitterPopulation2(source,verifiedorpopular,languages,minfollowers));
            return instances.get(source);
        }
    }
 public static TwitterPopulation2 getInstance(String source,boolean verifiedorpopular,Set<String> languages) {
        return getInstance(source,verifiedorpopular,languages,0L);
    }
    public TwitterUser2 getNext(boolean onlyverified, Set<String> langugaes,Long minfollowers) throws IOException {
        if (itbr==null) 
            return null;
        if (!itbr.ready()) { 
            itbr.close();
            itbr=null; 
            return null;
        }
        TwitterUser2 tu=null;
        while (tu==null&&itbr.ready()) 
        {
        String line = itbr.readLine();
               tu=TwitterUser2.fromLine(line,onlyverified,langugaes,minfollowers);
        }       
            return tu;
    }
}
