package koschei.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Needle7 {

    public Deth8 deth8;

    @Override
    public String toString() {
        return ", смерть Кощея на игле :( " + "";
    }

    @Autowired //через сеттер
    public void setDeth8(Deth8 deth8) {
        this.deth8 = deth8;
    }
}
