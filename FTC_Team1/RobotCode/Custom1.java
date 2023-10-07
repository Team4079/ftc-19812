
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous

public class Custom1 extends LinearOpMode {
    private DcMotor left;
    private DcMotor right;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime bigCounter = new ElapsedTime();
    private DcMotor slide;
    private Servo dumper;
    
    private final double wheelCircumference = 90*3.14;
    private final double gearReduction = 72.0;
    private final double counts = 4.0;
    
    private final double rev = counts*gearReduction;
    private final double revPerMM = rev/wheelCircumference;
    private final double inches = revPerMM*25.4;
    /*
     * Specify the source for the Tensor Flow Model.
     * If the TensorFlowLite object model is included in the Robot Controller App as an "asset",
     * the OpMode must to load it using loadModelFromAsset().  However, if a team generated model
     * has been downloaded to the Robot Controller's SD FLASH memory, it must to be loaded using loadModelFromFile()
     * Here we assume it's an Asset.    Also see method initTfod() below .
     */
    private static final String TFOD_MODEL_ASSET = "model_unquant.tflite";
    private static final String TFOD_MODEL_FILE  = "model_unquant.tflite";


    private static final String[] LABELS = {
            "yellow",
            "bg",
            "otheryellow"
    };
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AUscD7//////AAABmWjo21/+j0eis3A9VPzgcj+AdwgUeZsSDdcXpSNf1N/5xJLjfwcdKb+jXrd+77iyA848zq6fW568wfYOV82hVXlt/U4k/wujQcurYu48zkmJfxRYbCJHxybBjM2x0oFD2DGOXY/0Vb247FDY+DySXqFGGABlQLaIciGzmr5uGyfvC0edIQ6h+HFtax3be7KPjbbsk8w5ax6dN03ypkrX8twP+GcgEvvwuGNLRcEPbPvJ/mqNKJbX4KMz0iRBVGHMC7SOxpWGronkQFjTYudRJSLRaT7q37vPmJueTYp3FjA2hCMp0NJ8GJefkTHVGyEbeU5wtf5O13YK648SIPhT2pwURPL1UZ7sZEkKfeRJhBo7";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    
    
    @Override
    public void runOpMode() {

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        
     
        
        /** Wait for the game to begin */
        
        
        waitForStart();
        boolean atDestination = false;
        boolean run = true;
  
        bigCounter.reset();
        while (opModeIsActive()&&(bigCounter.seconds()<15.0))
        {
            while ((opModeIsActive())&&(atDestination==false) && (bigCounter.seconds()<30.0)) {
                telemetry.addData(">", "Running tfod");
                telemetry.addData("BigCounter", bigCounter.seconds());
                telemetry.addData("Run", run);
                telemetry.update();
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData("timer", runtime.seconds());
                            runtime.reset();
                            while (opModeIsActive()&&atDestination==false)
                            {
                                //move straight
                                if (recognition.getLabel()==LABELS[1])
                                {
                                    tfod.deactivate();
                                    runtime.reset();
                                    while(opModeIsActive()&&(runtime.seconds()<0.4))
                                    {
                                        telemetry.addData("Runtime", runtime.seconds());
                                        telemetry.addData("Label", LABELS[1]);
                                        telemetry.update();
                                    }
                                }
                                //move right
                                if (recognition.getLabel()==LABELS[2])
                                {
                                    tfod.deactivate();
                                    runtime.reset();
                                    while(opModeIsActive()&&(runtime.seconds()<2.4))
                                    {
                                        telemetry.addData("Runtime", runtime.seconds());
                                        telemetry.addData("Label", LABELS[2]);
                                        telemetry.update();
                                    }
                                }
                                //move left
                                if (recognition.getLabel()==LABELS[0])
                                {
                                    tfod.deactivate();
                                    runtime.reset();
                                    while(opModeIsActive()&&(runtime.seconds()<2.4))
                                    {                                    
                                        telemetry.addData("Runtime", runtime.seconds());
                                        telemetry.addData("Label", LABELS[0]);
                                        telemetry.update();
                                    }
                                }
                            }
                            //move straight
                            //telemetry.update();
                        }
                    }
                }
            }
            //telemetry.addData("Run: ", run);
            telemetry.update();
            runtime.reset();
            while(opModeIsActive()&&(runtime.seconds()<2.0))
            {
                //left.setPower(0.9);
                //right.setPower(0.8);
            }
            atDestination=true;                            
        }
    }
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
