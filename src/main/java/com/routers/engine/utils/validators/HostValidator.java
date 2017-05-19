package com.routers.engine.utils.validators;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class HostValidator implements Validator<String> {
    @Override
    public boolean validate(String host) {
        boolean valid = false;
        //Host validation
        if (host == null) {
            return false;
        }

        DomainValidator dVal = DomainValidator.getInstance();
        valid = dVal.isValid(host) || dVal.isValidLocalTld(host);

        if (!valid) {
            //assuming that host is ip address
            InetAddressValidator iVal = InetAddressValidator.getInstance();
            valid =  iVal.isValidInet4Address(host) || iVal.isValidInet6Address(host);
        }

        return valid;
    }
}
