/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.datastructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ditommaso
 */
public class StringValues {

    private static final Logger logger = LogManager.getLogger(StringValues.class);

    public String index;
    public String info;
    public String value;

    public StringValues() {

    }

    public StringValues(String index, String value) {
        this.index = index;
        this.value = value;
    }

    public StringValues(String index, String info, String value) {
        this.index = index;
        this.info = info;
        this.value = value;
    }

    public String toString() {
        return index + "," + value;
    }

    public String getIndex() {
        return this.index;
    }

}
