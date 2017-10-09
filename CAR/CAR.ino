#if 1
__asm volatile ("nop");
#endif

//#define USE_IRremote
#define USE_IRsensor
//#define USE_LCD
#define USE_BLUETOOTH
#define USE_SUPERSONIC
//#define USE_TONE

#ifdef USE_IRremote
  #include <IRremote.h>  
#endif

#include <Servo.h>
#include "Wire.h" // for I2C bus

#ifdef USE_LCD
  #include "LiquidCrystal_I2C.h" // for I2C bus LCD module https://www.dfrobot.com/wiki/index.php?title=I2C/TWI_LCD1602_Module_%28Gadgeteer_Compatible%29_%28SKU:_DFR0063%29
  LiquidCrystal_I2C lcd(0x27,16,2);  //16字符,2行,I2C地址为0x27 0~127,与其他设备不重复即可 NOTE: The default address is 0x20. All the jumper caps will be connected from the factory.  
#endif

/**
 * 
A2  A1  A0  IIC Address
0   0   0   0x20
0   0   1   0x21
0   1   0   0x22
0   1   1   0x23
1   0   0   0x24
1   0   1   0x25
1   1   0   0x26
1   1   1   0x27
 */

//#define CAR_ONE

//#ifdef CAR_ONE
  #define BlueToothTXPin 0
  #define BlueToothRXPin 1
//#else 
//2号车暂时连的是2,3
//   #define BlueToothTXPin 2
//   #define BlueToothRXPin 3
//#endif

//***********************定義馬達腳位*************************
const int MotorRight1=5;
const int MotorRight2=6;
const int MotorLeft1=10;
const int MotorLeft2=11;
const int irReceiverPin = 2; //紅外線接收器 OUTPUT 訊號接在 pin 2

#ifdef USE_TONE
const int TonePin = 8;
#endif

const int ServoPin = 9;
const int BeepPin = A0;
const int LeftLightPin=A1;
const int RightLightPin=A2;
const int PhotocellPin=A3;

int counter=0;
//*************************定義CNY70腳位************************************
const int SensorLeft = 7;      //左感測器輸入腳
const int SensorMiddle= 4 ;    //中感測器輸入腳
const int SensorRight = 3;     //右感測器輸入腳
int SL;    //左感測器狀態
int SM;    //中感測器狀態
int SR;    //右感測器狀態

#ifdef USE_IRremote
IRrecv irrecv(irReceiverPin);  // 定義 IRrecv 物件來接收紅外線訊號
decode_results results;       // 解碼結果將放在 decode_results 結構的 result 變數裏

long IRfront= 0x202D02F;        //前進碼 //ff629d
long IRback=0x202708F;         //後退    //ffa857
long IRturnright=0x2028877;    //右轉     //ffc23d
long IRturnleft= 0x20208F7;     //左轉  //ff22dd
long IRstop=0x202B04F;         //停止 OK   //d7e84b1b
long IRpower = 0x20250AF;
long IRcny70=0x202807F;        //CNY70自走模式 //1:nec ff6897
long IRAutorun=0x20240BF;       //2   
long IRBeep = 0x202F00F; //press 7
long IRLeftLight=0x20258A7; //pressed *
long IRRightLight=0x20242BD; //pressed #
//超音波自走模式 
long IRturnsmallleft= 0x20200FF;  //middle blank button
long IRrepeat=0xFFFFFFFF; //按住某键不松手

#endif

//*************************定義超音波腳位******************************
const int outputPin =12; // 定義超音波信號發射腳位'tx
const int inputPin =13 ; // 定義超音波信號接收腳位rx
int Fspeedd = 0; // 前方距離
int Rspeedd = 0; // 右方距離
int Lspeedd = 0; // 左方距離
int directionn = 0; // 前=8 後=2 左=4 右=6 
Servo myservo; // 設 myservo
int delay_time = 250; // 伺服馬達轉向後的穩定時間
int Fgo = 8; // 前進
int Rgo = 6; // 右轉
int Lgo = 4; // 左轉
int Bgo = 2; // 倒車

