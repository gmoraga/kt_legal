package cl.gd.kt.leg.verticle.event;

import cl.gd.kt.leg.util.AppEnum;
import cl.gd.kt.leg.util.SystemUtil;
import cl.gd.kt.leg.verticle.RestApiVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServerOptions;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.BridgeEvent;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeListenerEvent extends RestApiVerticle {
	
	private volatile Handler<BridgeEvent> eventHandler = event -> event.complete(true);
	
	@Override
	public void start() {
		log.info("Beginning EmployeeListenerEvent kt-legal...");
		final int appPort = SystemUtil.getEnvironmentIntValue(AppEnum.EVENT_PORT_LEGAL.name());
		String eventEmployee = SystemUtil.getEnvironmentStrValue(AppEnum.EVENT_EMPLOYEE.name());
		
	    vertx.eventBus().consumer(eventEmployee, (Message<JsonObject> msg) -> {
	        	JsonObject jsObj = JsonObject.mapFrom(msg.body());
	        	log.info("Message received: "+jsObj);
	        	
	    });
	    
	    TcpEventBusBridge bridge = TcpEventBusBridge.create(vertx, new BridgeOptions()
	    		.addInboundPermitted(new PermittedOptions().setAddress(eventEmployee)), 
	    		new NetServerOptions(), event -> eventHandler.handle(event));

	    bridge.listen(appPort, res -> {
	    	log.info("kt-legal listener port: "+appPort+" : "+res.succeeded());
	    	log.info("result: "+res.succeeded());
	    });
	}
}