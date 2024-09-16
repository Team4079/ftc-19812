package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="TwoOperatorRobotOriented", group="Iterative Opmode")
public class TwoOperatorRobotOriented extends OpMode {

    /*
     * The mecanum drivetrain involves four separate motors that spin in
     * different directions and different speeds to produce the desired
     * movement at the desired speed.
     */
    // declare and initialize four DcMotors.
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_left;
    private DcMotor back_right;
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
    
    private boolean slowmode;
    private double slideLastPressed;
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
    public void init() {
        timesofar.reset();
        servoLastPressed = timesofar.time();
        slideLastPressed = timesofar.time();
        armLastPressed = timesofar.time();
        slowmode = false;
        servoState = false;
        armState = 0;
        // Name strings must match up with the config on the Robot Controller
        front_left = hardwareMap.get(DcMotor.class, "fLeft");
        front_right = hardwareMap.get(DcMotor.class, "fRight");
        back_left = hardwareMap.get(DcMotor.class, "bLeft");
        back_right = hardwareMap.get(DcMotor.class, "bRight");

        LSlideVert = hardwareMap.get(DcMotor.class, "LSlideVert");
        RSlideVert = hardwareMap.get(DcMotor.class, "RSlideVert");
        leftServo = hardwareMap.get(CRServo.class, "leftServo");
        rightServo = hardwareMap.get(CRServo.class, "rightServo");
        RSlideMotor = hardwareMap.get(DcMotor.class, "RightSlideMotor");
        LSlideMotor = hardwareMap.get(DcMotor.class, "LeftSlideMotor");
        planeServo = hardwareMap.get(CRServo.class, "planeServo");
        back_right.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        // Mecanum drive is controlled with three axes: drive (front-and-back),
        // strafe (left-and-right), and twist (rotating the whole chassis).
        double drive  = gamepad1.left_stick_y*0.8;
        double strafe = gamepad1.left_stick_x*0.8;
        double twist  = gamepad1.right_stick_x*0.9;
        
        if (gamepad2.right_bumper){
            armState = 1;
        } else if(gamepad2.left_bumper){
            armState = 2;
        } else if(armState != 3){
            armState = 0;
            LSlideVertPower = 0.1;
            RSlideVertPower = -0.1;
        }
        
        if(gamepad2.right_trigger > 0.5){
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
        if (gamepad2.b) {
            LSlidePower = -1.0;
            RSlidePower = 0.85;
        } else if (gamepad2.x) {
            LSlidePower = 1.0;
            RSlidePower = -0.85;
        } else {
            LSlidePower = 0;
            RSlidePower = 0;
        }
        
        if (gamepad1.dpad_left) {
            LSlidePower = 0.50;
            RSlidePower = -0.425;
        }
        

        
        // claw
        if (gamepad2.a) {
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
        
        if(gamepad2.dpad_up) { //small nudge towards closing to counteract gear slippage (uhh kinda redundant now with the SRS)
            if(timesofar.time() - servoLastPressed > 0.5){
                servoLastPressed = timesofar.time();
                extraServoPower += 0.05;
            }
        }
        if(gamepad2.dpad_down) { //small nudge towards opening to counteract gear slippage (yeah this one too)
            if(timesofar.time() - servoLastPressed > 0.5){
                servoLastPressed = timesofar.time();
                extraServoPower -= 0.05;
            }
        }

        RServoPower -= extraServoPower;
        LServoPower += extraServoPower;
        
        // ^ Wait couldn't we use one bool and where's the other servo rishi
        
        /*
         * If we had a gyro and wanted to do field-oriented control, here
         * is where we would implement it.
         * 
         * The idea is fairly simple; we have a robot-oriented Cartesian (x,y)
         * coordinate (strafe, drive), and we just rotate it by the gyro
         * reading minus the offset that we read in the init() method.
         * Some rough pseudocode demonstrating:
         *
         * if Field Oriented Control:
         *     get gyro heading
         *     subtract initial offset from heading
         *     convert heading to radians (if necessary)
         *     new strafe = strafe * cos(heading) - drive * sin(heading)
         *     new drive  = strafe * sin(heading) + drive * cos(heading)
         *
         * If you want more understanding on where these rotation formulas come
         * from, refer to
         * https://en.wikipedia.org/wiki/Rotation_(mathematics)#Two_dimensions
         */

        // You may need to multiply some of these by -1 to invert direction of
        // the motor.  This is not an issue with the calculations themselves.
        double[] speeds = {
            (drive + -strafe + -twist),
            (drive - -strafe - -twist),
            (drive - -strafe + -twist),
            (drive + -strafe - -twist)
        };

        // Because we are adding vectors and motors only take values between
        // [-1,1] we may need to normalize them.

        // Loop through all values in the speeds[] array and find the greatest
        // *magnitude*.  Not the greatest velocity.
        double max = Math.abs(speeds[0]);
        for(int i = 0; i < speeds.length; i++) {
            if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
        }
        // If and only if the maximum is outside of the range we want it to be,
        // normalize all the other speeds based on the given speed value.
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) 
            {
                speeds[i] /= max;
            }
        }
        if(this.gamepad1.x&&slowmode == false)
        {
            slowmode=true;
        } else if(this.gamepad1.y&&slowmode == true) {
            slowmode=false;
        }
        
        if(slowmode==true) {
            speeds[0] /= 2;
            speeds[1] /= 2;
            speeds[2] /= 2;
            speeds[3] /= 2;
        }
        
        telemetry.addData("slowmode", slowmode);
        telemetry.addData("front_left", speeds[0]);
        telemetry.addData("front_right", speeds[1]);
        telemetry.addData("back_left", speeds[2]);
        telemetry.addData("back_right", speeds[3]);
        telemetry.addData("LSlideMotor", LSlideVertPower);
        telemetry.addData("RSlideMotor", RSlideVertPower);
        telemetry.addData("LServo", LServoPower);
        telemetry.addData("RServo", RServoPower);
        telemetry.addData("LExtend", LSlidePower);
        telemetry.addData("RExtend", RSlidePower);
        
        telemetry.addData("Break", "Break");
        telemetry.addData("armState", armState);
        telemetry.addData("ServoState", servoState);
        telemetry.addData("PlanePower", planePower);
        telemetry.update();
        // apply the calculated values to the motors.
        front_left.setPower(speeds[0]);
        front_right.setPower(speeds[1]);
        back_left.setPower(speeds[2]);
        back_right.setPower(speeds[3]);
        LSlideVert.setPower(LSlideVertPower);
        RSlideVert.setPower(RSlideVertPower);
        leftServo.setPower(LServoPower);
        rightServo.setPower(RServoPower);
        RSlideMotor.setPower(RSlidePower);
        LSlideMotor.setPower(LSlidePower);
        planeServo.setPower(planePower);
    }
}


