/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import static org.apache.lucene.util.Version.LUCENE_41;

/**
 *
 * @author ditommaso
 */
public class WikipediaIndexQuery {

    private String[] additionalQueryTit;
    private String[] additionalQueryAuth;
    private IndexSearcher searcher;
    private IndexReader indexReader;
    private Analyzer analyzer;
    private String indexDir;
    private String lang;
    private Version lVersion;

    public WikipediaIndexQuery(String lang, Version lVersion, String indexDir, String[] additionalQueryTit, String[] additionalQueryAuth) throws IOException {
        this.lang = lang;
        this.lVersion = lVersion;
        this.indexDir = indexDir;
        this.additionalQueryTit = additionalQueryTit;
        this.additionalQueryAuth = additionalQueryAuth;

        switch (lang) {
            case "it":
                analyzer = new ItalianAnalyzer(lVersion, CharArraySet.EMPTY_SET);
                break;
            case "en":
                analyzer = new EnglishAnalyzer(lVersion, CharArraySet.EMPTY_SET);
                break;
            default:
                analyzer = new StandardAnalyzer(lVersion, CharArraySet.EMPTY_SET);
        }

        Directory dir = new SimpleFSDirectory(new File(indexDir));
        indexReader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(indexReader);
    }

    public WikipediaIndexQuery(Analyzer analyzer, Version lVersion, String indexDir, String[] additionalQueryTit, String[] additionalQueryAuth) throws IOException {
        this.analyzer = analyzer;
        this.lVersion = lVersion;
        this.indexDir = indexDir;
        this.additionalQueryTit = additionalQueryTit;
        this.additionalQueryAuth = additionalQueryAuth;

        Directory dir = new SimpleFSDirectory(new File(indexDir));
        indexReader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(indexReader);
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public String tagging(String query) throws IOException, ParseException {
        return tagging(query, 1);
    }

    public String tagging(String query, int numResults) throws IOException, ParseException {
        System.out.println("tagging" + query);
        String wp = "";
        String[] queryfield = query.split("\t");
        String[] qr = queryfield[0].replace(";", "\t").trim().split("\t");
        if (queryfield[2].equals("IMDb")) {

            if (qr[2].equals("movie")) {
                String[] x = {"film"};
                additionalQueryTit = x;

            } else if (qr[2].equals("tvSeries") || qr[2].equals("tvMiniSeries")) {
                if (lang.equals("en")) {
                    String[] x = {"series", "tv series"};
                    additionalQueryTit = x;
                } else if (lang.equals("it")) {
                    String[] x = {"serie", "serie televisiva"};
                    additionalQueryTit = x;
                }

            }

        }
        wp = sendOriginalQueryl(qr, numResults);
        System.out.println("title not found" + queryfield.length);
        if ((wp.equals("null") || wp.equals("")) && qr.length >= 2) { //controllo autore
            if (!queryfield[2].equals("IMDb") && !queryfield[2].equals("TvShowTime")) { //non mi interessa la pagina dell'anno
                wp = sendAuthorQueryl(qr, numResults);
            }
        }
        System.out.println("wp " + wp);

        return wp;

    }

    private String sendOriginalQueryl(String[] qr, int numResults) throws IOException {// il titolo nel titolo e autore e altro nella glossa
        String url = "";
        System.out.println("sendOriginalQueryl" + qr[0]);
        if (qr[0].equals("NA")) {
            return "";
        }

        if (qr[0].contains("(")) {
            qr[0] = qr[0].substring(0, qr[0].indexOf("("));
        }
        BooleanQuery query = new BooleanQuery();
        QueryParser queryParserTitle = new QueryParser(lVersion, "TITLE_UNSTEMMED", new StandardAnalyzer(lVersion, CharArraySet.EMPTY_SET)); //titolo nel titolo
        Query p;
        try {
            p = queryParserTitle.parse("\"" + qr[0] + "\""); //titolo

            p.setBoost((float) 70000.0);

            query.add(p, BooleanClause.Occur.MUST);

            if (qr.length >= 2 && !qr[1].equals("NA")) {
                queryParserTitle = new QueryParser(lVersion, "TITLE", analyzer);//autore nel titolo
                Query p0 = queryParserTitle.parse("\"" + qr[1] + "\"");
                p0.setBoost((float) 1000.0);
                query.add(p0, BooleanClause.Occur.SHOULD);
            }

            BooleanQuery bqt = new BooleanQuery();
            queryParserTitle = new QueryParser(lVersion, "TITLE", analyzer); //addizionali del titolo nel titolo
            for (String str : additionalQueryTit) {
                Query p1 = queryParserTitle.parse("\"" + str + "\"");
                p1.setBoost((float) 1000.0);
                System.out.println("ad " + p1);
                bqt.add(p1, BooleanClause.Occur.SHOULD);
            }
            bqt.setBoost((float) 1000.0);
            query.add(bqt, BooleanClause.Occur.SHOULD);

            QueryParser queryParserGloss = new QueryParser(lVersion, "GLOSS", analyzer); //autore nella glossa
            if (qr.length >= 2 && !qr[1].equals("NA")) { //tutte in OR
                Query p2 = queryParserGloss.parse("\"" + qr[1] + "\"");
                query.add(p2, BooleanClause.Occur.MUST);
            }

            queryParserGloss = new QueryParser(lVersion, "GLOSS", analyzer); //per imdb, tipo nella glossa
            if (qr.length >= 3 && !qr[2].equals("NA")) {
                Query p10 = queryParserGloss.parse("\"" + qr[2] + "\"");
                query.add(p10, BooleanClause.Occur.SHOULD);

            }
       
            BooleanQuery bqg = new BooleanQuery();
            queryParserGloss = new QueryParser(lVersion, "TEXT", analyzer); //addizionali del titolo nel testo
            for (String str : additionalQueryTit) {
                Query p3 = queryParserGloss.parse("\"" + str + "\"");
                p3.setBoost((float) 400.0);
                System.out.println("ad " + p3);
                bqg.add(p3, BooleanClause.Occur.SHOULD);
            }
            bqg.setBoost((float) 700.0);
            query.add(bqg, BooleanClause.Occur.SHOULD);

            TopDocs top = searcher.search(query, numResults); // perform a query and limit results number 

            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDocis a tuple) 
            Document doc = null;

            for (ScoreDoc entry : hits) { // load document in memory (only the stored filed are available) 
                doc = searcher.doc(entry.doc);
                System.out.println("score: " + entry.score);
                System.out.println("id :" + entry.doc);

                if (doc.get("GLOSS") != null && !doc.get("GLOSS").equals("") && !doc.get("GLOSS").equals("")) {
                    url = doc.get("URL");
                }
                System.out.println("ID: " + doc.get("ID"));
                System.out.println("URLID: " + doc.get("URLID"));
                System.out.println("URL: " + doc.get("URL"));
                System.out.println("TITLE: " + doc.get("TITLE"));
                System.out.println("GLOSS: " + doc.get("GLOSS"));
            }
        } catch (ParseException ex) {
            return url;
        }
        return url;
    }

