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
public class TeleOpRevised extends LinearOpMode {
  private Blinker Control_Hub;
  // private Gyroscope imu;
  private DcMotor left;
  private DcMotor right;
  private DcMotor slide;
  private Servo dumper;
  private ElapsedTime runtime = new ElapsedTime();
  
  
  public void runOpMode() {
    Control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
    // imu = hardwareMap.get(Gyroscope.class, "imu");
    left = hardwareMap.get(DcMotor.class, "left");
    right = hardwareMap.get(DcMotor.class, "right");
    slide = hardwareMap.get(DcMotor.class, "slide");
    dumper = hardwareMap.get(Servo.class, "dumper");
    dumper.setDirection(Servo.Direction.REVERSE);

    boolean gamepada = false;
    boolean check = false;
    telemetry.addData("Status", "Initialized");
    telemetry.update();
    double robotspeed = 1;
    double intakespeed = 1;
    double ascent = 1.0;
    double retract = 1.0;
    double open = 0.35;
    double close = 1;
    double low = 0.9;
    double medium = 1.4;
    double high = 2.0;
    double adjust = 0.09;
    double downTime = 0.5;
    double ground = 0.02;
    int home=slide.getCurrentPosition();
    // Wait for the game to start (driver presses PLAY)
    waitForStart();

    double tgtPowerLeft = 0;
    double tgtPowerRight = 0;
    dumper.resetDeviceConfigurationForOpMode();
    dumper.setPosition(1);
    // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
        // DriveTrain program
        tgtPowerLeft = this.gamepad1.left_stick_y;
        left.setPower(-this.gamepad1.left_stick_y);
        tgtPowerRight = this.gamepad1.right_stick_y;
        right.setPower(tgtPowerRight*1);
        //slide program down
        if(this.gamepad1.left_bumper){
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(-retract);
            telemetry.addData("SlideTest", slide.getPower());
            telemetry.update();
        }
        else{
            slide.setPower(0.0);
        }
        //Slide up
        if(this.gamepad1.right_bumper == true){
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(ascent);
            telemetry.addData("SlideTest", slide.getPower());
            telemetry.update();
        }
        else{
            slide.setPower(0.0);
        }
        //low
        //if(this.gamepad1.x){
          //  runtime.reset();
            //while(runtime.seconds()<low)
            //{
              //  slide.setPower(ascent);
            //}
        //}
        //medium
        //if(this.gamepad1.y){
          //  runtime.reset();
            //while (runtime.seconds()<medium)
            //{
              //  slide.setPower(ascent);
            //}
        //}
        //high
        if(this.gamepad1.b){
            check=true;
            slide.setTargetPosition(home);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while(opModeIsActive()&&slide.isBusy())
            {
                slide.setPower(-0.8);
            }
            slide.setPower(0.0);
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            telemetry.addData("Slide", slide.getCurrentPosition());
            telemetry.addData("Home", home);
        }
        //adjust
        if (this.gamepad1.dpad_right){
            runtime.reset();
            while (runtime.seconds()<adjust)
            {
                slide.setPower(-retract);
            }
            dumper.setPosition(open);
            runtime.reset();
            while (runtime.seconds()<0.5)
            {
                slide.setPower(0.0);
            }
            runtime.reset();
            while(runtime.seconds()<adjust)
            {
                slide.setPower(ascent);
            }
        }
        else{
            check=false;
        }
        if (this.gamepad1.dpad_left){
            runtime.reset();
            while (runtime.seconds()<ground)
            {
                slide.setPower(-retract);
            }
            dumper.setPosition(open);
            runtime.reset();
            while (runtime.seconds()<0.5)
            {
                slide.setPower(0.0);
            }
            runtime.reset();
            while(runtime.seconds()<adjust)
            {
                slide.setPower(ascent);
            }
        }
        //down
        if (this.gamepad1.back){
            runtime.reset();
            while (runtime.seconds()<downTime)
            {
                slide.setPower(-0.8);
            }
        }
        
        //Dumper program
        //open
        if(this.gamepad1.a == true) {
            dumper.setPosition(open);
        }
        //close
        if(this.gamepad1.dpad_down) {
            dumper.setPosition(close);
        }
        //telemetry data reported to Driver Hub
        
        telemetry.addData("Target Power Left", tgtPowerLeft);
        telemetry.addData("Left Motor Power", left.getPower());
        telemetry.addData("Target Power Right", tgtPowerRight);
        telemetry.addData("Right Motor Power", right.getPower());
        telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
        telemetry.addData("Right Motor Encoder", right.getCurrentPosition());
        telemetry.addData("Slide Position", slide.getCurrentPosition());
        telemetry.addData("Timer", runtime.seconds());
        telemetry.addData("Check", check);
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
 
 
 


                                                        