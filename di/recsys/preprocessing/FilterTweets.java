/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.preprocessing;

import it.uniroma1.di.recsys.tagger.utils.Spotify;


/**
 *
 * @author ditommaso
 */
public abstract class FilterTweets implements Runnable {

    private String srcPath;
    private String dstPath;

    public FilterTweets(String srcPath, String dstPath) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }

    public abstract boolean controlUrl(String long_url);

    public static void main(String[] args) throws Exception {
        //ENGLISH
        Spotify se = new Spotify("./data/spotify_tweets.tsv", "./data/spotify_tweets_filtered.tsv", -1);
        se.filterTweetsFile();

//        GoodReads gre = new GoodReads("./data/goodreads_tweets.tsv", "./data/goodreads_tweets_filtered.tsv", -1);
//        gre.filterTweetsFile();
//
//        IMDb imdbe = new IMDb("./data/imdb_tweets.tsv", "./data/imdb_tweets_filtered.tsv", -1);
//        imdbe.filterTweetsFile();
    }

}
