package aut.bme.hu.friendsplus.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import aut.bme.hu.friendsplus.ui.friends.FriendsAdapter;
import aut.bme.hu.friendsplus.ui.listeners.RecyclerItemTouchHelperListener;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsAdapter;
import aut.bme.hu.friendsplus.ui.messages.MessagesOverviewAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    public static final int MEETING_FLAG = 0;
    public static final int FRIENDS_FLAG = 1;
    public static final int MESSAGE_OVERVIEW_FLAG = 2;
    private RecyclerItemTouchHelperListener listener;
    private int flag;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, int flag) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
        this.flag = flag;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView;
            if(flag == MEETING_FLAG) {
                foregroundView = ((MeetingsAdapter.MeetingRowViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            if(flag == FRIENDS_FLAG) {
                foregroundView = ((FriendsAdapter.FriendRowViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            if(flag == MESSAGE_OVERVIEW_FLAG) {
                foregroundView = ((MessagesOverviewAdapter.MessageRowViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }


        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

        View foregroundView;
        if(flag == MEETING_FLAG) {
            foregroundView = ((MeetingsAdapter.MeetingRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
        if(flag == FRIENDS_FLAG) {
            foregroundView = ((FriendsAdapter.FriendRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
        if(flag == MESSAGE_OVERVIEW_FLAG) {
            foregroundView = ((MessagesOverviewAdapter.MessageRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView;
        if(flag == MEETING_FLAG) {
            foregroundView = ((MeetingsAdapter.MeetingRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }
        if(flag == FRIENDS_FLAG) {
            foregroundView = ((FriendsAdapter.FriendRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }
        if(flag == MESSAGE_OVERVIEW_FLAG) {
            foregroundView = ((MessagesOverviewAdapter.MessageRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View foregroundView;
        if(flag == MEETING_FLAG) {
            foregroundView = ((MeetingsAdapter.MeetingRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
        if(flag == FRIENDS_FLAG) {
            foregroundView = ((FriendsAdapter.FriendRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
        if(flag == MESSAGE_OVERVIEW_FLAG) {
            foregroundView = ((MessagesOverviewAdapter.MessageRowViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }

}
