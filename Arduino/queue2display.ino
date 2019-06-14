
#include <PxMatrix.h>

#include "WiFi.h"
#include "ESPAsyncWebServer.h"
const char* ssid = "YOUR WIFI NAME";
const char* password =  "YOUR WIFI PASSWORD";
AsyncWebServer server(80);

String message = "";

TaskHandle_t Task1;


#define P_LAT 22
#define P_A 19
#define P_B 23
#define P_C 18
#define P_D 5
#define P_E 15
#define P_OE 2
hw_timer_t * timer = NULL;
portMUX_TYPE timerMux = portMUX_INITIALIZER_UNLOCKED;



uint8_t display_draw_time=0;

// Pins for LED MATRIX
PxMATRIX display(64,32,P_LAT, P_OE,P_A,P_B,P_C,P_D,P_E);

// Display specific stuff 
void IRAM_ATTR display_updater(){
  // Increment the counter and set the time of ISR
  portENTER_CRITICAL_ISR(&timerMux);
  display.display(display_draw_time);
  portEXIT_CRITICAL_ISR(&timerMux);
}

void display_update_enable(bool is_enable)
{
  if (is_enable)
  {
    timer = timerBegin(0, 80, true);
    timerAttachInterrupt(timer, &display_updater, true);
    timerAlarmWrite(timer, 2000, true);
    timerAlarmEnable(timer);
  }
  else
  {
    timerDetachInterrupt(timer);
    timerAlarmDisable(timer);
  }
}


void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
 
  Serial.println("Connected to the WiFi network");

  //Leak your IP
  Serial.println(WiFi.localIP());
 
  server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
 
    int paramsNr = request->params();
    Serial.println(paramsNr);
 
    for(int i=0;i<paramsNr;i++){
 
        AsyncWebParameter* p = request->getParam(i);
        Serial.print("Param name: ");
        Serial.println(p->name());
        Serial.print("Param value: ");
        Serial.println(p->value());
        Serial.println("------");
        if(p->name() == "message") {
          message =  p->value();
        }
    }
 
    request->send(200, "text/plain", "message received");
  });
 
  server.begin();
 

  //create a task that will be executed in the Task1code() function, with priority 1 and executed on core 0
  xTaskCreatePinnedToCore(
    Task1code,   /* Task function. */
    "Task1",     /* name of task. */
    10000,       /* Stack size of task */
    NULL,        /* parameter of the task */
    1,           /* priority of the task */
    &Task1,      /* Task handle to keep track of created task */
    0);          /* pin task to core 0 */                  
  delay(500); 
}


uint8_t icon_index=0;
void loop() {


}

//Task1code
void Task1code( void * pvParameters ){

  // Define your display layout here, e.g. 1/16 step
  display.begin(16);

  // Define your scan pattern here {LINE, ZIGZAG, ZAGGIZ} (default is LINE)
  //display.setScanPattern(LINE);

  // Define multiplex implemention here {BINARY, STRAIGHT} (default is BINARY)
  //display.setMuxPattern(BINARY);

  display.setFastUpdate(false);
  display_draw_time=15;
  display.clearDisplay();
  display_update_enable(true);


  display.setBrightness(1);

  //lul
  int circles[10][3]= {
    {1,1,14,},
    {1,1,44,},
    {1,1,54,},
    {1,1,18,},
    {1,1,37,},
    {1,1,24,},
    {1,1,14,},
    {1,1,22,},
    {1,1,60,},
    {1,1,43,},
  };

  for(;;) {

    display.clearDisplay();
    for (int i = 0; i<10;i++) {
      if(circles[i][2]==64){
        circles[i][0]=random(0, 64);
        circles[i][1]=random(0, 32);
        circles[i][2]=1;
      }  
      ++circles[i][2];
      display.drawCircle(circles[i][0], circles[i][1], circles[i][2] , display.color565(45,45,45));
    }
    
    display.setTextWrap(true);
    display.setTextColor(display.color565(214, 156, 29));
    display.setCursor(0,0);
    display.print("Position");
    display.setCursor(0,8);
    display.print("in queue:");
    display.setCursor(0,20);
    display.print(message);
    
    delay(100);
  }
  
}