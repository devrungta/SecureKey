package com.example.securekey;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class AppListAdapter extends ArrayAdapter<App> {
    private List<App> dataList;

    private final LayoutInflater inflater;
    private final AppClickListener appClickListener;

    public AppListAdapter(Context context, List<App> apps, AppClickListener listener) {
        super(context, 0, apps);
        inflater = LayoutInflater.from(context);
        this.appClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.user_specific, parent, false);
        }

        App app = getItem(position);
        if (app != null) {
            TextView appName = view.findViewById(R.id.app_name);
            TextView password = view.findViewById(R.id.password);

            appName.setText(app.getName());
            password.setText(app.getPassword());

            password.setVisibility(View.GONE);

            appName.setOnClickListener(v -> {
                if (password.getVisibility() == View.VISIBLE) {
                    password.setVisibility(View.GONE);
                } else {
                    // Show password or implement your system lock verification here
                    password.setVisibility(View.VISIBLE);
                }
            });
        }

        return view;

    }
    public void setData(List<App> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }
    public interface AppClickListener{
        void onAppClick(int position);
    }
}
