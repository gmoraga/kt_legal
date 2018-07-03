package cl.gd.kt.leg.verticle.event;

import cl.gd.kt.leg.verticle.RestApiVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.eventbus.bridge.tcp.impl.protocol.FrameHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishEvent extends RestApiVerticle {

	
	@Override
	public void start() {
		
		log.info("Start Publish Event kt_legal...");

		testSendMessage();
		
	}
	
	  
	public void testSendMessage() {
		NetClient client = vertx.createNetClient();
		
		client.connect(7002, "localhost", conn -> {
			NetSocket socket = conn.result();

			FrameHelper.sendFrame("send", "test", new JsonObject().put("value", "vert.x"), socket);
        
			client.close();
      });
	}
//	  public void testSendVoidMessage() {
//		    // Send a request and get a response
//		    NetClient client = vertx.createNetClient();
//
//		    vertx.eventBus().consumer("test", (Message<JsonObject> msg) -> {
//		      client.close();
//		    });
//
//		    client.connect(7002, "localhost", conn -> {
//
//		      NetSocket socket = conn.result();
//
//		      FrameHelper.sendFrame("send", "test", new JsonObject().put("value", "vert.x"), socket);
//		    });
//		  }
}