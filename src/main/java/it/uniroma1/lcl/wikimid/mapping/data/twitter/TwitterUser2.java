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

import java.util.Set;

public class TwitterUser2 {

    private String twitterID;
    private String screen_name;
    private String name;
    private String location;
    private String statusline;
    private String url;

    private Long followersCount;

    private String language;
    private IntegerCounter<String> context = null;

    public static TwitterUser2 fromLine(String line, boolean onlyverified, Set<String> onlanguages, Long minfollowers) {
        try {
            String[] lines = line.split("\t");
            String twitterID = lines[0];
            String screen_name = lines[1];
            String name = lines[2];
            String location = lines[3];
            String statusline = lines[4];
            String urls = lines[5];
            String verifieds = lines[6];
            String followersCounts = lines[7];
            String languages = lines[12];
            if (new Long(followersCounts) >= minfollowers) {
                if ((onlanguages == null || onlanguages.contains(languages))
                        && (!onlyverified || (onlyverified && (verifieds.equals("true"))))) {
                    return new TwitterUser2(twitterID, screen_name, name, location, statusline, urls, followersCounts, languages);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public TwitterUser2(String twitterID, String screen_name, String name, String location, String statusline, String urls, String followersCounts, String language) {
        this.twitterID = twitterID;
        this.screen_name = screen_name;
        this.name = name;
        this.location = location;
        this.statusline = statusline;
        this.url = urls;
        this.followersCount = new Long(followersCounts);
        this.language = language;
    }

    public Long getFolloweersCounts() {
        return followersCount;
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

    public String getUrl() {
        return url;
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
