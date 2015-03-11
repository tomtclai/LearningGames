package LinxLibrary;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import BezierCurve.BezierSpline;
import BezierCurve.Vector2;
import Engine.GameObject;

class Path {
	public enum PathTravelDirection
    {
        ePathTravelForward, ePathTravelReverse
    };
    
    private BezierSpline mBezier;

    protected List<GameObject> mPathToDraw; // all the nodes and the connecting lines
    
    public Path()
    {
        mBezier = new BezierSpline();
        mPathToDraw = new Vector<GameObject>();
    }

    public Engine.Vector2 getPoint(float t)
    {
        Engine.Vector2 engine = new Engine.Vector2();
        Vector2 bezier = mBezier.getPoint(t);
        engine.set(bezier.getX(), bezier.getY());
        return engine;
    }

    public Engine.Vector2 getDirection(float t)
    {
        Engine.Vector2 engine = new Engine.Vector2();
        Vector2 bezier = mBezier.getDirection(t);
        engine.set(bezier.getX(), bezier.getY());
        return engine;
    }

    public float getSpeed(float t)
    {
        return mBezier.getSpeed(t);
    }


    public void loadPath(String filepath) throws IOException
    {
        // make sure the path is clear
        clearPath();
        
        // read the file
        String[] lines;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String line;
            Vector<String> l = new Vector<String>();
            while ((line = in.readLine()) != null)
            {
                    l.add(line);
            }
            lines = new String[l.size()];

            for (int i = 0; i < l.size(); i++)
            {
                    lines[i] = (String) l.get(i);
            }
            in.close();
        } catch(FileNotFoundException e) { 
            // Use the hard coded values if file not found
            lines = new String[] {"52,20.5", "15,42", "74,40", "72,26",
                "20,32", "22,8", "78,8", "92,13"};                                                
        }
        
        // split the line into x and y, parse into Vector2
        Vector<Vector2> knots = new Vector<>();
        for (int i = 1; i < lines.length; i++)
        {
            String[] points = lines[i].split(",");
            Vector2 pos = new Vector2(Float.parseFloat(points[0]), Float.parseFloat(points[1]));
            knots.add(new Vector2(pos.getX(), 
                pos.getY()));
        }

        // setup the curve to pass through the control points - 'knots'
        Vector2[] k = new Vector2[knots.size()];
        for (int i = 0; i < knots.size(); i++)
        {
        	k[i] = knots.get(i);
        }
        knots.clear();
        knots = null;
        mBezier.setupCurve(k);

        // actually draw this curve
        drawCurve();            
    }


    public void setCurve(Vector2[] knots)
    {
        // setup the curve with these knots (erases old curve data)
        mBezier.setupCurve(knots);
    }

    private void drawCurve()
    {
        // from t = 0->1    
        Vector2 plotPoint;
        float t = 0;
        while (t < 1)
        {
            // find the point at 't' and add it to the curve
            plotPoint = mBezier.getPoint(t);
            GameObject point = new GameObject();
            point.setCenter(plotPoint.getX(), plotPoint.getY());
            point.setSize(1.2f, 2f);
            plotPoint = mBezier.getDirection(t);
            point.setRotation((float) Math.toDegrees(Math.atan2(plotPoint.getY(), plotPoint.getX())));
            point.setImage("Rail_Mid.png");
            mPathToDraw.add(point);
            // move 't' by the "speed" of the curve at this point for constant movement speed
            t += mBezier.getSpeed(t) * 10;
        }
    }

    public void clearPath()
    {
        // stop drawing the path
    	for (int i = 0; i < mPathToDraw.size(); i++)
    		mPathToDraw.get(i).destroy();

        // clear it
        mPathToDraw.clear();
    }
}