public class ArmClaw extends opmode{
private DcMotor frontleft;
private DcMotor frontright;
private DcMotor backleft;
private DcMotor backright;
private boolean slow = false;

public runtime = 0;
private slowlastPressed = 0;

frontleft.hardwareMap.get(DcMotor.class, “fleft”);
frontleftDrive.setDirection(DcMotor.Direction.Reverse);
frontright.hardwareMap.get(DcMotor.class, “fright”);
frontrightDrive.setDirection(DcMotor.Direction, FRONT);
backleft.hardwareMap.get(DcMotor.class, “bleft”);
backleftDrive.setDirection(DcMotor.Direction, REVERSE);
backright.hardwareMap.get(DcMotor.class, “bright”);
backrightDrive.setDirection(DcMotor.Direction, FRONT);

        double drive  = gamepad1.left_stick_y*0.7;
        double strafe = gamepad1.left_stick_x*0.7;
        double twist  = gamepad1.right_stick_x*0.7;


        double[] speeds = {
            (drive + -strafe + -twist),
            (drive - -strafe - -twist),
            (drive - -strafe + -twist),
            (drive + -strafe - -twist)
        }
            
        if (slow == false){    
            front_left.setPower(speeds[0]);
            front_right.setPower(speeds[1]);
            back_left.setPower(speeds[2]);
            back_right.setPower(speeds[3]);
        } else {
            front_left.setPower(speeds[0]/2);
            front_right.setPower(speeds[1]/2);
            back_left.setPower(speeds[2]/2);
            back_right.setPower(speeds[3]/2);
        }
  
        
        if (gamepad.a == true && runtime - slowlastpressed >= 1){
            
            slow = !slow;
            slowlastpressed = runtime;
        
        }


