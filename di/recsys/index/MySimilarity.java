/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.index;

import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.search.similarities.Similarity;

/**
 *
 * @author ditommaso
 */
public class MySimilarity extends PerFieldSimilarityWrapper {
    Similarity standardSim = new DefaultSimilarity();
    Similarity nolengthSim = new SimilarityWithoutLengthNorm();

    @Override
    public Similarity get(String fieldName) {
        if (fieldName.equals("TITLE") || fieldName.equals("TITLE_UNSTEMMED") ) {
            return standardSim;
        }
        else {
            
            return nolengthSim;
        }
    }

    //These two methods must be implemented here, as their
    //calculation is not field specific
    @Override
    public float queryNorm (float valueForNormalization) {
        return standardSim.queryNorm(valueForNormalization);
    }

    @Override
    public float coord (int overlap, int maxOverlap) {
        return standardSim.coord(overlap, maxOverlap);
    }
}
