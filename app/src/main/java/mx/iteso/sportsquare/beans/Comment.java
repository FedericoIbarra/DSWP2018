package mx.iteso.sportsquare.beans;

/**
 * Created by Desarrollo on 05/05/2018.
 */

public class Comment {
    private String iDPublication;
    private String iDComment;
    private String comment;

    public Comment(){
    }

    public Comment(String iDPublication, String iDComment, String comment) {
        this.iDPublication = iDPublication;
        this.iDComment = iDComment;
        this.comment = comment;
    }

    public String getiDPublication() {
        return iDPublication;
    }

    public void setiDPublication(String iDPublication) {
        this.iDPublication = iDPublication;
    }

    public String getiDComment() {
        return iDComment;
    }

    public void setiDComment(String iDComment) {
        this.iDComment = iDComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
