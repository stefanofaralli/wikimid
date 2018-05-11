/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tool;

/**
 *
 * @author ditommaso
 */
import it.uniroma1.di.recsys.datastructure.StringValues;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManipulateURL implements Runnable {
    
    ArrayList<StringValues> tweets;
    int workers;
    int i;
    CountDownLatch cd;

    public ManipulateURL(ArrayList<StringValues> tweets, int i, int workers, CountDownLatch cd) {
        this.tweets = tweets;
        this.i = i;
        this.workers = workers;
        this.cd = cd;
    }

    public static String getshortUrlFromText(String str) throws IOException {
        return str.substring(str.indexOf("http")).replaceAll("\"", "").replaceAll(" #.*$", "").replaceAll(" .*$", "");
    }

    public void run() {
        for (int j = i; j < tweets.size(); j = j + workers) {
            try {
                tweets.get(j).value = getLongUrl(tweets.get(j).value);
            } catch (IOException ex) {
                Logger.getLogger(ManipulateURL.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        cd.countDown();
    }

    public static String getLongUrl(String shortenedUrl) throws IOException {
        System.out.println("expandUrl");

        String expandedURL;

        try {
            URL url = new URL(shortenedUrl);
            // open connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);

            // stop following browser redirect
            httpURLConnection.setInstanceFollowRedirects(false);

            // extract location header containing the actual destination URL
            expandedURL = httpURLConnection.getHeaderField("Location");
            httpURLConnection.disconnect();
        } catch (Exception ex) {
            return null;
        }

        System.out.println("long_url: " + expandedURL);

        return expandedURL;
    }

    public static ArrayList<StringValues> startThreadURLConnection(ArrayList<StringValues> tweets) throws InterruptedException {
        System.out.println("startThreadURLConnection");
        //DoubleValues[] tweets = new DoubleValues[100];
        int workers = 100;
        CountDownLatch cd = new CountDownLatch(workers);
        Thread[] t = new Thread[workers];

        for (int i = 0; i < workers; i++) {
            t[i] = new Thread(new ManipulateURL(tweets, i, workers, cd));
            t[i].start();
        }
        cd.await();

        return tweets;

    }
    
      public static void urlParse(String u) {
      try {
         URL url = new URL(u);
         
         System.out.println("URL is " + url.toString());
         System.out.println("protocol is " + url.getProtocol());
         System.out.println("authority is " + url.getAuthority());
         System.out.println("file name is " + url.getFile());
         System.out.println("host is " + url.getHost());
         System.out.println("path is " + url.getPath());
         System.out.println("port is " + url.getPort());
         System.out.println("default port is " + url.getDefaultPort());
         System.out.println("query is " + url.getQuery());
         System.out.println("ref is " + url.getRef());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

    public static void main(String[] args) throws IOException {
        //String result = LongURLFromShortURL.getLongUrl("https://goo.gl/jR1tiX");
        //String result = LongURLFromShortURL.getLongUrl("https://t.co/s8wm1Ow7Tz");
        //String result = ManipulateURL.getLongUrl("https://t.co/yrxGz8lb8E");
        //System.out.println(result);
        
        urlParse("https://www.nbc.com/the-tonight-show");
    }
}
