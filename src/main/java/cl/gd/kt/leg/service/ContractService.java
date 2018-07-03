package cl.gd.kt.leg.service;

import java.util.List;

import cl.gd.kt.leg.dao.ContractDAO;
import cl.gd.kt.leg.model.contract.Contract;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ContractService {

	private final ContractDAO inventoryDAO;
	
	public ContractService(Vertx vertx, JsonObject config) {
	    this.inventoryDAO = new ContractDAO(vertx, config);
	}
	
	
	public void searchAllContract(Handler<AsyncResult<List<Contract>>> resultHandler) {
		this.inventoryDAO.searchAllContract(resultHandler);
	}
	
	public void searchContract(Long seq, Handler<AsyncResult<List<Contract>>> resultHandler) {
		this.inventoryDAO.searchContract(seq, resultHandler);
	}
}