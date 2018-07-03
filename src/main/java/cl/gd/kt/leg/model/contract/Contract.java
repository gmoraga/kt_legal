package cl.gd.kt.leg.model.contract;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract {

    private Long seq;
    private String agreement;
    private String annexed;
    private Integer grossSalary;
    private String typeCurrency;
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private Instant employeeDtStart;
    private Instant employeeDtEnd;
    private Boolean enable;

    public Contract(JsonObject json) {
    	this.seq = json.getLong("seq");
    	this.agreement = json.getString("agreement");
    	this.annexed = json.getString("annexed");
    	this.grossSalary = json.getInteger("gross_salary");
    	this.typeCurrency = json.getString("type_currency");
    	this.employeeId = json.getLong("employee_id");
    	this.employeeFirstName = json.getString("employee_first_name");
    	this.employeeLastName = json.getString("employee_last_name");
    	this.employeeDtStart = json.getInstant("employee_dt_start");
    	this.employeeDtEnd = json.getInstant("employee_dt_end");
    	this.enable = json.getBoolean("enable");
    }
 
}
