package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Basic: Omni Linear OpMode", group="Linear Opmode")


public class MechanumAutonomous extends LinearOpMode {
    private final double wheelCircumference = 75*3.14;
    private final double gearReduction = 3.61*5.23;
    private final double counts = 28.0;
    
    private final double rev = counts*gearReduction;
    private final int revPerMM = (int)rev/(int)wheelCircumference;
    private final double inches = revPerMM*25.4;

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;

    private int armPower = 0

    @Override
    public void runOpMode() {
        
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "fLeft");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "bLeft");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "fRight");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "bRight");

        armmotor=hardwareMap.get(DcMotor.class, “arm”);

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorEx.Direction.FORWARD);
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int count=0;
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()&&count==0) {

            backEncoders(200);
            turnleft(90);
            backEncoders(100);
            armPower = 1;
            rightEncoders(250);
            driveEncoders(100);
            rightEncoders(50);

            // Show the elapsed game time and wheel power.
            count++;
        }
    }
    public void input(double leftFront, double rightFront, double leftBack, double rightBack)
    {
        leftFrontDrive.setPower(leftFront);
        rightFrontDrive.setPower(rightFront);
        leftBackDrive.setPower(leftBack);
        rightBackDrive.setPower(rightBack);
        sleep(500);
    }
    public void turnLeft(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(-convert, convert, -convert, convert);
    }
    public void turnRight(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(convert, -convert, convert, -convert);
    }
    public void driveEncoders(int target)
    {
        encoders(target, target, target, target);
    }
    public void backEncoders(int target)
    {
        encoders(-target, -target, -target, -target);
    }
    public void rightEncoders(int target)
    {
        encoders(target, -target, -target, target);
    }
    public void leftEncoders(int target)
    {
        encoders(-target, target, target, -target);
    }
    public void leftTopDiagonal(int target)
    {
        encoders(0, target, target, 0);
    }
    public void rightTopDiagonal(int target)
    {
        encoders(target, 0, 0, target);
    }
    public void leftBottomDiagonal(int target)
    {
        encoders(-target, 0, 0, -target);
    }
    public void rightBottomDiagonal(int target)
    {
        encoders(0, -target, -target, 0);
    }
    public void encoders(int leftFront, int rightFront, int leftBack, int rightBack)
    {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        leftFrontDrive.setTargetPosition(leftFront*revPerMM);
        rightFrontDrive.setTargetPosition(rightFront*revPerMM);
        leftBackDrive.setTargetPosition(leftBack*revPerMM);
        rightBackDrive.setTargetPosition(rightBack*revPerMM);
        
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        double power=0.2;
        leftFrontDrive.setVelocity(1500);
        rightFrontDrive.setVelocity(1500);
        leftBackDrive.setVelocity(1500);
        rightBackDrive.setVelocity(1500);
        while (opModeIsActive()&&leftFrontDrive.isBusy()||rightFrontDrive.isBusy()||leftBackDrive.isBusy()||rightBackDrive.isBusy())
        {
        }
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
    public void drive()
    {
        leftFrontDrive.setPower(1.0);
        rightFrontDrive.setPower(1.0);
        leftBackDrive.setPower(1.0);
        rightBackDrive.setPower(1.0);
        //input(0.4, 0.4, 0.4, 0.4);
    }
    public void topRight()
    {
        input(0.4, 0, 0, 0.4);
    }
    public void right()
    {
        input(0.4, -0.4, -0.4, 0.4);
    }
    public void bottomRight()
    {
        input(0, -0.4, -0.4, 0);
    }
    public void back()
    {
        input(-0.4, -0.4, -0.4, -0.4);
    }
    public void bottomLeft()
    {
        input(-0.4, 0, 0, -0.4);
    }
    public void left()
    {
        input(-0.4, 0.4, 0.4, -0.4);   
    }
    public void topLeft()
    {
        input(0, 0.4, 0.4, 0);
    }
    public void stop(double time)
    {
        input(0, 0, 0, 0);
    }
}