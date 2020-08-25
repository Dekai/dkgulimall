package com.dk.common.constant;

public class ProductConstant {

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "Basic Info"), ATTR_TYPE_SALE(0, "Sale Properties");
        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
