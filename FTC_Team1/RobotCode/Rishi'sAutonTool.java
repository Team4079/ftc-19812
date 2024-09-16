package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.ArrayList;
import java.lang.reflect.Array;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import java.text.DecimalFormat;
import java.math.BigInteger;
import java.util.ArrayList;

@TeleOp(name="TEST", group="Iterative Opmode")

public class RishiTEST extends OpMode {

    /*
     * The mecanum drivetrain involves four separate motors that spin in
     * different directions and different speeds to produce the desired
     * movement at the desired speed.
     */
    // declare and initialize four DcMotors.
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_left;
    private DcMotor back_right;
    private DcMotor LSlideVert; //pivot
    private DcMotor RSlideVert; //pivot
    private DcMotor RSlideMotor; //extend
    private DcMotor LSlideMotor; //extend
    private CRServo leftServo;
    private CRServo rightServo;
    private CRServo planeServo;
    private double power=1;
    private double LSlideVertPower; //arm pivot motors
    private double RSlideVertPower;
    private double LSlidePower;
    private double RSlidePower;
    private double FRightPower;
    private double BRightPower;
    private double FLeftPower;
    private double BLeftPower;
    private double slideLastPressed;
    private double RServoPower;
    private double LServoPower;
    private double planePower;
    public ElapsedTime timesofar = new ElapsedTime();
    private double servoLastPressed;
    private boolean servoState = true;
    private int armState;
    private double armLastPressed;
    private boolean extraServoBool = false;
    private double extraServoPower = 0;
    
    private ArrayList<Double> forwardTimes;
    private ElapsedTime elapsedTimeB;
    private double elapsedTime;
    private boolean isPressed;
    private double direction;
    private double startTime;
    private double endTime;
    private double listNumber;

    @Override
    public void init() {
        timesofar.reset();
        servoLastPressed = timesofar.time();
        slideLastPressed = timesofar.time();
        armLastPressed = timesofar.time();
        servoState = false;
        armState = 0;
        
        forwardTimes = new ArrayList<>();
        elapsedTimeB = new ElapsedTime();
        elapsedTime = 0.0;
        isPressed = false;
        startTime = 0.0;
        endTime = 0.0;
        direction = 0.0;
        listNumber = 0;

        // Name strings must match up with the config on the Robot Controller
        front_left = hardwareMap.get(DcMotor.class, "fLeft");
        front_right = hardwareMap.get(DcMotor.class, "fRight");
        back_left = hardwareMap.get(DcMotor.class, "bLeft");
        back_right = hardwareMap.get(DcMotor.class, "bRight");

        LSlideVert = hardwareMap.get(DcMotor.class, "LSlideVert");
        RSlideVert = hardwareMap.get(DcMotor.class, "RSlideVert");
        leftServo = hardwareMap.get(CRServo.class, "leftServo");
        rightServo = hardwareMap.get(CRServo.class, "rightServo");
        RSlideMotor = hardwareMap.get(DcMotor.class, "RightSlideMotor");
        LSlideMotor = hardwareMap.get(DcMotor.class, "LeftSlideMotor");
        planeServo = hardwareMap.get(CRServo.class, "planeServo");
        front_right.setDirection(DcMotor.Direction.REVERSE);
        back_left.setDirection(DcMotor.Direction.REVERSE);
        front_left.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        elapsedTime = roundNumber(elapsedTimeB.time());
        if (gamepad1.right_bumper){
            armState = 1;
        } else if(gamepad1.left_bumper){
            armState = 2;
        } else if(armState != 3){
            armState = 0;
            LSlideVertPower = 0.1;
            RSlideVertPower = -0.1;
        }
        
        if(gamepad1.right_trigger > 0.5){
            if(timesofar.time() - armLastPressed > 0.5){
                armLastPressed = timesofar.time();
                if(armState == 3){
                    armState = 0;
                } else {
                    armState = 3;
                }
            }
        }
        
        //state 0 is holding the arms there
        
        if(armState == 1){ //up
            LSlideVertPower = 0.6;
            RSlideVertPower = -0.6;
        } else if(armState == 2){ //down (slowly) find a way to scale with arm pos (uhh encoders)
            LSlideVertPower = 0.005;
            RSlideVertPower = -0.005;
        } else if(armState == 3){ //downerly (free fall)
            LSlideVertPower = 0.0;
            RSlideVertPower = 0.0;
        }
        
        
        if (gamepad1.b) {
            LSlidePower = -1.0;
            RSlidePower = 0.85;
        } else if (gamepad1.x) {
            LSlidePower = 1.0;
            RSlidePower = -0.85;
        } else {
            LSlidePower = 0;
            RSlidePower = 0;
        }
        
        if (gamepad1.dpad_left) {
           move(0.4, -0.4, 0.4, -0.4);
           addList(0.004);
        } else if (gamepad1.dpad_right) {
           move(-0.4, 0.4, -0.4, 0.4);
           addList(0.002);
        } else if (gamepad1.dpad_down) {
            move(-0.4, -0.4, -0.4, -0.4);
            addList(0.003);
        } else if (gamepad1.dpad_up) {
            move(0.4, 0.4, 0.4, 0.4);
            addList(0.001);
        } else if (gamepad1.left_trigger > 0) {
            move(0.4, 0.4, -0.4, -0.4);
            addList(0.005);
        } else if (gamepad1.y) {
            move(-0.4, -0.4, 0.4, 0.4);
            addList(0.006);
        } else {
            move(0, 0, 0, 0);
        }
        
        if(startTime > endTime && !gamepad1.dpad_up && !gamepad1.dpad_right && !gamepad1.dpad_down && !gamepad1.dpad_left && gamepad1.left_trigger == 0 && !gamepad1.y) {
            endTime = elapsedTime;
            listNumber = roundNumber(endTime - startTime);
            listNumber += direction;
            forwardTimes.add(listNumber);
            isPressed = false;
        }
        // claw
        if (gamepad1.a) {
            if(timesofar.time() - servoLastPressed > 0.3){
                servoLastPressed = timesofar.time();
                servoState = !servoState;
            }
        }
        
        //Leftservo, + is in - is out
        
        if(servoState){ //let go
            RServoPower = 0.6;
            LServoPower = -0.6;
        } else { // grabby
            RServoPower = -1.0;
            LServoPower = 1.0;
        }

        RServoPower -= extraServoPower;
        LServoPower += extraServoPower;
        
        telemetry.addData("Encrypted: ", arrayListToBase64(typeCast(forwardTimes)));
        telemetry.addData("LSlideMotor", LSlideVertPower);
        telemetry.addData("RSlideMotor", RSlideVertPower);
        telemetry.addData("LServo", LServoPower);
        telemetry.addData("RServo", RServoPower);
        telemetry.addData("LExtend", LSlidePower);
        telemetry.addData("RExtend", RSlidePower);
        telemetry.addData("Break", "Break");
        telemetry.addData("armState", armState);
        telemetry.addData("ServoState", servoState);
        telemetry.addData("PlanePower", planePower);
        
        telemetry.addData("forwardTimes", forwardTimes);
        telemetry.addData("elapsedTime", elapsedTime);
        telemetry.update();

        // apply the calculated values to the motors.
        front_left.setPower(FLeftPower);
        front_right.setPower(FRightPower);
        back_left.setPower(BLeftPower);
        back_right.setPower(BRightPower);
        LSlideVert.setPower(LSlideVertPower);
        RSlideVert.setPower(RSlideVertPower);
        leftServo.setPower(LServoPower);
        rightServo.setPower(RServoPower);
        RSlideMotor.setPower(RSlidePower);
        LSlideMotor.setPower(LSlidePower);
        planeServo.setPower(planePower);
    }
    
