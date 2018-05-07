package com.iot.IotSink;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.common.Sensor;
import com.iot.model.SensorsCrud;

@EnableBinding(Sink.class)
public class IotSink {
	@Autowired 
	SensorsCrud sensors;
	ObjectMapper mapper=new ObjectMapper();
	@StreamListener(Sink.INPUT)
	void getSensor(String sensorData) throws InterruptedException, JsonParseException, JsonMappingException, IOException{
		
		System.out.println(sensorData);
		
	
		
	Sensor sensor=mapper.readValue(sensorData, Sensor.class);
		
	//System.out.println(sensor.id + " " +sensor.temperature+ " "+LocalDateTime.now().toString());	
	//void getMessage(String message) throws InterruptedException{
	//System.out.println(message+" "+
	//LocalDateTime.now().toString());
	
	sensors.addSensorData(sensor);
	//Thread.sleep(3000);
}
}
