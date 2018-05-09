/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.data;


import com.google.common.collect.Multimap;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser;
import java.util.List;


/**
 *
 * @author sfaralli
 */
public abstract class Candidates<T> 
{
    public abstract List<T> getCandidates(TwitterUser tu);
    public abstract Multimap<TwitterUser,T> getAllCandidates(TwitterPopulation tp);
    public abstract List<T> getOnFlyCandidates(TwitterUser tu);
    public abstract Multimap<TwitterUser,T> getOnFlyAllCandidates(TwitterPopulation tp);
}
