package android.example.newsapitest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CryptoAdapter  extends ArrayAdapter<CryptoInfo> {

    private static final String DATE_SEPARATOR = "T";
    private static final String AUTHOR_SPLIT = ",";

    public CryptoAdapter(@NonNull Context context, ArrayList<CryptoInfo> newitems) {
        super(context, 0, newitems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if the existing view is being reused?

        //View cryptolist = convertView;
        if (convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout, parent, false);
        }

        CryptoInfo currentnewscrypto = getItem(position);


        String primaryDate = null;

        String time1 = currentnewscrypto.getmPublished();
            if(time1.contains(DATE_SEPARATOR)) {
                String[] separatedItems = time1.split(DATE_SEPARATOR);
                primaryDate = separatedItems[0];
            } else {
                primaryDate = "This is the date";
            }

            //put the string splitter on the author, so the info is not too long.

        String primaryAuthor = null;

            String author = currentnewscrypto.getAuthor();
            if (author.contains(AUTHOR_SPLIT)) {
                String[] separatedItems = author.split(AUTHOR_SPLIT);
                primaryAuthor = separatedItems[0];
            } else {
                primaryAuthor = currentnewscrypto.getAuthor();
            }


        assert currentnewscrypto != null;
        TextView titleView = convertView.findViewById(R.id.tv1);
        titleView.setText(currentnewscrypto.getTitle());

        TextView descView = convertView.findViewById(R.id.descview);
        descView.setText(currentnewscrypto.getDesc());

        TextView authorView = convertView.findViewById(R.id.authorview);
        authorView.setText("Author: " + primaryAuthor);

        TextView dateTime = convertView.findViewById(R.id.datetime);
        dateTime.setText("Date Published: " + primaryDate);


        return convertView;
    }
}
