package cn.clickvalue.cv2.test;

public class SquareArea implements IComputeArea {

    public double compute(Object obj) {
        Square sq = (Square) obj;
        return sq.getSize() * sq.getSize();
    }

}
