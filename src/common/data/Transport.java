package common.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="transport")
@XmlEnum
public enum Transport implements Serializable {
    NONE,
    LITTLE,
    NORMAL;

    /**
     * Generates a beautiful list of enum string values.
     * @return String with all enum values splitted by comma.
     */
    public static String nameList(){
        String nameList = "";
        for (Transport transport : values()){
            nameList += transport.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
