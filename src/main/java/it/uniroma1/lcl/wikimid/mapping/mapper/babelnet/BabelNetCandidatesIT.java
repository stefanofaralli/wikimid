/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.babelnet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser2;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.CandidateMethod;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.Candidates;
import it.uniroma1.lcl.wikimid.mapping.mapper.text.NameDecomposition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author sfaralli
 */
public class BabelNetCandidatesIT extends Candidates<BabelSynset> {

    private Multimap<TwitterUser, BabelSynset> candidates = null;
    private Multimap<TwitterUser2, BabelSynset> candidates2= null;
    private BabelNet bn;
    private CandidateMethod cm;
    private BabelSenseSource[] bsss = new BabelSenseSource[]{BabelSenseSource.WIKI, BabelSenseSource.WIKIRED, BabelSenseSource.WIKIDIS};

    public BabelNetCandidatesIT(CandidateMethod cm) {
        bn = BabelNet.getInstance();
        this.cm = cm;
    }

    @Override
    public List<BabelSynset> getCandidates(TwitterUser tu) {

        if (candidates == null) {
            candidates = HashMultimap.create();
        }
        if (candidates.containsKey(tu)) {
            return new ArrayList<BabelSynset>(candidates.get(tu));
        }
        try {

            List<String> names = NameDecomposition.nameDecompostion(tu, cm);
            for (String n : names) {
            //    logger.log(Level.INFO,"Name:"+n);

                List<BabelSynset> senses = bn.getSynsets(n, Language.IT, BabelPOS.NOUN, null, true, bsss);
                candidates.putAll(tu, senses);
                //  logger.log(Level.INFO,"Candidates:"+senses);
                if (senses != null && senses.size() > 0) {
                    break;
                }
            }
            return new ArrayList<BabelSynset>(candidates.get(tu));
        } catch (IOException ex) {
            Logger.getLogger(BabelNetCandidatesIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Multimap<TwitterUser, BabelSynset> getAllCandidates(TwitterPopulation tp) {
        // if (candidates==null) candidates= HashMultimap.create();
        HashMultimap<TwitterUser, BabelSynset> result = HashMultimap.create();
        for (TwitterUser tu : tp.getPopulation()) {
            result.putAll(tu, getCandidates(tu));
        }
        return null;
    }

    @Override
    public List<BabelSynset> getOnFlyCandidates(TwitterUser tu) {

        if (candidates == null) {
            candidates = HashMultimap.create();
        }
        if (candidates.containsKey(tu)) {
            return new ArrayList<BabelSynset>(candidates.get(tu));
        }
        try {

            List<String> names = NameDecomposition.nameDecompostion(tu, cm);
            for (String n : names) {
            //    logger.log(Level.INFO,"Name:"+n);

                List<BabelSynset> senses = bn.getSynsets(n, Language.IT, BabelPOS.NOUN, null, true, bsss);
                candidates.putAll(tu, senses);
                //  logger.log(Level.INFO,"Candidates:"+senses);
                if (senses != null && senses.size() > 0) {
                    break;
                }
            }
            return new ArrayList<BabelSynset>(candidates.get(tu));
        } catch (IOException ex) {
            Logger.getLogger(BabelNetCandidatesIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
 public List<BabelSynset> getOnFlyCandidates2(TwitterUser2 tu) {

       // if (candidates2 == null) {
            candidates2= HashMultimap.create();
        //}
        //if (candidates2.containsKey(tu)) {
        //    return new ArrayList<BabelSynset>(candidates2.get(tu));
        //}
        try {

            List<String> names = NameDecomposition.nameDecompostion2(tu, cm);
            for (String n : names) {
            //    logger.log(Level.INFO,"Name:"+n);

                List<BabelSynset> senses = bn.getSynsets(n, Language.IT, BabelPOS.NOUN, null, true, bsss);
                candidates2.putAll(tu, senses);
                //  logger.log(Level.INFO,"Candidates:"+senses);
                if (senses != null && senses.size() > 0) {
                    break;
                }
            }
            return new ArrayList<BabelSynset>(candidates2.get(tu));
        } catch (IOException ex) {
            Logger.getLogger(BabelNetCandidatesIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    @Override
    public Multimap<TwitterUser, BabelSynset> getOnFlyAllCandidates(TwitterPopulation tp) {
        HashMultimap<TwitterUser, BabelSynset> result = HashMultimap.create();
        for (TwitterUser tu : tp.getPopulation()) {
            result.putAll(tu, getOnFlyCandidates(tu));
        }
        return result;
    }

}
