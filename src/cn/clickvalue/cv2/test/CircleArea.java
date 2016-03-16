package cn.clickvalue.cv2.test;

public class CircleArea implements IComputeArea {

    public double compute(Object obj) {
        Circle circle = (Circle) obj;
        return Math.PI * circle.getRadius() * circle.getRadius();

    }

}
