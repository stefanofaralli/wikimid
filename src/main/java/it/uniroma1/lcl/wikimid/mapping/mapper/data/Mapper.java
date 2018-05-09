/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.data;

import it.uniroma1.lcl.jlt.util.DoubleCounter;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterPopulation;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser;
import java.util.Map;


/**
 *
 * @author sfaralli
 */
public abstract class Mapper<T> 
{
    public abstract DoubleCounter<T> map(TwitterUser tu);
    public abstract DoubleCounter<T> onFlyMap(TwitterUser tu);
    
    public abstract Map<TwitterUser,DoubleCounter<T>> map(TwitterPopulation tp);
    public abstract void onFlyMap(TwitterPopulation tp,String outputfile);
    public abstract void onFlyScreenname2Wiki(TwitterPopulation tp,String outputfile);
}
