/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.babelnet;

import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.DoubleCounter;
import it.uniroma1.lcl.jlt.util.Files;
import it.uniroma1.lcl.jlt.util.IntegerCounter;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation2;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser2;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.CandidateMethod;
import it.uniroma1.lcl.wikimid.mapping.mapper.similarity.CosineSimilarity;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.Mapper;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author sfaralli
 */
public class BabelNetMapperIT extends Mapper<BabelSynset> {

    BabelNetCandidatesIT candidatesName;
    BabelNetCandidatesIT candidatesScreenName;
    private BabelNetContextEN bc;
    Map<TwitterUser, DoubleCounter<BabelSynset>> mapping;
    final static Logger logger = Logger.getGlobal();
    private static HashMap<String, BabelNetMapperIT> instances = null;
    private String outputfile;

    private BabelNetMapperIT() {
        candidatesName = new BabelNetCandidatesIT(CandidateMethod.NAME_DECOMPOSITION);
        candidatesScreenName = new BabelNetCandidatesIT(CandidateMethod.SCREENNAME_DECOMPOSITION);
        bc = new BabelNetContextEN();
        mapping = new HashMap<TwitterUser, DoubleCounter<BabelSynset>>();
    }

    public static BabelNetMapperIT getInstance(String source) {
        if (instances == null) {
            instances = new HashMap<String, BabelNetMapperIT>();
        }
        if (instances.containsKey(source)) {
            return instances.get(source);
        } else {
            instances.put(source, new BabelNetMapperIT());
            return instances.get(source);
        }
    }

    @Override
    public DoubleCounter<BabelSynset> map(TwitterUser tu) {
        if (mapping.containsKey(tu)) {
            return mapping.get(tu);
        }
        DoubleCounter<BabelSynset> result = new DoubleCounter<BabelSynset>();
        List<BabelSynset> candidates = candidatesName.getCandidates(tu);
        if (!candidates.isEmpty()) {
            result.addFrom(disambiguate(tu, candidates));
        } else {
            candidates = candidatesScreenName.getCandidates(tu);
            if (!candidates.isEmpty()) {
                result.addFrom(disambiguate(tu, candidates));
            }
        }
        mapping.put(tu, result);
        return result;
    }

    @Override
    public DoubleCounter<BabelSynset> onFlyMap(TwitterUser tu) {
        DoubleCounter<BabelSynset> result = new DoubleCounter<BabelSynset>();
        List<BabelSynset> candidates = candidatesName.getOnFlyCandidates(tu);
        if (!candidates.isEmpty()) {
            result.addFrom(disambiguate(tu, candidates));
        } else {
            candidates = candidatesScreenName.getOnFlyCandidates(tu);
            if (!candidates.isEmpty()) {
                result.addFrom(disambiguate(tu, candidates));
            }
        }
        return result;
    }
 
