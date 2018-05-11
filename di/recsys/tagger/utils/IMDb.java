/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tagger.utils;

import it.uniroma1.di.recsys.tool.ManipulateURL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author ditommaso
 */
public class IMDb extends CleanAndExtractInformation {

    int count = 0;
    int limits = 0;


    public IMDb(String srcPath, String dstPath, int limits) {
        super(srcPath, dstPath);
        this.limits = limits;
    }

    //NOT IMPLEMENTED
    public String clean(String txt) {
        count++;
        return txt;
    }

    public boolean correctUrl(String long_url) {
        if (long_url == null || (!long_url.contains("imdb.com"))) {
            //System.out.println("null");
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

    public static void createIMDbTitleMapFromDb() throws FileNotFoundException, IOException {
        System.out.println("createMapFromDb");
        DB db = DBMaker.fileDB("./map/imdb_title_map_from_db").make();
        ConcurrentMap imdbInterest = db.hashMap("imdbInterest").create();
        FileReader reader = new FileReader(new File("./map/imdb_dataset/title.basics.tsv"));
        BufferedReader in = new BufferedReader(reader);

        String text;
        while ((text = in.readLine()) != null) {
            String[] elem = text.split("\t");
            String value = elem[2] + "*" + elem[5] + "*" + elem[1] + "*" + elem[3] + "*" + elem[8]; //primaryTitle, startYear, titleType, originalTitle, genres
            System.out.println(elem[2] + "*" + elem[5] + "*" + elem[1] + "*" + elem[3] + "*" + elem[8]);
            imdbInterest.put(elem[0], value);
        }

        reader.close();

        db.close();

    }

    public static void createIMDbEpisodeMapFromDb() throws FileNotFoundException, IOException {
        System.out.println("createMapFromDb");
        DB db = DBMaker.fileDB("./map/imdb_episode_map_from_db").make();
        ConcurrentMap imdbEpisode = db.hashMap("imdbEpisode").create();
        FileReader reader = new FileReader(new File("./map/imdb_dataset/title.episode.tsv"));
        BufferedReader in = new BufferedReader(reader);

        String text;
        while ((text = in.readLine()) != null) {
            String[] elem = text.split("\t");
            System.out.println(elem[0] + "*" + elem[1]);
            imdbEpisode.put(elem[0], elem[1]); //id episode, id series
        }

        reader.close();

        db.close();

    }

    public static void main(String[] args) throws Exception {
        //createIMDbTitleMapFromDb();
        //createIMDbEpisodeMapFromDb();

    }

}
