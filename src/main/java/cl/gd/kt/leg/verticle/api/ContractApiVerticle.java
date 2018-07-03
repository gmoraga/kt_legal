package cl.gd.kt.leg.verticle.api;

import cl.gd.kt.leg.service.ContractService;
import cl.gd.kt.leg.verticle.LegalVerticle;
import cl.gd.kt.leg.verticle.RestApiVerticle;
import cl.gd.kt.leg.verticle.util.ValidateInventoryApiUtil;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContractApiVerticle extends RestApiVerticle {
	private static final String ID = "id";
	private static final String MAIN_PATH = LegalVerticle.API_LEGAL;
	//routes
	private static final String API_GET_LEGAL_CONTRACTS_ID = MAIN_PATH + "/v1/contracts/:id";
	private static final String API_GET_LEGAL_CONTRACTS_ALL = MAIN_PATH + "/v1/contracts/";
	//services
	private ContractService contractService;

	/**
	 * 
	 * @param router
	 * @param notifyImpl
	 */
	public ContractApiVerticle(Router router, ContractService contractService) {
		//routes
		this.loadRoute(router);

		//services
		this.contractService = contractService;
	}

	/**
	 * 
	 * @param router
	 */
	public void loadRoute(Router router) {
		//events
		router.get(API_GET_LEGAL_CONTRACTS_ID).handler(this::getContractBySeq);
		router.get(API_GET_LEGAL_CONTRACTS_ALL).handler(this::getAllContract);
	}

	/**
	 *
	 * @param context
	 */
	private void getContractBySeq(RoutingContext context) {
		context.vertx().executeBlocking(future -> {
			try {
				final String seqStr = context.request().getParam(ID);
				ValidateInventoryApiUtil.getId(seqStr);
				final Long seq = Long.valueOf(seqStr);
				//List<AverageDefectiveMaintenanceSlots> listAverages = Arrays.asList(Json.decodeValue(context.getBodyAsString(), AverageDefectiveMaintenanceSlots[].class));
				this.contractService.searchContract(seq, resultHandler(context, Json::encodePrettily, 200));
			} catch (Exception e) {
				badRequest(context, e);
			}
		}, false, resultHandler(context));
	}
	
	private void getAllContract(RoutingContext context) {
		context.vertx().executeBlocking(future -> {
			this.contractService.searchAllContract(resultHandler(context, Json::encodePrettily, 200));
		}, false, resultHandler(context));
}
	

}