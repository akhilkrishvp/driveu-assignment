package com.driveu.driveutest.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class LocationModel{
  @SerializedName("latitude")
  @Expose
  private Double latitude;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("longitude")
  @Expose
  private Double longitude;
  public void setLatitude(Double latitude){
   this.latitude=latitude;
  }
  public Double getLatitude(){
   return latitude;
  }
  public void setStatus(String status){
   this.status=status;
  }
  public String getStatus(){
   return status;
  }
  public void setLongitude(Double longitude){
   this.longitude=longitude;
  }
  public Double getLongitude(){
   return longitude;
  }
}