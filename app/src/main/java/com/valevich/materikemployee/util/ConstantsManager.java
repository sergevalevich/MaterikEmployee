package com.valevich.materikemployee.util;


public interface ConstantsManager {
    //Constants for the RestModule
    String BASE_URL = "https://2037db0b.ngrok.io/";
    int CONNECTION_TIME_OUT = 20;
    int READ_TIME_OUT = 50;

    String PRICE_FORMAT = "%.2f Р./%s.";
    String SEARCH_ID = "1";
    String OPERATION_SUCCESSFULL = "OPERATION PASSED ";

    int IMPORT_REQ_CODE = 1;

    int EXPORT_REQ_CODE = 2;
    String FILE_EXTRA = "FILENAME";
}
