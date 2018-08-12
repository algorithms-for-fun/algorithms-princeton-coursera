import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/*--------------------------------------------------------------------------------------------------------------
 *  By Irene Storozhko. August 11, 2018. Week 1 assignment for Algorithms I course by Wayne and Sedgewick.
 *
 *  Creates a percolation system, performs Monte Carlo simulation experiemnt with it and calculates statistics.
 *-------------------------------------------------------------------------------------------------------------*/
public class PercolationStats {
    private final int n;
    private final double[] percolationTresholds;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;
    private final double CONFIDENCE_95;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials and must be positive integers greater than 0");

        this.CONFIDENCE_95 = 1.96;
        this.n = n;
        this.percolationTresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            this.percolationTresholds[i] = trial();
        }

        this.mean = calculateMean();
        this.stddev = calculateStddev();
        this.confidenceLo = calculateConfidenceLo();
        this.confidenceHi = calculateConfidenceHi();
    }

    private double trial() {
        Percolation p = new Percolation(n);
        int openSites = 0;

        while (!p.percolates()) {
            int row;
            int col;

            do {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
            } while (p.isOpen(row, col));

            p.open(row, col);
            openSites++;
        }

        return openSites / (double)(n * n);
    }

    private double calculateMean() {
        double sum = 0;
        for (double percolationTreshold : percolationTresholds) {
            sum += percolationTreshold;
        }
        return sum / percolationTresholds.length;
    }

    private double calculateStddev() {
        if (percolationTresholds.length == 1) return Double.NaN;

        double squaredDiffFromMeanSum = 0;
        for (double percolationTreshold : percolationTresholds) {
            squaredDiffFromMeanSum += Math.pow(percolationTreshold - mean, 2);
        }
        return Math.sqrt(squaredDiffFromMeanSum / (percolationTresholds.length - 1));
    }

    private double calculateConfidenceLo() {
        return mean - ((CONFIDENCE_95 * stddev) / Math.sqrt(percolationTresholds.length));
    }

    private double calculateConfidenceHi() {
        return mean + ((CONFIDENCE_95 * stddev) / Math.sqrt(percolationTresholds.length));
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.confidenceLo;
    }

    public double confidenceHi() {
        return this.confidenceHi;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.printf("mean %20s %.6f\n", "=", ps.mean());
        StdOut.printf("stddev %18s %.17f\n", "=", ps.stddev());
        StdOut.printf("95%% confidence interval %s [%.15f, %.15f]\n", "=", ps.confidenceLo(), ps.confidenceHi());
    }
}
