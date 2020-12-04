#include <Servo.h>
 
Servo servo3;
Servo servo5;
Servo servo6;
Servo servo9;
Servo servo10;   
                
int angle = 90;
int servo = 0;    
int data = 0;

void setup() 
{ 
  servo3.attach(3);
  servo5.attach(5);
  servo6.attach(6);
  servo9.attach(9);
  servo10.attach(10);
  Serial.begin(9600);
} 
void loop() 
{ 
  if (Serial.available()>2) {
    data = Serial.read() << 8;
    data += Serial.read();
    data = data << 8;
    data += Serial.read(); 
    if(data>0)
    {
     angle=data%1000;
    servo=int(data/1000);
    switch(servo)
    {
      case 0:
      servo5.write(angle);
      break;
      
      case 1:
      servo3.write(angle);
      break;

      case 2:
      servo6.write(angle);
      break;

      case 3:
      servo9.write(angle);
      break;

      case 4:
      servo10.write(angle);
      break;

      default:
      break;  
    }
  }  
    }
  delay(20);
  ;
}
