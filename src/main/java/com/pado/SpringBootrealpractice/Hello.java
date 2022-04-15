package com.pado.SpringBootrealpractice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hello {
    private String data;

    public void setData(String data) {
        //git test용 코드
        this.data = data;
    }
}