    public double roundNumber(double number) {
        return Math.round(number * 100.00) / 100.00;
    }
    
    public double move(double FRPower, double BRPower, double BLPower, double FLPower) {
        FRightPower = FRPower;
        BRightPower = BRPower;
        BLeftPower = BLPower;
        FLeftPower = FLPower;
        return 0.0;
    }
    
    public double addList(double directionM) {
        if (isPressed == false) {
            isPressed = true;
            startTime = elapsedTime;
        }
        direction = directionM;
        return 0.0;
    }
        private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,+/=.;:_-<>?!@#$%^&*()";
    
    public static String decimalToBase64(int decimal) {
        StringBuilder base64 = new StringBuilder();
        do {
            base64.insert(0, BASE64_CHARS.charAt(decimal % BASE64_CHARS.length()));
            decimal /= BASE64_CHARS.length();
        } while (decimal > 0);
        
        // Add padding if necessary
        while (base64.length() < 3) {
            base64.insert(0, 'A'); // Assuming 'A' is the padding character
        }
        
        return base64.toString();
    }
    
    public static BigInteger base64ToDecimal(String base64) {
        return getBigInteger(base64, BASE64_CHARS);
    }
    
    public static BigInteger getBigInteger(String base64, String base64Chars) {
        BigInteger decimal = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(base64Chars.length());
        
        for (int i = 0; i < base64.length(); i++) {
            char c = base64.charAt(i);
            int index = base64Chars.indexOf(c);
            decimal = decimal.multiply(base).add(BigInteger.valueOf(index));
        }
        
        return decimal;
    }
    
    // Convert ArrayList<Integer> to Base64 String
    public static String arrayListToBase64(ArrayList<Integer> list) {
        StringBuilder base64 = new StringBuilder();
        for (int num : list) {
            String decimalBase64 = decimalToBase64(num);
            base64.append(decimalBase64);
        }
        return base64.toString();
    }
    
