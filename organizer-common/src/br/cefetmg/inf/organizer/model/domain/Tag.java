package br.cefetmg.inf.organizer.model.domain;

public class Tag {

    private User user;
    private Long seqTag;
    private String tagName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getSeqTag() {
        return seqTag;
    }

    public void setSeqTag(Long seqTag) {
        this.seqTag = seqTag;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }    
}