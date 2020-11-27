package com.rent.utility;

import com.rent.exception.UnsuccessfulFileToStringConversion;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FillableTextTest {

    @Test
    void fill_textNull() {
        String txt = null;
        String[] inserts = {"zero", "one"};
        assertThrows(IllegalArgumentException.class, () -> FillableText.fill(txt, inserts));
    }

    @Test
    void fill_twoInnerSlot() {
        String txt = "aa?0bb ?1 cc";
        String[] inserts = {"zero", "one"};
        assertEquals("aazerobb one cc", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_mostLeftSlot() {
        String txt = "?0 aa";
        String[] inserts = {"zero"};
        assertEquals("zero aa", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_mostRightSlot() {
        String txt = "aa ?0";
        String[] inserts = {"zero"};
        assertEquals("aa zero", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_insertionIsMarkerToo() {
        String txt = "aa ?0 bb";
        String[] inserts = {"?1"};
        assertEquals("aa ?1 bb", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_twoInnerSlot_moreInserts() {
        String txt = "aa?0bb ?1 cc";
        String[] inserts = {"zero", "one", "two"};
        assertThrows(VerifyError.class, () -> FillableText.fill(txt, inserts));
    }

    @Test
    void fill_twoInnerSlot_lessInserts() {
        String txt = "aa?0bb ?1 cc";
        String[] inserts = {"zero"};
        assertThrows(VerifyError.class, () -> FillableText.fill(txt, inserts));
    }

    @Test
    void fill_innerPlainQuestionMarks() {
        String txt = "aa?bb ? cc ?0";
        String[] inserts = {"zero"};
        assertEquals("aa?bb ? cc zero", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_leftPlainQuestionMark() {
        String txt = "?aa?0bb";
        String[] inserts = {"zero"};
        assertEquals("?aazerobb", FillableText.fill(txt, inserts));
    }

    @Test
    void fill_rightPlainQuestionMark() {
        String txt = "aa?0bb?";
        String[] inserts = {"zero"};
        assertEquals("aazerobb?", FillableText.fill(txt, inserts));
    }

    @Test
    void fileFill_fileNameNull() {
        String fileName = null;
        String [] inserts = {"zero"};
        assertThrows(IllegalArgumentException.class, () -> FillableText.fileFill(fileName, inserts));
    }

    @Test
    void fileFill_fileNameNonExistent() {
        String fileName = "nonexistent.txt";
        String [] inserts = {"zero"};
        assertThrows(UnsuccessfulFileToStringConversion.class, () -> FillableText.fileFill(fileName, inserts));
    }

    @Test
    void fileFill_testFile() throws IOException {
        Path testFile = Path.of("fileFillTestFile.txt");
        Files.writeString(testFile, "aa?0bb ?1 cc");
        String [] inserts = {"zero", "one"};
        assertEquals("aazerobb one cc", FillableText.fileFill("fileFillTestFile.txt", inserts));
        Files.delete(testFile);
    }

}