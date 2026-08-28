package koschei.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Duck5 {

    private Egg6 egg6;
    //через бин
    public Duck5(Egg6 egg6) {this.egg6 = egg6;}

    @Override
    public String toString() {
        return ", в утке яйцо " + egg6.toString();
    }
}