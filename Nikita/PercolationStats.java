import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats 
{
    private static final double CONF_VALUE = 1.96;
    private final double[] fractionsOfSites;
    private final int totalTries;
    private final double meanRes;
    private final double stddevRes;
    private final double confidenceLo;
    private final double confidenceHi;
    
    public PercolationStats(int n, int trials)
    {   
        validateInput(n, trials);
        fractionsOfSites = new double[trials];
        totalTries = trials;
        int randomRow;
        int randomCol;
        for (int i = 0; i < trials; i++) 
        {
            Percolation experiment = new Percolation(n);
            while (!experiment.percolates()) 
            {
                randomRow = StdRandom.uniform(1, n + 1);
                randomCol = StdRandom.uniform(1, n + 1);
                if (!experiment.isOpen(randomRow, randomCol))
                    experiment.open(randomRow, randomCol);
            }
        fractionsOfSites[i] = experiment.numberOfOpenSites() / ((double) n * n);
        }
        meanRes = StdStats.mean(fractionsOfSites);
        stddevRes = StdStats.stddev(fractionsOfSites);
        confidenceLo = meanRes - (CONF_VALUE * stddevRes) / Math.sqrt(totalTries);
        confidenceHi = meanRes + (CONF_VALUE * stddevRes) / Math.sqrt(totalTries);
    }
    
    private void validateInput(int gridSize, int trialsNum)
    {
        if ((gridSize <= 0) || (trialsNum <= 0))
            throw new IllegalArgumentException("Should be greater than 0");   
    }
    
    public double mean() 
    { return meanRes; }
    
    public double stddev()
    { return stddevRes; }
    
    public double confidenceLo()
    { return confidenceLo; }
    
    public double confidenceHi()
    { return confidenceHi; }

    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats per = new PercolationStats(n, trials);
        StdOut.printf("mean %20s %.6f\n", "=", per.mean());
        StdOut.println("stddev " + per.stddev());
        StdOut.printf("95% confidence interval = [%f, %f]", 
                       per.confidenceLo(), per.confidenceHi());
    }
    
}