package program.program.Model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Random;

@Getter
@Data
@Setter
public class Issuer {

    int ID;

    String name;

    String date;

    public Issuer(String name, String date, int ID) {
        this.name = name;
        this.date = date;

        this.ID = ID;

    }
}
