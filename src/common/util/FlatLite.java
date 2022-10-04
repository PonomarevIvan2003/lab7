package common.util;

import common.data.Coordinates;
import common.data.House;
import common.data.Transport;

import java.io.Serializable;
import java.time.LocalDateTime;


public class FlatLite implements Serializable {
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Long area;
    private Integer numberOfRooms;
    private Integer height;
    private Boolean newness;
    private Transport transport;
    private House house;

    public FlatLite(String name, Coordinates coordinates, LocalDateTime localDateTime, Long area, Integer numberOfRooms, Integer height, Boolean newness, Transport transport, House house) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = localDateTime;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.height = height;
        this.newness = newness;
        this.transport = transport;
        this.house = house;
    }

    public FlatLite() {}

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Long getArea() {
        return area;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public Integer getHeight() {
        return height;
    }

    public Boolean getNewness() {
        return newness;
    }

    public Transport getTransport() {
        return transport;
    }

    public House getHouse() {
        return house;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setNewness(Boolean newness) {
        this.newness = newness;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
