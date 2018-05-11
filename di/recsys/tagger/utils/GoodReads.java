/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tagger.utils;


import it.uniroma1.di.recsys.tool.ManipulateURL;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author ditommaso
 */
public class GoodReads extends CleanAndExtractInformation {

    int count = 0;
    int limits = 0;


    public GoodReads(String srcPath, String dstPath, int limits) {
        super(srcPath, dstPath);
        this.limits = limits;
    }

    //NOT IMPLEMENTED
    public String clean(String txt) {
        count++;
        return txt;
    }

    public boolean correctUrl(String long_url) {
        if (long_url == null || (!long_url.contains("goodreads.com"))) {
            System.out.println("null");
            return false;
        }
        return true;
    }

    @Override
    public boolean haveDone() {
        if (limits == -1) {
            return false;
        }
        return count >= limits;
    }

}
