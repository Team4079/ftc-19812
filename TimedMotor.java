package org.firstinspires.ftc.teamcode;
/*
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
*/
public class TimedMotor{
  long start = -1;
  long end = -1;
  int repeat = 0;
  int counter = 0;
  long repeatDelay = 0;
  long time = 0;
  boolean running = false;
  double speed = 1;
   public TimedMotor(){
      this.start = -1;
  };
  public void runMotor(long time, int repeat, int repeatDelay, double speed){
      this.start = System.currentTimeMillis();
      this.end = System.currentTimeMillis()+time;
      this.time = time;
      this.repeat = repeat;
      this.repeatDelay = repeatDelay;
      this.running = true;
      this.speed = speed;
  };
   public void runMotor(long time){
      this.start = System.currentTimeMillis();
      this.end = System.currentTimeMillis()+time;
      this.time = time;
      this.running = true;
  }
   public double running(){
      if(System.currentTimeMillis() > end){
          if(counter<repeat){
              counter++;
              start = end + repeatDelay;
              end = start + time;
          } else {
              this.reset();
          }
          return 0;
      } else if(System.currentTimeMillis() > start) {
          return speed;
      } else {
          return 0;
      }
  }
   public void cancelMotor(){
      this.reset();
  }
 
  public boolean isRunning(){
      return running;
  }
  public void reset(){
      start = -1;
      end = -1;
      repeat = 0;
      counter = 0;
      repeatDelay = 0;
      time = 0;
      running = false;
  }
}

