package com.example.madcampweek1.ui.dashboard;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madcampweek1.databinding.FragmentDashboardBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private static final int REQUEST_CODE = 0;
    private int count_selected = 0;
    GridLayout grid = null;
    Button btn;
//    Vector<String> imagesName = new Vector<String>();
    private int maxIdx = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        grid = binding.GridLayout1;
        btn = binding.button;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        loadStoredImages();

        return root;
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void loadStoredImages() {
        try {
            File file = getActivity().getCacheDir();
            File[] flist = file.listFiles();
            String[] nameList = getSortedNameList(flist);
            for (int i = 0; i < flist.length; i++) {
                String imgpath = getActivity().getCacheDir() + "/" + flist[i].getName();
                Bitmap bm = BitmapFactory.decodeFile(imgpath);
                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bm);
                addImageViewToGridLayout(imageView, getIdx(flist[i].getName()));
                maxIdx = Math.max(maxIdx, getIdx(flist[i].getName()));
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] getSortedNameList(File[] fList) {
        String[] ret = new String[fList.length];
        for (int i = 0; i < fList.length; i++) {
            ret[i] = fList[i].getName();
        }
        Arrays.sort(ret, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1.substring(5)).compareTo(Integer.valueOf(o2.substring(5)));
            }
        });
        return ret;
    }

    private int getIdx(String name) {
        return Integer.parseInt(name.substring(5));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    saveBitmapToJpeg(img, ++maxIdx);

                    ImageView iv = new ImageView(getContext());
                    iv.setImageBitmap(img);
                    addImageViewToGridLayout(iv, maxIdx);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap, int id) {
        File tempFile = new File(getActivity().getCacheDir(), "image" + id);
        try {
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void addImageViewToGridLayout(ImageView iv, int id) {

        Point size = getWindowSize();

        iv.setId(id);
//        imagesName.add("image" + id);
        iv.setLayoutParams(new LinearLayout.LayoutParams(size.x / 3, size.x / 3));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                intent.putExtra("imageIdx", iv.getId());
                startActivity(intent);
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((ViewManager) iv.getParent()).removeView(iv);
                                deleteImage(iv.getId());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), "삭제 취소", Toast.LENGTH_SHORT);
                            }
                        })
                        .show();
                return false;
            }
        });
        grid.addView(iv);
    }

    private Point getWindowSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private void deleteImage(int imgIdx) {
        String imgName = "image" + imgIdx;
        try {
            File file = getActivity().getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++)
                if (flist[i].getName().equals(imgName)) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                }
//            deleteImageFromVector(imgIdx);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }

//    private void deleteImageFromVector(int imageIdx) {
//        imagesName.remove(imageIdx);
//        for (int i = imageIdx; i < imagesName.size(); ++i) {
//            ImageView iv = (ImageView) getView().findViewById(i + 1);
//            iv.setId(i);
//            imagesName.set(i, "image" + imagesName.size());
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}