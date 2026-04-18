import model.IndoorPlant;
import model.OutdoorPlant;
import model.Plant;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args){
        Plant p1 = new IndoorPlant("Cactus", 7,LocalDate.now(),"low");
        Plant p2 = new OutdoorPlant("Rose", 3, LocalDate.now(),"summer");

        System.out.println(p1.getInfo());
        System.out.println(p2.getInfo());
    }
}