//***********************設定所偵測到的IRcode*************************
/*
long IRfront= 0x00FF629D;        //前進碼 //ff629d
 long IRback=0x00FFA857;         //後退    //ffa857
 long IRturnright=0x00FFC23D;    //右轉     //ffc23d
 long IRturnleft= 0x00FF22DD;     //左轉  //ff22dd
 //long IRfront= 0x00FFA25D;        //前進碼 //ff629d
 //long IRback=0x00FF629D;         //後退    //ffa857
 //long IRturnright=0x00FFC23D;    //右轉     //ffc23d
 //long IRturnleft= 0x00FF02FD;     //左轉  //ff22dd
 //long IRstop=0x00FFE21D;         //停止    //d7e84b1b
 long IRstop=0x00FF02FD;         //停止 OK   //d7e84b1b
 //long IRcny70=0x00FFA857;        //CNY70自走模式 //1:nec ff6897
 long IRcny70=0x00FF6897;        //CNY70自走模式 //1:nec ff6897
 //long IRAutorun=0x00FF906F;      //超音波自走模式 //2:nec ff9867 
 long IRAutorun=0x00FF9867;     
 
 long IRBeep = 0xFF10EF; //press 7
 long IRLeftLight=0xFF42BD; //pressed *
 long IRRightLight=0xFF52AD; //pressed #
 //超音波自走模式 
 long IRturnsmallleft= 0x00FF22DD; 
 
 */
boolean leftLightOn=false;
boolean rightLightOn=false;
boolean autoLight = true; //用光敏电阻测光,启动后为自动模式,一旦手工干预即转为手动模式
int minLight = 350;//200;   // 最小光線門檻值
int ledState = 0;         //当前灯光亮还是不亮

//default 1:ff6897  2:nec ff9867 3:ffb04f 4:ff30cf 5:ff18e7 
//        6:ff7a85 7:ff10ef  8: ff38e7 9:ff5aa5 0:ff4ab5 *:ff42bd  #:ff52ad ok:ff02fd
//malata  1:90B57163 2:EA62C967  3:3E62BAFF 4:3EE5EF3F 5:98934743 
//        6:ED1678DB 7:CF34F03 8:9D67A0FF 9:4925C7E2 0:90E6EF1F
//        -/-- 3739971B  search D5A04AFF
//        up 3DE7BD9F left A27F12DF right FDE89AC3 down AECFC143 stop EFDB397B play 32F364DB
//TCL  1:202807F 2:20240BF  3:202C03F 4:20220DF 5:202A05F 
//        6:202609F 7:202E01F 8:20210EF 9:202906F 0:202A25D
//        soundmode 20258A7  videomode 20242BD blank 20200FF
//        up 202D02F left 20208F7 right 2028877 down 202708F ok 202B04F  power 20250AF
//        mute 202F00F sound-up 202A857



//********************************************************************(SETUP)

void setup()
{  
  Serial.begin(9600);
  Serial.println("-------------------Smart_Car_Start---------------------------------");

  pinMode(MotorRight1, OUTPUT);  // 腳位 8 (PWM)
  pinMode(MotorRight2, OUTPUT);  // 腳位 9 (PWM)
  pinMode(MotorLeft1,  OUTPUT);  // 腳位 10 (PWM) 
  pinMode(MotorLeft2,  OUTPUT);  // 腳位 11 (PWM)

#ifdef USE_IRremote  
  irrecv.enableIRIn();     // 啟動紅外線解碼
#endif

  pinMode(SensorLeft, INPUT); //定義左感測器
  pinMode(SensorMiddle, INPUT);//定義中感測器
  pinMode(SensorRight, INPUT); //定義右感測器
  digitalWrite(2,HIGH);
  pinMode(inputPin, INPUT); // 定義超音波輸入腳位
  pinMode(outputPin, OUTPUT); // 定義超音波輸出腳位 
  myservo.attach(ServoPin); // 定義伺服馬達輸出第9腳位(PWM)

  pinMode(BeepPin, OUTPUT);
  pinMode(LeftLightPin, OUTPUT);
  pinMode(RightLightPin, OUTPUT);
  pinMode(PhotocellPin, INPUT);

#ifndef CAR_ONE
  BT_setup();
#endif

#ifdef USE_TONE
  Notes_play(TonePin);
#endif

#ifdef USE_LCD
  lcd.init();          // initialize the lcd
  lcd.backlight(); // turn on LCD backlight
  lcd.print("Hello SmartCar  ");
  delay(500);
  lcd.setCursor(0, 0);
  lcd.print("I am Robot!     ");
  delay(500);
 #endif
 
  out("Who are you?    ");
}

void out(String message){
  Serial.print(message);
 #ifdef USE_LCD 
  lcd.setCursor(0, 1);
  lcd.print(message);
 #endif
}

