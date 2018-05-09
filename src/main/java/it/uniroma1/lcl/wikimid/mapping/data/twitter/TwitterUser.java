/*
* WikiMID
* Giorgia Di Tommaso, Stefano Faralli, Giovanni Stilo, Paola Velardi
*
* 
* Project and Resources:
*  http://wikimid.tweets.di.uniroma1.it/wikimid/
*  https://figshare.com/articles/Wiki-MID_Dataset_LOD_TSV_/6231326/1
*  https://github.com/stefanofaralli/wikimid
* License  
*  https://creativecommons.org/licenses/by/4.0/
*
*  This is part of the pipiline used for the contruction of the WikiMID resource
*  There are several aspects of the project (source and documentation) we are improving. 
*  
 */
package it.uniroma1.lcl.wikimid.mapping.data.twitter;

import it.uniroma1.lcl.jlt.util.IntegerCounter;
import it.uniroma1.lcl.wikimid.mapping.mapper.text.Tokenizer;

public class TwitterUser {

    private String twitterID;
    private String screen_name;
    private String name;
    private String location;
    private String statusline;
    private String language;
    private IntegerCounter<String> context = null;

    public static TwitterUser fromLine(String line, boolean verifiedorpopular) {
        try {
            String[] lines = line.split("\t");
            String twitterID = lines[0];
            String screen_name = lines[1];
            String name = lines[2];
            String location = lines[3];
            String statusline = lines[4];
            String language = lines[7];
            if (language.equals("en")
                    && (!verifiedorpopular || (verifiedorpopular && (lines[9].equals("true") || lines[11].equals("1"))))) {
                return new TwitterUser(twitterID, screen_name, name, location, statusline, language);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public TwitterUser(String twitterID, String screen_name, String name, String location, String statusline, String language) {
        this.twitterID = twitterID;
        this.screen_name = screen_name;
        this.name = name;
        this.location = location;
        this.statusline = statusline;
        this.language = language;
    }

    /**
     * @return the twitterID
     */
    public String getTwitterID() {
        return twitterID;
    }

    /**
     * @return the screen_name
     */
    public String getScreen_name() {
        return screen_name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the statusline
     */
    public String getStatusline() {
        return statusline;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    public IntegerCounter<String> getContext() {
        if (context == null) {
            context = new IntegerCounter<String>();

            for (String s : Tokenizer.tokenize(statusline.toLowerCase()).split(" ")) {
                if (!s.trim().isEmpty()) {
                    context.count(s);
                }
            }
            for (String s : Tokenizer.tokenize(location.toLowerCase()).split(" ")) {
                if (!s.trim().isEmpty()) {
                    context.count(s);
                }
            }
            for (String s : Tokenizer.tokenize(name.toLowerCase()).split(" ")) {
                if (!s.trim().isEmpty()) {
                    context.count(s);
                }
            }
            for (String s : Tokenizer.tokenize(screen_name.toLowerCase()).split(" ")) {
                if (!s.trim().isEmpty()) {
                    context.count(s);
                }
            }
        }
        return context;
    }

    public String getAsText() {
        String text = name + " " + screen_name + " " + statusline + " " + location;
        return text;
    }
}
