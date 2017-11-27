package com.kingmanzhang.ProjectII;

import edu.princeton.cs.algs4.RandomSeq;
import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SeamCarverTest {

    private Picture test;

    @Test
    void picture() {

        Picture picture = null;
        assertThrows(IllegalArgumentException.class, () -> {
            SeamCarver seamCarver = new SeamCarver(picture);
        });

        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");

    }

    @Test
    void width() {
        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");
        assertEquals(20, pattern_small.width());
        Picture pattern_Lee = new Picture("src/test/resources/ProjectII/pattern-Lee.png");
        assertEquals(20, pattern_Lee.width());
        Picture pattern_Law = new Picture("src/test/resources/ProjectII/pattern-Law.png");
        assertEquals(50, pattern_Law.width());

    }

    @Test
    void height() {

        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");
        assertEquals(30, pattern_small.height());
        Picture pattern_Lee = new Picture("src/test/resources/ProjectII/pattern-Lee.png");
        assertEquals(30, pattern_Lee.height());
        Picture pattern_Law = new Picture("src/test/resources/ProjectII/pattern-Law.png");
        assertEquals(100, pattern_Law.height());
    }

    @Test
    void energy() {
        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");
        SeamCarver seamCarver = new SeamCarver(pattern_small);
        //dimention: width 20; height 30
        //calculate all
        for (int i = 0; i < seamCarver.height(); i++) {
            for (int j = 0; j < seamCarver.width(); j++) {
                StdOut.printf("Pixel [col: %d, row: %d, energy: %f] \n", j, i, seamCarver.energy(j, i));
            }
        }

    }

    @Test
    void findHorizontalSeam() {
        Picture pattern_Lee = new Picture("src/test/resources/ProjectII/pattern-Lee.png");
        SeamCarver seamCarver = new SeamCarver(pattern_Lee);
        int[] hSeam = seamCarver.findHorizontalSeam();
        for (int i = 0; i < hSeam.length; i++) {
            StdOut.printf("Col %d: \t\t pixel[%d, %d]\n", i, i, hSeam[i]);
        }


    }

    @Test
    void findVerticalSeam() {
        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");
        SeamCarver seamCarver = new SeamCarver(pattern_small);
        int[] vSeam = seamCarver.findVerticalSeam();
        for (int i = 0; i < vSeam.length; i++) {
            StdOut.printf("row %d: \t\t pixel[%d, %d]\n", i, vSeam[i], i);
        }

    }

    @Test
    void removeHorizontalSeam() {

        Picture pattern_ocean = new Picture("src/test/resources/ProjectII/HJoceanSmall.png");
        SeamCarver seamCarver = new SeamCarver(pattern_ocean);
        int original_height = seamCarver.height();
        for (int i = 0; i < 100; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
            pattern_ocean = seamCarver.picture();
            seamCarver = new SeamCarver(pattern_ocean);
        }
        assertEquals(original_height - 100, seamCarver.height());
        seamCarver.picture().save("src/test/resources/ProjectII/pattern-ocean_h100.png");


    }

    @Test
    void removeVerticalSeam() {
        /**
        Picture pattern_small = new Picture("src/test/resources/ProjectII/pattern-small.png");
        SeamCarver seamCarver = new SeamCarver(pattern_small);
        int original_width = seamCarver.width();
        seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        assertEquals(original_width - 1, seamCarver.width());
        seamCarver.picture().save("src/test/resources/ProjectII/pattern-small_1.png");

        Picture pattern_Law = new Picture("src/test/resources/ProjectII/pattern-Law.png");
        seamCarver = new SeamCarver(pattern_Law);
        original_width = seamCarver.width();
        seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        assertEquals(original_width - 1, seamCarver.width());
        seamCarver.picture().save("src/test/resources/ProjectII/pattern-Law_1.png");

**/


        Picture pattern_ocean = new Picture("src/test/resources/ProjectII/HJoceanSmall.png");
        SeamCarver seamCarver = new SeamCarver(pattern_ocean);
        int original_width = seamCarver.width();
        for (int i = 0; i < 50; i++) {
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
            pattern_ocean = seamCarver.picture();
            seamCarver = new SeamCarver(pattern_ocean);
        }
        assertEquals(original_width - 50, seamCarver.width());
        seamCarver.picture().save("src/test/resources/ProjectII/pattern-ocean_50.png");

    }

}