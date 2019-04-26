import string
import pynmea2
import RPi.GPIO as gpio
import requests
import webbrowser
import os

URL = "http://192.168.43.254/change.php"
file=open("data.txt","r+")
count=0
for line in file:
        count=count+1
        if line.startswith('$GPGGA'):
                msg = pynmea2.parse(line)
                latval=float(msg.lat)
                longval=float(msg.lon)
                min=latval%100
                int1 = int(latval/100)
                dec=min/60
                latf=int1+dec
                concatlat = "lat:" + str(latf);
#                print concatlat
        #parse the longitude and print
                int2=int(longval/100)
                min2=longval%100
                deg2=min2/60
                longf=int2+deg2
                concatlong = "long:"+ str(longf)
#               print concatlong
print concatlat
print concatlong
params = {'lat':str(latf),'long':str(longf)}
r = requests.get(url = URL, params = params)
webbrowser.open(r.url)