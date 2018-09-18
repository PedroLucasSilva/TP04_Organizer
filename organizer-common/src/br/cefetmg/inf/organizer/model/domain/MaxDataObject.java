
package br.cefetmg.inf.organizer.model.domain;


public class MaxDataObject {
    private User user;
    private String[] itemsID;
    private String[] itemsType ;
    private String[] itemsName ;
    private String[] itemsDescription;
    private String[] itemsDate;
    private String[] itemsStatus;
    private String[] tagsID;
    private String[] tagsName;
    private String[] tagsItems;
    private String[] itemsTags;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getItemsID() {
        return itemsID;
    }

    public void setItemsID(String[] itemsID) {
        this.itemsID = itemsID;
    }

    public String[] getItemsType() {
        return itemsType;
    }

    public void setItemsType(String[] itemsType) {
        this.itemsType = itemsType;
    }

    public String[] getItemsName() {
        return itemsName;
    }

    public void setItemsName(String[] itemsName) {
        this.itemsName = itemsName;
    }

    public String[] getItemsDescription() {
        return itemsDescription;
    }

    public void setItemsDescription(String[] itemsDescription) {
        this.itemsDescription = itemsDescription;
    }

    public String[] getItemsDate() {
        return itemsDate;
    }

    public void setItemsDate(String[] itemsDate) {
        this.itemsDate = itemsDate;
    }

    public String[] getItemsStatus() {
        return itemsStatus;
    }

    public void setItemsStatus(String[] itemsStatus) {
        this.itemsStatus = itemsStatus;
    }

    public String[] getTagsID() {
        return tagsID;
    }

    public void setTagsID(String[] tagsID) {
        this.tagsID = tagsID;
    }

    public String[] getTagsName() {
        return tagsName;
    }

    public void setTagsName(String[] tagsName) {
        this.tagsName = tagsName;
    }

    public String[] getTagsItems() {
        return tagsItems;
    }

    public void setTagsItems(String[] tagsItems) {
        this.tagsItems = tagsItems;
    }

    public String[] getItemsTags() {
        return itemsTags;
    }

    public void setItemsTags(String[] itemsTags) {
        this.itemsTags = itemsTags;
    }
    
    
}
