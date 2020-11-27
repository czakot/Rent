package com.rent.exception;

public class UnsuccessfulFileToStringConversion extends RuntimeException {

    public UnsuccessfulFileToStringConversion(String fileAccessString) {
        super(String.format("'%s' file could not be reached or its content could not be read into a string.", fileAccessString));
    }
}