//******************************************************************(Void)
const int speed_factor=20;
void advance(int a) // 前進
{
  digitalWrite(MotorRight1,LOW);
  digitalWrite(MotorRight2,HIGH);
  digitalWrite(MotorLeft1,LOW);
  digitalWrite(MotorLeft2,HIGH);
  delay(a * speed_factor); 
}
void right(int b) //右轉(單輪)
{
  digitalWrite(MotorLeft1,LOW);
  digitalWrite(MotorLeft2,HIGH);
  digitalWrite(MotorRight1,LOW);
  digitalWrite(MotorRight2,LOW);
  delay(b * speed_factor);
}
void left(int c) //左轉(單輪)
{
  digitalWrite(MotorRight1,LOW);
  digitalWrite(MotorRight2,HIGH);
  digitalWrite(MotorLeft1,LOW);
  digitalWrite(MotorLeft2,LOW);
  delay(c * speed_factor);
}
void turnR(int d) //右轉(雙輪)
{
  digitalWrite(MotorRight1,HIGH);
  digitalWrite(MotorRight2,LOW);
  digitalWrite(MotorLeft1,LOW);
  digitalWrite(MotorLeft2,HIGH);
  delay(d * speed_factor);
}
void turnL(int e) //左轉(雙輪)
{
  digitalWrite(MotorRight1,LOW);
  digitalWrite(MotorRight2,HIGH);
  digitalWrite(MotorLeft1,HIGH);
  digitalWrite(MotorLeft2,LOW);
  delay(e * speed_factor);
} 
void stopp(int f) //停止
{
  digitalWrite(MotorRight1,LOW);
  digitalWrite(MotorRight2,LOW);
  digitalWrite(MotorLeft1,LOW);
  digitalWrite(MotorLeft2,LOW);
  delay(f * speed_factor);
}
void back(int g) //後退
{
  digitalWrite(MotorRight1,HIGH);
  digitalWrite(MotorRight2,LOW);
  digitalWrite(MotorLeft1,HIGH);
  digitalWrite(MotorLeft2,LOW);
  delay(g * speed_factor); 
}


void run_by_joystick(int joyX, int joyY, int time_factor){
  joyX /=10;
  joyY /=10;
  int factor = time_factor;
  if(joyX==0 && joyY==0){
    stopp(factor);
     Serial.println("---");
  }else if(joyX<=2 && joyX>=-2){
    if(joyY>0){
     Serial.print("advance: ");
     Serial.println(joyY*factor);
     advance(joyY*factor);
    }else{
     Serial.print("back: ");
     Serial.println(-joyY*factor);
     back(-joyY*factor);
    }
  }else if(joyX>2 && joyX<=6){
     Serial.print("right: ");
     Serial.println(factor);
    turnR(factor);
  }else if(joyX>6){
     Serial.print("big right: ");
     Serial.println(factor);
     right(factor);
  }else if(joyX<-2 && joyX>=-6){
     Serial.print("left: ");
     Serial.println(factor);
    turnL(factor);
  }else if(joyX<-6){
     Serial.print("big left: ");
     Serial.println(factor);
    left(factor);
  }
}

void detection() //測量3個角度(前.左.右)
{ 
  int delay_time = 250; // 伺服馬達轉向後的穩定時間
  ask_pin_F(); // 讀取前方距離

  if(Fspeedd < 10) // 假如前方距離小於10公分
  {
    stopp(1); // 清除輸出資料 
    back(2); // 後退 0.2秒
  }
  if(Fspeedd < 25) // 假如前方距離小於25公分
  {
    stopp(1); // 清除輸出資料 
    ask_pin_L(); // 讀取左方距離
    delay(delay_time); // 等待伺服馬達穩定
    ask_pin_R(); // 讀取右方距離 
    delay(delay_time); // 等待伺服馬達穩定 

    if(Lspeedd > Rspeedd) //假如 左邊距離大於右邊距離
    {
      directionn = Lgo; //向左走
    }

    if(Lspeedd <= Rspeedd) //假如 左邊距離小於或等於右邊距離
    {
      directionn = Rgo; //向右走
    } 

    if (Lspeedd < 15 && Rspeedd < 15) //假如 左邊距離和右邊距離皆小於10公分
    {
      directionn = Bgo; //向後走 
    } 
  }
  else //加如前方大於25公分 
  {
    directionn = Fgo; //向前走 
  }
}   
//*********************************************************************************
float distance(int degree){
  myservo.write(degree);
  digitalWrite(outputPin, LOW); // 讓超聲波發射低電壓2μs
  delayMicroseconds(2);
  digitalWrite(outputPin, HIGH); // 讓超聲波發射高電壓10μs，這裡至少是10μs
  delayMicroseconds(10);
  digitalWrite(outputPin, LOW); // 維持超聲波發射低電壓
  float Fdistance = pulseIn(inputPin, HIGH); // 讀差相差時間
  Fdistance= Fdistance/5.8/10; // 將時間轉為距離距离（單位：公分）
  out(String(degree)+" distance:"+String(Fdistance)+"\n");
  // Serial.print(degree); //輸出距離（單位：公分）
  //Serial.print(" distance:"); //輸出距離（單位：公分）
  //Serial.println(Fdistance); //顯示距離
  return Fdistance; 
}

