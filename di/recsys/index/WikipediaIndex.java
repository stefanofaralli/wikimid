/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.di.recsys.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import static org.apache.lucene.util.Version.LUCENE_41;

/**
 *
 * @author ditommaso
 */
public class WikipediaIndex {

    private IndexWriter writer;
    private Analyzer analyzer;
    private String indexDir;
    private String lang;
    private Version lVersion;

    public WikipediaIndex(String lang, Version lVersion, String indexDir) throws IOException {
        this.lang = lang;
        this.indexDir = indexDir;
        this.lVersion = lVersion;

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

        //custom analyzer
        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        analyzerPerField.put("TITLE_UNSTEMMED", new StandardAnalyzer(lVersion, CharArraySet.EMPTY_SET));
        analyzerPerField.put("TITLE_NO_SPACE", new StandardAnalyzer(lVersion, CharArraySet.EMPTY_SET));

        PerFieldAnalyzerWrapper aWrapper = new PerFieldAnalyzerWrapper(analyzer, analyzerPerField);
        IndexWriterConfig cfg = new IndexWriterConfig(lVersion, aWrapper);

        //IndexWriterConfig cfg = new IndexWriterConfig(lVersion, analyzer);
        Directory index = new SimpleFSDirectory(new File(indexDir));
        MySimilarity similarity = new MySimilarity();
        cfg.setSimilarity(similarity);
        writer = new IndexWriter(index, cfg);
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    private void addDoc(IndexWriter w, String text) throws IOException {

        String input = text.substring(text.indexOf("<"), text.indexOf(">"));
        String[] elem = input.split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");
        String id = elem[1].substring(elem[1].indexOf("id=\"") + 4);
        System.out.println("id " + id);
        String urlid = elem[2].substring(elem[2].indexOf("url=\"") + 5);
        System.out.println("urlid " + urlid);
        String title = elem[3].substring(elem[3].indexOf("title=\"") + 7);
        String url = "https://" + lang + ".wikipedia.org/wiki/" + (title.replace(" ", "_"));
        String titleNoSpace = title.replace(" ", "").toLowerCase();
        System.out.println("url " + url);
        System.out.println("title " + title);
        System.out.println("title no space" + titleNoSpace);
        String[] v = text.split("\n\n");
        //System.out.println("glossl " + v.length);
        String gloss = "";
        if (v.length >= 2) {
            gloss = v[1];
            System.out.println("gloss " + gloss);
        }

        FieldType MY_FLD_TYPE = new FieldType();
        MY_FLD_TYPE.setIndexed(true);
        MY_FLD_TYPE.setTokenized(true);
        MY_FLD_TYPE.setStored(true);
        MY_FLD_TYPE.setStoreTermVectors(true);
        MY_FLD_TYPE.setStoreTermVectorPositions(true);
        MY_FLD_TYPE.freeze();

        Document doc = new Document();
        doc.add(new IntField("ID", Integer.parseInt(id), Field.Store.YES));//id
        doc.add(new StringField("URLID", urlid, Field.Store.YES)); //wikipedia url with id
        doc.add(new StringField("URL", url, Field.Store.YES)); //wikipedia url with title
        doc.add(new Field("TITLE", title, MY_FLD_TYPE)); //wikipedia title
        doc.add(new Field("TITLE_UNSTEMMED", title, MY_FLD_TYPE));  //wikipedia title unstemmed
        doc.add(new Field("TITLE_NO_SPACE", titleNoSpace, MY_FLD_TYPE)); //wikipedia title unstemmed without space
        doc.add(new Field("GLOSS", gloss, MY_FLD_TYPE)); //wikipedia gloss
        doc.add(new Field("TEXT", text, MY_FLD_TYPE)); //wikipedia text
        w.addDocument(doc);
    }

    //read parsed wikipedia file https://github.com/attardi/wikiextractor
    private void indexWikiDumpParsed(String srcPath) throws IOException {
        System.out.println("indexWikiDumpParsed");
        int count = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(srcPath)));
            String line;
            String text = "";
            try {
                while ((line = in.readLine()) != null) {
                    if (!line.contains("</doc>")) {
                        text += line + "\n";
                    } else {
                        System.out.println("indexing instance n: " + count);
                        addDoc(writer, text);
                        text = "";
                        count++;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writer.commit();
        writer.close();

        System.out.println("index done");
    }

    public static void main(String[] args) throws IOException {
        String text = "<doc id=\"2\" url=\"http://it.wikipedia.org/wiki/Celtic_music\">\n"
                + "Celtic music.\n"
                + "\n"
                + "Celtic music is a broad grouping of music genres that evolved out of the folk music traditions of the Celtic people of Western Europe.\n"
                + "It refers to both orally-transmitted traditional music and recorded music and the styles vary considerably to include everything from \"trad \" (traditional) music to a wide range of hybrids.\n"
                + "</doc>";

        Version lversion = LUCENE_41;

        //EN
        Analyzer analyzer = new EnglishAnalyzer(lversion);
        WikipediaIndex task = new WikipediaIndex("en", lversion, "./index/en/en_wiki_index");
        task.indexWikiDumpParsed("./Wikipedia_dump2017/en/parsed_en.xml");
    }
}
