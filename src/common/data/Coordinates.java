package common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements  Comparable <Coordinates>, Serializable {
    private int x;
    private Float y; //значение поля должно быт больше -279, поле не может быть null

    public Coordinates(int x, Float y){
        this.x = x;
        this.y = y;
    }

    public Coordinates() {}

    public void setX(int x){
        this.x = x;
    }
    public void setY(Float y){
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public Float getY(){
        return y;
    }

    @Override
    public String toString(){
        return String.valueOf(x) + " " + String.valueOf(y);
    }
    public static Coordinates valueOf(String s){
        String[] values = s.split(" ");
        return new Coordinates(Integer.valueOf(values[0]), Float.valueOf(values[1]));
    }
    @Override
    public int compareTo(Coordinates coordinates){
       return (int) ((Math.pow(x, 2) + Math.pow(y, 2)) - (Math.pow(coordinates.x, 2)) + Math.pow(coordinates.y, 2));
    }

}
