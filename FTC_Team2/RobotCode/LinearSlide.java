package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Mecanum Drive Example", group="Iterative Opmode")
public class MecanumTele extends OpMode {

    
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_left;
    private DcMotor back_right;
    private double power=1;
    private boolean reduce;
    private DcMotor arm;
    public ElapsedTime timesofar = new ElapsedTime();
    private double armLastPressed;
    private int armStates = 2
    private double motorPosition = 0.0;
    
    @Override
    public void init() {
      
        front_left = hardwareMap.get(DcMotor.class, "fLeft");
        front_right = hardwareMap.get(DcMotor.class, "fRight");
        back_left = hardwareMap.get(DcMotor.class, "bLeft");
        back_right = hardwareMap.get(DcMotor.class, "bRight");
        arm = hardwareMap.get(DcMotor.class, "slideMotor")
        back_right.setDirection(DcMotor.Direction.REVERSE);
        arm.setMode(STOP_AND_RESET_ENCODER)
        arm.setMode(RUN_WITHOUT_ENCODER)
        motorPosition = arm.getCurrentPosition();

        telemetry.addData("Status", "Intizaised");
        telemetry.update();
    }
    @Override
    public void loop() {
       
        double drive  = gamepad1.left_stick_y*0.7;
        double strafe = gamepad1.left_stick_x*0.7;
        double twist  = gamepad1.right_stick_x*0.7;

        motorPosition = arm.getCurrentPosition();


        
        double[] speeds = {
            (drive + -strafe + -twist),
            (drive - -strafe - -twist),
            (drive - -strafe + -twist),
            (drive + -strafe - -twist)
        };

       
        double max = Math.abs(speeds[0]);
        for(int i = 0; i < speeds.length; i++) {
            if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
        }
       
       
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) 
            {
                speeds[i] /= max;
            }
        }

        if (gamepad1.a) {
            if(timesofar.time() - armLastPressed > 0.5){
                armLastPressed = timesofar.time();
                armStates++
            if (armStates >= 5){
                armStates = 1;
            }
            }
        }
        
    
        if (armStates==1) {
            arm.setPower(0.75)
            if (motorPosition>=-110.0) {
                armStates++
            }
        } else if (armStates == 2) {
            arm.setPower(-0.1)
        } else if (armStates == 3) {
            arm.setPower(-0.75)
            if (motorPosition<=-3115.0) {
                armStates++
            }
        } else if (armStates == 4) {
            arm.setPower(-0.1)
        }
        
        
        telemetry.addData("reduce", reduce);
        telemetry.addData("front_left", speeds[0]);
        telemetry.addData("front_right", speeds[1]);
        telemetry.addData("back_left", speeds[2]);
        telemetry.addData("back_right", speeds[3]);
        telemetry.addData("motor position", motorPosition)
        telemetry.update();
       
        front_left.setPower(speeds[0]/2);
        front_right.setPower(speeds[1]/2);
        back_left.setPower(speeds[2]/2);
        back_right.setPower(speeds[3]/2);
    }
}

// sam my daddy daddy my sam\
// pull reuqiEODJFKLSUGHUFEIYTRHGHFRUYGFHUGVHF