package com.kingmanzhang.ProjectII;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;
    private double[][] distTo; //store distance
    private int[][] edgeTo; //store edge
    private double [][] energy; //store energy

    /**
     * create a seam carver object based on the given picture
     *
     * @param picture
     */
    public SeamCarver(Picture picture) {

        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.distTo = new double[this.width][this.height];
        this.edgeTo = new int[this.width][this.height];
        this.energy = new double[this.width][this.height];
        populateEnergyTable(this.energy);
/**
        for (int i = 0; i < this.height; i++) {        //initialize energy 2D array
            for (int j = 0; j < this.width; j++) {
                Pixel pixel = new Pixel(j, i);
                energy[j][i] = pixel.energy();
            }
        }
 **/
    }

    private void populateEnergyTable (double[][] energy) {
        for (int i = 0; i < this.height; i++) {        //initialize energy 2D array
            for (int j = 0; j < this.width; j++) {
                Pixel pixel = new Pixel(j, i);
                energy[j][i] = pixel.energy();
            }
        }
    }


    /**
     * current picture
     *
     * @return
     */
    public Picture picture() {

        return new Picture(this.picture);

    }

    /**
     * width of current picture
     *
     * @return
     */
    public int width() {

        return this.width;
    }

    /**
     * height of current picture
     *
     * @return
     */
    public int height() {

        return this.height;

    }

    /**
     * energy of pixel at column x and row y
     *
     * @param x
     * @param y
     * @return
     */
    public double energy(int x, int y) {

        validate(x, y);
        return energy[x][y];

    }


    /**
     * sequence of indices for horizontal seam
     *
     * @return
     */
    public int[] findHorizontalSeam() {

        for (int i = 0; i < this.height; i++) {
            edgeTo[0][i] = i;
            distTo[0][i] = 0.0; //first column: disTo set to 0
        }

        for (int i = 1; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                edgeTo[i][j] = -1;
                distTo[i][j] = Double.MAX_VALUE; //Other columns: distTo set to positive infinity
            }
        }

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Pixel pixel = new Pixel(i, j);
                if (pixel.hAdj() != null) {
                    for (Pixel v : pixel.hAdj()) {
 // StdOut.printf("width: %d, height: %d, i: %d, j: %d, v.x: %d, v.y: %d", this.width, this.height, i, j, v.x, v.y);
                        if (this.distTo[v.x][v.y] > this.distTo[i][j] + energy[i][j]) {
                            this.distTo[v.x][v.y] = this.distTo[i][j] + energy[i][j];
                            this.edgeTo[v.x][v.y] = j; //store row of pixel coming to it
                        }
                    }
                }
            }
        }

        return hSP();
    }


    private int[] hSP() {
        int target = -1;
        double shortest = Double.MAX_VALUE;
        for (int i = 0; i < this.height; i++) {
//StdOut.println(distTo[i][this.height -1]);
            if (distTo[this.width - 1][i] < shortest) {
                target = i;
                shortest = distTo[this.width - 1][i];
            }
        }

        int[] path = new int[this.width];
        for (int i = this.width - 1; i >= 0; i--) {
//StdOut.printf("i is: %d, target is: %d \n", i, target);
            path[i] = target;
            target = edgeTo[i][target];
        }

        return path;
    }

    /**
     * sequence of indices for vertical seam
     *
     * @return
     */
    public int[] findVerticalSeam() {

        for (int i = 0; i < this.width; i++) {
            edgeTo[i][0] = i;
            distTo[i][0] = 0.0; //first row: distTo set to 0;
        }

        for (int i = 1; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                edgeTo[j][i] = -1;
                distTo[j][i] = Double.MAX_VALUE;
            }
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Pixel pixel = new Pixel(j, i);
                if (pixel.vAdj() != null) {
                    for (Pixel v : pixel.vAdj()) {
                        if (this.distTo[v.x][v.y] > this.distTo[j][i] + energy[j][i]) {
                            this.distTo[v.x][v.y] = this.distTo[j][i] + energy[j][i];
                            this.edgeTo[v.x][v.y] = j;   //stores colume of pixel coming to it
                        }
                    }
                }
            }
        }

        return vSp();
    }

    private int[] vSp() {
        int target = -1;
        double shortest = Double.MAX_VALUE;
        for (int i = 0; i < this.width; i++) {
//StdOut.println(distTo[i][this.height -1]);
            if (distTo[i][this.height - 1] < shortest) {
                target = i;
                shortest = distTo[i][this.height - 1];
            }
        }

        int[] path = new int[this.height];
        for (int i = this.height - 1; i >= 0; i--) {
//StdOut.printf("i is: %d, target is: %d \n", i, target);
            path[i] = target;
            target = edgeTo[target][i];
        }

        return path;
    }

    /**
     * remove horizontal seam from current picture
     *
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {

        if (this.height <= 1) {
            throw new IllegalArgumentException();
        }

        validateHSeam(seam);


        Picture newPicture = new Picture(this.width, this.height - 1);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < seam[i]; j++) {
                newPicture.set(i, j, this.picture.get(i, j));
            }
            for (int j = seam[i]; j < this.height - 1; j++) {
                newPicture.set(i, j, this.picture.get(i, j + 1));
            }
        }
        this.picture = newPicture;
        this.height = this.picture.height(); //update height
        //afterRemoveHSeam(seam);  //update energy table
        populateEnergyTable(this.energy); //TODO: use a more efficient way.

    }

    private void afterRemoveHSeam(int[] seam){ //update after removing a seam

        double [][] newEnergy = new double[this.picture.width()][this.picture.height()];

        for (int i = 0; i < this.width; i++) { //copy energy table to new energy array
            System.arraycopy(energy[i], 0, newEnergy[i], 0, seam[i]); //TODO: WRONG way of copying. use for().
            if(seam[i] < this.height) {
//  StdOut.printf("picture size: col: %d, row: %d \n", this.width, this.height);
// StdOut.printf("seam[%d]: %d, this.height: %d", i, seam[i], this.height);

                System.arraycopy(energy[i], seam[i] + 1, newEnergy[i], seam[i], this.width - seam[i]);
            }
            if(seam[i] == 0) {
                newEnergy[i][0] = new Pixel(i, 0).energy();
            } else if (seam[i] == this.height) {
                newEnergy[i][this.height - 1] = new Pixel(i, this.height - 1).energy();
            } else {
                newEnergy[i][seam[i] - 1] = new Pixel(seam[i] - 1, i).energy(); //recalculate energy for pixels along seams
                newEnergy[i][seam[i]] = new Pixel(seam[i], i).energy();
            }
        }

        this.energy = newEnergy;
    }

    /**
     * remove vertical seam from current picture
     *
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {

        if (this.width <= 1) {
            throw new IllegalArgumentException();
        }

        validateVSeam(seam);

        Picture newPicture = new Picture(this.width - 1, this.height);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < seam[i]; j++) {
                newPicture.set(j, i, this.picture.get(j, i));
            }
            for (int j = seam[i]; j < this.width - 1; j++) {
                newPicture.set(j, i, this.picture.get(j + 1, i));
            }
        }
        this.picture = newPicture;
        this.width = this.picture.width(); //update width
        //afterRemmoveVSeam(seam);//update energy table
        populateEnergyTable(this.energy);//TODO: use a more efficient way.
    }

    private void afterRemmoveVSeam(int[] seam) {

        double [][] newEnergy = new double[this.picture.width()][this.picture.height()];

        for (int i = 0; i < this.height; i++) { //copy energy table to new energy array
            System.arraycopy(energy[i], 0, newEnergy[i], 0, seam[i]);
            if (seam[i] < this.width) {
 StdOut.printf("seam[%d]: %d; picture dimention: col: %d, row: %d", i, seam[i], this.width, this.height);
                System.arraycopy(energy[i], seam[i] + 1, newEnergy[i], seam[i], this.width - seam[i]);
            }
            if (seam[i] == 0) {
                newEnergy[0][i] = new Pixel(0, i).energy();
            } else if (seam[i] == this.width) {
                newEnergy[this.width - 1][i] = new Pixel(this.width -1, i).energy();
            } else {
                newEnergy[seam[i] - 1][i] = new Pixel(seam[i] - 1, i).energy(); //recalculate energy for pixels along seams
                newEnergy[seam[i]][i] = new Pixel(seam[i], i).energy();
            }

        }

        this.energy = newEnergy;

    }

    private void validateVSeam(int[] vSeam) {

        if (vSeam == null) {
            throw new IllegalArgumentException();
        }

        if (vSeam.length != this.height) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < vSeam.length; i++) {
            if (vSeam[i] < 0 || vSeam[i] > this.width - 1) {
                throw new IllegalArgumentException();
            }

            if (i < vSeam.length - 1 && Math.abs(vSeam[i + 1] - vSeam[i]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void validateHSeam(int[] hSeam) {

        if (hSeam == null) {
            throw new IllegalArgumentException();
        }

        if(hSeam.length != this.width) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < hSeam.length; i++) {
            if(hSeam[i] < 0 || hSeam[i] > this.height - 1) {
                throw new IllegalArgumentException();
            } else {
                if (i < hSeam.length - 1 && Math.abs(hSeam[i + 1] - hSeam[i]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void validate(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException();
        }
    }


    private class Pixel {
        int x; //col
        int y; //row

        Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private double energy() {
            return this.calculateEnergy();
        }

        private double calculateEnergy() {
            if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                return 1000.0;
            } else {
                Color right = picture.get(x + 1, y);
                Color left = picture.get(x - 1, y);
                Color up = picture.get(x, y -1);
                Color down = picture.get(x, y + 1);
                return Math.sqrt(diff(left, right) + diff(up, down));
            }
        }

        private double diff(Color pixel, Color other) {

            double red = other.getRed() - pixel.getRed();
            double green = other.getGreen() - pixel.getGreen();
            double blue = other.getBlue() - pixel.getBlue();
            return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
        }

        Iterable<Pixel> vAdj(){

            List<Pixel> vAdj = new ArrayList<>();
            try {
                validate(x -1, y + 1);
                vAdj.add(new Pixel(x - 1, y + 1));
            } catch (IllegalArgumentException e) {
                //StdOut.println("No left lower pixel. Current pixel is on the left edge.");
            }

            try {
                validate(x, y + 1);
                vAdj.add(new Pixel(x, y + 1));
            } catch (IllegalArgumentException e) {
                //StdOut.println(("No lower pixel. Current pixel is on the bottom edge."));
            }

            try {
                validate(x + 1, y + 1);
                vAdj.add(new Pixel(x + 1, y + 1));
            } catch (IllegalArgumentException e) {
                //StdOut.println(("No lower right pixel. Current pixel is on the right edge"));
            }

            return vAdj;
        }

        Iterable<Pixel> hAdj(){
            List<Pixel> hAdj = new ArrayList<>();
            try {
                validate(x + 1, y -1);
                hAdj.add(new Pixel(x + 1, y - 1));
            } catch (IllegalArgumentException e) {
                //StdOut.println("No right upper pixel. Current pixel is on the upper edge.");
            }

            try {
                validate(x + 1, y);
                hAdj.add(new Pixel(x + 1, y));
            } catch (IllegalArgumentException e) {
                //StdOut.println("No right pixel. Current pixel is on the right edge.");
            }

            try {
                validate(x + 1, y + 1);
                hAdj.add(new Pixel(x + 1, y + 1));
            } catch (IllegalArgumentException e) {
                //StdOut.println("No right bottom pixel. Current pixel is on the bottom edge");
            }

            return hAdj;
        }
    }

}
