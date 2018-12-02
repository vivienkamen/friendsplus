package aut.bme.hu.friendsplus.ui.listeners;

public interface ItemChangeListener {
    void onItemChanged(int position);
    void onItemInserted(int position);
    void onItemRemoved(int position);
}
