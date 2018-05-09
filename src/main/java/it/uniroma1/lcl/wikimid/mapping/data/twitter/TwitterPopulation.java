package it.uniroma1.lcl.wikimid.mapping.data.twitter;

import it.uniroma1.lcl.jlt.util.Files;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sfaralli
 */
public class TwitterPopulation {

    private static HashMap<String, TwitterPopulation> instances = null;
    private static BufferedReader itbr=null;
    public static TwitterPopulation getIterator(String source, boolean verifiedorpopular) 
    {
        if (instances == null) {
            instances = new HashMap<String, TwitterPopulation>();
        }
        if (instances.containsKey(source)) {
            return instances.get(source);
        } else {
            instances.put(source, new TwitterPopulation(source,verifiedorpopular,true));
            return instances.get(source);
        }
    }
    private List<TwitterUser> population = null;

    private TwitterPopulation(String source,boolean verifiedorpopular) {
        population = new ArrayList<TwitterUser>();
        BufferedReader br;
        try {
            br = Files.getBufferedReader(source);
            while (br.ready()) {
                String line = br.readLine();
                TwitterUser tu=TwitterUser.fromLine(line,verifiedorpopular);
                if (tu!=null)
                population.add(tu);
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(TwitterPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   private TwitterPopulation(String source,boolean verifiedorpopular,boolean iter) {
        
       
        try {
            //itbr = Files.getBufferedReader(source);
          itbr = new BufferedReader(new InputStreamReader(new FileInputStream(source)/*, "UTF-8"*/)); 

        } catch (IOException ex) {
            Logger.getLogger(TwitterPopulation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public List<TwitterUser> getPopulation() {
        return population;
    }

    public static TwitterPopulation getInstance(String source,boolean verifiedorpopular) {
        if (instances == null) {
            instances = new HashMap<String, TwitterPopulation>();
        }
        if (instances.containsKey(source)) {
            return instances.get(source);
        } else {
            instances.put(source, new TwitterPopulation(source,verifiedorpopular));
            return instances.get(source);
        }
    }

    public TwitterUser getNext() throws IOException {
        if (itbr==null) 
            return null;
        if (!itbr.ready()) { 
            itbr.close();
            itbr=null; 
            return null;
        }
        TwitterUser tu=null;
        while (tu==null&&itbr.ready()) 
        {
        String line = itbr.readLine();
               tu=TwitterUser.fromLine(line,false);
        }       
            return tu;
    }
}
