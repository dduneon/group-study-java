/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

package turtle;

import java.util.ArrayList;
import java.util.List;

public class TurtleSoup {
    final static double circleDgree = 360;
    final static double circleHalfDgree = 180;

    /**
     * Draw a square.
     *
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        turtle.forward(sideLength);
        turtle.turn(120);
        turtle.forward(sideLength);
        turtle.turn(120);
        turtle.forward(sideLength);
        turtle.turn(120);
        turtle.forward(sideLength);
        turtle.turn(120);
        turtle.forward(sideLength);
        turtle.turn(120);
        turtle.forward(sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * <p>
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     *
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        /**
         * 남이 읽고 고치기 편해야함 readable
         * 수정하기 쉬워야함(고쳤을 때 범위가 영향을 받지 않는 곳까지 안 건드린다 modifier
         * 안전성 보장되어야 함 safety
         */
        if (sides <= 2) {
            throw new IllegalArgumentException("sides can not be under 2");
        }
        double sumOfAngles = circleHalfDgree * (sides - 2);
        double partOfAngle = sumOfAngles / sides;
        return partOfAngle;

    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * <p>
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     *
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        if (angle <= 0 || angle >= 180) {
            throw new IllegalArgumentException("must be 0 < " + angle + " < 180");
        }
        int side = (int) Math.round(circleDgree / (circleHalfDgree - angle));
        return side;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * <p>
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     *
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        if (sides <= 2) {
            throw new IllegalArgumentException(sides + "must be over 2");
        }

        final double regularPolygonAngle = circleHalfDgree - calculateRegularPolygonAngle(sides);

        while (sides-- > 0) {
            turtle.forward(sideLength);
            turtle.turn(regularPolygonAngle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * <p>
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360.
     * <p>
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     *
     * @param currentHeading current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     * must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        if (currentHeading < 0 || currentHeading >= 360) {
            throw new IllegalArgumentException("must be in 0 <= " + currentHeading + " < 360");
        }
        final double turtleBase = 90;
        double radian = Math.atan2(targetY - currentY, targetX - currentX);
        double atanDgree = turtleBase - currentHeading - radian * circleHalfDgree / Math.PI;

        if (atanDgree < 0) {
            atanDgree += 360;
        }
        return atanDgree;
    }


    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * <p>
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     *
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     * otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> result = new ArrayList<>();
        double headingPoint = 0.0;
        for (int i = 0; i < xCoords.size()-1; i++) {
            result.add(calculateHeadingToPoint(headingPoint,xCoords.get(i),yCoords.get(i),xCoords.get(i+1),yCoords.get(i+1)));
            headingPoint+=result.get(i);
//            if(headingPoint>=360){
//                headingPoint-=360;
//            }
        }
        System.out.println(result.size());
        System.out.println(result.get(0));
        System.out.println(result.get(1));
        return result;
    }

    /**
     * Draw your personal, custom art.
     * <p>
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     *
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        turtle.color(PenColor.RED);
        for (int i = 0; i < 15; i++) {
            turtle.turn(40);
            drawRegularPolygon(turtle,7,80);
        }
        turtle.color(PenColor.MAGENTA);
        for (int i = 0; i < 15; i++) {
            turtle.turn(20);
            drawRegularPolygon(turtle,3,60);
        }
        turtle.color(PenColor.MAGENTA);
        for (int i = 0; i < 15; i++) {
            turtle.turn(70);
            drawRegularPolygon(turtle,4,70);
        }
    }

    /**
     * Main method.
     * <p>
     * This is the method that runs when you run "java TurtleSoup".
     *
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        drawPersonalArt(turtle);
//        drawRegularPolygon(turtle, 3, 50);
//        drawSquare(turtle,40);
        // draw the window
        turtle.draw();
    }

}
