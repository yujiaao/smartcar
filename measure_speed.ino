volatile byte revolutions;

float millipr;
float speed;
int counter;
int pre_counter;



unsigned long timeold;

unsigned long prev_int;

void setup()
{
  Serial.begin(9600);
  attachInterrupt(0, rpm_fun, RISING);

  revolutions = 0;
  counter=0;
  millipr = 1000000;
  timeold = millis();
  prev_int = timeold;
}

void loop()
{
  if (revolutions >= 20) { 
    //Update RPM every 20 counts, increase this for better RPM resolution,
    //decrease for faster update

    // calculate the revolutions per milli(second)
    //**rpmilli = (millis() - timeold)/revolutions;** EDIT: it should be revolutions/(millis()-timeold)

    millipr = (millis() - timeold-0.001)/revolutions;
    
    timeold = millis();
    //**rpmcount = 0;** (EDIT: revolutions = 0;)

    revolutions = 0;
    
    // WHEELCIRC = 2 * PI * radius (in meters)
    // speed = rpmilli * WHEELCIRC * "milliseconds per hour" / "meters per kilometer"

    // simplify the equation to reduce the number of floating point operations
    // speed = rpmilli * WHEELCIRC * 3600000 / 1000
    // speed = rpmilli * WHEELCIRC * 3600
    float WHEELCIRC = 0.1;

    speed =  WHEELCIRC * 3600/millipr/20;

    Serial.print("RPM:");
    Serial.print(60000.0/millipr/20,0);
    Serial.print(" Speed:");
    Serial.print(speed,2);
    Serial.print(" kph ");    
    Serial.println("");
  }
  int res = counter/2;
  if(pre_counter!=res)
    Serial.println(res);
  pre_counter = res;
}

void rpm_fun()
{
  unsigned long t = millis();
  if(t-prev_int > 2){
    revolutions++;
    counter++;
    prev_int = t;
  }
}
