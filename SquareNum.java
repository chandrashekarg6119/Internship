public class SquareNum{
    static int distSq(int x1, int y1, int x2, int y2) {
        return (x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2);
    }

    public static void main(String[] args) {

        int x1 = 20, y1 = 10;
        int x2 = 10, y2 = 20;
        int x3 = 20, y3 = 20;
        int x4 = 10, y4 = 10;

        int centerX = (x1 + x2 + x3 + x4) / 4;
        int centerY = (y1 + y2 + y3 + y4) / 4;

        int d1 = distSq(x1, y1, centerX, centerY);
        int d2 = distSq(x2, y2, centerX, centerY);
        int d3 = distSq(x3, y3, centerX, centerY);
        int d4 = distSq(x4, y4, centerX, centerY);

        if (d1 == d2 && d2 == d3 && d3 == d4)
            System.out.println("Yes");
        else
            System.out.println("No");
    }
}
