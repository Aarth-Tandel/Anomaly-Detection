import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Readfile {

    public int number;

    public int dimension;

    public int size;

    public double[][] readData() {
        Scanner input = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int k = 0;
        try {
            k = input.nextInt();
        } catch (Exception e) {
            System.out.println("File may be empty or wrong set of data " + e);
            System.exit(-1);
        }
        

        if (k < 0) {
            System.out.println("Value of K cannot be negative");
            System.exit(-1);
        }

        setNumber(k);
        ArrayList<String> oneDimension = new ArrayList<String>();
        while (input.hasNextLine()) {
            try{
                String line = input.nextLine();
                if (line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) {
                } else if (!(line.length() < 1))
                oneDimension.add(line);
            } catch (Exception e){
                System.out.println("Inconsistent data");
                System.exit(-1);
            }
            
        }

        ArrayList<ArrayList<Double>> twoDimesnion = new ArrayList<ArrayList<Double>>();
        input.close();

        for (String ss : oneDimension) {

            ArrayList<Double> temp = new ArrayList<Double>();
            String temp1[] = ss.split(",");

            for (int i = 0; i < temp1.length; i++) {
                try {
                    temp.add(Double.parseDouble(temp1[i]));
                } catch (Exception e) {
                    System.out.println("Data is not valid");
                    System.exit(-1);
                }
            }
            twoDimesnion.add(temp);
        }

        int dimensionSize = twoDimesnion.get(0).size();
        for (ArrayList<Double> x : twoDimesnion) {
            if (dimensionSize != x.size()) {
                System.out.println("Inconsistent data dimension");
                System.exit(-1);
            }
        }

        if (k > twoDimesnion.size()) {
            System.out.println("K is greater than input data");
            System.exit(-1);
        }

        double[][] samples = new double[twoDimesnion.size()][twoDimesnion.get(0).size()];

        for (int i = 0; i < twoDimesnion.size(); i++) {
            for (int j = 0; j < twoDimesnion.get(i).size(); j++) {
                samples[i][j] = twoDimesnion.get(i).get(j);
            }
        }

        setDimension(twoDimesnion.get(0).size());
        return samples;
    }

    public int getDimension() {
        return dimension;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int num) {
        number = num;
    }

    public void setDimension(int num) {
        dimension = num;
    }
}