void ask_pin_F() // 量出前方距離 
{
  Fspeedd = distance(90); // 將距離 讀入Fspeedd(前速)
} 
//********************************************************************************
void ask_pin_L() // 量出左邊距離 
{
  Lspeedd = distance(177); // 將距離 讀入Lspeedd(左速)
} 
//******************************************************************************
void ask_pin_R() // 量出右邊距離 
{
  Rspeedd = distance(5); // 將距離 讀入Rspeedd(右速)
} 
//******************************************************************************(LOOP)
void beep(int milisecond){
  for(int i=0; i<3; i++){
    analogWrite(BeepPin, 255);
    delay(milisecond/6*(i+1));
    analogWrite(BeepPin,0);
    delay(milisecond/6*(i+1));
  }
}

void onButton1On(){
   autoLight = false;
   leftLightOn = true;
   rightLightOn = true;
   out("onButton1On");
}
void onButton1Off(){
   autoLight = false;
   leftLightOn = false;
   rightLightOn = false;
  out("onButton1Off");
}


void toggleLight(){
  if(autoLight){
    int photocellVal = analogRead(PhotocellPin);
    Serial.print("photocell=");   
    Serial.println(photocellVal);   
    
     
    // 光線不足時打開 LED
    if (photocellVal < minLight && ledState == 0) {
      leftLightOn = true;
      rightLightOn = true;
      ledState = 1;
    }
     
    // 光線充足時關掉 LED
    if (photocellVal > minLight && ledState == 1) {
      leftLightOn = false;
      rightLightOn = false;
      ledState = 0;
    }  
  } 
      
  if(leftLightOn){
    analogWrite(LeftLightPin,150);
  } 
  else{
    analogWrite(LeftLightPin,0);
  }
  if(rightLightOn){
    analogWrite(RightLightPin,150);
  } 
  else{
    analogWrite(RightLightPin,0);
  }
}

void xunji(){
  while(true){

  SL = digitalRead(SensorLeft);
  SM = digitalRead(SensorMiddle);
  SR = digitalRead(SensorRight);

  if (SM == HIGH)//中感測器在黑色區域
  { 
    if (SL == LOW & SR == HIGH) // 左黑右白, 向左轉彎
    {  
      digitalWrite(MotorRight1,LOW);
      digitalWrite(MotorRight2,HIGH);
      analogWrite(MotorLeft1,0);
      analogWrite(MotorLeft2,80);
    } 
    else if (SR == LOW & SL == HIGH) //左白右黑, 向右轉彎
    {  
      analogWrite(MotorRight1,0);//右轉
      analogWrite(MotorRight2,80);
      digitalWrite(MotorLeft1,LOW);
      digitalWrite(MotorLeft2,HIGH);
    }
    else  // 兩側均為白色, 直進
    { 
      digitalWrite(MotorRight1,LOW);
      digitalWrite(MotorRight2,HIGH);
      digitalWrite(MotorLeft1,LOW);
      digitalWrite(MotorLeft2,HIGH);
      analogWrite(MotorLeft1,200);
      analogWrite(MotorLeft2,200);
      analogWrite(MotorRight1,200);
      analogWrite(MotorRight2,200);
    }      
  } 
  else // 中感測器在白色區域
  {  
    if (SL == LOW & SR == HIGH)// 左黑右白, 快速左轉 
    {  
      digitalWrite(MotorRight1,LOW);
      digitalWrite(MotorRight2,HIGH);
      digitalWrite(MotorLeft1,LOW);
      digitalWrite(MotorLeft2,LOW);
    }
    else if (SR == LOW & SL == HIGH) // 左白右黑, 快速右轉
    {  
      digitalWrite(MotorRight1,LOW);
      digitalWrite(MotorRight2,LOW);
      digitalWrite(MotorLeft1,LOW);
      digitalWrite(MotorLeft2,HIGH);
    }
    else // 都是白色, 停止
    {    
      digitalWrite(MotorRight1,HIGH);
      digitalWrite(MotorRight2,LOW);
      digitalWrite(MotorLeft1,HIGH);
      digitalWrite(MotorLeft2,LOW);
      
    }
  }
  #ifdef USE_IRremote  
  if (irrecv.decode(&results))
  {
    irrecv.resume(); 
    Serial.println(results.value,HEX);
    if(results.value ==IRstop)
    { 
      digitalWrite(MotorRight1,HIGH);
      digitalWrite(MotorRight2,HIGH);
      digitalWrite(MotorLeft1,HIGH);
      digitalWrite(MotorLeft2,HIGH);
      break;
    }
  }
  #endif
  
  }
  
  #ifdef USE_IRremote  
  results.value=0;  
  #endif
}

