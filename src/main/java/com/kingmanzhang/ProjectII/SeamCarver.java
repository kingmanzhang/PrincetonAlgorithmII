package com.kingmanzhang.ProjectII;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


public class SeamCarver {

    private final Picture picture;
    private int width;
    private int height;
    double[] distTo ; //store distance
    int[] edgeTo; //store edge

    /**
     * create a seam carver object based on the given picture
     * @param picture
     */
    public SeamCarver(Picture picture){

        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
        this.distTo = new double[this.width * this.height];
        this.edgeTo = new int[this.width * this.height];
    }

    /**
     * current picture
     * @return
     */
    public Picture picture() {

        return new Picture(this.picture);

    }

    /**
     * width of current picture
     * @return
     */
    public int width(){

        return this.width;
    }

    /**
     * height of current picture
     * @return
     */
    public int height()  {

        return this.height;

    }

    /**
     * energy of pixel at column x and row y
     * @param x
     * @param y
     * @return
     */
    public  double energy(int x, int y){

        Pixel pixel = new Pixel(x, y);
        return pixel.energy;

    }


    /**
     * sequence of indices for horizontal seam
     * @return
     */
    public int[] findHorizontalSeam(){



        return null;
    }

    /**
     * sequence of indices for vertical seam
     * @return
     */
    public int[] findVerticalSeam(){

        for (int i = 0; i < this.width * this.height; i++) {
            edgeTo[i] = -1;
            if (i < this.width) distTo[i] = 0.0;  //first row: distTo set to 0;
            else distTo[i] = Double.MAX_VALUE; //other rows: distTo set to positive infinity.
        }

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Pixel pixel = new Pixel(i, j);
                if (pixel.vAdj() != null) {
                    for (Pixel v : pixel.vAdj()) {
                        if (this.distTo[v.y * this.width + v.x] > this.distTo[i * this.width + j] + pixel.energy) {
                            this.distTo[v.y * this.width + v.x] = this.distTo[i * this.width + j] + pixel.energy;
                            this.edgeTo[v.y * this.width + v.x] = i * this.width + j;
                        }

                    }
                }
            }
        }

        return sp();
    }

    private int[] sp() {
        int target = -1;
        double shortest = Double.MAX_VALUE;
        for (int i = this.width * (this.height - 1); i < this.width * this.height; i++) {
            if (distTo[i] < shortest) {
                target = i;
                shortest = distTo[i];
            }
        }

        Stack<Integer> sp = new Stack<>();
        int i = target;
        do {
            sp.push(i % this.width);
            i = edgeTo[target];
        } while (i != -1);
        sp.push(i % this.width);

        int[] sp_array = new int[this.height];
        Iterator itr = sp.iterator();

        i = 0;
        while (itr.hasNext()) {
            sp_array[i] = sp.pop();
            i++;
        }
        return sp_array;
    }

    /**
     * remove horizontal seam from current picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam){

        if (seam == null) {
            throw new IllegalArgumentException();
        }


    }

    /**
     * remove vertical seam from current picture
     * @param seam
     */
    public void removeVerticalSeam(int[] seam){

        if (seam == null) {
            throw new IllegalArgumentException();
        }

        int j = 0;
        int[] vSeam = findVerticalSeam();
        for (int i = 0; i < this.height; i++) {
            Pixel pixel = new Pixel(vSeam[i] ,j);
        }



    }


    private class Pixel {
        int x;
        int y;
        double energy;

        Pixel(int x, int y) {
            validate(x, y);
            this.x = x;
            this.y = y;
            this.energy = this.calculateEnergy();
        }

        private void validate(int x, int y) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException();
            }
        }

        private double calculateEnergy() {
            if (x == 0 || x == width || y == 0 || y == height) {
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
                vAdj.add(new Pixel(x - 1, y + 1));
            } catch (IllegalArgumentException e) {
                StdOut.println("No left lower pixel. Current pixel is on the left edge.");
            }

            try {
                vAdj.add(new Pixel(x, y + 1));
            } catch (IllegalArgumentException e) {
                StdOut.println(("No lower pixel. Current pixel is on the bottom edge."));
            }

            try {
                vAdj.add(new Pixel(x + 1, y + 1));
            } catch (IllegalArgumentException e) {
                StdOut.println(("No lower right pixel. Current pixel is on the right edge"));
            }

            return vAdj;
        }

        Iterable<Pixel> hAdj(){
            List<Pixel> hAdj = new ArrayList<>();
            try {
                hAdj.add(new Pixel(x + 1, y - 1));
            } catch (IllegalArgumentException e) {
                StdOut.println("No right upper pixel. Current pixel is on the upper edge.");
            }

            try {
                hAdj.add(new Pixel(x + 1, y));
            } catch (IllegalArgumentException e) {
                StdOut.println("No right pixel. Current pixel is on the right edge.");
            }

            try {
                hAdj.add(new Pixel(x + 1, y + 1));
            } catch (IllegalArgumentException e) {
                StdOut.println("No right bottom pixel. Current pixel is on the bottom edge");
            }

            return hAdj;
        }
    }
/**
    private int[] vAdj(int i) {
        int x = i % this.width;
        int y = 1 / this.width;
        if (x == 0 && y == this.height - 1) {
            return null;
        } else if (x == 0 && y < this.height - 1) {
            return new int[]{i + this.width, i + this.width + 1};
        } else if (x == this.width - 1 && y < this.height - 1) {
            return new int[]{i + this.width - 1, i + this.width};
        } else if (x == this.width - 1 && y == this.height - 1) {
            return null;
        } else {
            return new int[]{i + this.width - 1, i + this.width, i + this.width + 1};
        }
    }
**/
}
