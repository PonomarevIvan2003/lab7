package common.xml;

import common.data.Flat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TreeMap;

/**
 * Root object of XML file. Contains collection of Flats objects
 */
@XmlRootElement(name = "Flats")
@XmlAccessorType(XmlAccessType.FIELD)
public class Flats {
    @XmlElement(name="Flat")
    private TreeMap<Integer, Flat> flats = null;

    public Flats() {}

    /**
     * @return collection of Flat objects
     */
    public TreeMap<Integer, Flat> getFlats() {
        return flats;
    }

    public void setFlats(TreeMap<Integer, Flat> flats) {
        this.flats = flats;
    }
}
