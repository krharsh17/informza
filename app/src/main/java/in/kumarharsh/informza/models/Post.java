package in.kumarharsh.informza.models;

public class Post {
    String uploader;
    String authorName;
    String time;
    String postText;
    String id;
    String authorProfilePhoto;

    public Post(){

    }

    public Post(String uploader, String authorName, String time, String postText, String id, String authorProfilePhoto) {
        this.uploader = uploader;
        this.authorName = authorName;
        this.time = time;
        this.postText = postText;
        this.id = id;
        this.authorProfilePhoto = authorProfilePhoto;
    }

    public String getAuthorProfilePhoto() {
        return authorProfilePhoto;
    }

    public void setAuthorProfilePhoto(String authorProfilePhoto) {
        this.authorProfilePhoto = authorProfilePhoto;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