boolean wantStop(){
  #ifdef USE_IRremote  
      if (irrecv.decode(&results))
      {
        irrecv.resume(); 
        Serial.println(results.value,HEX);
        if(results.value ==IRstop || results.value == IRpower)
        { 
          stopp(0);
          return true;
        }
      }
      results.value=0;
  #endif
    return false;
}


void echo(){
  while(true){
    myservo.write(90); //讓伺服馬達回歸 預備位置 準備下一次的測量
    detection(); //測量角度 並且判斷要往哪一方向移動
    if(directionn == 8) //假如directionn(方向) = 8(前進) 
    { 
      if(!wantStop()){
      advance(1); // 正常前進 
      Serial.print(" Advance "); //顯示方向(前進)
      Serial.print(" ");
      } else break;
    }
    if(directionn == 2) //假如directionn(方向) = 2(倒車) 
    {
      if(!wantStop()){
      back(8); // 倒退(車)
      turnL(3); //些微向左方移動(防止卡在死巷裡)
      Serial.print(" Reverse "); //顯示方向(倒退)
     }else break;
    }
    if(directionn == 6) //假如directionn(方向) = 6(右轉) 
    {
      if(!wantStop()){
      
      back(1); 
      turnR(6); // 右轉
      Serial.print(" Right "); //顯示方向(左轉)
      }else break;
    }
    if(directionn == 4) //假如directionn(方向) = 4(左轉) 
    { 
     if(!wantStop()){
      back(1); 
      turnL(6); // 左轉
      Serial.print(" Left "); //顯示方向(右轉) 
      }else break;
    } 

//#ifdef USE_IRremote
//    if (irrecv.decode(&results))
//    {
//      irrecv.resume(); 
//      Serial.println(results.value,HEX);
//      if(results.value ==IRstop)
//      { 
//        stopp(0);
//        break;
//      }
//    }
//#endif

  }
#ifdef USE_IRremote  
  results.value=0;
#endif
}


void loop() 
{

  BT_loop();

  static int loop_count =  0;
  if(loop_count++>100){
      loop_count =0;
      distance(90);
  }
  //***************************************************************************正常遙控模式    
  #ifdef USE_IRremote    
  if (irrecv.decode(&results)) 
  {         // 解碼成功，收到一組紅外線訊號
    /***********************************************************************/
    Serial.print("IR decode:"); //輸出距離（單位：公分）
    Serial.println(results.value, HEX);


    if (results.value == IRfront)//前進
    {
      advance(10);//前進
    }
    /***********************************************************************/
    if (results.value ==  IRback)//後退
    {
      back(10);//後退
    }
    /***********************************************************************/
    if (results.value == IRturnright)//右轉
    {
      right(6); // 右轉
    }
    /***********************************************************************/
    if (results.value == IRturnleft)//左轉
    {
      left(6); // 左轉);
    }
    /***********************************************************************/
    if (results.value == IRstop || results.value == IRpower)//停止
    {
       stopp(0);
    }
    //***********************************************************************cny70模式自走模式 黑:LOW 白:
    if (results.value == IRcny70)
    {                     
      
        xunji();
            
    }
    else
      //***********************************************************************超音波自走模式
      if (results.value ==IRAutorun )
      {
        
        echo();
        
        
      }
      else
      {
        stopp(0);
      }
    /***********************************************************************/
    if (results.value ==IRBeep )
    {
      beep(1000);
    }
    /***********************************************************************/

    /***********************************************************************/
    if (results.value ==IRLeftLight )
    {
      onButton1On();
    }
    /***********************************************************************/
    /***********************************************************************/
    if (results.value ==IRRightLight )
    {
      onButton1Off();
    }
    /***********************************************************************/


    irrecv.resume();    // 繼續收下一組紅外線訊號        
  }  
  #endif
  toggleLight();
}



