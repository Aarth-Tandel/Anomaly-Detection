package isolationforest;

import java.util.Random;

public class Tree {

    public int traitIndex;
    public double traitValue;
    public int leafNodes;
    public int treeHeight;
    public Tree leftTree, rightTree;

    public Tree(int traitIndex, double traitValue) {

        this.treeHeight = 0;
        this.leftTree = null;
        this.rightTree = null;
        this.leafNodes = 1;
        this.traitIndex = traitIndex;
        this.traitValue = traitValue;
    }

    public static Tree createTrees(double[][] samples, int treeHeight, int limitHeight) {

        Tree tree = null;

        if (samples.length == 0)
            return tree;
        else if (treeHeight >= limitHeight || samples.length == 1) {
            tree = new Tree(0, samples[0][0]);
            tree.leafNodes = 1;
            tree.treeHeight = treeHeight;
            return tree;
        }

        int rows = samples.length;
        int cols = samples[0].length;
        boolean isAllSame = true;
        break_label: for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (samples[i][j] != samples[i + 1][j]) {
                    isAllSame = false;
                    break break_label;
                }
            }
        }

        if (isAllSame == true) {
            tree = new Tree(0, samples[0][0]);
            tree.leafNodes = samples.length;
            tree.treeHeight = treeHeight;
            return tree;
        }

        Random random = new Random(System.currentTimeMillis());
        int traitIndex = random.nextInt(cols);
        double min, max;
        min = samples[0][traitIndex];
        max = min;
        for (int i = 1; i < rows; i++) {
            if (samples[i][traitIndex] < min)
                min = samples[i][traitIndex];

            if (samples[i][traitIndex] > max)
                max = samples[i][traitIndex];

        }

        double traitValue = random.nextDouble() * (max - min) + min;
        int lnodes = 0, rnodes = 0;
        double curValue;
        for (int i = 0; i < rows; i++) {
            curValue = samples[i][traitIndex];
            if (curValue < traitValue)
                lnodes++;
            else
                rnodes++;

        }

        double[][] lSamples = new double[lnodes][cols];
        double[][] rSamples = new double[rnodes][cols];

        lnodes = 0;
        rnodes = 0;
        for (int i = 0; i < rows; i++) {
            curValue = samples[i][traitIndex];
            if (curValue < traitValue)
                lSamples[lnodes++] = samples[i];
            else
                rSamples[rnodes++] = samples[i];

        }

        Tree parent = new Tree(traitIndex, traitValue);
        parent.leafNodes = rows;
        parent.treeHeight = treeHeight;
        parent.leftTree = createTrees(lSamples, treeHeight + 1, limitHeight);
        parent.rightTree = createTrees(rSamples, treeHeight + 1, limitHeight);

        return parent;
    }
}