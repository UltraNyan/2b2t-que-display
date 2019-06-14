# 2b2t Queue position to display
Simple utility to read your place in que and send it to a display.

Currently based on a solution that has a PC with Minecraft client open and connected to 2b2t.org, sending changes in queue status via GET request over WiFi to a ESP32 that is running a AsyncHTTP server.

## Source code provided in two sections

### /Java
Simple Java programm (jre 1.8) that reads .minecraft/logs/latest.log. Checks if the position has changed and sends the numer to the URL specified.

### /Arduino

Now I dont want to get really spetcific with the code because it will largely depend on your setup, I will just provide the code as an example.
Dependencies:
Adafruit GFX library

For the display I used.
PxMatrix - [PxMatrix](https://github.com/2dom/PxMatrix)
