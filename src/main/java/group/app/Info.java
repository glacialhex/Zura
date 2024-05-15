package group.app;

import javafx.scene.Scene;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Info {

    private static User currentUser;
    private static User profileUser;

    public static User getCurrentUser(){
        return currentUser;
    }

    public static User getProfileUser(){
        return profileUser;
    }

    public static void setCurrentUser(User inputUser){
        currentUser = inputUser;
    }

    public static void setProfileUser(User inputUser){
        profileUser = inputUser;
    }
}
