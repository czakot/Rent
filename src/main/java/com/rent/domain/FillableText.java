/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.domain;

import java.io.File;

/**
 *
 * @author czakot
 */
public class FillableText {
    
    public static String Fill(String text, String[] inserts) {
        if (text == null) {
            throw new IllegalArgumentException("'text' argument is null.");
        }
        
        StringBuilder sb = new StringBuilder();
        
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
}
