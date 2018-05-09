/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.text;

import it.uniroma1.lcl.jlt.util.Files;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sfaralli
 */
public class Tokenizer {

    public static Set<String> stopwords = null;

    public static String tokenize(String t) {
        if (stopwords == null) {
            stopwords = new HashSet<String>();
            BufferedReader br;
            try {
                br = Files.getBufferedReader("resources/stopwords_en.txt");
                while (br.ready()) {
                    stopwords.add(br.readLine().trim());
                }
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        t = t.toLowerCase().trim();
        String s = t + "";
        s = s.replace(",", " ");
        s = s.replace(".", " ");
        s = s.replace("/", " ");
        s = s.replace(":", " ");
        s = s.replace(";", " ");
        s = s.replace("(", " ");
        s = s.replace(")", " ");
        s = s.replace("[", " ");
        s = s.replace("]", " ");
        s = s.replace("{", " ");
        s = s.replace("}", " ");
        s = s.replace("_", " ");
        s = s.replace("'", " ");
        s = s.replace("+", "");
        s = s.replace("-", "");
        s = s.replace("&", "");
        for (String sw : stopwords) {
            s = s.replace(" " + sw + " ", " ");
        }
        s = s.replace(" am ", " ");
        s = s.replace(" and ", " ");
        s = s.replace(" by ", " ");
        s = s.replace(" at ", " ");
        s = s.replace(" to ", " ");
        s = s.replace(" the ", " ");
        s = s.replace(" of", " ");
        s = s.replace(" or ", " ");
        s = s.replace(" in ", " ");
        s = s.replace(" a ", " ");
        s = s.replace(" for ", " ");
        s = s.replace(" is ", " ");
        s = s.replace(" i ", " ");
        s = s.replace(" I ", " ");
        s = s.replace(" m ", " ");
        s = s.replace(" new ", " ");

        s = s.replace(" it ", " ");
        s = s.replace(" was ", " ");
        s = s.replace(" are ", " ");
        s = s.replace(" not ", " ");
        s = s.replace(" one ", " ");
        s = s.replace(" an ", " ");
        s = s.replace(" other ", " ");

        return s.trim();

    }
}
