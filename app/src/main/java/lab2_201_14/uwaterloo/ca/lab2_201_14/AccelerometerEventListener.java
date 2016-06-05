package lab2_201_14.uwaterloo.ca.lab2_201_14;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by frederick on 5/17/2016.
 */
public class AccelerometerEventListener  implements SensorEventListener {

    // FIELDS //
    TextView output;
    LineGraphView graph;
    Context context;
    FileOutputStream os;
    PrintWriter osw;
    int maxDataPoints = 5000;
    int currentAmountOfDataPoints = 0;

    float x;
    float y;
    float z;
    // END OF FIELDS //

    // Constructor
    public AccelerometerEventListener(TextView outputView,LineGraphView graphView) {

        output = outputView;
        graph = graphView;
        context = Lab2_201_14.getAppContext();

        // Initializing the output stream and print writer
        try {
            os = new FileOutputStream(new File(context.getExternalFilesDir(null), "external.txt"));
            osw = new PrintWriter(new OutputStreamWriter(os));
        } catch (IOException e) {
            Log.d("CATCHING IOEXCEPTION", "IOException caught!! AHHH!! OMG PANIC!");
        }

    }

    // Initializing sensor max value class
    sensorMaxValue accelerometerMax = new sensorMaxValue();

    public void onAccuracyChanged(Sensor s, int i) {

    }

    public void onSensorChanged(SensorEvent se){

        if(se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Storing sensor readings into fields
            x = se.values[0];
            y = se.values[1];
            z = se.values[2];

            // Screen output
            output.setText("Accelerometer: " + String.valueOf(x + " m/s^2, " + y + " m/s^2, " + z + " m/s^2") + "\n" + String.valueOf(accelerometerMax.getMaxString()) + "\n");

            // Compare current sensor readings to current max
            accelerometerMax.calcMaxX(x);
            accelerometerMax.calcMaxY(y);
            accelerometerMax.calcMaxZ(z);
            // se.values[0] is x-axis, 1 y, 2 z; units m/s^2

            // Add point to graph
            graph.addPoint(se.values);

            // Output data to file
            // A safety precaution to make sure it doesn't output data forever
            if (currentAmountOfDataPoints < maxDataPoints) {
                osw.println(x + " " + y + " " + z);
                currentAmountOfDataPoints++;
            } else if (currentAmountOfDataPoints == maxDataPoints) { // Close file after desired number of data points is reached
                osw.close();
            }

        }



    }

//JUNE 1
//    Context context = Context.MODE_PRIVATE;
//
//    File file = new File(context.getFilesDir(),file);
//    PrintWriter printWriter = new PrintWriter("file.txt");
//    printWriter.append("Hello World" );
//    printWriter.close();
    //File file = new File(context.getFilesDir(), filename);
    ;

}
