package org.mooncolony.moonmayor.captainsonarradarcompanion;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by matthewtduffin on 03/10/16.
 */
public class BoardAdapter extends
  RecyclerView.Adapter<BoardAdapter.ViewHolder> {

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout frame;
    public ImageView cross;

    public ViewHolder(View itemView) {
      super(itemView);

      frame = (FrameLayout) itemView.findViewById(R.id.recyclerFrame);
      cross = (ImageView) itemView.findViewById(R.id.recyclerImage);
    }
  }

  private List<Boolean> mModel;
  private Context mContext;

  public BoardAdapter(Context context, List<Boolean> model) {
    mModel = model;
    mContext = context;
  }

  private Context getContext() {
    return mContext;
  }

  @Override
  public BoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.recycler_item, parent, false);

    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(BoardAdapter.ViewHolder viewHolder, int position) {
    boolean isWater = mModel.get(position);
    if (!isWater) {
      viewHolder.frame.setBackgroundResource(R.drawable.land);
      GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) viewHolder.frame.getLayoutParams();
      params.setMargins(0,0,0,0);
    }
    if (position < 15) {
      viewHolder.cross.setVisibility(View.VISIBLE);
    }
  }

  // Returns the total count of items in the list
  @Override
  public int getItemCount() {
    return mModel.size();
  }
}
