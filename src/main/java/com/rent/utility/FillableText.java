/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.utility;

import java.io.File;

/**
 *
 * @author czakot
 */
public class FillableText {
    
        private static String text;
        private static String[] inserts;
        private static StringBuilder sb;
        private static int textIdx;
        private static int insertIdx;

    public static String Fill(String inputText, String[] inputInserts) {
        if (text == null) {
            throw new IllegalArgumentException("'text' argument is null.");
        }
        
        text = inputText;
        inserts = inputInserts.clone();
        sb = new StringBuilder();
        textIdx = 0;
        insertIdx = 0;
        
        
        do {
            // find first '?' from textIdx
            // append substring from textIdx upto '?' to sb
            // get following digits up to any other character
            // does retrieved number equal insertIdx?
            // append insert to sb
            // update idxs
            
        } while (!isEndOfInputText());
        
        int charPointer = 0;
        while (charPointer != text.length()) {
            int nextPointer = text.indexOf(text, charPointer);
            if (nextPointer == -1) {
                nextPointer = text.length();
            }
        }
        
        return sb.toString();
    }
    
    public static String FileFill(File file, String[] inserts) {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }

    private static boolean isEndOfInputText() {
        return textIdx < 0 || textIdx >= text.length() || insertIdx == insert;
    }
}
