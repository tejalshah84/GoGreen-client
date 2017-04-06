package model;

/**
 * Created by Tejal on 5/4/2016.
 */


public class Share {

    private int userId;
    private int postId;

    public Share(){}

    public Share(int uid, int pid){
        this.userId=uid;
        this.postId=pid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

}

