package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="MainCode", group="Iterative Opmode")
public class RishiSecret extends LinearOpMode {
    
    //Initialize variables
    private DcMotor LSlideVert; //pivot
    private DcMotor RSlideVert; //pivot
    private DcMotor RSlideMotor; //extend
    private DcMotor LSlideMotor; //extend
    private CRServo leftServo;
    private CRServo rightServo;
    private CRServo planeServo;
    private double power=1;
    private double LSlideVertPower; //arm pivot motors
    private double RSlideVertPower;
    private double LSlidePower;
    private double RSlidePower;
    
    private double slideLastPressed;
    private int slideState;
    private double RServoPower;
    private double LServoPower;
    private double planePower;
    
    public ElapsedTime timesofar = new ElapsedTime();
    private double servoLastPressed;
    private boolean servoState = true;
    private int armState;
    private double armLastPressed;
    private boolean extraServoBool = false;
    private double extraServoPower = 0;
    
    
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("fLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("bLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("fRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("bRight");
        
        timesofar.reset();
        servoLastPressed = timesofar.time();
        slideLastPressed = timesofar.time();
        armLastPressed = timesofar.time();
        servoState = false;
        slideState = 0;
        armState = 0;

        LSlideVert = hardwareMap.get(DcMotor.class, "LSlideVert");
        RSlideVert = hardwareMap.get(DcMotor.class, "RSlideVert");
        leftServo = hardwareMap.get(CRServo.class, "leftServo");
        rightServo = hardwareMap.get(CRServo.class, "rightServo");
        RSlideMotor = hardwareMap.get(DcMotor.class, "RightSlideMotor");
        LSlideMotor = hardwareMap.get(DcMotor.class, "LeftSlideMotor");
        planeServo = hardwareMap.get(CRServo.class, "planeServo");

        

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);
        
        
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;


            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;
        
        if (gamepad1.right_bumper){
            armState = 1;
        } else if(gamepad1.left_bumper){
            armState = 2;
        } else if(armState != 3){
            armState = 0;
            LSlideVertPower = 0.1;
            RSlideVertPower = -0.1;
        }
        
        if(gamepad1.right_trigger > 0.5){
            if(timesofar.time() - armLastPressed > 0.5){
                armLastPressed = timesofar.time();
                if(armState == 3){
                    armState = 0;
                } else {
                    armState = 3;
                }
            }
        }
        
        //state 0 is holding the arms there
        
        if(armState == 1){ //up
            LSlideVertPower = 0.6;
            RSlideVertPower = -0.6;
        } else if(armState == 2){ //down (slowly) find a way to scale with arm pos (uhh encoders)
            LSlideVertPower = 0.005;
            RSlideVertPower = -0.005;
        } else if(armState == 3){ //downerly (free fall)
            LSlideVertPower = 0.0;
            RSlideVertPower = 0.0;
        }
        
        //armes slide espanol (wtf)
        if (gamepad1.b) {
            if(timesofar.time() - slideLastPressed > 0.2){
                slideLastPressed = timesofar.time();
                
                if(slideState < 4){
                    slideState++;
                } else {
                    slideState = 1;
                }        
                
            }
        }
        
        
        if (slideState == 1) { //extend
            LSlidePower = -1.0;
            RSlidePower = 0.85;
        } else if(slideState == 2){ //stop
            LSlidePower = 0.0;
            RSlidePower = 0.0;
        } else if(slideState == 3){ //retract
            LSlidePower = 0.5;
            RSlidePower = -0.425;
        } else if(slideState == 4){ //stop
            LSlidePower = 0.0;
            RSlidePower = 0.0;
        } else { //failsafe
            LSlidePower = 0.0;
            RSlidePower = 0.0;
        }
        
        
        // claw
        if (gamepad1.a) {
            if(timesofar.time() - servoLastPressed > 0.3){
                servoLastPressed = timesofar.time();
                servoState = !servoState;
            }
        }
        
        if (gamepad1.left_trigger > 0.95) {
            planePower = 1.0;
        } else {
            planePower = 0.0;
        }
        
        //Leftservo, + is in - is out
        
        if(servoState){ //let go
            RServoPower = 0.6;
            LServoPower = -0.6;
        } else { // grabby
            RServoPower = -1.0;
            LServoPower = 1.0;
        }
        
        if(gamepad1.dpad_up) { //small nudge towards closing to counteract gear slippage (uhh kinda redundant now with the SRS)
            if(timesofar.time() - servoLastPressed > 0.5){
                servoLastPressed = timesofar.time();
                extraServoPower += 0.05;
            }
        }
        if(gamepad1.dpad_down) { //small nudge towards opening to counteract gear slippage (yeah this one too)
            if(timesofar.time() - servoLastPressed > 0.5){
                servoLastPressed = timesofar.time();
                extraServoPower -= 0.05;
            }
        }

        RServoPower -= extraServoPower;
        LServoPower += extraServoPower;


        
        telemetry.addData("front_left", frontLeftPower);
        telemetry.addData("front_right", frontRightPower);
        telemetry.addData("back_left", backLeftPower);
        telemetry.addData("back_right", backRightPower);
        telemetry.addData("LSlideMotor", LSlideVertPower);
        telemetry.addData("RSlideMotor", RSlideVertPower);
        telemetry.addData("LServo", LServoPower);
        telemetry.addData("RServo", RServoPower);
        telemetry.addData("LExtend", LSlidePower);
        telemetry.addData("RExtend", RSlidePower);
        
        telemetry.addData("Break", "Break");
        telemetry.addData("armState", armState);
        telemetry.addData("ServoState", servoState);
        telemetry.addData("SlideState", slideState);
        telemetry.addData("PlanePower", planePower);
        telemetry.update();
        LSlideVert.setPower(LSlideVertPower);
        RSlideVert.setPower(RSlideVertPower);
        leftServo.setPower(LServoPower);
        rightServo.setPower(RServoPower);
        RSlideMotor.setPower(RSlidePower);
        LSlideMotor.setPower(LSlidePower);
        planeServo.setPower(planePower);
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }
    }
}
