package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//@Disabled haha you thought I would do it again lol
@Autonomous(name="Autonomous Long Basket", group="Linear Opmode")

public class AutoLongBasket extends LinearOpMode {
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
    
    @Override
    
    public void runOpMode() {
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "fLeft");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "bLeft");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "fRight");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "bRight");

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);

         // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int runCount=0;
        waitForStart();
        runtime.reset();

        basket();

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
    public void basket(){
        turnLeft(90);
        driveEncoders(610);
        // arm to score thing here
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(2505);
        slideMotor.setVelocity(1500);
        while (slideMotor.isBusy())
        {

        }
        slideArm.setPower(0);

        turnLeft(180);
        driveEncoders(2440);
        turnLeft(90);
        driveEncoders(1220);
        rightEncoders(610);
        backEncoders(1220);
    }
    public void observation(){
        driveEncoders(300wwwww);
        turnLeft(90);
        driveEncoders(1371.6);
        turnLeft(45);
        sleep(3000)
        turnRight(45);
        backEncoders(1676.2);
        leftEncoders(300);
     }
}
