package com.helio.myreelty.common.dialog;



/**
 * Created by Taras on 3/1/2016.
 */
public class ItemDialogModel {

    public String itemName;
    public int resource;

    public ItemDialogModel(String itemName, int resource) {
        this.itemName = itemName;
        this.resource = resource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
