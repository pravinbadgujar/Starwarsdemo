package pravin.com.starwars.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pravin.com.starwars.R;
import pravin.com.starwars.interfaces.OnItemClickListener;
import pravin.com.starwars.models.StarModel;

public class StarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_MOVIE = 0;
    private final int TYPE_LOAD = 1;

    static Context context;
    List<StarModel> movies=new ArrayList<>();
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;
    private static OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        clickListener = onItemClickListener;
    }


    public StarsAdapter(Context context, List<StarModel> movies) {
        StarsAdapter.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new NotificationHolder(inflater.inflate(R.layout.rowstars, parent, false));
        } else if(viewType==TYPE_LOAD) {
            return new LoadHolder(inflater.inflate(R.layout.loadmore, parent, false));
        }
        else
            return new Blankholder(inflater.inflate(R.layout.blank,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            ((NotificationHolder) holder).bindData(movies.get(position));
        }

        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

            if (movies.get(position).type.equals("movie")) {
                return TYPE_MOVIE;
            }
            else if(movies.get(position).type.equals("load")){
                return TYPE_LOAD;
            }
            else
                return 3;

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /* VIEW HOLDERS */


    public class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tx_name;


        NotificationHolder(View view) {
            super(view);

            tx_name = itemView.findViewById(R.id.name);

            view.setOnClickListener(this);

        }

        void bindData(StarModel nm) {
            try {
                    tx_name.setText(nm.getName());
                } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }

    }


    static class LoadHolder extends RecyclerView.ViewHolder {
        LoadHolder(View itemView) {
            super(itemView);
        }
    }

    static class Blankholder extends RecyclerView.ViewHolder {
        Blankholder(View itemView) {
            super(itemView);
        }
    }


    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }


    public void notifyDataChanged(boolean isLoading) {
        notifyDataSetChanged();
        this.isLoading = isLoading;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
