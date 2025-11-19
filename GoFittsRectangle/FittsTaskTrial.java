import java.awt.geom.Point2D;
import java.util.ArrayList;

// ---------------------
// F I T T S T R I A L
// ---------------------

class FittsTaskTrial
{
	// conditions
	public double amplitude; // amplitude (of presented trial)
	public double width; // width (of presented trial)
	public double xFrom;
	public double yFrom;
	public double xTo;
	public double yTo;
	public double xSelect;
	public double ySelect;

	// observations
	private double ae; // effective ('actual') amplitude
	private double dx; // delta x
	private long pt; // positioning time
	private long st; // selection time
	public long mt; // movement time (Note: mt = pt + st)
	private int err; // error
	private long[] t; // timestamps

	AccuracyMeasures am;
	int taskType;

	FittsTaskTrial(int aArg, int wArg, int taskTypeArg)
	{
		amplitude = aArg;
		width = wArg;
		taskType = taskTypeArg;
	}

	public void setAe(double aeArg)
	{
		ae = aeArg;
	}

	public void setDx(double dxArg)
	{
		dx = dxArg;
	}

	public void setPT(long ptArg)
	{
		pt = ptArg;
	}

	public void setST(long stArg)
	{
		st = stArg;
	}

	public void setMT(long mtArg)
	{
		mt = mtArg;
	}

	public void setErr(int errArg)
	{
		err = errArg;
	}

	public void setXFrom(double x)
	{
		xFrom = x;
	}

	public void setYFrom(double y)
	{
		yFrom = y;
	}

	public void setXTo(double x)
	{
		xTo = x;
	}

	public void setYTo(double y)
	{
		yTo = y;
	}

	public void setXSelect(double x)
	{
		xSelect = x;
	}

	public void setYSelect(double y)
	{
		ySelect = y;
	}

	public void setTraceSamples(ArrayList<FittsTask.TraceSample> ts)
	{
		// time stamps
		t = new long[ts.size()];
		for (int i = 0; i < t.length; ++i)
			t[i] = ts.get(i).t;

		Point2D.Double[] p = new Point2D.Double[ts.size()];
		for (int i = 0; i < p.length; ++i)
			p[i] = new Point2D.Double(ts.get(i).point.x, ts.get(i).point.y);

		// compute accuracy measures
		am = new AccuracyMeasures(taskType, new Point2D.Double(xFrom, yFrom), new Point2D.Double(xTo, yTo), width, p);
	}

	// get-methods for positioning time and selection time (NOTE: MT = PT + ST)
	

	public long getPT()
	{
		return pt;
	}

	public long getST()
	{
		return st;
	}

	public long getMT()
	{
		return mt;
	}	
	
	public int getError()
	{
		return err;
	}
	
	// get-methods for accuracy measures
	public int getTRE()
	{
		return am.getTRE();
	}

	public int getTAC()
	{
		return am.getTAC();
	}

	public int getMDC()
	{
		return am.getMDC();
	}

	public int getODC()
	{
		return am.getODC();
	}

	public double getMV()
	{
		return am.getMV();
	}

	public double getME()
	{
		return am.getME();
	}

	public double getMO()
	{
		return am.getMO();
	}

	public String toString()
	{
		return "A=" + amplitude + " W=" + width + " Ae=" + ae + " dx=" + dx + " pt=" + pt + " st=" + st + " mt=" + mt
				+ " err=" + err + " tre=" + getTRE() + " tac=" + getTAC() + " mdc=" + getMDC() + " odc=" + getODC()
				+ " mv=" + getMV() + " me=" + getME() + " mo=" + getMO();
	}

	public String getTrialData()
	{
		return amplitude + "," + width + "," + ae + "," + dx + "," + pt + "," + st + "," + mt + "," + err + ","
				+ getTRE() + "," + getTAC() + "," + getMDC() + "," + getODC() + "," + getMV() + "," + getME() + ","
				+ getMO();
	}

	public String getTrialDebug()
	{
		return String.format("A=%d, W=%d, fromX=%d, fromY=%d, toX=%d, toY=%d, selectX=%d, selectY=%d", amplitude, width,
				xFrom, yFrom, xTo, yTo, xSelect, ySelect);
	}

	public static String getTrialHeader()
	{
		return "A,W,Ae,dx,PT(ms),ST(ms),MT(ms),Errors,TRE,TAC,MDC,ODC,MV,ME,MO";
	}
}
