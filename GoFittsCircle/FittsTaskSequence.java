import java.awt.geom.Point2D;

// -------------------------
// F I T T S S E Q U E N C E
// -------------------------

class FittsTaskSequence
{
	private FittsTaskTrial[] ftt;
	private int taskTypeCode;
	private int numTrialsTargets;
	private int a;
	private int w;
	private int sequenceRepeatCount;
	private double pt; // pointing time
	private double st; // selection time

	int totalMisses;
	double errorRate;

	/*
	 * The accuracy measures are calculated and stored in each trial. Here, we are concerned with the means for the
	 * entire sequence.
	 */
	private double tre; // target re-entries
	private double tac; // task-axis crossings
	private double mdc; // movement direction changes
	private double odc; // orthogonal direction changes
	private double mv; // movement variability
	private double me; // movement error
	private double mo; // movement offset

	/*
	 * The Throughput object will provide us with the throughput for the sequence and other measures (e.g., ID, IDe,
	 * error rate, etc).
	 */
	private Throughput tp;

	FittsTaskSequence(int taskTypeCodeArg, int numTrialsTargetsArg, int aArg, int wArg)
	{
		tp = new Throughput();

		taskTypeCode = taskTypeCodeArg;
		numTrialsTargets = numTrialsTargetsArg;
		a = aArg;
		w = wArg;

		// create an array to hold information about each trial in the sequence
		ftt = new FittsTaskTrial[numTrialsTargets];

		// fill the array with FittsTaskTwoTrial instances
		for (int i = 0; i < numTrialsTargets; ++i)
			ftt[i] = new FittsTaskTrial(a, w, taskTypeCode);

		sequenceRepeatCount = 0;
	}

	public FittsTaskTrial getTrial(int i)
	{
		return ftt[i];
	}

	public void setSequenceRepeatCount(int n)
	{
		sequenceRepeatCount = n;
	}

	public int getSequenceRepeatCount()
	{
		return sequenceRepeatCount;
	}

	public void clearSequenceRepeatCount()
	{
		sequenceRepeatCount = 0;
	}

	public void incrementSequenceRepeatCount()
	{
		++sequenceRepeatCount;
	}

	public int getNumberOfTrials()
	{
		return ftt.length;
	}

	public double getID() // index of difficulty (bits)
	{
		// need to compute the mean, because A is potentially different from trial to trial (i.e., 2D with even trials)
		double id = 0.0;
		for (int i = 0; i < ftt.length; ++i)
			id += tp.getID();
		id /= ftt.length;
		return id;
	}

	public double getIDe() // effective index of difficulty (bits)
	{
		return tp.getIDe();
	}

	public double getTP() // throughput (bits/s)
	{
		return tp.getThroughput();
	}

	public void computeSequenceSummaryStats()
	{
		/*
		 * The accuracy measures are stored in the AccuracyMeasures object in the FittsTaskTwoTrial instance. Here, we
		 * are computing the mean of the measures for the sequence. We are also computing the mean of the positioning
		 * time (pt) and selection time (st) values, since these are not handled in the Throughput object.
		 */
		pt = 0.0;
		st = 0.0;
		tre = 0.0;
		tac = 0.0;
		mdc = 0.0;
		odc = 0.0;
		mv = 0.0;
		me = 0.0;
		mo = 0.0;
		totalMisses = 0;
		errorRate = 0.0;
		for (int i = 0; i < ftt.length; ++i)
		{
			pt += ftt[i].getPT();
			st += ftt[i].getST();
			tre += ftt[i].getTRE();
			tac += ftt[i].getTAC();
			mdc += ftt[i].getMDC();
			odc += ftt[i].getODC();
			mv += ftt[i].getMV();
			me += ftt[i].getME();
			mo += ftt[i].getMO();
			totalMisses += ftt[i].getError();
			//System.out.println("getError()=" + ftt[i].getError());
		}
		pt /= ftt.length;
		st /= ftt.length;
		tre /= ftt.length;
		tac /= ftt.length;
		mdc /= ftt.length;
		odc /= ftt.length;
		mv /= ftt.length;
		me /= ftt.length;
		mo /= ftt.length;
		errorRate = (double)totalMisses / ftt.length * 100.0;
		//System.out.println("computSequenceSummaryStats: errorRate=" + errorRate);

		/*
		 * Prepare and pass on the necessary data to the Throughput object. We'll let the Throughput object take of
		 * computing throughput and related measures.
		 */
		Point2D.Double[] from = new Point2D.Double[ftt.length];
		Point2D.Double[] to = new Point2D.Double[ftt.length];
		Point2D.Double[] select = new Point2D.Double[ftt.length];
		int[] mt = new int[ftt.length];
		for (int i = 0; i < ftt.length; ++i)
		{
			from[i] = new Point2D.Double(ftt[i].xFrom, ftt[i].yFrom);
			to[i] = new Point2D.Double(ftt[i].xTo, ftt[i].yTo);
			select[i] = new Point2D.Double(ftt[i].xSelect, ftt[i].ySelect);
			mt[i] = (int)ftt[i].mt;
			// System.out.println(ftt[i].getTrialDebug());
		}
		
		//for (int i = 0; i < from.length; ++i)
		//{
		//	System.out.println(String.format(
		//			"FittsTaskSequence: xFrom=%6.1f, yFrom=%6.1f, xTo=%6.1f, yTo=%6.1f, xSelect=%6.1f, ySelect=%6.1f, mt=%4d",
		//			from[i].x, from[i].y, to[i].x, to[i].y, select[i].x, select[i].y, mt[i]));
		//}

		// OK, let the Throughput object work its magic!
		tp.setData("nocode", a, w, taskTypeCode, Throughput.SERIAL, from, to, select, mt);
	}

