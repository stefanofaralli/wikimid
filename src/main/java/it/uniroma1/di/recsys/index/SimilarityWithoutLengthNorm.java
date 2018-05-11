/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.index;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;

/**
 *
 * @author ditommaso
 */

public class SimilarityWithoutLengthNorm extends DefaultSimilarity{
    @Override
    public float lengthNorm(FieldInvertState state) {
        return 1;
    }
}