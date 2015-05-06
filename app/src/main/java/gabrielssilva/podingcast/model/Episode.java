package gabrielssilva.podingcast.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable {

    public static final Creator<Episode> CREATOR = new MyCreator();

    private String episodeName;
    private String filePath;
    private int lastPlayedPosition;

    public Episode() {

    }

    public Episode(String episodeName, String filePath, int lastPlayedPosition) {
        this.episodeName = episodeName;
        this.filePath = filePath;
        this.lastPlayedPosition = lastPlayedPosition;
    }

    public Episode(Parcel parcel) {
        this.episodeName = parcel.readString();
        this.filePath = parcel.readString();
        this.lastPlayedPosition = parcel.readInt();
    }


    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(int lastPlayedPosition) {
        this.lastPlayedPosition = lastPlayedPosition;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.episodeName);
        parcel.writeString(this.filePath);
        parcel.writeInt(this.lastPlayedPosition);
    }


    private static class MyCreator implements Creator<Episode> {

        @Override
        public Episode createFromParcel(Parcel parcel) {
            return new Episode(parcel);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    }
}
