/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.data;

import it.uniroma1.lcl.jlt.util.IntegerCounter;

/**
 *
 * @author sfaralli
 */
public abstract class Context<T>
{
    public abstract IntegerCounter<String> getContext(T elem);
    public abstract IntegerCounter<String> getNeighborhoodContext(T elem);
}
    
