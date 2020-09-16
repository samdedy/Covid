
package id.sam.covid.model.getall;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Covid implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("kondisi")
    @Expose
    private String kondisi;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("umur")
    @Expose
    private String umur;
    @SerializedName("jenis_kelamin")
    @Expose
    private String jenisKelamin;
    @SerializedName("kota_domisili")
    @Expose
    private String kotaDomisili;
    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;
    @SerializedName("picture")
    @Expose
    private String picture;
    public final static Creator<Covid> CREATOR = new Creator<Covid>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Covid createFromParcel(Parcel in) {
            return new Covid(in);
        }

        public Covid[] newArray(int size) {
            return (new Covid[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2568869298701689932L;

    protected Covid(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.kondisi = ((String) in.readValue((String.class.getClassLoader())));
        this.lat = ((String) in.readValue((String.class.getClassLoader())));
        this.lon = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.namaLengkap = ((String) in.readValue((String.class.getClassLoader())));
        this.umur = ((String) in.readValue((String.class.getClassLoader())));
        this.jenisKelamin = ((String) in.readValue((String.class.getClassLoader())));
        this.kotaDomisili = ((String) in.readValue((String.class.getClassLoader())));
        this.noTelepon = ((String) in.readValue((String.class.getClassLoader())));
        this.picture = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Covid() {
    }

    /**
     * 
     * @param umur
     * @param kondisi
     * @param lon
     * @param kotaDomisili
     * @param picture
     * @param noTelepon
     * @param jenisKelamin
     * @param id
     * @param lat
     * @param namaLengkap
     * @param username
     * @param timestamp
     * @param status
     */
    public Covid(String id, String username, String kondisi, String lat, String lon, String timestamp, String status, String namaLengkap, String umur, String jenisKelamin, String kotaDomisili, String noTelepon, String picture) {
        super();
        this.id = id;
        this.username = username;
        this.kondisi = kondisi;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.status = status;
        this.namaLengkap = namaLengkap;
        this.umur = umur;
        this.jenisKelamin = jenisKelamin;
        this.kotaDomisili = kotaDomisili;
        this.noTelepon = noTelepon;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getKotaDomisili() {
        return kotaDomisili;
    }

    public void setKotaDomisili(String kotaDomisili) {
        this.kotaDomisili = kotaDomisili;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(username);
        dest.writeValue(kondisi);
        dest.writeValue(lat);
        dest.writeValue(lon);
        dest.writeValue(timestamp);
        dest.writeValue(status);
        dest.writeValue(namaLengkap);
        dest.writeValue(umur);
        dest.writeValue(jenisKelamin);
        dest.writeValue(kotaDomisili);
        dest.writeValue(noTelepon);
        dest.writeValue(picture);
    }

    public int describeContents() {
        return  0;
    }

}
