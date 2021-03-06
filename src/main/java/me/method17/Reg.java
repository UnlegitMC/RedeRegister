package me.method17;

import lombok.Getter;
import me.method17.func.Client;
import me.method17.func.RegisterLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Reg {
    @Getter
    private static Logger logger;
    @Getter
    private static RegisterLog registerLog;
    @Getter
    private static final ArrayList<String> registerQueue=new ArrayList<>();
    @Getter
    private static final long maxTime=20000;

    public static void main(String[] args){
        if(args.length<4){
            System.out.println("plz enter rule:");
            System.out.println("java -jar REGMachine.jar <base_name%id%> <min> <max> <password>");
            System.exit(114514);
        }

        logger = LogManager.getLogger(Reg.class);
        logger.info("loading");
        registerLog=new RegisterLog();

        String nameBase=args[0],password=args[3];
        int min=new Integer(args[1]),max=new Integer(args[2]);

        for(int i=min;i<=max;i++){
            String name=nameBase.replaceAll("%id%",i+"");
            registerQueue.add(name);
            new Client(name,password);
        }
        //autoQuit tick
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info(registerQueue.size()+" Accounts Registering...");
                if(registerQueue.size()==0){
                    System.exit(1919810);
                }
            }
        }, 1000, 1000);
    }
}
