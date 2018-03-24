package com.lmp.db.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@Document(collection="store")
public class Store {

  @Id
  private String id;
  private String name;
  private String franchise;
  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  @Nullable
  private Location location;
  private String address;
  private List<StoreCapabilities> capabilities;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getFranchise() {
    return franchise;
  }
  public void setFranchise(String franchise) {
    this.franchise = franchise;
  }
  public Location getLocation() {
    return location;
  }
  public void setLocation(Location location) {
    this.location = location;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
}

class StoreCapabilities {

}

class Location {
  private String type;
  private double[] coordinates;
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public double[] getCoordinates() {
    return coordinates;
  }
  public void setCoordinates(double[] coordinates) {
    if(coordinates == null || coordinates.length < 2) {
      Assert.notNull(coordinates, "coordinates must not be null or less than 2!");
    }
    if(coordinates[0] > coordinates[1]) {
      double temp = coordinates[0];
      coordinates[0] = coordinates[1];
      coordinates[1] = temp;
    }
    this.coordinates = coordinates;
  }
}
