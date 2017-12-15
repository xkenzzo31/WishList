package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 06/12/2017.
 */

public class UsersModel {
    private String idUser;
    private ArrayList<WishModel> listWishs;
    private ArrayList<FriendModel> friendsEmail;

    public UsersModel(String idUser) {
        this.idUser = idUser;
    }

    public void addWishModel(WishModel mListWish){
        listWishs.add(mListWish);
    }

    public void addFriendModel(List<FriendModel> mListFriend){
        friendsEmail.addAll(mListFriend);
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<WishModel> getListWishs() {
        return listWishs;
    }

    public void setListWishs(ArrayList<WishModel> listWishs) {
        this.listWishs = listWishs;
    }

    public ArrayList<FriendModel> getFriendsEmail() {
        return friendsEmail;
    }

    public void setFriendsEmail(ArrayList<FriendModel> friendsEmail) {
        this.friendsEmail = friendsEmail;
    }
}
