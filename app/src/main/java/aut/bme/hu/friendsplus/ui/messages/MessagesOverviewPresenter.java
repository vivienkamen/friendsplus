package aut.bme.hu.friendsplus.ui.messages;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.MessageDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.ui.helpers.MessageOverviewItem;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.LastMessageListener;
import aut.bme.hu.friendsplus.ui.listeners.UnreadMessageListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class MessagesOverviewPresenter extends Presenter<MessagesOverviewScreen> implements FriendsListener,
        UsersListener, UnreadMessageListener, LastMessageListener {

    private AuthInteractor authInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private MessageDatabaseInteractor messageDatabaseInteractor;

    private ItemChangeListener itemChangeListener;
    private ArrayList<MessageOverviewItem> friendItems;
    private String myUID;


    public MessagesOverviewPresenter() {

        authInteractor = new AuthInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();
        messageDatabaseInteractor = new MessageDatabaseInteractor();

        userDatabaseInteractor.setUsersListener(this);
        messageDatabaseInteractor.setFriendsListener(this);
        messageDatabaseInteractor.setUnreadMessageListener(this);
        messageDatabaseInteractor.setLastMessageListener(this);

        friendItems = new ArrayList<>();
        myUID = authInteractor.getCurrentUser().getUid();

        messageDatabaseInteractor.addFriendsChildEventListener(myUID);

    }

    @Override
    public void attachScreen(MessagesOverviewScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void onBindMessageRowViewAtPosition(int position) {
        MessageOverviewItem messageOverviewItem = friendItems.get(position);
        screen.setFriend(messageOverviewItem.user);

        String message = "";
        if(messageOverviewItem.lastMessage.senderUID.equals(myUID)) {
            message += "You: ";
        }
        screen.setLastMessage(message + messageOverviewItem.lastMessage.text);

        if(messageOverviewItem.unreadMessageCount > 0) {
            screen.setUnreadMessageTextView(messageOverviewItem.unreadMessageCount);
        }

    }

    public int getMeetingRowsCount() {
        return friendItems.size();
    }

    public void setItemChangeListener(ItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public void addFriendWithNewMessage(Message message, String friendUID) {
        messageDatabaseInteractor.addMessage(message, friendUID, myUID);
        message.unread = false;
        messageDatabaseInteractor.addMessage(message, myUID, friendUID);

    }

    public void removeMessagesFromFriend(int index) {
        MessageOverviewItem messageOverviewItem = friendItems.get(index);
        messageDatabaseInteractor.removeMessagesFromFriend(myUID, messageOverviewItem.user.uid);

    }

    public void signOut() {
        authInteractor.signOut();
    }

    public void cleanupListener() {
        messageDatabaseInteractor.cleanupListeners(myUID);
    }

    @Override
    public void onFriendListReady(List<String> friends) {}

    @Override
    public void onFriendAdded(String friendUID) {
        userDatabaseInteractor.getUserByUid(friendUID);
    }

    @Override
    public void onFiendRemoved(String uid) {
        MessageOverviewItem item = getItem(uid);
        if(item != null) {
            int index = friendItems.indexOf(item);
            friendItems.remove(index);
            itemChangeListener.onItemChanged(index);
        }

    }

    public boolean containsFriend(String uid) {
        for(MessageOverviewItem messageOverviewItem : friendItems) {
            if(messageOverviewItem.user.uid.equals(uid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUserFound(User user) {

        if(!containsFriend(user.uid)) {
            MessageOverviewItem item = new MessageOverviewItem();
            item.user = user;
            friendItems.add(item);
        }
        messageDatabaseInteractor.getLastMessage(myUID, user.uid);
    }

    @Override
    public void onUserNotFound() {}

    private MessageOverviewItem getItem(String uid) {
        for(MessageOverviewItem item : friendItems) {
            if(item.user.uid.equals(uid)) {
                return  item;
            }
        }
        return null;
    }

    @Override
    public void onCountFound(int newMessageCount, String friendUID) {

        MessageOverviewItem item = getItem(friendUID);
        item.unreadMessageCount = newMessageCount;
        int index = friendItems.indexOf(item);
        friendItems.set(index, item);
        itemChangeListener.onItemChanged(index);

    }

    @Override
    public void onLastMessageFound(Message message, String friendUID) {
        MessageOverviewItem item = getItem(friendUID);
        item.lastMessage = message;
        int index = friendItems.indexOf(item);
        friendItems.set(index, item);
        messageDatabaseInteractor.getUnreadMessagesCount(myUID, friendUID);
    }

}
