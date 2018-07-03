package cl.gd.kt.leg.verticle.event;

import cl.gd.kt.leg.verticle.RestApiVerticle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerEvent extends RestApiVerticle {

	private long timePeriodic = 3600000; //1 Hour
	
	@Override
	public void start() {
		
		log.info("Beginning the process of calculate defective and maintenance slots of tasks...");
		log.info("Periodicity for process: "+ (timePeriodic / 1000)+" seconds, "+(timePeriodic / 60000D)+" minutes.");

		
		vertx.eventBus().consumer("", message ->{});
		
		
	}
}