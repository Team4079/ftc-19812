
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


@Autonomous

public class HighJunctionAutonomous extends LinearOpMode {
    private DcMotor left;
    private DcMotor right;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime backTimer = new ElapsedTime();
    private ElapsedTime bigCounter = new ElapsedTime();
    private ElapsedTime turnTimer = new ElapsedTime();
    private ElapsedTime driveTimer = new ElapsedTime();
    private DcMotor slide;
    private Servo dumper;
    double robotspeed = 1;
    double intakespeed = 1;
    double ascent = 1.0;
    double retract = 1.0;
    double open = 0.4;
    double close = 0.8;
    double low = 0.9;
    double medium = 1.4;
    double high = 2.0;
    double adjust = 0.09;
    double downTime = 0.5;
    double ground = 0.02;
    
    private final double wheelCircumference = 90*3.14;
    private final double gearReduction = 72.0;
    private final double counts = 4.0;
    
    private final double rev = counts*gearReduction;
    private final double revPerMM = rev/wheelCircumference;
    private final double inches = revPerMM*25.4;


    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };

    public void useTimer(double sec){
        runtime.reset();
        while (opModeIsActive()&&runtime.seconds()<sec)
        {
            left.setPower(0.9);
            right.setPower(0.8);
        }
        left.setPower(0.0);
        right.setPower(0.0);
    }
    public void leftTurn(double direction)
    {
        double secPerRotation = 4.32;
        double ratio = secPerRotation/360;
        double tgtTime = direction*ratio;
        waitForStart();
        turnTimer.reset();
        while(opModeIsActive()&&turnTimer.seconds()<tgtTime){
            right.setPower(-0.5);
            left.setPower(0.5);
            telemetry.addData("Left", left.getPower());
            telemetry.addData("Left Motor Power", left.getPower());
            telemetry.addData("Right Motor Power", right.getPower());
            telemetry.addData("Status", "Running");
            telemetry.addData("LeftPosition", left.getCurrentPosition());                    
            telemetry.addData("RightPosition", right.getCurrentPosition());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void rightTurn(double direction)
    {
        double secPerRotation = 4.6;
        double ratio = secPerRotation/360;
        double tgtTime = direction*ratio;
        waitForStart();
        turnTimer.reset();
        while(opModeIsActive()&&turnTimer.seconds()<tgtTime){
            right.setPower(0.5);
            left.setPower(-0.5);
            telemetry.addData("Right", left.getPower());
            telemetry.addData("Left Motor Power", left.getPower());
            telemetry.addData("Right Motor Power", right.getPower());
            telemetry.addData("Status", "Running");
            telemetry.addData("LeftPosition", left.getCurrentPosition());                    
            telemetry.addData("RightPosition", right.getCurrentPosition());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void drive(double target)
        {
            double secPerFt = 1.6;
            double tgt = (target)*secPerFt;
            driveTimer.reset();
            waitForStart();
            while (opModeIsActive()&&(driveTimer.seconds()<tgt)) {
                left.setPower(-0.9);
                right.setPower(-0.8);
                telemetry.addData("Forward", left.getPower());
                telemetry.addData("Left Motor Power", left.getPower());
                telemetry.addData("Right Motor Power", right.getPower());
                telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
                telemetry.addData("Right Motor Encoder", right.getCurrentPosition());
                telemetry.addData("Status", "Running");
                telemetry.addData("LeftPosition", left.getCurrentPosition());
                telemetry.addData("RightPosition", right.getCurrentPosition());
                telemetry.update();
            }
            left.setPower(0.0);
            right.setPower(0.0);
        }
        public void backward(double target)
        {
            double secPerFt = 1.6;
            double tgt = (target)*secPerFt;
            waitForStart();
            backTimer.reset();
            while (opModeIsActive()&&(backTimer.seconds()<tgt)) {
                left.setPower(0.9);
                right.setPower(0.8);
                telemetry.addData("Backward", left.getPower());
                telemetry.addData("Left Motor Power", left.getPower());
                telemetry.addData("Right Motor Power", right.getPower());
                telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
                telemetry.addData("Right Motor Encoder", right.getCurrentPosition());
                telemetry.addData("Status", "Running");
                telemetry.addData("LeftPosition", left.getCurrentPosition());
                telemetry.addData("RightPosition", right.getCurrentPosition());
                telemetry.update();
            }
            left.setPower(0.0);
            right.setPower(0.0);
        }
    public void slideUp(double time){
        runtime.reset();
        while(runtime.seconds()<time&&opModeIsActive())
        {
            slide.setPower(1.0);
        }
        slide.setPower(0.0);
    }
    public void slideDown(double time){
        runtime.reset();
        while(runtime.seconds()<(time)&&opModeIsActive())
        {
            slide.setPower(-1.0);
        }
        slide.setPower(0.0);
    }
    public void score(){
        slideUp(2.5);
        backward(0.08);
        slideDown(0.25);
        stop(0.5);
        dumper.setPosition(open);
        stop(2.0);
        dumper.setPosition(close);
        stop(0.5);
        slideUp(0.2);
        drive(0.1);
        slideDown(1.4);
    }
    public void toHighJunction()
    {
        drive(0.78);
        leftTurn(100);
        backward(0.6);
        leftTurn(36);
        score();
    }
    public void stop(double time)
    {
        runtime.reset();
        while(opModeIsActive()&&runtime.seconds()<time)
        {
            right.setPower(0.0);
            left.setPower(0.0);
            telemetry.addData("Still", left.getPower());
            telemetry.addData("Left Motor Power", left.getPower());
            telemetry.addData("Right Motor Power", right.getPower());
            telemetry.addData("Left Motor Encoder", left.getCurrentPosition());
            telemetry.addData("Right Motor Encoder", right.getCurrentPosition());                
            telemetry.addData("Status", "Running");
            telemetry.addData("LeftPosition", left.getCurrentPosition());
            telemetry.addData("RightPosition", right.getCurrentPosition());
            telemetry.update();
        }
    }

    @Override
    public void runOpMode() {
        
        left=hardwareMap.get(DcMotor.class, "right");
        right=hardwareMap.get(DcMotor.class, "left");
        slide=hardwareMap.get(DcMotor.class, "slide");
        dumper=hardwareMap.get(Servo.class, "dumper");
        right.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection(DcMotor.Direction.FORWARD);
        
        /** Wait for the game to begin */
        
        
        waitForStart();
        boolean atDestination = false;
        boolean run = true;
        dumper.setPosition(close);
        stop(2.0);
        slideUp(0.1);
        toHighJunction();
        //rightTurn(32);
        //.85
        
    }
}
