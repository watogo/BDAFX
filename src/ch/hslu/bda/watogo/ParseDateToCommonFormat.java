/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

/**
 *
 * @author Niklaus
 */
public class ParseDateToCommonFormat {

    public String parseDate(String input) {
        String tmp = input.substring(30);
        int end = tmp.indexOf(")");
        tmp = tmp.substring(0, end);
        tmp = tmp.replace("(", "");
        tmp = tmp.replace(")", "");
        tmp = tmp.replace(" ", "");
        String[] tmp2 = tmp.split(",");

        for (int i = 0; i < tmp2.length; i++) {
            if (tmp2[i].length() <= 1) {
                tmp2[i] = "0" + tmp2[i];
            }
        }

        String result = tmp2[0] + "-" + tmp2[1] + "-" + tmp2[2] + " " + tmp2[3] + ":" + tmp2[4];

        return result;
    }

}
