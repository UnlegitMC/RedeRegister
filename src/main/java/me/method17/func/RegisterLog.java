package me.method17.func;

import java.io.File;

public class RegisterLog {
    private String fileString;

    public RegisterLog(){
        File file=new File("./accounts");
        if(!file.exists()){
            FileUtil.writeFile("./accounts","##THIS IS YOUR REDE-SKY ACCOUNTS!");
        }
        //read the file
        fileString=FileUtil.readFile(file);
    }

    public void addAccount(String name,String password){
        fileString+="\n"+name+":"+password;
        this.save();
    }

    private void save(){
        FileUtil.writeFile("./accounts",fileString);
    }
}
