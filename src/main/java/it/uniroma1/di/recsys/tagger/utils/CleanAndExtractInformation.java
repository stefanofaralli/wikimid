/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.tagger.utils;

import it.uniroma1.di.recsys.datastructure.StringValues;
import it.uniroma1.di.recsys.tool.ManipulateURL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
/**
 *
 * @author ditommaso
 */
public abstract class CleanAndExtractInformation {

    private String srcPath;
    private String dstPath;



    public CleanAndExtractInformation(String srcPath, String dstPath) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
    }

    
    public abstract String clean(String str);

    public abstract boolean haveDone();

    public abstract boolean correctUrl(String long_url);


     //discared tweets without urls, with malformed urls or not platform's urls
    public void filterTweetsFile() throws FileNotFoundException, UnsupportedEncodingException, IOException, InterruptedException {
        System.out.println("filterTweetsFile ");
        String text = "";
        int count = 0;

        FileReader reader = new FileReader(new File(srcPath));
        BufferedReader in = new BufferedReader(reader);
        PrintWriter writer = new PrintWriter(dstPath, /*ISO-8859-1*/ "UTF-8");

        ArrayList<StringValues> tweets = new ArrayList<StringValues>();

        while ((text = in.readLine()) != null) {
            System.out.println(this.getClass().getName() + ": " + count);
            count++;
            String[] str = text.split("\t");

            if (str.length <= 2 || !str[1].contains("http")) {
                continue;
            }

            String short_url = ManipulateURL.getshortUrlFromText(str[1]);

            tweets.add(new StringValues(text, short_url));

            System.out.println("short: " + short_url);

        }
        System.out.println("finito");
        tweets = ManipulateURL.startThreadURLConnection(tweets);
        WritingAndFilterFile(writer, tweets);

        writer.flush();

        writer.close();

        reader.close();

    }

    private void WritingAndFilterFile(PrintWriter writer, ArrayList<StringValues> tweets) throws IOException {

        System.out.println("Writing File filtered");
        int count = 0;
        for (StringValues str : tweets) {

            if (this.correctUrl(str.value) == false) {
                continue;
            } else {
                String[] text = str.getIndex().split("\t");
                writer.println(text[0] + "\t"
                        + text[1] + "\t"
                        + text[2] + "\t"
                        + ManipulateURL.getshortUrlFromText(text[1]) + "\t"
                        + str.value + "\t"
                ); 

            }

            if (count >= 100) {
                count = 0;
                writer.flush();
            }
        }
    }
    
    

}
