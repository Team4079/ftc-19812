public class Contraposto extends opmode{
    private DcMotor frontleft;
    private DcMotor frontright;
    private DcMotor backleft;
    private DcMotor backright;
    private DcMotor armOne;
    private DcMotor armTwo;
    private int armOneState;
    private double armOneCooldown = 0.0;
    private int armTwoState;
    private double armTwoCooldown = 0.0;

    public ElapsedTime runtime = new.ElapsedTime();

    @Override
    public void runOpMode() {

        frontleft.hardwareMap.get(DcMotor.class, “fleft”);
        frontleftDrive.setDirection(DcMotor.Direction.Reverse);
        frontright.hardwareMap.get(DcMotor.class, “fright”);
        frontrightDrive.setDirection(DcMotor.Direction, FRONT);
        backleft.hardwareMap.get(DcMotor.class, “bleft”);
        backleftDrive.setDirection(DcMotor.Direction, REVERSE);
        backright.hardwareMap.get(DcMotor.class, “bright”);
        backrightDrive.setDirection(DcMotor.Direction, FRONT);
        armOne.hardwareMap.get(DcMotor.class , "arm")

        armOneState = 1;
        armOneCooldown = 0;
        
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            double drive  = gamepad1.left_stick_y*0.7;
            double strafe = gamepad1.left_stick_x*0.7;
            double twist  = gamepad1.right_stick_x*0.7;


            double[] speeds = {
                (drive + -strafe + -twist),
                (drive - -strafe - -twist),
                (drive - -strafe + -twist),
                (drive + -strafe - -twist)
            }

            if(gamepad1.x && (runtime-armOneCooldown >= 0.2)){
                if(armOneState < 3){
                    armOneState++
                    armOneCooldown = runtime;
                } else {
                    armOneState = 1;
                    armOneCooldown = runtime;
                }
            }
            
            if(armOneState == 1){
                armOne.setPower(1.0);
            } else if(armOneState == 2){
                armOne.setPower(-1.0;);
            } else{
                armOne.setPower(0.0);
            }

            
            if(gamepad1.a && (runtime-armTwoCooldown >= 0.2)){
                if(armTwoState == 1){
                    armTwoState++
                    armTwoCooldown = runtime;
                } else {
                    armTwoState = 1;
                    armTwoCooldown = runtime;
                }
            }

            if(armTwoState == 1){
                armTwo.setPower(1.0);
            } else if(armTwoState == 2){
                armTwp.setPower(-1.0;);
            } else{
                armOne.setPower(0.0);
            }

        }
    }
}
