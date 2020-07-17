Adding to your project
----------------------
Step 1. You should add this to your dependencies:



            allprojects {
			repositories {
		    
                        maven { url 'https://jitpack.io' }
			}
		}
	
	
Step 2. You should add this to your root build.gradle at the end of repositories:

	        implementation 'com.github.ttawatchai:data-behavior-traking-library:Tag'

##Library

For starting the library service:
''Kotlin
        var tracking = DataBehabviorTracking.getInstance(this)
## Location
### Starting

For starting the location service:

- implement to youre service

	````Kotlin
		// 1sec == 1000
		var interval = 1000
		tracking!!.startTracking(interval)
	````

If you just want to get a single location (not periodic) you can just use the oneFix modifier. Example:

````Kotlin
        tracking.getLastLocation()
````

## Start sent DataBehavior to Mqtt

*If you just want to send data behavior to mqtt away time interval. Example:

Step 1. create mqtt data

````Kotlin
            var config =  MqttConfig("url","port","portSSL","portWs","username","password","isactive","createBy","createDate","updateBy","updateDate","topic","id")
            tracking.setConfigMqtt(config)
````
    
*see logcat tag " MQTT " if show"  [CON] Connected to: $serverURI " is success to connect mqtt
    

Step 2. Create data info *requrie for publish to mqtt server

````Kotlin
 	var config = TrackInfo("carId","carPlate","companyId","companyName","driverId"(Int),"driverName","policyNo","streamId"(Int))
	tracking.sendDataInfo(config)
````

*see logcat tag "MQTT_message" if show  Data is success check your mqtt server
        
