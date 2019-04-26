import os
from array import *
from array import *
import time
import json

#bus_route = [{"index":0 ,"name":"Main_Gate","lat": 28.545813,"long": 77.196516,"flag": 0}, {"index":1, "name":"Lecture_Hall","lat": 28.544309,"long": 77.192719,"flag": 0}, {"index":2,"name": "Chaayos","lat": 28.545975,"long": 77.186868,"flag" :0} ,{"index": 3,"name": "T_point","lat": 28.547362,"long": 77.183608,"flag": 0} ,{"index":4,"name": "Chaayos","lat": 28.545975,"long": 77.186868,"flag" :0} , {"index":5, "name":"Lecture_Hall","lat": 28.544309,"long": 77.192719,"flag": 0},{"index":6, "name":"Main_Gate","lat": 28.545813,"long": 77.196516,"flag": 0}]
bus_route = [{"index":0 ,"name":"Main_Gate","lat": 28.545813,"long": 77.196516,"flag": 0}, {"index":1, "name":"Himadri_Circle","lat": 28.544908,"long": 77.194593,"flag": 0} , {"index":2, "name":"Lecture_Hall","lat": 28.544309,"long": 77.192719,"flag": 0}, {"index":3, "name":"Bharti","lat": 28.544539,"long": 77.190379,"flag": 0} , {"index":4, "name":"Hospital","lat": 28.545420,"long": 77.188244,"flag": 0} , {"index":5,"name": "Chaayos","lat": 28.545975,"long": 77.186868,"flag": 0} , {"index":6, "name":"SAC_Circle","lat": 28.546652,"long": 77.185249,"flag": 0} , {"index":7, "name":"T_Point","lat": 28.547375,"long": 77.183622,"flag": 0} , {"index":8, "name":"Jwala_Circle","lat": 28.548972,"long": 77.184461,"flag": 0} , {"index":9, "name":"Vindhyanchal","lat": 28.548598,"long": 77.185741,"flag": 0}]
#Bus_route = [{"index":0, "name":"Himadri_Circle","lat": 28.544908,"long": 77.194593,"flag": 0}, {"index":1, "name":"Jia_Sarai","lat": 28.547899,"long": 77.190395,"flag": 0}, {"index":2,"name": "Flyover","lat": 28.548808,"long": 77.187988,"flag": 0} , {"index":3, "name":"over","lat": 28.553441,"long": 77.179460,"flag": 0} , {"index":4, "name":"Palam","lat": 28.555843,"long": 77.175799,"flag": 0} ,  {"index":5, "name":"Right_Turn","lat": 28.557752,"long": 77.172932,"flag": 0}, {"index":6, "name":"Destination","lat": 28.560780,"long": 77.175537,"flag": 0}]
#bus_route = Bus_route
diff_lat = 0.0004
diff_long = 0.0004

feed = 0
#Adding Channel To Listen to changes in route


from pubnub.callbacks import SubscribeCallback
from pubnub.enums import PNStatusCategory
from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub
import time
import os
pnconfig = PNConfiguration()
pnconfig.publish_key = "pub-c-27b94a8d-253c-4e9b-a169-a9b028a101cc"
pnconfig.subscribe_key = "sub-c-dfa4069c-3b53-11e9-b221-7a660e69c40f"
pnconfig.ssl = False
pnconfig.reconnect_policy = 'PNReconnectionPolicy.LINEAR'
pubnub = PubNub(pnconfig)
def my_publish_callback(envelope, status):
    # Check whether request successfully completed or not
    if not status.is_error():
        pass

class MySubscribeCallback(SubscribeCallback):
    def presence(self, pubnub, presence):
        pass
    def status(self, pubnub, status):
        pass
    def message(self, pubnub, message):
        # lst = json.loads(message.message)
        print(message.message)
        global feed
        global bus_route
        feed = 1
        bus_route = message.message
	pubnub.publish().channel("route-conf").message("okay").pn_async(my_publish_callback)
        # print(bus_route)

pubnub.add_listener(MySubscribeCallback())
pubnub.subscribe().channels("route-bus2").execute()




current_lat = 0
current_long = 0
next_stops = [0,0,0]

def start():
	bus_route[0]["flag"] = 1;
	next_stops[0] = bus_route[1]["name"]
	next_stops[1] = bus_route[2]["name"]
	next_stops[2] = bus_route[3]["name"]

def update():
	# current_lat = input("lat: ")
	# current_long = input("long: ")
	try:
		f = open("coordinate.txt", "r") 
	# print("2" + f.read())
		coordinate = f.read()

		current_lat = coordinate.split(",")[0]
		current_long = coordinate.split(",")[1]
		print(current_long)
		print(current_lat)

	# def algo():
		for stop in bus_route:
			if(stop["flag"] == 0):
				# print(stop["index"])
				# print(current_lat)
				# print(current_long)
				if(abs(float(current_lat) - float(stop["lat"])) <= diff_lat and abs(float(current_long) - float(stop["long"]) <= diff_long)):
					print("hi")
					stop["flag"] = 1
					if(stop["index"] + 3 < len(bus_route)):
						next_stops[0] = bus_route[bus_route[stop["index"] + 1]["index"] ]["name"]
						next_stops[1] = bus_route[bus_route[stop["index"] + 2]["index"] ]["name"]
						next_stops[2] = bus_route[bus_route[stop["index"] + 3]["index"] ]["name"]
					elif(stop["index"] + 3 == len(bus_route)):
						next_stops[0] = bus_route[bus_route[stop["index"] + 1]["index"] ]["name"]
						next_stops[1] = bus_route[bus_route[stop["index"] + 2]["index"] ]["name"]
						next_stops[2] = "stop"
					elif(stop["index"] + 2 == len(bus_route)):
						next_stops[0] = bus_route[bus_route[stop["index"] + 1]["index"] ]["name"]
						next_stops[1] = "stop"
						next_stops[2] = "stop"
					else:
						next_stops[0] = "stop"
						next_stops[1] = "stop"
						next_stops[2] = "stop" 

				break
	except:
		print("not found")

def execute():
	global feed
	start()
	i = 0
	while(feed == 0):
		update()
		# algo()
		print(next_stops)
		command = "PyBeacon -u\"https://" + str(i+1) + next_stops[i] + ".com\""
		os.system(command)
		i = (i+1)%3
		time.sleep(1)
		print(feed)
	print(bus_route)
	feed = 0
	execute()

execute()







