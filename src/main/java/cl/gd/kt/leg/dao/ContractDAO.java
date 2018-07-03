package cl.gd.kt.leg.dao;

import java.util.List;
import java.util.stream.Collectors;

import cl.gd.kt.leg.model.contract.Contract;
import cl.gd.kt.leg.util.Constant;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ContractDAO extends JdbcRepositoryWrapper {

    public ContractDAO (Vertx vertx, JsonObject config) {
        super(vertx, config);
    }
    
    private String selectAllContractSql() {

    	StringBuilder query = new StringBuilder();
    	
        query.append("SELECT cont.seq, cont.agreement, cont.annexed, cont.gross_salary, cont.type_currency, ");
        query.append("cont.employee_id, cont.employee_first_name, cont.employee_last_name, cont.employee_dt_start, ");
        query.append("cont.employee_dt_end, cont.enable ");
        query.append("FROM ").append(Constant.DB_SCHEMA).append(".leg_contract cont ");
        query.append("WHERE cont.enable = true ");
        
        return query.toString();
    }
    
    private String selectContractSql() {

    	StringBuilder query = new StringBuilder();
    	
        query.append("SELECT cont.seq, cont.agreement, cont.annexed, cont.gross_salary, cont.type_currency, ");
        query.append("cont.employee_id, cont.employee_first_name, cont.employee_last_name, cont.employee_dt_start, ");
        query.append("cont.employee_dt_end, cont.enable ");
        query.append("FROM ").append(Constant.DB_SCHEMA).append(".leg_contract cont ");
        query.append("WHERE cont.seq = ? ");
        
        return query.toString();
    }
    

    public void searchAllContract(Handler<AsyncResult<List<Contract>>> resultHandler) {

        this.retrieveAll(selectAllContractSql()).map(rawList -> rawList.stream().map(Contract::new).collect(Collectors.toList())).setHandler(resultHandler);
    }
    
    public void searchContract(Long seq, Handler<AsyncResult<List<Contract>>> resultHandler) {

        this.retrieveMany(new JsonArray().add(seq), selectContractSql()).map(rawList -> rawList.stream().map(Contract::new).collect(Collectors.toList())).setHandler(resultHandler);
    }
}
