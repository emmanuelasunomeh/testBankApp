/**
 * @author: Emmanuel Asunomeh
 * @Date: 8/23/2021
 */

public class PerfectSquareCount {

    public static void main(String[] args) {

         cntSquares(4);

    }

    static void cntSquares (int n) {
        System.out.println(n);
        System.out.println(n * (n + 1) * (2 * n + 1) / 6);
    }

}
