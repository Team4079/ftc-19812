package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

public class ArmCode extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor armDrive;
    
    @Override
    public void runOpMode(){
        armDrive = hardwareMap.get(DcMotor.class, "grabbyMot");
        armDrive.setDirection(DcMotor.Direction.FORWARD);
        
        waitForStart();
        runtime.reset();
        while(opModeIsActive()){
            armDrive.setPower(0.1);
        }
    }
}
