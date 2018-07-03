CREATE SCHEMA IF NOT EXISTS kitchen_talk;

DROP TABLE IF EXISTS kitchen_talk.leg_contract;
DROP SEQUENCE IF EXISTS kitchen_talk.leg_contract_seq;

CREATE SEQUENCE IF NOT EXISTS kitchen_talk.leg_contract_seq;

CREATE TABLE kitchen_talk.leg_contract
(
    seq bigint DEFAULT nextval('kitchen_talk.leg_contract_seq'::regclass) NOT NULL,
    agreement VARCHAR(500) NOT NULL,
    annexed VARCHAR(500) NOT NULL,
    gross_salary int4 NOT NULL,
    type_currency VARCHAR(12) NOT NULL,
    employee_id INTEGER,
    employee_first_name VARCHAR(30),
    employee_last_name VARCHAR(40),
    employee_dt_start TIMESTAMP WITH TIME ZONE,
    employee_dt_end TIMESTAMP WITH TIME ZONE,
    enable boolean DEFAULT true NOT NULL
);

CREATE UNIQUE INDEX key_leg_contract ON kitchen_talk.leg_contract (seq);
CREATE UNIQUE INDEX ix_leg_contract_unique_business ON kitchen_talk.leg_contract (employee_id);

INSERT INTO kitchen_talk.leg_contract (agreement, annexed, gross_salary, type_currency, employee_id, employee_first_name, employee_last_name, employee_dt_start)
VALUES ('Contrato de celebración laboral entre la empresa Y del trabajador A', 'Anexo tipo 1', 1000000, 'CLP', 135718, 'Thedrick', 'Flippen', now());

INSERT INTO kitchen_talk.leg_contract (agreement, annexed, gross_salary, type_currency, employee_id, employee_first_name, employee_last_name, employee_dt_start)
VALUES ('Contrato de celebración laboral entre la empresa Y del trabajador B', 'Anexo tipo 1', 1000000, 'CLP', 135719, 'Baudoin', 'Josilevich', now());

INSERT INTO kitchen_talk.leg_contract (agreement, annexed, gross_salary, type_currency, employee_id, employee_first_name, employee_last_name, employee_dt_start)
VALUES ('Contrato de celebración laboral entre la empresa Y del trabajador C', 'Anexo tipo 1', 1000000, 'CLP', 135720, 'Gabbey', 'Stradling', now());
