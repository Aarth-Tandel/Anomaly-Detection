// =========================================================
// 
// Assignment P1 Anomoly Detection
// ---------------------------------------------------------
// Author:      Aarth Tandel
// Date:        1/20/19
// =========================================================

public class P1 { 
    public static void main(String[] args) {
        Readfile read = new Readfile();
        double[][] data = read.readData();
        ComputePoints calculate = new ComputePoints();
        calculate.generate(data, read.getNumber(), read.getDimension(), data.length);
    }
}