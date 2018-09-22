/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.organizer.model.domain;

import java.util.ArrayList;

/**
 *
 * @author aline
 */
public class ItemTag {
    
    private Item item;
    private ArrayList<Tag> listTags;

    public void setItem(Item item) {
        this.item = item;
    }

    public void setListTags(ArrayList<Tag> listTags) {
        this.listTags = listTags;
    }

    public Item getItem() {
        return item;
    }

    public ArrayList<Tag> getListTags() {
        return listTags;
    }
    
}
