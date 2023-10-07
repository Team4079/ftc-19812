import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp

public class ServoMove extends LinearOpMode {
   private CRServo clawServo;
   boolean clawOpen = false;
   int count = 0;
   
   @Override
   public void runOpMode(){
        clawServo = hardwareMap.get(CRServo.class, "clawServo");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        waitForStart();
        
        telemetry.addData("Status", "Started");
        telemetry.update();
        
        while(opModeIsActive()){
            double armPower = 0;
           if(gamepad1.a){
               if(count > 100000){
                   count = 0;
                   if(!clawOpen){
                       clawOpen = true;
                   } else {
                       clawOpen = false;
                   }
               }
           }
           if(clawOpen){
                armPower = 1;
           } else {
                armPower = 0;
           }
           telemetry.addData("Status", armPower);
           telemetry.addData("Status", count);
           telemetry.update();
           clawServo.setPower(armPower);
           count++;
        }
   }
}
