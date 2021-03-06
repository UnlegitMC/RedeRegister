package me.method17;

import lombok.Getter;
import me.method17.func.Client;
import me.method17.func.RegisterLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reg {
    @Getter
    private static Logger logger;
    @Getter
    private static RegisterLog registerLog;

    public static void main(String[] args){
        if(args.length<4){
            System.out.println("plz enter rule:");
            System.out.println("java -jar REGMachine.jar <base_name> <min> <max> <password>");
            System.exit(114514);
        }

        logger = LogManager.getLogger(Reg.class);
        logger.info("loading");
        registerLog=new RegisterLog();

        String nameBase=args[0],password=args[3];
        int min=new Integer(args[1]),max=new Integer(args[2]);

        for(int i=min;i<=max;i++){
            new Client(nameBase+i,password);
        }
    }
}
