package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

public class GoAroundChair extends LinearOpMode {
    private DcMotor left;
    private DcMotor right;
    private ElapsedTime timer= new ElapsedTime();
    
    private final double wheelCircumference = 90*3.14;
    private final double gearReduction = 72.0;
    private final double counts = 4.0;
    
    private final double rev = counts*gearReduction;
    private final double revPerMM = rev/wheelCircumference;
    private final double inches = revPerMM*25.4;
    
    
    public void drive(int forward)
    {
        int rightTarget = forward*(int)inches+right.getCurrentPosition();
        int leftTarget = forward*(int)inches+left.getCurrentPosition();
        waitForStart();
        while (opModeIsActive()){
            
            
            left.setTargetPosition(leftTarget);
            right.setTargetPosition(rightTarget);
            
            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            
            while(opModeIsActive()&&left.isBusy()&&right.isBusy())
            {
                left.setPower(1.0);
                right.setPower(1.0);
                telemetry.addData("Left Target", leftTarget);
                telemetry.addData("Right Target", rightTarget);
                telemetry.addData("Right Position", right.getCurrentPosition());
                telemetry.addData("Left Position", left.getCurrentPosition());
                telemetry.update();
            }
        left.setPower(0.0);
        right.setPower(0.0);
          
        }
    }
    
    public void runOpMode() {
        left=hardwareMap.get(DcMotor.class, "left");
        right=hardwareMap.get(DcMotor.class, "right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        drive(6);
    }
}