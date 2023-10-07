package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.Blinker;
// import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
//import java.util.concurrent.TimeUnit
//com.qualcomm.robotcore.<class/class-group>;

@TeleOp
public class TeleopSimple extends LinearOpMode {
  private Blinker Control_Hub;
  // private Gyroscope imu;
  private DcMotor left;
  private DcMotor right;
  private DcMotor slide;
  private ElapsedTime runtime = new ElapsedTime();
  
  
  public void runOpMode() {
    Control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
    // imu = hardwareMap.get(Gyroscope.class, "imu");
    left = hardwareMap.get(DcMotor.class, "left");
    right = hardwareMap.get(DcMotor.class, "right");
    slide=hardwareMap.get(DcMotor.class, "slide");
    

    boolean gamepada = false;
    telemetry.addData("Status", "Initialized");
    telemetry.update();
    double robotspeed = 1;
    double intakespeed = 1;
    double ascent = 1.0;
    double retract = 0.9;
    double open = 0.3;
    double close = 0.7;
    // Wait for the game to start (driver presses PLAY)
    waitForStart();

    double tgtPowerLeft = 0;
    double tgtPowerRight = 0;
    //dumper.resetDeviceConfigurationForOpMode();
    //dumper.setPosition(open);
    // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
    
        // DriveTrain program
        tgtPowerLeft = this.gamepad1.left_stick_y;
        left.setPower(-this.gamepad1.left_stick_y);
        tgtPowerRight = this.gamepad1.right_stick_y;
        right.setPower(tgtPowerRight*1);
        //slide program down
        if(this.gamepad1.left_bumper){
            slide.setPower(-retract);
            telemetry.addData("SlideTest", slide.getPower());
            telemetry.update();
        }
        else{
            slide.setPower(0.0);
        }
        //Slide up
        if(this.gamepad1.right_bumper == true){
            slide.setPower(ascent);
            telemetry.addData("SlideTest", slide.getPower());
            telemetry.update();
        }
        else{
            slide.setPower(0.0);
        }
        //Dumper program
        //open
        if(this.gamepad1.a == true) {
            //dumper.setPosition(open);
        
        }
        //close
        if(this.gamepad1.dpad_down) {
            //dumper.setPosition(close);
        }
        //telemetry data reported to Driver Hub
        telemetry.addData("Target Power Left", tgtPowerLeft);
        telemetry.addData("Left Motor Power", left.getPower());
        telemetry.addData("Target Power Right", tgtPowerRight);
        telemetry.addData("Right Motor Power", right.getPower());
        telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
        telemetry.addData("Right Motor Encoder", right.getCurrentPosition());
        //telemetry.addData("Dumper Position", dumper.getPosition());
        telemetry.addData("Status", "Running");
        telemetry.update();

    }
  }
  public void robotsleep(long timeout){
      if (timeout > 0) {
          try {
              TimeUnit.MILLISECONDS.sleep(timeout);
          } catch (InterruptedException e){
              System.out.print(e);
          }
      }
  }
}
 
 
 


                                                        