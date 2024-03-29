package goyal.arpit.testassignment.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import goyal.arpit.testassignment.global.Vars;
import goyal.arpit.testassignment.model.TouchInformation;
import goyal.arpit.testassignment.tasks.VolleyPost;

/**
 * Created by arpit on 12/08/16.
 */
public class TouchView extends View {

    private Paint mPaint;
    private Path mPath;
    private Context mContext;

    /**
     * Constructor for touch view
     * @param context
     * @param attrs
     */
    public TouchView(Context context, AttributeSet attrs) {
        super(context);

        mContext = context;

        mPath = new Path();

        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TouchInformation temp = new TouchInformation();
        temp.xCoord = event.getX();   // x-coordinate
        temp.yCoord = event.getY();   //y-coordinate
        temp.pressure = event.getPressure();  //pressure
        temp.eventTime = event.getEventTime();  //size
        temp.size = event.getSize();  //event time

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Vars.touchInformationList.clear();      //singleton object to hold touch information is cleared when user first puts his finger
                mPath.moveTo(temp.xCoord, temp.yCoord);
                Vars.touchInformationList.add(temp);     //add current touchpoint to the list
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(temp.xCoord, temp.yCoord);  //draw a line to the point
                Vars.touchInformationList.add(temp);
                break;
            case MotionEvent.ACTION_UP:
                fingerRemoved();
                break;
            default:
                return false;
        }
        invalidate();     //refreshing the view
        return true;
    }

    /**
     * This function is called when user has lifted his finger
     */
    public void fingerRemoved() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            JSONArray array = new JSONArray(gson.toJson(Vars.touchInformationList));
            (new VolleyPost(mContext, array)).postData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Some error has occured", Toast.LENGTH_SHORT).show();
        }
    }

}
