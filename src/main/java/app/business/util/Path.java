package app.business.util;

public class Path {

    public static class Web {
        public static final String INDEX = "/index";
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String PAYMENTS = "/payments";
        public static final String ONE_PAYMENT = "/payments/:id";
    }

    public static class Template {
        public static final String INDEX = "/WEB-INF/templates/index/index.html";
        public static final String LOGIN = "/WEB-INF/templates/login/login.html";
        public static final String PAYMENTS_ALL = "/WEB-INF/templates/payment/all.html";
        public static final String PAYMENTS_ONE = "/WEB-INF/templates/payment/one.html";
        public static final String NOT_FOUND = "/WEB-INF/templates/notFound.html";
    }
}
