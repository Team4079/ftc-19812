package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Linear Slide Test 1", group="Linear OpMode")

public class LinearSlideTest1 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor slideArm = null;
    private double slideArmCD = 0.0;
    private int slideArmState = 0;
    private double motorPosition = 0.0;

    motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODERS);
    motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "fLeft");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "bLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "fRight");
        rightBackDrive = hardwareMap.get(DcMotor.class, "bRight");
        slideArm = hardwareMap.get(DcMotor.class, "slideMotor");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);

        slideArmState = 4;
        motor.getCurrentPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double max;
            motorPosition = motor.getCurrentPosition();

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            if(gamepad1.b && runtime.time()-slideArmCD >= 0.2){
                if(slideArmState >= 4){
                    slideArmState = 1;
                } else {
                    slideArmState++;
                }
                slideArmCD = runtime.time();
            }

            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);
            if(slideArmState == 1){
                slideArm.setPower(1.0);
                if(motorPosition <= -3115.0){
                    slideArmState++;
                }
            } else if(slideArmState == 2){
                slideArm.setPower(-0.1);
            } else if(slideArmState == 3){
                slideArm.setPower(-1.0);
                if(motorPosition >= -20.0){
                    slideArmState++;
                }
            } else if(slideArmState == 4){
                slideArm.setPower(-0.1);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Motor Position:", motorPosition);
            telemetry.update();
        }
    }}
