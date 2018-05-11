/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tagger.utils;

import it.uniroma1.di.recsys.tool.ManipulateURL;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ditommaso
 */
public class Spotify extends CleanAndExtractInformation {

    int count = 0;
    int limits = 0;


    public Spotify(String srcPath, String dstPath, int limits) {
        super(srcPath, dstPath);
        this.limits = limits;
    }


    public String clean(String txt) {
        if (!txt.startsWith("NowPlaying")) {
            return null;
        }
        count++;
        String st = txt.replace("NowPlaying ", "").replaceAll(" http.*$", "");
        //System.out.println(st);
        return st;

    }

    @Override
    public boolean correctUrl(String long_url) {
        if (long_url == null || (!long_url.contains("spotify.com") && !long_url.contains("spoti.fi"))) {
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
