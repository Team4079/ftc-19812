//CloseBlue
package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name="SutraAutoCloseBlueFlowchart", group="Linear Opmode")


public class SutraAutoCloseBlueFlowchart extends LinearOpMode {
    private final double wheelCircumference = 75*3.14;
    private final double gearReduction = 3.61*5.23;
    private final double counts = 28.0;
    
    private final double rev = counts*gearReduction;
    private final int revPerMM = (int)rev/(int)wheelCircumference;
    private final double inches = revPerMM*25.4;

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive;
    private DcMotorEx leftBackDrive;
    private DcMotorEx rightFrontDrive;
    private DcMotorEx rightBackDrive;
    private CRServo leftServo;
    private CRServo rightServo;
    private DcMotor leftArm;
    private DcMotor rightArm;
    private DcMotor RSlideMotor; //extend
    private DcMotor LSlideMotor; //extend
    private String testCase = "";
    
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "MyModelStoredAsAsset.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/ROLL-TAPE-Biased.tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
      "blue",
      "red",
    };
    
    private TfodProcessor tfod;
    private String detectedObj;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;
    
    @Override
    public void runOpMode() {
        
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "fLeft");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "bLeft");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "fRight");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "bRight");
        leftArm = hardwareMap.get(DcMotor.class, "LSlideVert");
        rightArm = hardwareMap.get(DcMotor.class, "RSlideVert");
        rightServo = hardwareMap.get(CRServo.class, "rightServo");
        leftServo = hardwareMap.get(CRServo.class, "leftServo");
        RSlideMotor = hardwareMap.get(DcMotor.class, "RightSlideMotor");
        LSlideMotor = hardwareMap.get(DcMotor.class, "LeftSlideMotor");

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorEx.Direction.FORWARD);
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int count=0;
        
        initTfod();
        
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()&&count==0) {
            servoPower(1.0,"left");
            servoPower(-1.0, "right");
            sleep(1000);
            
            armUp(); 
            sleep(50);
            armHold();
            
            // So the code below sucks ass, but I'll keep it for now
            
            //set pos -----------------------------------------------------------------------
            driveEncoders(640);
            
            //scan object - mid -------------------------------------------------------------
            for(int i=0; i < 100; i++){
                telemetryTfod();
                sleep(5);
            }
            
            if(detectedObj == "blue" || detectedObj == "red"){
                placePurplePixel();
                testCase="mid";
            }
            sleep(50);
            
            if(testCase !="mid"){
                turnRight(50);
                
                // scan right spike
                
                for(int i=0; i < 100; i++){
                    telemetryTfod();
                    sleep(5);
                }
                sleep(50);
                
                if(detectedObj == "blue" || detectedObj == "red"){
                    placePurplePixel();
                    testCase="right";
                }
                turnLeft(50); //orient back to mid
            }
            
            if(testCase !="right" && testCase !="mid"){
                turnLeft(50);
                //facing left spike
                placePurplePixel();
                turnRight(50); //orient back to mid
            }
            
            turnLeft(50);
            
            driveEncoders(884);
            
            armUp();
            sleep(500);
            armHold();
            
            armExtend();
            sleep(1500);
            armStop();
            
            servoPower(0.6, "right");
            sleep(300);
            servoPower(-1.0, "right");
            
            armRetract();
            sleep(1500);
            armStop();
            
            leftEncoders(550);
            driveEncoders(500);
            
            // if(detectedObj == "blue"){
            //     parkBlue();
            // } else {
            //     parkRed();
            // }
            
            servoPower(-0.6,"left");
            servoPower(0.6, "right");
            
            // Show the elapsed game time and wheel power.
            count++;
        }
    }
    
    
    //Handy functions
    
    public void placePurplePixel()
    {
        driveEncoders(200);
        servoPower(-0.6, "left");
        sleep(200);
        backEncoders(200);
        servoPower(1.0, "left");
        
    }
    
    public void parkBlue()
    {
        backEncoders(640);
        turnLeft(90);
        driveEncoders(1168);
        servoPower(0.0, "right");
    }
    
    public void parkRed()
    {
        backEncoders(640);
        turnRight(90);
        driveEncoders(1168);
        servoPower(0.0, "right");
    }
    
    public void armExtend()
    {
        LSlideMotor.setPower(-1.0);
        RSlideMotor.setPower(0.85);
    }
    
    public void armStop()
    {
        LSlideMotor.setPower(0.0);
        RSlideMotor.setPower(0.0);
    }
    
    public void armRetract()
    {
        LSlideMotor.setPower(1.0);
        RSlideMotor.setPower(-0.85);
    }
    
    public void input(double leftFront, double rightFront, double leftBack, double rightBack)
    {
        leftFrontDrive.setPower(leftFront);
        rightFrontDrive.setPower(rightFront);
        leftBackDrive.setPower(leftBack);
        rightBackDrive.setPower(rightBack);
        sleep(500);
    }
    public void turnLeft(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(-convert, convert, -convert, convert);
    }
    public void turnRight(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(convert, -convert, convert, -convert);
    }
    public void driveEncoders(int target)
    {
        encoders(target, target, target, target);
    }
    public void backEncoders(int target)
    {
        encoders(-target, -target, -target, -target);
    }
    public void rightEncoders(int target)
    {
        encoders(target, -target, -target, target);
    }
    public void leftEncoders(int target)
    {
        encoders(-target, target, target, -target);
    }
    public void leftTopDiagonal(int target)
    {
        encoders(0, target, target, 0);
    }
    public void rightTopDiagonal(int target)
    {
        encoders(target, 0, 0, target);
    }
    public void leftBottomDiagonal(int target)
    {
        encoders(-target, 0, 0, -target);
    }
    public void rightBottomDiagonal(int target)
    {
        encoders(0, -target, -target, 0);
    }
    public void encoders(int leftFront, int rightFront, int leftBack, int rightBack)
    {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        leftFrontDrive.setTargetPosition(leftFront*revPerMM);
        rightFrontDrive.setTargetPosition(rightFront*revPerMM);
        leftBackDrive.setTargetPosition(leftBack*revPerMM);
        rightBackDrive.setTargetPosition(rightBack*revPerMM);
        
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        double power=0.2;
        leftFrontDrive.setVelocity(1500);
        rightFrontDrive.setVelocity(1500);
        leftBackDrive.setVelocity(1500);
        rightBackDrive.setVelocity(1500);
        while (opModeIsActive()&&leftFrontDrive.isBusy()||rightFrontDrive.isBusy()||leftBackDrive.isBusy()||rightBackDrive.isBusy())
        {
        }
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
    //THIS PART DOWNWARDS ONLY SETS POWER, NOT POSITION
    public void servoPower(double power, String servoUsed)
    {
        if(servoUsed == "left"){
          leftServo.setPower(power);
        } else {
          rightServo.setPower(power);
        }
    }
    public void armHold()
    {
        rightArm.setPower(-0.1);
        leftArm.setPower(0.1);
    }
    public void armUp(){
        rightArm.setPower(-0.6);
        leftArm.setPower(0.6);
    }
    public void setTheArmPowersToZeroSoTheyFallAndAreStoppedByTheHardstop(){
        rightArm.setPower(0.0);
        leftArm.setPower(0.0);
    }
    public void drive()
    {
        leftFrontDrive.setPower(1.0);
        rightFrontDrive.setPower(1.0);
        leftBackDrive.setPower(1.0);
        rightBackDrive.setPower(1.0);
        //input(0.4, 0.4, 0.4, 0.4);
    }
    public void topRight()
    {
        input(0.4, 0, 0, 0.4);
    }
    public void right()
    {
        input(0.4, -0.4, -0.4, 0.4);
    }
    public void bottomRight()
    {
        input(0, -0.4, -0.4, 0);
    }
    public void back()
    {
        input(-0.4, -0.4, -0.4, -0.4);
    }
    public void bottomLeft()
    {
        input(-0.4, 0, 0, -0.4);
    }
    public void left()
    {
        input(-0.4, 0.4, 0.4, -0.4);   
    }
    public void topLeft()
    {
        input(0, 0.4, 0.4, 0);
    }
    public void stop(double time)
    {
        input(0, 0, 0, 0);
    }
    
    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

            // With the following lines commented out, the default TfodProcessor Builder
            // will load the default model for the season. To define a custom model to load, 
            // choose one of the following:
            //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
            //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
            //.setModelAssetName(TFOD_MODEL_ASSET)
            .setModelFileName(TFOD_MODEL_FILE)

            // The following default settings are available to un-comment and edit as needed to 
            // set parameters for custom models.
            .setModelLabels(LABELS)
            .setIsModelTensorFlow2(true)
            .setIsModelQuantized(true)
            .setModelInputSize(300)
            .setModelAspectRatio(16.0 / 9.0)

            .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()
    
    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
            
            detectedObj = recognition.getLabel();

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
            telemetry.addData("DetectedOBJ:", detectedObj);
        }   // end for() loop
        
        telemetry.update();

    }   // end method telemetryTfod()
    
}


