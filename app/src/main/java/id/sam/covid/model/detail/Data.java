
package id.sam.covid.model.detail;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("covid")
    @Expose
    private Covid covid;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7292421478194509815L;

    protected Data(Parcel in) {
        this.covid = ((Covid) in.readValue((Covid.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param covid
     */
    public Data(Covid covid) {
        super();
        this.covid = covid;
    }

    public Covid getCovid() {
        return covid;
    }

    public void setCovid(Covid covid) {
        this.covid = covid;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(covid);
    }

    public int describeContents() {
        return  0;
    }

}
