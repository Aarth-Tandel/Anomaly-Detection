import isolationforest.*;

public class ComputePoints {

    public double[][] generate(double[][] samples, int k, int dimension, int size) {
        Forest iForest = new Forest();
        double[][] mapOfAnomoly = new double[size][2];
        try {
            double[] labels = iForest.teach(samples, 200);

            for (int i = 0; i < labels.length; i++) {
                mapOfAnomoly[i][0] = labels[i];
                mapOfAnomoly[i][1] = i;
            }

            java.util.Arrays.sort(mapOfAnomoly, new java.util.Comparator<double[]>() {
                public int compare(double[] a, double[] b) {
                    return Double.compare(b[0], a[0]);
                }
            });

            double check = 0.5000000;
            boolean flag = true;
            for (int i = 0; i < k; i++) {
                if (mapOfAnomoly[i][0] > check)
                    // System.out.print("Anomoly " + mapOfAnomoly[i][0] + " ");
                    if (mapOfAnomoly[i][0] > check) {
                        for (int j = 0; j < dimension; j++) {
                            System.out.print(samples[(int) mapOfAnomoly[i][1]][j] + " ");
                            flag = false  ;
                        }
                        System.out.println();
                    }
            }

            if (flag)
                System.out.println("No Anomly Detected");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapOfAnomoly;
    }
}