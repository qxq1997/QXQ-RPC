package common.entity;

import java.io.Serializable;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class HelloObject implements Serializable {
    private String greeting;

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
