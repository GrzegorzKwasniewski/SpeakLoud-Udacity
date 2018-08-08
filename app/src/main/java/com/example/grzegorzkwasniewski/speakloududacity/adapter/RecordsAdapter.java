package com.example.grzegorzkwasniewski.speakloududacity.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.audioFilesView.PlayAudioFragment;
import com.example.grzegorzkwasniewski.speakloududacity.database.RecordDBHelper;
import com.example.grzegorzkwasniewski.speakloududacity.model.RecordItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by grzegorz.kwasniewski on 2018-07-19.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordingsViewHolder> {

    private static final String LOG_TAG = "FileViewerAdapter";

    private List<RecordItem> mModels;
    private final Comparator<RecordItem> mComparator;

    private RecordItem item;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private ConstraintLayout mRootLayout;

    public RecordsAdapter(Context context, List<RecordItem> models, LinearLayoutManager linearLayoutManager, ConstraintLayout rootLayout , Comparator<RecordItem> comparator) {
        super();
        mContext = context;
        mModels = models;
        Log.d("fdfdfdfdfdfdf", "fdfdfdfdfdfdf" + models.size());
        mLayoutManager = linearLayoutManager;
        mRootLayout = rootLayout;
        mComparator = comparator;
    }

    @Override
    public RecordingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        mContext = parent.getContext();

        return new RecordingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecordingsViewHolder holder, int position) {
        item = mModels.get(position);
        long itemDuration = item.getRecordLength();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        holder.recordName.setText(item.getFileName());
        holder.recordLength.setText(String.format("%02d:%02d", minutes, seconds));
        holder.recordTimeAdded.setText(
                DateUtils.formatDateTime(
                        mContext,
                        item.getRecordTime(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR
                )
        );

        // open fragment for playing audio file
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlayAudioFragment playbackFragment =
                            new PlayAudioFragment().newInstance(mModels.get(holder.getAdapterPosition()));

                    FragmentTransaction transaction = ((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                            .beginTransaction();

                    playbackFragment.show(transaction, "dialog_playback");
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Problems with fragment on file card view", e);
                }
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ArrayList<String> entrys = new ArrayList<String>();
                entrys.add(mContext.getString(R.string.file_share_dialog));
                entrys.add(mContext.getString(R.string.file_rename_rename));
                entrys.add(mContext.getString(R.string.file_delete_dialog));

                final CharSequence[] items = entrys.toArray(new CharSequence[entrys.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getString(R.string.options_dialog_title));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            shareFileDialog(holder.getAdapterPosition());
                        } if (item == 1) {
                            renameFileDialog(holder.getAdapterPosition());
                        } else if (item == 2) {
                            deleteFileDialog(holder.getAdapterPosition());
                        }
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton(mContext.getString(R.string.action_cancel_dialog),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });
    }

    public void add(RecordItem model) {
        mSortedList.add(model);
    }

    public void remove(RecordItem model) {
        mSortedList.remove(model);
    }

    public void add(List<RecordItem> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<RecordItem> models) {
        mSortedList.beginBatchedUpdates();
        for (RecordItem model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<RecordItem> models) {
        mSortedList.beginBatchedUpdates();

        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final RecordItem model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public static class RecordingsViewHolder extends RecyclerView.ViewHolder {
        protected TextView recordName;
        protected TextView recordLength;
        protected TextView recordTimeAdded;
        protected View cardView;

        public RecordingsViewHolder(View view) {
            super(view);
            recordName = (TextView) view.findViewById(R.id.file_name);
            recordLength = (TextView) view.findViewById(R.id.file_length);
            recordTimeAdded = (TextView) view.findViewById(R.id.file_date_added);
            cardView = view.findViewById(R.id.card_view);
        }
    }

    public void shareFileDialog(int position) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(mModels.get(position).getFilePath())));
        shareIntent.setType("audio/mp4");
        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getText(R.string.send_to)));
    }

    public void renameFileDialog (final int position) {
        // File rename dialog
        AlertDialog.Builder renameFileBuilder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.rename_file_dialog, null);

        final EditText input = (EditText) view.findViewById(R.id.new_name);

        renameFileBuilder.setTitle(mContext.getString(R.string.title_rename_dialog));
        renameFileBuilder.setCancelable(true);
        renameFileBuilder.setPositiveButton(mContext.getString(R.string.action_ok_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String value = input.getText().toString().trim() + ".mp4";
                            rename(position, value);

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Can't get text from input", e);
                        }

                        dialog.cancel();
                    }
                });
        renameFileBuilder.setNegativeButton(mContext.getString(R.string.action_cancel_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        renameFileBuilder.setView(view);
        AlertDialog alert = renameFileBuilder.create();
        alert.show();
    }

    public void deleteFileDialog (final int position) {
        // File delete confirm
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(mContext);
        confirmDelete.setTitle(mContext.getString(R.string.delete_file_dialog));
        confirmDelete.setMessage(mContext.getString(R.string.delete_file_message_dialog));
        confirmDelete.setCancelable(true);
        confirmDelete.setPositiveButton(mContext.getString(R.string.confirm_delete_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            delteRecordItem(position);

                        } catch (Exception e) {
                            Log.e(LOG_TAG, "exception", e);
                        }

                        dialog.cancel();
                    }
                });
        confirmDelete.setNegativeButton(mContext.getString(R.string.cancel_delete_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = confirmDelete.create();
        alert.show();
    }

    public void rename(int position, String name) {

        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/SpeakLoud/" + name;
        File file = new File(mFilePath);

        if (file.exists() && !file.isDirectory()) {
            Toast.makeText(mContext,
                    mContext.getString(R.string.file_exists_toast),
                    Toast.LENGTH_SHORT).show();

        } else {
            //file name is unique, rename file
            File oldFilePath = new File(mModels.get(position).getFilePath());
            oldFilePath.renameTo(file);
            //mDatabase.renameRecordItem(mModels.get(position), name, mFilePath);

            // notify recycler view that data was changed
            notifyItemChanged(position);
        }
    }

    public void delteRecordItem(int position) {
        //delete file from storage
        File file = new File(mModels.get(position).getFilePath());
        file.delete();

        // TODO Change to snack bar
        Snackbar mySnackbar = Snackbar.make(mRootLayout, mContext.getString(R.string.file_deleted_message), Snackbar.LENGTH_SHORT);
        mySnackbar.show();
        //Toast.makeText(mContext, mContext.getString(R.string.file_deleted_message), Toast.LENGTH_SHORT
        //).show();

        //mDatabase.removeItemWithId(mDatabase.getItemAt(position).getmDatabaseID());

        // notify recycler view that data was changed
        notifyItemRemoved(position);
    }

    private final SortedList<RecordItem> mSortedList = new SortedList<>(RecordItem.class, new SortedList.Callback<RecordItem>() {
        @Override
        public int compare(RecordItem a, RecordItem b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
            notifyDataSetChanged();
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(RecordItem oldItem, RecordItem newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(RecordItem item1, RecordItem item2) {
            return item1.getmDatabaseID() == item2.getmDatabaseID();
        }
    });
}
