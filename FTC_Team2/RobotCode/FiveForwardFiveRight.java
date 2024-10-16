package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous

public class FiveForwardFiveRight extends LinearOpMode{
    private final double wheelCircumference = 75*3.14;
    private final double gearReduction = 3.61*5.23;
    private final double counts = 28.0;
    
    private final double rev = counts*gearReduction;
    private final int revPerMM = (int)rev/(int)wheelCircumference;
    private final double inches = revPerMM*25.4;
    
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lf = null;
    private DcMotor lb = null;
    private DcMotor rf = null;
    private DcMotor rb = null;
    
    @Override
    public void runOpMode() {
        lf = hardwareMap.get(DcMotor.class, "fLeft");
        lb = hardwareMap.get(DcMotor.class, "bLeft");
        rf = hardwareMap.get(DcMotor.class, "fRight");
        rb = hardwareMap.get(DcMotor.class, "bRight");
        
        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.FORWARD);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.REVERSE);
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int count=0;
        
        waitForStart();
        runtime.reset();
        
        while (opModeIsActive() && count == 0){
            encoders(500*revPerMM);
            
            count++;
        }
    }
    
    public void encoders(int target) {
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        lf.setTargetPosition(target);
        rf.setTargetPosition(target);
        lb.setTargetPosition(target);
        rb.setTargetPosition(target);
        
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        
        lf.setPower(1.0);
        rf.setPower(1.0);
        lb.setPower(1.0);
        rb.setPower(1.0);
        
        while (opModeIsActive()&&lf.isBusy()&&rf.isBusy()&&lb.isBusy()&&rb.isBusy())
        {
            
        }
        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
    }
    
    public void turnR (int deg) {
        lf.setPower(1);
        lb.setPower(1);
        rf.setPower(-1);
        rb.setPower(-1);
    }
    
    public void setTimer (DcMotor motor, int sec) {
        runtime.reset();
        motor.setPower(1);
    }
    
}