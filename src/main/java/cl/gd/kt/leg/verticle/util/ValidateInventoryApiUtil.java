package cl.gd.kt.leg.verticle.util;

import com.google.common.base.Preconditions;

import cl.gd.kt.leg.exception.AppException;

public class ValidateInventoryApiUtil {

    private static final String VALIDATE_GET_ID = "Id must not be null";
//    private static final String VALIDATE_EMPLOYEE_FIRST_NAME = "Employee first name must not be null";
//    private static final String VALIDATE_EMPLOYEE_LAST_NAME = "Employee last name must not be null";
//    private static final String VALIDATE_EMPLOYEE_GENDER = "Employee gender must not be null";
//    private static final String VALIDATE_EMPLOYEE_DEPARTMENT = "Employee department must not be null";
//    private static final String VALIDATE_EMPLOYEE_PHONE = "Employee phone must not be null";
//    private static final String VALIDATE_EMPLOYEE_CATEGORY = "Employee category must not be null";
//    private static final String VALIDATE_EMPLOYEE_LEVEL = "Employee level must not be null";
//    private static final String VALIDATE_EMPLOYEE_STREET_ADDRESS = "Employee street address must not be null";
//    private static final String VALIDATE_EMPLOYEE_STREET_NUMBER = "Employee street address must not be null";

    private ValidateInventoryApiUtil() {
        throw new IllegalAccessError(ValidateInventoryApiUtil.class.toString());
    }

    /**
     * 
     * @param id
     * @throws AppException
     */
    public static void getId(String id) throws AppException {
        Preconditions.checkNotNull(id, VALIDATE_GET_ID);
    }

//    public static void getPage(Page page) throws AppException {
//        Preconditions.checkNotNull(page, VALIDATE_PAGE);
//    }

//    public static void postEmployee (Employee employee) throws AppException {
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_FIRST_NAME);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_LAST_NAME);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_GENDER);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_DEPARTMENT);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_PHONE);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_CATEGORY);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_LEVEL);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_STREET_ADDRESS);
//        Preconditions.checkNotNull(employee, VALIDATE_EMPLOYEE_STREET_NUMBER);
//    }
}
