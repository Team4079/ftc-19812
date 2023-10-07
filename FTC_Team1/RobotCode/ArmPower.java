

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="ArmJoint", group="Linear Opmode")

public class ArmPower extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor arm = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        arm  = hardwareMap.get(DcMotor.class, "arm");


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double power = 1.0;
            arm.setPower(-power);
            if(gamepad1.x)
            {
                arm.setPower(-power);
            }
            if(gamepad1.a)
            {
                arm.setPower(power);
            }
            if(gamepad1.y)
            {
                arm.setPower(0);
            }
            telemetry.addData("ArmPower", power);
            telemetry.update();

            // Show the elapsed game time and wheel power.
        }
    }
}
