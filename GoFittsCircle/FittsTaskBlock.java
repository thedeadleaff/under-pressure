import java.util.Random;

// -------------------
// F I T T S B L O C K
// -------------------

class FittsTaskBlock
{
	private FittsTaskSequence[] sequence;
	private int taskType;
	private int numberOfSequences;
	private int numberOfTrials;
	private int[] a;
	private int[] w;
	private int idx;

	private double[] pt; // positioning time
	private double[] st; // selection time
	private double[] mt; // movement time: MT = PT + ST
	private double[] er;
	private double[] id;
	private double[] ide;
	private double[] tp;
	private int[] sequenceOrder; // this array controls the order of presenting sequences

	FittsTaskBlock(int taskTypeArg, int numberOfSequencesArg, int numberOfTrialsArg, int[] aArg, int[] wArg, boolean randomize)
	{
		taskType = taskTypeArg;
		numberOfSequences = numberOfSequencesArg;
		numberOfTrials = numberOfTrialsArg;
		a = aArg;
		w = wArg;

		// load the sequenceOrder array -- determines the order of presenting sequences
		sequenceOrder = new int[numberOfSequences];
		for (int i = 0; i < sequenceOrder.length; ++i)
			sequenceOrder[i] = i; // natural order (this is the order in the configuration file)
		
		Random r = new Random();		
		if (randomize) // if randomize = "yes" in the configuration file
		{
			for (int i = 0; i < sequenceOrder.length; ++i)
			{
				int newIdx = r.nextInt(sequenceOrder.length);
				int tmp = sequenceOrder[newIdx];
				sequenceOrder[newIdx] = sequenceOrder[i];
				sequenceOrder[i] = tmp; // randomized order (more or less)
			}
		}

		initSequences();
		mt = new double[sequence.length];
		pt = new double[sequence.length];
		st = new double[sequence.length];
		er = new double[sequence.length];
		id = new double[sequence.length];
		ide = new double[sequence.length];
		tp = new double[sequence.length];
		idx = 0;
	}

	private void initSequences()
	{
		sequence = new FittsTaskSequence[numberOfSequences];
		for (int i = 0; i < numberOfSequences; ++i)
		{
			int idxA = sequenceOrder[i] / w.length;
			int idxW = sequenceOrder[i] % w.length;
			sequence[i] = new FittsTaskSequence(taskType, numberOfTrials, a[idxA], w[idxW]);
		}
	}

	public FittsTaskSequence getSequence(int i)
	{
		return sequence[i];
	}

	public void nextSequence()
	{
		++idx;
	}

	public boolean lastSequence()
	{
		return idx == sequence.length - 1;
	}
	
	// return the sequence index --> natural order (0, 1, 2, 3, ...)
	public int getIDX()
	{
		return idx;
	}

	// return the sequence index --> randomized order (to get the next randomized A-W condition)
	public int getNextSequenceIndex()
	{
		return sequenceOrder[idx];
	}	

	public void buildArrays()
	{
		// these arrays hold the values for each sequence in the block
		for (int i = 0; i < sequence.length; ++i)
		{
			mt[i] = sequence[i].getMT();
			pt[i] = sequence[i].getMT() - sequence[i].getST();
			st[i] = sequence[i].getST();
			er[i] = sequence[i].getErrorRate();
			id[i] = sequence[i].getID();
			ide[i] = sequence[i].getIDe();
			tp[i] = sequence[i].getTP();
		}
	}

	public static String getBlockHeader()
	{
		return FittsTaskSequence.getSequenceHeader(); // presentation
	}

	public int getNumberOfSequences()
	{
		return sequence.length;
	}
	
	public String getSequenceData(int i)
	{		
		return sequence[i].getSequenceData();
	}

	public double getMT()
	{
		return mean(mt);
	}

	public double getER()
	{
		return mean(er);
	}

	public double getID()
	{
		return mean(id);
	}

	public double getIDe()
	{
		return mean(ide);
	}

	public double getTP()
	{
		return mean(tp);
	}

	public double getIntercept()
	{
		return intercept(ide, mt);
	}

	public double getSlope()
	{
		return slope(ide, mt);
	}

	public double getCorrelation()
	{
		return corr(ide, mt);
	}
	
	// SM 25/3/2025
	public double getSquaredCorrelation()
	{
		return getCorrelation() * getCorrelation();
	}

	public double getIntercept2()
	{
		return intercept(ide, pt);
	}

	public double getSlope2()
	{
		return slope(ide, pt);
	}

	public double getCorrelation2()
	{
		return corr(ide, pt);
	}

	public double getIntercept3()
	{
		return intercept(ide, st);
	}

	public double getSlope3()
	{
		return slope(ide, st);
	}

	public double getCorrelation3()
	{
		return corr(ide, st);
	}

