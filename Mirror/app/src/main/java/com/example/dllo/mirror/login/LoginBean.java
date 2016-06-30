package com.example.dllo.mirror.login;

/**
 * Created by dllo on 16/6/23.
 */
public class LoginBean {

    /**
     * result : 1
     * msg :
     * data : {"token":"9487ec1a4c43870ab1391cb5ba4cf56d","uid":"546"}
     */

    private String result;
    private String msg;
    /**
     * token : 9487ec1a4c43870ab1391cb5ba4cf56d
     * uid : 546
     */

    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String token;
        private String uid;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
