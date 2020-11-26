/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author czakot
 */
public class FillableText {

    private static final String INSERT_MARKER = "?";
    private static final Logger logger = LoggerFactory.getLogger(FillableText.class);
    
    public static String fill(String inputText, final String[] inputInserts) {
        
        if (inputText == null) {
            throw new IllegalArgumentException("'inputText' argument is null.");
        }
        if (inputInserts == null) {
            throw new IllegalArgumentException("'inputInserts' argument is null.");
        }
        
        StringBuilder text = new StringBuilder(inputText);
        String[] inserts = inputInserts.clone();
        int expectedInsertIndex = 0; // expected index of inserts array
        int textPointer = getNextMarkerStartPointer(text, 0); // current char position for procession in text

        
        while (!isPointerOutOfText(text, textPointer)) {
            int markerEndPointer = getMarkerEndPointer(text, textPointer);
            if (textPointer == markerEndPointer - 1) {
                textPointer++;
                continue;
            }
            int extractedInsertIndex = extractInsertIndex(text, textPointer, markerEndPointer);
            if (expectedInsertIndex >= inserts.length || expectedInsertIndex != extractedInsertIndex) {
                throw new VerifyError(String.format("Extracted (%d) insert index does not match to expected (%d).", extractedInsertIndex, expectedInsertIndex));
            }
            textPointer = insert(text, textPointer, inserts[expectedInsertIndex]);
            expectedInsertIndex++;
            textPointer = getNextMarkerStartPointer(text, textPointer);
        }
        if (expectedInsertIndex != inserts.length) {
                throw new VerifyError(String.format("Number of inserts (%d) does not match to expected (%d).", inserts.length, expectedInsertIndex));
        }
        
        return text.toString();
    }
    
    public static String fileFill(String fileAccessString, String[] inserts) {

        if (fileAccessString == null) {
            throw new IllegalArgumentException("'fileAccessString' argument is null.");
        }

        String inputText = null;
        try {
            inputText = Files.readString(Path.of(fileAccessString));
        } catch (IOException e) {
            logger.error(String.format("'%s' file could not be reached or its content read into a string", fileAccessString));
        }

        return fill(inputText, inserts);
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

    private static int insert(StringBuilder text, int pointer, String insert) {
        text.insert(pointer, insert);
        
        return pointer + insert.length();
    }
}
