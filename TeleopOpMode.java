package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.Blinker;
// import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
//import java.util.concurrent.TimeUnit
import org.firstinspires.ftc.teamcode.TimedMotor;
@TeleOp
public class TeleopOpMode extends LinearOpMode {
  private Blinker expansion_Hub_2;
  // private Gyroscope imu;
  private DcMotor left;
  private DcMotor right;
  private DcMotor intake;
  private DcMotor top;
  private DcMotor bottom;
  private DcMotor carouselLeft;
  private DcMotor carouselRight;
  private TimedMotor carouselLeftTimer;
  private TimedMotor carouselRightTimer;
  @Override
  public void runOpMode() {
    expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
    // imu = hardwareMap.get(Gyroscope.class, "imu");
    left = hardwareMap.get(DcMotor.class, "left");
    right = hardwareMap.get(DcMotor.class, "right");
    right.setDirection(DcMotor.Direction.REVERSE);

    // left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    // right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    intake = hardwareMap.get(DcMotor.class, "intake");
    top = hardwareMap.get(DcMotor.class, "top");
    bottom = hardwareMap.get(DcMotor.class, "bottom");
    carouselLeft = hardwareMap.get(DcMotor.class, "carouselLeft");
    carouselRight = hardwareMap.get(DcMotor.class, "carouselRight");
    carouselLeftTimer = new TimedMotor();
    carouselRightTimer = new TimedMotor();
    // USE THE FOLLOWING LINES TO CHANGE THE CAROUSEL SPIN FOR THE ENTIRE FILE
    // carouselRight.setDirection(DcMotor.Direction.REVERSE);
    // carouselLeft.setDirection(DcMotor.Direction.REVERSE);
    
    boolean gamepada = false;
    telemetry.addData("Status", "Initialized");
    telemetry.update();
    double carspeed = 0.5;
    double robotspeed = 0.8;
    double intakespeed = 1;
    // Wait for the game to start (driver presses PLAY)
    waitForStart();

    double tgtPowerLeft = 0;
    double tgtPowerRight = 0;
    int endgame = -1;
    // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
    
        // DriveTrain program
        tgtPowerLeft = this.gamepad1.left_stick_y;
        left.setPower(tgtPowerLeft*robotspeed);
        tgtPowerRight = this.gamepad1.right_stick_y;
        right.setPower(tgtPowerRight*robotspeed);

        // intake program
        if(this.gamepad1.right_bumper == true) {
            intake.setPower(intakespeed);
            top.setPower(-intakespeed);
            bottom.setPower(intakespeed);
            robotsleep(50);
            intake.setPower(0);
            top.setPower(0);
            bottom.setPower(0);
        }

        // outtake program
        if(this.gamepad1.y == true) {
            intake.setPower(-intakespeed);
            top.setPower(intakespeed);
            bottom.setPower(-intakespeed);
            robotsleep(50);
            intake.setPower(0);
            top.setPower(0);
            bottom.setPower(0);
        }

        // Carousel program Blue
        if(this.gamepad1.x == true) {
            carouselRight.setPower(carspeed);
            carouselLeft.setPower(carspeed);
            robotsleep(100);
            carouselRight.setPower(0);
            carouselLeft.setPower(0);
        }
        // Carousel program Red 
        if(this.gamepad1.b == true) {
            carouselRight.setPower(-carspeed);
            carouselLeft.setPower(-carspeed);
            robotsleep(100);
            carouselRight.setPower(0);
            carouselLeft.setPower(0);
        }
       
    
        // check motors
        carouselRight.setPower(carouselRightTimer.running());
        carouselLeft.setPower(carouselLeftTimer.running());
    
        //telemetry data reported to Driver Hub
        telemetry.addData("Target Power Left", tgtPowerLeft);
        telemetry.addData("Left Motor Power", left.getPower());
        telemetry.addData("Target Power Right", tgtPowerRight);
        telemetry.addData("Right Motor Power", right.getPower());
        telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
        telemetry.addData("Right Motor Encoder", right.getCurrentPosition());
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
 
 
 


