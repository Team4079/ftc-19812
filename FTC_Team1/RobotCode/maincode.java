// DO NOT ADD THE FIELD ORIENTED CONTROLS YET. It's still being revised, so use the RishiCodeBACKUP file to make changes until I can get the IMU sorted out. Thanks.
// November 8 - 11:29
package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * This is an example minimal implementation of the mecanum drivetrain
 * for demonstration purposes.  Not tested and not guaranteed to be bug free.
 *
 * @author Michele Obama & Barrack Obama
 */
@TeleOp(name="RishiCode", group="Iterative Opmode")
public class maincode extends OpMode {

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
    private DcMotor LSlideVert;
    private DcMotor RSlideVert;
    private DcMotor slideMotor;
    private CRServo leftServo;
    private CRServo rightServo;
    private double power=1;
    private double LSlideVertPower;
    private double RSlideVertPower;
    private double slidePower;
    private boolean reduce;
    private double slideLastPressed;
    private int slideState;
    private double servoPower;
    public ElapsedTime timesofar = new ElapsedTime();
    private double servoLastPressed;
    private boolean servoState;
    private int armState;
    private double armLastPressed;
    
    @Override
    public void init() {
        timesofar.reset();
        servoLastPressed = timesofar.time();
        slideLastPressed = timesofar.time();
        armLastPressed = timesofar.time();
        reduce = false;
        servoState = false;
        slideState = 0;
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
        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        back_right.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        // Mecanum drive is controlled with three axes: drive (front-and-back),
        // strafe (left-and-right), and twist (rotating the whole chassis).
        double drive  = gamepad1.left_stick_y*0.8;
        double strafe = gamepad1.left_stick_x*0.8;
        double twist  = gamepad1.right_stick_x*0.9;
        
        if (gamepad1.right_bumper){
            armState = 2;
        } else if(gamepad1.left_bumper){
            armState = 1;
        } else if(armState != 3){
            armState = 0;
            LSlideVertPower = 0.2;
            RSlideVertPower = -0.2;
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
        
        if(armState == 1){
            LSlideVertPower = 0.7;
            RSlideVertPower = -0.7;
        } else if(armState == 2){
            LSlideVertPower = 0.015;
            RSlideVertPower = -0.015;
        } else if(armState == 3){
            LSlideVertPower = 0.0;
            RSlideVertPower = 0.0;
        }
        
        if (slideState == 1) {
            slidePower = -0.2;
        } else if(slideState == 2){
            slidePower = 0.2;
        } else if(slideState == 3){
            slidePower = 0.0;
        } else {
            slidePower = 0.0;
        }
        
        if (gamepad1.b) {
            if(timesofar.time() - slideLastPressed > 0.2){
                slideLastPressed = timesofar.time();
                if(slideState < 3){
                    slideState++;
                } else {
                    slideState = 1;
                }
            }
        }
        
        if(gamepad1.y) {
            slidePower = 1.0;
        }

        if (gamepad1.a) {
            if(timesofar.time() - servoLastPressed > 2.0){
                servoLastPressed = timesofar.time();
                servoState = !servoState;
            }
        }
        
        if(servoState){
            servoPower = 0.3;
        } else {
            servoPower = 0.0;
        }
        
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
        if(this.gamepad1.y)
        {
            reduce=true;
        }
        if(this.gamepad1.x&&((speeds[0]*2)<=1)&&((speeds[1]*2)<=1)&&((speeds[2]*2)<=1)&&((speeds[3]*2)<=1))
        {
            reduce=false;
        }
        // If and only if the maximum is outside of the range we want it to be,
        // normalize all the other speeds based on the given speed value.
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) 
            {
                speeds[i] /= max;
            }
        }
        for(int i =0; i<speeds.length;i++)
        {
            if(reduce)
            {
                speeds[i]/=3;
            }
        }
        
        telemetry.addData("reduce", reduce);
        telemetry.addData("front_left", speeds[0]);
        telemetry.addData("front_right", speeds[1]);
        telemetry.addData("back_left", speeds[2]);
        telemetry.addData("back_right", speeds[3]);
        telemetry.addData("LSlideMotor", LSlideVertPower);
        telemetry.addData("RSlideMotor", RSlideVertPower);
        telemetry.addData("LServo", servoPower);
        telemetry.addData("RServo", servoPower);
        telemetry.addData("armState", armState);
        telemetry.addData("ServoState", servoState);
        telemetry.addData("SlideState", slideState);
        telemetry.update();
        // apply the calculated values to the motors.
        front_left.setPower(speeds[0]);
        front_right.setPower(speeds[1]);
        back_left.setPower(speeds[2]);
        back_right.setPower(speeds[3]);
        LSlideVert.setPower(LSlideVertPower);
        RSlideVert.setPower(RSlideVertPower);
        leftServo.setPower(servoPower);
        rightServo.setPower(servoPower);
        slideMotor.setPower(slidePower);
    }
}
