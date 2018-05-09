/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.lcl.wikimid.mapping.mapper.text;

import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser;
import it.uniroma1.lcl.wikimid.mapping.data.twitter.TwitterUser2;
import it.uniroma1.lcl.wikimid.mapping.mapper.data.CandidateMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 *
 * @author sfaralli
 */
public class NameDecomposition {

    public static List<String> nameDecompostion(TwitterUser tu, CandidateMethod cm) {
        List<String> result = new ArrayList<String>();
        switch (cm) {
            case NAME_BASIC:
                result.add(tu.getName());
                break;
            case SCREENAME_BASIC:
                result.add(tu.getScreen_name());
                break;
            case NAME_DECOMPOSITION:
                result.addAll(decompose(tu.getName()));
                break;
            case SCREENNAME_DECOMPOSITION:
                result.addAll(decompose(tu.getScreen_name()));
                break;
            default:
                break;
        }
        return result;
    }
    
 

    public static List<String> nameDecompostion2(TwitterUser2 tu, CandidateMethod cm) {
        List<String> result = new ArrayList<String>();
        switch (cm) {
            case NAME_BASIC:
                result.add(tu.getName());
                break;
            case SCREENAME_BASIC:
                result.add(tu.getScreen_name());
                break;
            case NAME_DECOMPOSITION:
                result.addAll(decompose(tu.getName()));
                break;
            case SCREENNAME_DECOMPOSITION:
                result.addAll(decompose(tu.getScreen_name()));
                break;
            default:
                break;
        }
        return result;
    }
    static Pattern p = Pattern.compile("(?<=.)(?=(\\p{Upper}))");

    private static List<String> decompose(String name) {
        List<String> result = new ArrayList<String>();

        String a_name = name.replace("_", " ").trim();
        result.add(a_name);
        String[] s1 = p.split(a_name);
        if (s1.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (String s : s1) {
                sb.append(s);
                sb.append(" ");
            }
            result.add(sb.toString().trim());
        } else {
            for (int i = 0; i < a_name.length(); i++) {
                String newname = "";
                if (i == 0) {
                    newname = a_name;
                } else {
                    newname = a_name.substring(0, i) + " " + a_name.substring(i);
                }
                result.add(newname);
            }
        }

        return result;
    }

    public static void main(String args[]) {

        System.out.println(decompose("stefanofaralli"));
        System.out.println(decompose("stefanoFaralli"));
        System.out.println(decompose("StefanoFaralli"));
    }
}
