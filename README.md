# 2b2t Queue position to display
Simple utility to read your place in queue and send it to a display.

This setup consists of a PC with Minecraft client open and connected to 2b2t.org, sending changes in queue status via GET request over WiFi to a ESP32 that is running a AsyncHTTP server.


## Features
* Systray with exit button
![tray](https://github.com/UltraNyan/2b2t-que-display/img/systray.png)

## Source code provided in two sections

### /Java
Simple Java programm (jre 1.8) that reads .minecraft/logs/latest.log. Checks if the position has changed and sends a message to the URL specified.

### /Arduino

Now I dont want to get really specific with the code because it will largely depend on your setup, I will provide the code as an example.

#### Dependencies:
*Adafruit GFX library

*For the display I used [PxMatrix](https://github.com/2dom/PxMatrix)