	public double getErrorRate()
	{		
		return errorRate;		
	}

	public int getA()
	{
		// return (int)Math.round(tp.getA());
		return a;
	}

	public int getW()
	{
		// return (int)Math.round(tp.getW());
		return w;
	}

	public double getTotalErrors()
	{
		return tp.getMisses();
	}

	public double getAe()
	{
		return tp.getAe();
	}

	public double getWe()
	{
		return tp.getWe();
	}

	public double getPT()
	{
		return pt;
	}

	public double getST()
	{
		return st;
	}

	public double getMT()
	{
		return tp.getMT();
	}

	public double getTRE()
	{
		return tre;
	}

	public double getTAC()
	{
		return tac;
	}

	public double getMDC()
	{
		return mdc;
	}

	public double getODC()
	{
		return odc;
	}

	public double getMV()
	{
		return mv;
	}

	public double getME()
	{
		return me;
	}

	public double getMO()
	{
		return mo;
	}

	// this is the data, as written to the .sd2 file
	public String getSequenceData()
	{
		return getSequenceRepeatCount() + "," + getNumberOfTrials() + "," + getA() + "," + getW() + "," + getID() + ","
				+ getAe() + "," + getWe() + "," + getIDe() + "," + getPT() + "," + getST() + "," + getMT() + ","
				+ getErrorRate() + "," + getTP() + "," + getTRE() + "," + getTAC() + "," + getMDC() + "," + getODC()
				+ "," + getMV() + "," + getME() + "," + getMO();
	}

	public String getTrialData(int n)
	{
		return n + "," + ftt[n].getTrialData();
	}

	public static String getSequenceHeader()
	{
		return "SRC,Trials,A,W,ID,Ae,We,IDe(bits),PT(ms),ST(ms),MT(ms),ER(%),TP(bps),TRE,TAC,MDC,ODC,MV,ME,MO";
	}

	// this is the data, as presented in the popup window at the end of a sequence
	public String getSequenceSummary()
	{
		return "TASK CONDITIONS:\n" + String.format("   Trials = %d\n", tp.getNumberOfTrials())
				+ String.format("   A = %1.1f\n", tp.getA()) + String.format("   W = %1.1f\n", tp.getW())
				+ String.format("   ID = %1.1f bits\n", tp.getID()) + "MOVEMENT BEHAVIOUR:\n"
				+ String.format("   Ae = %1.1f\n", tp.getAe()) + String.format("   We = %1.1f\n", tp.getWe())
				+ String.format("   IDe = %1.1f bits\n", tp.getIDe())
				+ String.format("   Sequence repeats = %d\n", this.getSequenceRepeatCount())
				+ String.format("   Errors = %d\n", tp.getMisses()) + "PARTICIPANT PERFORMANCE:\n"
				+ String.format("   MT = %1.1f ms/trial\n", tp.getMT())
				+ String.format("   ER = %1.1f%%\n", tp.getErrorRate())
				+ String.format("   TP = %1.2f bits/s", tp.getThroughput());
	}

	public String getRepeatSequenceMessage()
	{
		return "OOPS!!!\n" + "Too many errors: " + tp.getMisses() + "\n" + "Please Repeat Sequence";
	}
}
