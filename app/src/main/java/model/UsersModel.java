package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 06/12/2017.
 */

public class UsersModel {
    private String idUser;
    private ArrayList<WishsModel> listWishs;
    private ArrayList<String> friendsEmail;

    public UsersModel(String idUser) {
        this.idUser = idUser;
    }

    public void addWishModel(List<WishsModel> mListWish){
        listWishs.addAll(mListWish);
    }

    public void addFriendModel(List<String> mListFriend){
        friendsEmail.addAll(mListFriend);
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<WishsModel> getListWishs() {
        return listWishs;
    }

    public void setListWishs(ArrayList<WishsModel> listWishs) {
        this.listWishs = listWishs;
    }

    public ArrayList<String> getFriendsEmail() {
        return friendsEmail;
    }

    public void setFriendsEmail(ArrayList<String> friendsEmail) {
        this.friendsEmail = friendsEmail;
    }
}
