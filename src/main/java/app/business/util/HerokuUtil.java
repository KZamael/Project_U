package app.business.util;

public class HerokuUtil {

    public HerokuUtil(){}

    public static int getAssignedPort(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        if(processBuilder.environment().get("PORT") != null){
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000;
    }
}
