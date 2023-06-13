package com.tech.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang
 * 2023/6/6
 */
@Getter
@Setter
public class Document {

    private String title;

    private String content;

    private String finger;

    private String finalSim;


    public Document() {
    }

    public Document(String finger, String finalSim) {
        this.finger = finger;
        this.finalSim = finalSim;
    }
}
