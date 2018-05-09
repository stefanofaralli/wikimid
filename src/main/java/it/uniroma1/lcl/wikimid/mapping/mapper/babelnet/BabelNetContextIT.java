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

package it.uniroma1.lcl.wikimid.mapping.mapper.babelnet;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;
import it.uniroma1.lcl.babelnet.data.BabelGloss;
import it.uniroma1.lcl.jlt.util.IntegerCounter;
import it.uniroma1.lcl.jlt.util.Language;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.Context;
import it.uniroma1.lcl.wikimid.mapping.mapper.text.Tokenizer;

public class BabelNetContextIT extends Context<BabelSynset> {

    public static BabelNet bbn = BabelNet.getInstance();
    public static Map<BabelSynset, IntegerCounter<String>> context = null;

    @Override
    public IntegerCounter<String> getContext(BabelSynset elem) {
        if (context == null) {
            context = new HashMap<BabelSynset, IntegerCounter<String>>();
        }
        if (context.containsKey(elem)) {
            return context.get(elem);
        }
        IntegerCounter<String> result = new IntegerCounter<String>();

        try {
            for (BabelGloss s : elem.getGlosses(Language.IT)) {
                String[] tokenized = Tokenizer.tokenize(s.getGloss()).split(" ");
                for (String t : tokenized) {
                    if (!t.trim().isEmpty()) {
                        result.count(t);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.put(elem, result);
        return result;
    }

    @Override
    public IntegerCounter<String> getNeighborhoodContext(BabelSynset elem) {
        IntegerCounter<String> result = new IntegerCounter<String>();
        result.addFrom(getContext(elem));
        int count = 0;
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.ALSO_SEE)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.GLOSS_DISAMBIGUATED)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.DERIVATIONALLY_RELATED)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.GLOSS_MONOSEMOUS)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.ANY_HYPERNYM)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.ANY_MERONYM)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.TOPIC)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
                if (count % 35 == 0) {
                    System.gc();
                }
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.TOPIC_MEMBER)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (BabelSynsetIDRelation edge : elem.getEdges(BabelPointer.ANY_HOLONYM)) {
            count++;
            try {
                result.addFrom(getContext(bbn.getSynset(edge.getBabelSynsetIDTarget())));
            } catch (IOException ex) {
                Logger.getLogger(BabelNetContextIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
