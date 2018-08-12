import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Percolation 
{
    private static final int TOP_VIRTUAL = 0;
    private final int bottomVirt;
    private final boolean[][] sitesStatuses;
    private int numberOfOpenSites;
    private final int gridSize;
    private final WeightedQuickUnionUF weightedSites;
    private final WeightedQuickUnionUF fullSites;
    
    public Percolation(int n) 
    {
        validateIndex(n);
        gridSize = n;
        int fullSize = gridSize * gridSize;
        bottomVirt = fullSize + 1;
        sitesStatuses = new boolean[n+1][n+1];
        weightedSites = new WeightedQuickUnionUF(fullSize + 2);
        fullSites = new WeightedQuickUnionUF(fullSize + 1);
        for (int i = 0; i <= gridSize; i++) 
            for (int j = 0; j <= gridSize; j++) 
                sitesStatuses[i][j] = false;
    }
  
    
    private int xyTo1D(int row, int col) 
    { return gridSize * (row - 1) + col; }
    
    private void validateIndex(int index)
    {
        if (index <= 0 || index > gridSize) 
           throw new IllegalArgumentException("index" + index + " not valid");
    }
    
    
    private void validateRowAndCol(int row, int col)
    {
         validateIndex(row);
         validateIndex(col);
    }
    
    public void open(int row, int col)
    {
        validateRowAndCol(row, col);
        if (!sitesStatuses[row][col])
        {
            sitesStatuses[row][col] = true;
            numberOfOpenSites++;
            // left
            if ((col-1) > 0 && (sitesStatuses[row][col-1]))
            {
                fullSites.union(xyTo1D(row, col), xyTo1D(row, col-1));
                weightedSites.union(xyTo1D(row, col), xyTo1D(row, col-1));
            }
            // top
            if ((row-1) > 0 && (sitesStatuses[row-1][col])) 
            {
                fullSites.union(xyTo1D(row, col), xyTo1D(row-1, col));
                weightedSites.union(xyTo1D(row, col), xyTo1D(row-1, col));
            }
            // right
            if ((col+1) <= gridSize && (sitesStatuses[row][col+1]))
            {
                fullSites.union(xyTo1D(row, col), xyTo1D(row, col+1));
                weightedSites.union(xyTo1D(row, col), xyTo1D(row, col+1));
            }
            // bottom
            if ((row+1) <= gridSize && (sitesStatuses[row+1][col]))
            {
                fullSites.union(xyTo1D(row, col), xyTo1D(row+1, col));
                weightedSites.union(xyTo1D(row, col), xyTo1D(row+1, col));
            }
        }
        if (row == 1) 
        {
            fullSites.union(xyTo1D(row, col), TOP_VIRTUAL);
            weightedSites.union(xyTo1D(row, col), TOP_VIRTUAL);
        }
        if (row == gridSize) weightedSites.union(xyTo1D(row, col), bottomVirt);
    }
    
    public boolean isOpen(int row, int col)
    {
        validateRowAndCol(row, col);
        return sitesStatuses[row][col];
    }
    
    public boolean isFull(int row, int col)
    { 
        validateRowAndCol(row, col);
        return fullSites.connected(xyTo1D(row, col), TOP_VIRTUAL); 
    }
    
    public int numberOfOpenSites() 
    { return numberOfOpenSites; }
    
    public boolean percolates() 
    { return weightedSites.connected(TOP_VIRTUAL, bottomVirt); }
    
    
    public static void main(String[] args) {
        
        int inputN = StdIn.readInt();
        Percolation per = new Percolation(inputN);
        while (!StdIn.isEmpty())
        {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            if (per.isOpen(row, col)) continue;
            per.open(row, col);
        }
        StdOut.println(per.numberOfOpenSites() + " sites");
    }
}