    private String sendAuthorQueryl(String[] qr, int numResults) throws IOException {
        System.out.println("sendAuthorQueryl " + qr[1]);

        if (qr.length < 2 || qr[1].equals("NA")) {
            return "";
        }

        String url = "";
        BooleanQuery query = new BooleanQuery();

        QueryParser queryParserTitle = new QueryParser(lVersion, "TITLE_UNSTEMMED", new StandardAnalyzer(lVersion, CharArraySet.EMPTY_SET));//autore nel titolo
        Query p2;
        try {
            p2 = queryParserTitle.parse("\"" + qr[1] + "\"");

            p2.setBoost((float) 70000.0);
            query.add(p2, BooleanClause.Occur.MUST);
            System.out.println(qr[1]);

            BooleanQuery bqt = new BooleanQuery();
            queryParserTitle = new QueryParser(lVersion, "TITLE", analyzer);//addizionali dell'autore nel titolo
            for (String str : additionalQueryAuth) {
                Query p = queryParserTitle.parse("\"" + str + "\"");
                p.setBoost((float) 50000.0);
                System.out.println("ad " + p);
                bqt.add(p, BooleanClause.Occur.SHOULD);
            }
            bqt.setBoost((float) 50000.0);
            query.add(bqt, BooleanClause.Occur.SHOULD);

            BooleanQuery bqg = new BooleanQuery();
            QueryParser queryParserGloss = new QueryParser(lVersion, "TEXT", analyzer); //addizionali dell'autore nel testo
            for (String str : additionalQueryAuth) {
                Query p3 = queryParserGloss.parse("\"" + str + "\"");
                p3.setBoost((float) 200.0);
                System.out.println("ad " + p3);
                bqg.add(p3, BooleanClause.Occur.SHOULD);
            }
            bqg.setBoost((float) 400.0);
            query.add(bqg, BooleanClause.Occur.SHOULD);

            if (!qr[0].equals("NA")) {
                QueryParser queryParserText = new QueryParser(lVersion, "TEXT", analyzer); //titolo nel testo
                Query p = queryParserText.parse("\"" + qr[0] + "\""); //titolo
                p.setBoost((float) 200.0);
                query.add(p, BooleanClause.Occur.SHOULD);
            }

            TopDocs top = searcher.search(query, numResults); // perform a query and limit results number 
            ScoreDoc[] hits = top.scoreDocs; // get only the scored documents (ScoreDocis a tuple) 
            Document doc = null;

            for (ScoreDoc entry : hits) { // load document in memory (only the stored filed are available) 
                doc = searcher.doc(entry.doc);
                searcher.explain(query, entry.doc);
                System.out.println("score :" + entry.score);
                System.out.println("id :" + entry.doc);

                if (entry.score < 0.00009) {
                    System.out.println("score " + entry.score + " " + "minore");
                    return "";
                }
                System.out.println("score " + entry.score + " " + "maggiore");

                /* the same as ir.document(entry.doc); */
                if (doc.get("GLOSS") != null && !doc.get("GLOSS").equals("") && !doc.get("GLOSS").equals(" ")) {
                    url = doc.get("URL");
                }
                System.out.println("ID: " + doc.get("ID"));
                System.out.println("URLID: " + doc.get("URLID"));
                System.out.println("URL: " + doc.get("URL"));
                System.out.println("TITLE: " + doc.get("TITLE"));
                System.out.println("GLOSS: " + doc.get("GLOSS"));
                //System.out.println("TEXT: " + doc.get("TEXT"));
            }
        } catch (ParseException ex) {
            Logger.getLogger(WikipediaIndexQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    public void getWikiPageWithLucene(String srcPath, String dstPath, String lang) throws IOException, InterruptedException, ParseException {
        FileReader reader = new FileReader(new File(srcPath));
        BufferedReader in = new BufferedReader(reader);
        PrintWriter writer = new PrintWriter(dstPath, /*ISO-8859-1*/ "UTF-8");
        String text = "";

        int flag = 1;
        while ((text = in.readLine()) != null) {
            if (text != null && !text.equals("")) {//get data of interests
                String wiki_link = tagging(text); //ottieni pagina wiki
                String wiki_page = "";

                if (wiki_link != null && wiki_link.length() > 0) {
                    wiki_page = "WIKI:" + lang.toUpperCase() + ":" + wiki_link.substring(wiki_link.indexOf("/wiki/") + 6);
                }

                flag++;
                System.out.println("wiki_page: " + wiki_page);

                //String[] s = text.split("\t");
                writer.print(text + "\t" + wiki_page);
                writer.println();

                if (flag >= 100) {
                    writer.flush();
                    flag = 1;

                }

            }
        }

        writer.flush();
        writer.close();

        reader.close();
        indexReader.close();
    }


    public static void main(String[] args) throws Exception {

        Version lVersion = LUCENE_41;

//ENGLISH associa wikipage        
        String lang = "en";
        
        String[] additionalQueryTit= {"song"};
        String[] additionalQueryAuth= {"singer", "band", "artist", "songwriter", "composer", "musician", "record producer"};
        WikipediaIndexQuery wti = new WikipediaIndexQuery(lang,lVersion, "./index/en/en_wiki_index", additionalQueryTit, additionalQueryAuth);
        wti.getWikiPageWithLucene("./data/spotify_interests.tsv", "./data/spotify_interests_wikimapped.tsv", lang);

//        String[] additionalQueryTit = {"books", "novel", "saga"};
//        String[] additionalQueryAuth = {"writer", "novelist", "cartoonist", "journalist", "orator", "poet", "Japanese manga author"};
//        WikipediaIndexQuery wti = new WikipediaIndexQuery(lang, lVersion, "./index/en/en_wiki_index", additionalQueryTit, additionalQueryAuth);
//        wti.getWikiPageWithLucene("./data/goodreads_interests.tsv", "./data/goodreads_interests_wikimapped.tsv", lang);

//        String[] additionalQueryTit = {"film", "series", "TV series", "episode"};
//        String[] additionalQueryAuth = {};
//        WikipediaIndexQuery wti = new WikipediaIndexQuery(lang, lVersion, "./index/en/en_wiki_index", additionalQueryTit, additionalQueryAuth); //nel paper usata cartella v1 per imdb
//        wti.getWikiPageWithLucene("./data/imdb_interests.tsv", "./data/imdb_interests_wikimapped.tsv", lang);
    }

}
