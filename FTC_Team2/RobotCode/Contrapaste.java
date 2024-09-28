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
    private ElapsedTime runtime = new ElapsedTimed();
    private int Armstate = 0;
    private double armCooldown = 0;

    jerkoffarm= hardwareMap.get(DcMotor.class, “jerk_off_arm”);
    jerkoffarm.setPower(0.0);

    private int ArmstateTwo =0;
    private double armCooldownTwo = 0;

    weakarm= hardwareMap.get(DcMotor.class, “weak_arm”);
    weakarm.setPower(0.0);
    
    @Override
    public void runOpMode(){
        
while(opModeIsActive()){
    if(gamepad1_button_b == true && (runtime - ArmCooldown)>==1){
    Armstate++
    if (Armstate == 1){
        jerkoffarm.setpower(1.0);
    }else if (Armstate == 2){
        jerkoffarm.setpower(-1.0);
    }else if (Armstate == 3){
        jerkoffarm.setpower(0.0)
    }
}
If (Armstate >4){
Armstate = 1;
}ArmCooldown = runtime;

    if(gamepad1_button_a == true && (runtime - ArmCooldownTwo)>==1){
    ArmstateTwo++
    if (ArmstateTwo == 1){
        weakarm.setpower(1.0);
    }else if (ArmstateTwo == 2){
        weakarm.setpower(-1.0);
    }
}
If (ArmstateTwo >3){
ArmstateTwo = 1;
}ArmCooldownTwo = runtime;



        }
    }
}
// Alex Cui September 25 2024 on 3:47 PM
// nya ichi ni san nya arigato