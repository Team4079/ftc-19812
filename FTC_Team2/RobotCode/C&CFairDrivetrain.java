package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="Mecanum Drive Example", group="Iterative Opmode")
public class MecanumTele extends OpMode {

    
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_left;
    private DcMotor back_right;
    private double power=1;
    private boolean reduce;
    @Override
    public void init() {
        reduce=false;
      
        front_left = hardwareMap.get(DcMotor.class, "fLeft");
        front_right = hardwareMap.get(DcMotor.class, "fRight");
        back_left = hardwareMap.get(DcMotor.class, "bLeft");
        back_right = hardwareMap.get(DcMotor.class, "bRight");
        back_right.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
       
        double drive  = gamepad1.left_stick_y*0.7;
        double strafe = gamepad1.left_stick_x*0.7;
        double twist  = gamepad1.right_stick_x*0.7;


        /
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
        if(this.gamepad1.a)
        {
            if(!reduce)
            {
                reduce=true;
            }
            else if(reduce&&((speeds[0]*2)<=1)&&((speeds[1]*2)<=1)&&((speeds[2]*2)<=1)&&((speeds[3]*2)<=1)){
                reduce=false;
            }
        }
       
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
                speeds[i]/=2;
            }
        }
        telemetry.addData("reduce", reduce);
        telemetry.addData("front_left", speeds[0]);
        telemetry.addData("front_right", speeds[1]);
        telemetry.addData("back_left", speeds[2]);
        telemetry.addData("back_right", speeds[3]);
        telemetry.update();
       
        front_left.setPower(speeds[0]);
        front_right.setPower(speeds[1]);
        back_left.setPower(speeds[2]);
        back_right.setPower(speeds[3]);
    }
}
// daddy is sam