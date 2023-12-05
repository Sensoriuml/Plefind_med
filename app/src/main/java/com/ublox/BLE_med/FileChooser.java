package com.ublox.BLE_med;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FileChooser {
    private static final String PARENT_DIR = "..";

    private final Activity activity;
    private Button cancel, select;
    private ListView list;
    private Dialog dialog;
    private File currentPath;

    // filter on file extension
    private String extension = "csv";
    private boolean empty;

    public void setExtension(String extension) {
        this.extension = (extension == null) ? null :
                extension.toLowerCase();
    }

    // file selection event handling
    public interface FileSelectedListener {
        void fileSelected(String[] files);
    }
    public FileChooser setFileListener(FileSelectedListener fileListener) {
        this.fileListener = fileListener;
        return this;
    }
    private FileSelectedListener fileListener;

    public FileChooser(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);

        LinearLayout linr = new LinearLayout(activity);
        linr.setOrientation(LinearLayout.VERTICAL);
        //LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(linr);

        LinearLayout.LayoutParams lpView1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.763f);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.237f);

        list = new ListView(activity);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                String fileChosen = (String) list.getItemAtPosition(which);
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    refresh(chosenFile);
                } else {
                    //if(view.)
                     //   view.setBackgroundColor(Color.TRANSPARENT);
                    //else
                      //  view.setBackgroundColor(Color.LTGRAY);
//                    if (fileListener != null) {
//                        fileListener.fileSelected(chosenFile);
//                    }
//                    dialog.dismiss();
                }
            }
        });
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        list.setItemsCanFocus(false);
        linr.addView(list, lpView1);

        LinearLayout linh = new LinearLayout(activity);
        linh.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linhParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);
        LinearLayout.LayoutParams llh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llh.gravity = Gravity.CENTER_HORIZONTAL;
        linr.addView(linh, llh);

        cancel = new Button(activity);
        cancel.setText("CANCEL");
        cancel.setBackgroundColor(Color.parseColor("#5fc9f8"));
        cancel.setTextColor(Color.WHITE);
        cancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        cancel.setGravity(Gravity.CENTER);
        cancel.setPadding(0,0,0,0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        linh.addView(cancel, linhParam);

        select = new Button(activity);
        select.setText("SELECT");
        select.setBackgroundColor(Color.parseColor("#5fc9f8"));
        select.setTextColor(Color.WHITE);
        select.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        select.setGravity(Gravity.CENTER);
        select.setPadding(0,0,0,0);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = list.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    if (checked.valueAt(i))
                        selectedItems.add(list.getAdapter().getItem(position).toString());
                }

                String[] outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                            + "/" + selectedItems.get(i);
                }

                if (fileListener != null) {
                    fileListener.fileSelected(outputStrArr);
                }
                dialog.dismiss();
            }
        });
        linh.addView(select, linhParam);

        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        refresh(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
    }

    public void showDialog() {
        if(!empty) dialog.show();
        else Toast.makeText(activity, "No .csv files to read", Toast.LENGTH_LONG).show();
    }


    /**
     * Sort, filter and display the files for the given path.
     */
    private void refresh(File path) {
        this.currentPath = path;

        if (path.exists()) {
            File[] dirs = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isDirectory() && file.canRead());
                }
            });
            File[] files = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (!file.isDirectory()) {
                        if (!file.canRead()) {
                            return false;
                        } else if (extension == null) {
                            return true;
                        } else {
                            return file.getName().toLowerCase().endsWith(extension);
                        }
                    } else {
                        return false;
                    }
                }
            });

            // convert to an array
            int i = 0;
            String[] fileList;
            if (path.getParentFile() == null) {
                fileList = new String[files.length];//dirs.length +
            } else {
                fileList = new String[files.length]; //+ 1];dirs.length +
                //fileList[i++] = PARENT_DIR;
            }
            Arrays.sort(dirs);

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });

            //for (File dir : dirs) { fileList[i++] = dir.getName(); }
            for (File file : files ) {
                if (!file.getName().contains("null")) fileList[i++] = file.getName();
            }
System.out.println(Arrays.toString(fileList));
            if(fileList.length != 0){
                empty = false;
                // refresh the user interface
                dialog.setTitle(currentPath.getPath());
                list.setAdapter(new ArrayAdapter(activity,
                        android.R.layout.simple_list_item_multiple_choice, fileList) {
                    @Override
                    public View getView(int pos, View view, ViewGroup parent) {
                        view = super.getView(pos, view, parent);
                        ((TextView) view).setSingleLine(true);
                        return view;
                    }
                });
            } else empty = true;

        }
    }


    /**
     * Convert a relative filename into an actual File object.
     */
    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) {
            return currentPath.getParentFile();
        } else {
            return new File(currentPath, fileChosen);
        }
    }
}