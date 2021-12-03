package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.Blinker;
// import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
// import java.util.concurrent.TimeUnit
public class AutoOpMode extends LinearOpMode {
    private Blinker expansion_Hub_2;
    private Gyroscope imu;
    private DcMotor left;
    private DcMotor right;
    private DcMotor carouselRight;
    private DcMotor carouselLeft;
    private String color;

    public AutoOpMode(String color){
        this.color = color;
    }

    @Override
    public void runOpMode() {
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        // imu = hardwareMap.get(Gyroscope.class, "imu");
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotor.Direction.REVERSE);

        carouselRight = hardwareMap.get(DcMotor.class, "carouselRight");
        carouselLeft = hardwareMap.get(DcMotor.class, "carouselLeft");
        if(color == "blue"){
            carouselRight.setDirection(DcMotor.Direction.REVERSE);
            carouselLeft.setDirection(DcMotor.Direction.REVERSE);
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TimedMotor robotTimer = new TimedMotor();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            /*
            // time based ----------------

            // push forward and turn carousel
            left.setPower(0.2);
            right.setPower(0.2);
            carouselRight.setPower(1);
            carouselLeft.setPower(1);
            robotsleep(2000);
            
            // pause
            left.setPower(0);
            right.setPower(0);
            carouselRight.setPower(0);
            carouselLeft.setPower(0);
            robotsleep(1000);

            // turn
            left.setPower(1);
            right.setPower(-1);
            robotsleep(700);
            
            // pause
            left.setPower(0);
            right.setPower(0);
            robotsleep(1000);

            // move forward
            left.setPower(1);
            right.setPower(1);
            robotsleep(1000);

            // stop
            left.setPower(0);
            right.setPower(0);
            robotsleep(100000);
            */

            // encoder based ----------------

            // setup
            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // carouselRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // carouselLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // push forward and turn carousel
            left.setPower(0.2);
            right.setPower(0.2);
            carouselRight.setPower(1);
            carouselLeft.setPower(1);

            robotTimer.runMotor(10000);
            while(robotTimer.running()==1){
                left.setTargetPosition(500);
                right.setTargetPosition(-500);

                carouselRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                carouselLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                // carouselRight.setTargetPosition(10000);
                // carouselLeft.setTargetPosition(-10000);
            }

            // reset 
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // turn robot
            left.setPower(1);
            right.setPower(-1);
            
            robotTimer.runMotor(2000);
            while(robotTimer.running()==1){
                left.setTargetPosition(1500);
                right.setTargetPosition(-1500);
            }

            // reset 
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // move forward
            left.setPower(1);
            right.setPower(1);
            robotTimer.runMotor(2000);
            while(robotTimer.running()==1){
                left.setTargetPosition(3000);
                right.setTargetPosition(-3000);
            }

            // reset 
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            carouselLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // stop
            left.setPower(0);
            right.setPower(0);
            carouselRight.setPower(0);
            carouselLeft.setPower(0);
            robotsleep(100000);
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
