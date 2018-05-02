package mx.iteso.sportsquare.beans;

/**
 * Created by Desarrollo on 31/03/2018.
 */

public class Publication {
    private String id;
    private String publicationText;

    public Publication(){
    }

    public Publication(String id, String publicationText) {
        this.id = id;
        this.publicationText = publicationText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicationText() {
        return publicationText;
    }

    public void setPublicationText(String publicationText) {
        this.publicationText = publicationText;
    }
}