    // Convert Base64 String to ArrayList<Integer>
    public static ArrayList<Integer> base64ToArrayList(String base64) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < base64.length(); i += 3) {
            String chunk = base64.substring(i, Math.min(i + 3, base64.length()));
            BigInteger decimal = base64ToDecimal(chunk);
            list.add(decimal.intValue());
        }
        return list;
    }
    
    public ArrayList<Integer> typeCast(ArrayList<Double> code) {
        ArrayList<Integer> code2 = new ArrayList<>();
        int typeCasting = 0;
        double save = 0.0;
        for (int j = 0; j < code.size(); j++) {
            save = code.get(j)*1000;
            typeCasting = (int) save;
            code2.add(typeCasting);
        }
        return code2;
    }

}

















//BASE64 DECRYPTION:

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class MyClass {
    
    private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,+/=.;:_-<>?!@#$%^&*()";
    
    public static String decimalToBase64(int decimal) {
        StringBuilder base64 = new StringBuilder();
        do {
            base64.insert(0, BASE64_CHARS.charAt(decimal % BASE64_CHARS.length()));
            decimal /= BASE64_CHARS.length();
        } while (decimal > 0);
        
        // Add padding if necessary
        while (base64.length() < 3) {
            base64.insert(0, 'A'); // Assuming 'A' is the padding character
        }
        
        return base64.toString();
    }
    
    public static BigInteger base64ToDecimal(String base64) {
        return getBigInteger(base64, BASE64_CHARS);
    }
    
    public static BigInteger getBigInteger(String base64, String base64Chars) {
        BigInteger decimal = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(base64Chars.length());
        
        for (int i = 0; i < base64.length(); i++) {
            char c = base64.charAt(i);
            int index = base64Chars.indexOf(c);
            decimal = decimal.multiply(base).add(BigInteger.valueOf(index));
        }
        
        return decimal;
    }
    
    // Convert ArrayList<Integer> to Base64 String
    public static String arrayListToBase64(ArrayList<Integer> list) {
        StringBuilder base64 = new StringBuilder();
        for (int num : list) {
            String decimalBase64 = decimalToBase64(num);
            base64.append(decimalBase64);
        }
        return base64.toString();
    }
    
    // Convert Base64 String to ArrayList<Integer>
    public static ArrayList<Integer> base64ToArrayList(String base64) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < base64.length(); i += 3) {
            String chunk = base64.substring(i, Math.min(i + 3, base64.length()));
            BigInteger decimal = base64ToDecimal(chunk);
            list.add(decimal.intValue());
        }
        return list;
    }
    
    public static void main(String[] args) {
        int typeCast = 0;
        double save = 0.0;
        ArrayList<Integer> code2 = new ArrayList<>();
        //enter THE code here:
        code2.addAll(base64ToArrayList("AFVAGTAJuAKE"));
        System.out.println("Make sure you have this method added:");
        System.out.println("public double move(double FRPower, double BRPower, double BLPower, double FLPower) { \n     frontLeft.setPower(FLPower);\n     frontRight.setPower(FRPower);\n     backLeft.setPower(BLPower);\n     backRight.setPower(BRPower);\n     return 0.0; \n } \n \n");
        System.out.println("");
        for (int i = 0; i < code2.size(); i++) {
            if ((code2.get(i)%10) == 1) {
                System.out.println("            move(0.4, 0.4, 0.4, 0.4); \n sleep(" + code2.get(i) + ");");
            }   else if ((code2.get(i)%10) == 2) {
                    System.out.println("            move(-0.4, 0.4, -0.4, 0.4); \n sleep(" + code2.get(i) + ");");
            }   else if ((code2.get(i)%10) == 3) {
                    System.out.println("            move(-0.4, -0.4, -0.4, -0.4); \n sleep(" + code2.get(i) + ");");
            }   else if ((code2.get(i)%10) == 4) {
                    System.out.println("            move(0.4, -0.4, 0.4, -0.4); \n sleep(" + code2.get(i) + ");");
            }   else if ((code2.get(i)%10) == 5) {
                    System.out.println("            move(-0.4, -0.4, 0.4, 0.4); \n sleep(" + code2.get(i) + ");");
            }   else if ((code2.get(i)%10) == 6) {
                    System.out.println("            move(0.4, 0.4, -0.4, -0.4); \n sleep(" + code2.get(i) + ");");
            }
        }
        System.out.println("move(0, 0, 0, 0);");
        
        System.out.println("\n \n \n For stinky poopooheads who use encoders: ");
        for(int j = 0; j < code2.size(); j++) {
            if ((code2.get(j)%10) == 1) {
                System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }   else if ((code2.get(j)%10) == 2) {
                    System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }   else if ((code2.get(j)%10) == 3) {
                    System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }   else if ((code2.get(j)%10) == 4) {
                    System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }   else if ((code2.get(j)%10) == 5) {
                    System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }   else if ((code2.get(j)%10) == 6) {
                    System.out.println("            driveEncoders(" + code2.get(j) + ");");
            }
        }
    }
}
