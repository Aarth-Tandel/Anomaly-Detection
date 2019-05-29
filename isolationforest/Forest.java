package isolationforest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest {

    private Double pivot0;
    private Double pivot1;

    private int SampleSize;

    private List<Tree> treesList;

    public Forest() {
        this.pivot0 = null;
        this.pivot1 = null;
        this.SampleSize = 256;
        this.treesList = new ArrayList<>();
    }

    private double calculatePathLength(double[] sample, final Tree Tree) throws Exception {
        if (Tree == null || Tree.leafNodes == 0)
            throw new Exception("Tree is bull or empty");

        double pathLength = -1;
        double attrValue;
        Tree tmpITree = Tree;

        while (tmpITree != null) {
            pathLength += 1;
            attrValue = sample[tmpITree.traitIndex];

            if (tmpITree.leftTree == null || tmpITree.rightTree == null || attrValue == tmpITree.traitValue)
                break;
            else if (attrValue < tmpITree.traitValue)
                tmpITree = tmpITree.leftTree;
            else
                tmpITree = tmpITree.rightTree;
        }
        return pathLength + calculateCn(tmpITree.leafNodes);
    }

    private double[] classify(double[] values, int iters) {

        pivot0 = values[0];
        pivot1 = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > pivot0) 
                pivot0 = values[i];
            
            if (values[i] < pivot1) 
                pivot1 = values[i];
            
        }

        int center1, center2;
        double diff0, diff1;
        double[] labels = new double[values.length];

        for (int n = 0; n < iters; n++) {
            center1 = 0;
            center2 = 0;

            for (int i = 0; i < values.length; i++) {
                diff0 = Math.abs(values[i] - pivot0);
                diff1 = Math.abs(values[i] - pivot1);
                labels[i] = values[i];
                if (diff0 < diff1)
                    center1++;
                else
                    center2++;

            }
            diff0 = pivot0;
            diff1 = pivot1;
            pivot0 = 0.0;
            pivot1 = 0.0;
            for (int i = 0; i < values.length; i++)
                if (labels[i] == 0)
                    pivot0 += values[i];
                else
                    pivot1 += values[i];
            pivot0 /= center1;
            pivot1 /= center2;
            if (pivot0 - diff0 <= 1e-6 && pivot1 - diff1 <= 1e-6)
                break;
        }
        return labels;
    }

    private void createForest(double[][] samples, int t) throws Exception {

        if (t <= 0)
            throw new Exception("Subtree must be postive");
        else if (SampleSize <= 0)
            throw new Exception("Sample size must be postive");

        int limitHeight = (int) Math.ceil(Math.log(SampleSize) / Math.log(2));
        Tree iTree;
        double[][] subSample;
        for (int i = 0; i < t; i++) {
            subSample = this.getSamples(samples, SampleSize);
            iTree = Tree.createTrees(subSample, 0, limitHeight);
            this.treesList.add(iTree);
        }
    }

    private double[] calculateAnomalyIndex(double[][] samples) throws Exception {
        double[] values = new double[samples.length];
        for (int i = 0; i < samples.length; i++)
            values[i] = calculateAnomalyIndex(samples[i]);

        return values;
    }

    private double calculateAnomalyIndex(double[] sample) throws Exception {

        if (treesList == null || treesList.size() == 0)
            throw new Exception("Treelist might be empty");
        else if (sample == null || sample.length == 0)
            throw new Exception("Sample might be null");

        double ehx = 0;
        double pathLength = 0;
        for (Tree iTree : treesList) {
            pathLength = calculatePathLength(sample, iTree);
            ehx += pathLength;
        }
        ehx /= treesList.size();
        double cn = calculateCn(SampleSize);
        double index = ehx / cn;
        double anomalyIndex = Math.pow(2, -index);
        return anomalyIndex;
    }

    private double calculateCn(double n) {
        if (n <= 1)
            return 0;
        return 2 * (Math.log(n - 1) + 0.5772156649) - 2 * ((n - 1) / n);
    }

    public double[] teach(double[][] samples, int t) throws Exception {
        return teach(samples, t, 256, 100);
    }

    public double[] teach(double[][] samples, int t, int SampleSize, int iters) throws Exception {

        this.SampleSize = SampleSize;
        if (this.SampleSize > samples.length)
            this.SampleSize = samples.length;

        createForest(samples, t);
        double[] values = calculateAnomalyIndex(samples);
        double[] labels = classify(values, iters);
        return labels;
    }

    private double[][] getSamples(double[][] samples, int sampleNum) throws Exception {
        if (sampleNum <= 0)
            throw new Exception("Sample must be potive");

        if (samples.length < sampleNum)
            sampleNum = samples.length;

        int cols = samples[0].length;
        double[][] subSamples = new double[sampleNum][cols];
        int randomIndex;
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < sampleNum; i++) {
            randomIndex = random.nextInt(samples.length);
            subSamples[i] = samples[randomIndex];
        }
        return subSamples;
    }
}
