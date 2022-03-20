package com.leathersheer.tools.SpiderUnit.Exceptions;

public class ArgumentNotFoundException extends RuntimeException{
    public ArgumentNotFoundException(){
        super();
    }

    public ArgumentNotFoundException(String msg){
        super(msg);
    }
}