    public DoubleCounter<BabelSynset> onFlyMap2(TwitterUser2 tu) {
        DoubleCounter<BabelSynset> result = new DoubleCounter<BabelSynset>();
        List<BabelSynset> candidates = candidatesName.getOnFlyCandidates2(tu);
        if (!candidates.isEmpty()) {
            result.addFrom(disambiguate2(tu, candidates));
        } else {
            candidates = candidatesScreenName.getOnFlyCandidates2(tu);
            if (!candidates.isEmpty()) {
                result.addFrom(disambiguate2(tu, candidates));
            }
        }
        return result;
    }
    public DoubleCounter<BabelSynset> disambiguate(TwitterUser tu, List<BabelSynset> candidates) {

        DoubleCounter<BabelSynset> result = new DoubleCounter<BabelSynset>();
        if (candidates.size() == 1) {
            result.count(candidates.get(0), 1.0);
            return result;
        }
        IntegerCounter<String> context = tu.getContext();
        for (BabelSynset candidate : candidates) {
            result.count(candidate, CosineSimilarity.calculateCosineSimilarity(context, bc.getNeighborhoodContext(candidate)));
        }
        return result;
    }
 public DoubleCounter<BabelSynset> disambiguate2(TwitterUser2 tu, List<BabelSynset> candidates) {

        DoubleCounter<BabelSynset> result = new DoubleCounter<BabelSynset>();
        if (candidates.size() == 1) {
            result.count(candidates.get(0), 1.0);
            return result;
        }
        IntegerCounter<String> context = tu.getContext();
        for (BabelSynset candidate : candidates) {
            result.count(candidate, CosineSimilarity.calculateCosineSimilarity(context, bc.getNeighborhoodContext(candidate)));
        }
        return result;
    }
    @Override
    public void onFlyMap(TwitterPopulation tp, String outputfile) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);

            for (TwitterUser tu : tp.getPopulation()) {
                DoubleCounter<BabelSynset> res = onFlyMap(tu);
                logger.log(Level.INFO, "TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                logger.log(Level.INFO, "BNs: " + res);
                bw.write("TU:\t" + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName() + "\t" + tu.getLocation() + "\t" + tu.getStatusline() + "\t" + tu.getLanguage() + "\n");
                for (BabelSynset bs : res.getSortedElements()) {
                    bw.write("\t" + res.get(bs) + "\t" + bs.getId().getID() + "\t" + bs.getMainSense(Language.EN).getDBPediaURI() + "\n");
                    bw.flush();
                }
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
 @Override
    public void onFlyScreenname2Wiki(TwitterPopulation tp, String outputfile) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);

            for (TwitterUser tu : tp.getPopulation()) 
            {
                DoubleCounter<BabelSynset> res = onFlyMap(tu);
                logger.log(Level.INFO, "TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                logger.log(Level.INFO, "BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                    bw.write(tu.getScreen_name()+"\t\n");    
                    }
                else
                    bw.write(tu.getScreen_name()+"\t\n");    
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void onFlyScreenname2Wiki2STOPATID(TwitterPopulation2 tp, String outputfile,String idtostop) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);

            for (TwitterUser2 tu : tp.getPopulation()) 
            {
                if (tu.getTwitterID().equals(idtostop)) break;
                DoubleCounter<BabelSynset> res = onFlyMap2(tu);
                System.out.println("TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                 System.out.println( "BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                   // bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
                    }
             //   else
             //       bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void onFlyScreenname2Wiki2STARTAFTERIDANDSTOP(TwitterPopulation2 tp, String outputfile,String idtostart,String idtostop) {
         BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);
            boolean trovato=false;
            for (TwitterUser2 tu : tp.getPopulation()) 
            {
                
                    if (tu.getTwitterID().equals(idtostop)) break;
                if (tu.getTwitterID().equals(idtostart)) 
                {
                    trovato=true;
                    continue;
                }
                if (!trovato) continue;
                DoubleCounter<BabelSynset> res = onFlyMap2(tu);
                System.out.println("TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                 System.out.println( "BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                   // bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
                    }
             //   else
             //       bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void onFlyScreenname2Wiki2STARTAFTERID(TwitterPopulation2 tp, String outputfile,String idtostart) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);
            boolean trovato=false;
            for (TwitterUser2 tu : tp.getPopulation()) 
            {
                
                if (tu.getTwitterID().equals(idtostart)) 
                {
                    trovato=true;
                    continue;
                }
                if (!trovato) continue;
                DoubleCounter<BabelSynset> res = onFlyMap2(tu);
                System.out.println("TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                 System.out.println( "BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                   // bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
                    }
             //   else
             //       bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     public void onFlyScreenname2Wiki2(TwitterPopulation2 tp, String outputfile) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile);

            for (TwitterUser2 tu : tp.getPopulation()) 
            {
                DoubleCounter<BabelSynset> res = onFlyMap2(tu);
                System.out.println("TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                 System.out.println( "BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                   // bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
                    }
             //   else
             //       bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\tNOMAP\n");    
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void onFlyScreenname2Wiki2(TwitterPopulation2 tp, String outputfile,String reprisefrom) {
        BufferedWriter bw;
        try {
            bw = Files.getBufferedWriter(outputfile,true);
            boolean proceed=false;
            for (TwitterUser2 tu : tp.getPopulation()) 
            {
                if (!proceed)
                {
                if (tu.getScreen_name().equals(reprisefrom)) proceed=true;
                continue;
                }
                DoubleCounter<BabelSynset> res = onFlyMap2(tu);
                System.out.println("TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
                System.out.println("BNs: " + res);
                if (!res.keySet().isEmpty())
                    try
                    {
                    bw.write(tu.getTwitterID()+"\t"+tu.getScreen_name()+"\t"+"WIKI:EN:"+res.getSortedElements().iterator().next().getMainSense(Language.EN).getDBPediaURI().replace("http://DBpedia.org/resource/","").replace(" ","_")+"\n");    
                    bw.flush();
                    }
                    catch(Exception e)
                    {
                   // bw.write(tu.getScreen_name()+"\t\n");    
                    }
              /*  else
                    bw.write(tu.getScreen_name()+"\t\n");   */
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(BabelNetMapperIT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Override
    public Map<TwitterUser, DoubleCounter<BabelSynset>> map(TwitterPopulation tp) {
        HashMap<TwitterUser, DoubleCounter<BabelSynset>> result = new HashMap<TwitterUser, DoubleCounter<BabelSynset>>();
        for (TwitterUser tu : tp.getPopulation()) {
            DoubleCounter<BabelSynset> res = map(tu);
            result.put(tu, res);
            logger.log(Level.INFO, "TP: " + tu.getTwitterID() + "\t" + tu.getScreen_name() + "\t" + tu.getName());
            logger.log(Level.INFO, "BNs: " + res);

        }
        return result;
    }

}
