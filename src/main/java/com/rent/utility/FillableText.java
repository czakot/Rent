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

    private static final String INSERT_MARKER = "?";
    
    public static String Fill(final String inputText, final String[] inputInserts) {
        
        if (inputText == null) {
            throw new IllegalArgumentException("'text' argument is null.");
        }
        if (inputInserts == null) {
            return inputText;
        }
        
        StringBuilder text = new StringBuilder(inputText);
        String[] inserts = inputInserts.clone();
        int expectedInsertIndex = 0; // expected index of inserts array
        int textPointer = getNextMarkerStartPointer(text, 0); // current char position for procession in text

        
        while (!isPointerOutOfText(text, textPointer)) {
            int markerEndPointer = getMarkerEndPointer(text, textPointer);
            if (textPointer == markerEndPointer + 1) {
                textPointer++;
                break;
            }
            int extractedInsertIndex = extractInsertIndex(text, textPointer, markerEndPointer);
            if (extractedInsertIndex != expectedInsertIndex) {
                throw new VerifyError(String.format("Extracted (%d) insert index does not match to expected (%d).", extractedInsertIndex, expectedInsertIndex));
            }
            textPointer = insert(text, textPointer - 1, inserts[expectedInsertIndex]);
            expectedInsertIndex++;
            textPointer = getNextMarkerStartPointer(text, textPointer);
        }
        
        return text.toString();
    }
    
    public static String FileFill(File file, String[] inserts) {
        StringBuilder sb = new StringBuilder();
        
        String inputText = "read in content of file";
        Fill(inputText, inserts);
        
        return sb.toString();
    }

    private static boolean isPointerOutOfText(StringBuilder text, int pointer) {
        return pointer < 0 || pointer >= text.length();
    }

    private static int getNextMarkerStartPointer(StringBuilder text, int fromPointer) {
        return text.indexOf(INSERT_MARKER, fromPointer);
    }

    private static int getMarkerEndPointer(StringBuilder text, int fromPointer) {
        int pointer = fromPointer + 1;
        StringBuilder extract = new StringBuilder();
        
        while (pointer != text.length() && "0123456789".contains(String.valueOf(text.charAt(pointer)))) {
            extract.append(text.charAt(pointer));
            pointer++;
        }
        
        return pointer;
    }

    private static int extractInsertIndex(StringBuilder text, int textPointer, int markerEndPointer) {
        int insertIndex = Integer.parseInt(text.substring(textPointer + 1, markerEndPointer));
        text.delete(textPointer, markerEndPointer);
        
        return insertIndex;
    }

    private static int insert(StringBuilder text, int pointer, String insertion) {
        text.insert(pointer, insertion);
        
        return pointer + insertion.length();
    }
}
