package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by User on 8/3/2015.
 */
public class ModelPlace {


    public CharSequence placeId;
    public CharSequence description;

    public ModelPlace(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }

}
