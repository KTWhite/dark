package com.github.dark.exception;

import com.github.dark.constants.CommonConstants;
import com.github.dark.constants.CommonMessage;

public class NoPermissionException extends BaseException  {
    public NoPermissionException(String message) {
        super(message, CommonConstants.EX_USER_REPORT_ROLE_CODE, CommonMessage.NOT_HAVE_AUTH);
    }
}
