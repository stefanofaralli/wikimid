/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tagger.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author ditommaso
 */
public class JsoupRequest {
    
        public Document doRequest(String request) {
        Document doc = null;
        try {

            // need http protocol, set this as a Google bot agent
            doc = Jsoup
                    .connect(request)
                    .followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(10000).get();

            // get all links
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HttpStatusException e) {
            //e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return doc;
    }

}
