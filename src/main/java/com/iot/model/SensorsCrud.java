package com.iot.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.iot.common.MeasureUnit;
import com.iot.common.Sensor;
import com.iot.common.SensorCarFuel;
import com.iot.common.SensorHeartRate;
import com.iot.common.SensorTemp;
import com.iot.entities.SensorCrud;
import com.iot.interfaces.ISensor;
import com.iot.repo.SensorsRepository;



public class SensorsCrud implements ISensor {
	@Autowired
SensorsRepository sensors;

	@Override
	public void addSensorData(Sensor sensor) {
		sensors.save(new SensorCrud(sensor));

	}

	@Override
	public int getMaximalData(int sensorId, String dataTimeFrom, String dataTimeTo) {
		return getMinMax(sensorId, dataTimeFrom, dataTimeTo,
		(x,y)->Integer.compare(x.data, y.data));
	}

	private int getMinMax(int sensorId, String dataTimeFrom, String dataTimeTo,
			Comparator<? super SensorCrud> comparator) {
		long timeFrom=getTimestamp(dataTimeFrom);
		long timeTo=getTimestamp(dataTimeTo);
		if(timeTo==0)
			timeTo=Long.MAX_VALUE;
		
		
		return sensors.findBySensorIdAndTimestampBetween
				(sensorId,timeFrom, timeTo)
				.max(comparator).orElse(new SensorCrud()).data;
	}
	private long getTimestamp(String dataTime) {
		DateTimeFormatter dtf=DateTimeFormatter
				.ofPattern("yyyy-MM-dd:HH.mm");
		try {
			LocalDateTime ldt=LocalDateTime.parse
					(dataTime, dtf);
			return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
		} catch (Exception e) {
			
		}
		
		return 0;
	}

	private Sensor toSensor(SensorCrud sensorCrud){
		Sensor sensor;
		
		MeasureUnit mUnit = sensorCrud.getmUnit();
		
		if(mUnit.equals(MeasureUnit.TEMPERATURE))
			return new SensorTemp(sensorCrud.sensorId,
					sensorCrud.timestamp,
					sensorCrud.data);
		if(mUnit.equals(MeasureUnit.HEART_RATE))
			return new SensorHeartRate(sensorCrud.sensorId,
					sensorCrud.timestamp,
					sensorCrud.data);
		return new SensorCarFuel(sensorCrud.sensorId,
				sensorCrud.timestamp,
				sensorCrud.data);
	}
	@Override
	public int getMinimalData(int sensorId, String dataTimeFrom, String dataTimeTo) {
		return getMinMax(sensorId, dataTimeFrom, dataTimeTo,
				(x,y)->Integer.compare(y.data, x.data));
	}

	@Override
	public List<Sensor> getSensorsData(String dataTimeFrom, String dataTimeTo) {
		long timeFrom=getTimestamp(dataTimeFrom);
		long timeTo=getTimestamp(dataTimeTo);
		if(timeTo==0)
			timeTo=Long.MAX_VALUE;
		return sensors.findByTimestampBetween(timeFrom,timeTo)
				.map(this::toSensor)
				.collect(Collectors.toList());
	}
	
}
