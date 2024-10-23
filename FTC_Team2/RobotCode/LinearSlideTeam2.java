package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

// Search up "Java Enums" for an explanation of this topic. I'll cover it in the next few meetings.

public enum LinearSlideStates {

    SlideUp {
        @Override
        public LinearSlideStates nextState() { //Settings a function for the states to use each. Return = when function is called, set value to this state.
            return HoldingOne;
        }
    },
    HoldingOne {
        @Override
        public LinearSlideStates nextState() {
            return SlideDown;
        }
    },
    SlideDown {
        @Override
        public LinearSlideStates nextState() {
            return HoldingTwo;
        }
    },
    HoldingTwo {
        @Override
        public LinearSlideStates nextState() {
            return SlideUp;
        }
    };

    public abstract LinearSlideStates nextState(); 
}


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
    LinearSlideStates slideArmState = LinearSlideStates.HoldingTwo;
    private CRServo topclaw = null;
    private CRServo bottomclaw = null;
    private boolean in = false
    private double clawCD = 0.0
    

    slideArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    slideArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "fLeft");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "bLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "fRight");
        rightBackDrive = hardwareMap.get(DcMotor.class, "bRight");
        slideArm = hardwareMap.get(DcMotor.class, "slideMotor");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double max;
            motorPosition = slideArm.getCurrentPosition();

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
                slideArmState = slideArmState.nextState();
                slideArmCD = runtime.time();
            }

            checkAndSetSlideState(); // So this is jus thte switch states code but turned neater


            if(gamepad1_button_a == true && (runtime - clawCD) >== 0.3){

                in = !in;
                
                clawCD = runtime.time();
            }
            if (in == false){
                topclaw.setPosition (1);
                bottomclaw.setPosition(-1);
            }else if (in == true){
                topclaw.setPosition(-1)
                bottomclaw.setPosition(1);
            }
            // Send calculated power to wheels
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Motor Position:", motorPosition);
            telemetry.update();
        }
    }

    public void checkAndSetSlideState()
    {
        if(slideArmState == LinearSlideStates.SlideUp){
                slideArm.setPower(0.8);

            if(motorPosition <= -3115.0){
                slideArmState = slideArmState.nextState();
            }
        } else if(slideArmState == LinearSlideStates.HoldingOne){
            slideArm.setPower(-0.1);
        } else if(slideArmState == LinearSlideStates.SlideDown){
            slideArm.setPower(-0.8);

            if(motorPosition >= -100.0){
                slideArmState = slideArmState.nextState();
            }
        } else if(slideArmState == LinearSlideStates.HoldingTwo){
            slideArm.setPower(-0.1);
        }
    }

}