	// calculate the mean of the values in a double array.
	public double mean(double n[])
	{
		double mean = 0.0;
		for (int j = 0; j < n.length; j++)
			mean += n[j];
		return mean / n.length;
	}

	// calculate the mean of the values in an array
	public double mean(double n[], int length)
	{
		double mean = 0.0;
		for (int j = 0; j < length; j++)
			mean += n[j];
		return mean / length;
	}
	
	// calculate the standard deviation of values in an array	 
	public double sd(double[] n, int length)
	{
		double m = mean(n, length);
		double t = 0.0;
		for (int j = 0; j < length; j++)
			t += (m - n[j]) * (m - n[j]);
		return Math.sqrt(t / (length - 1.0));
	}

	/**
	 * Compute the slope of a regression line.
	 * 
	 * Regression line coefficients are computed using the least squares method. Typically, the
	 * <code>x</code> values are controlled (independent) and the <code>y</code> values are measured
	 * (dependent). In computing the regression line, the <code>y</code> values are 'regressed on'
	 * the <code>x</code> values. The regression line equation predicts <code>y</code> as a linear
	 * function of <code>x</code>.
	 * 
	 * @param x
	 *            a double array
	 * @param y
	 *            a double array
	 * @return a double representing the slope of the regression line
	 */
	public static double slope(double[] x, double[] y)
	{
		double sx2 = 0.0;
		double sx = 0.0;
		double sxy = 0.0;
		double sy = 0.0;
		for (int i = 0; i < x.length; ++i)
		{
			sx2 += x[i] * x[i];
			sx += x[i];
			sxy += x[i] * y[i];
			sy += y[i];
		}

		double sxx = x.length * sx2 - sx * sx;
		sxy = x.length * sxy - sx * sy;

		return sxy / sxx;
	}

	/**
	 * Compute the intercept of a regression line.
	 * 
	 * Regression line coefficients are computed using the least squares method. Typically, the
	 * <code>x</code> values are controlled (independent) and the <code>y</code> values are measured
	 * (dependent). In computing the regression line, the <code>y</code> values are 'regressed on'
	 * the <code>x</code> values. The regression line equation predicts <code>y</code> as a linear
	 * function of <code>x</code>.
	 * <p>
	 * 
	 * @param x
	 *            a double array
	 * @param y
	 *            a double array
	 * @return a double representing the slope of the regression intercept
	 */
	public double intercept(double[] x, double[] y)
	{
		return mean(y) - slope(x, y) * mean(x);
	}

	/**
	 * Calculate the correlation between two sets of values.
	 * 
	 * This method computes the Pearson coefficient of correlation. It is the statistical summary of
	 * the degree and direction of relationship of association between two variables.
	 * 
	 * @param d1
	 *            an array of doubles
	 * @param d2
	 *            an array of doubles
	 * @param length
	 *            the length of each array (only 'length' values will be processed)
	 * @return a double equal to the Pearson correlation coefficient
	 */
	public double corr(double[] d1, double[] d2, int length)
	{
		double cov = covariance(d1, d2, length);
		double sd1 = sd(d1, length);
		double sd2 = sd(d2, length);
		return cov / (sd1 * sd2);
	}

	/**
	 * Calculate the correlation between two sets of values (two-argument version).
	 */
	public double corr(double[] x, double[] y)
	{
		return corr(x, y, x.length);
	}

	/**
	 * Calculate the covariance between two sets of values.
	 * 
	 * The covariance is a measure of association in many problems in physical sciences and
	 * engineering. It is an adequate measure as long as the scales (means and variances) of the
	 * variables are not arbitrary. But, most variables in social and behavioural sciences are
	 * measured on an arbitrary scale; hence, correlation coefficients are generally preferred to
	 * covariance as measures of relationship.
	 * 
	 * @param d1
	 *            an array of doubles
	 * @param d2
	 *            an array of doubles
	 * @param length
	 *            the length of each array (only the first 'length' values are processed
	 * @return a double equal to the covariance between the two sets of values
	 */
	public double covariance(double[] d1, double[] d2, int length)
	{
		double cov = 0.0;
		double m1 = mean(d1, length);
		double m2 = mean(d2, length);
		for (int i = 0; i < length; ++i)
			cov += (d1[i] - m1) * (d2[i] - m2);
		return cov /= (length - 1);
	}

	public String getBlockSummary()
	{
		String s = "GRAND MEANS\n";
		s += String.format("  MT = %.1f ms\n", this.getMT());
		s += String.format("  ER = %.2f%%\n", this.getER());
		s += String.format("  TP = %.2f bits/s\n", this.getTP());
		s += String.format("REGRESSION COEFFICIENTS\n");
		s += String.format("  MT = %.1f + %.1f IDe (R^2 = %.4f)", this.getIntercept(), this.getSlope(), this
				.getSquaredCorrelation());
		return s;
	}
}
