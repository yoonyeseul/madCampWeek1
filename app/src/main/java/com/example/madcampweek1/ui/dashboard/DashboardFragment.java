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
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Size;
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

import com.example.madcampweek1.R;
import com.example.madcampweek1.databinding.FragmentDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private int maxIdx = 0;
    private Thread currentThread;
    private int gridColumnCount = 3;
    public static int viewToDelete = 0;

    public Handler handler = new Handler();

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

        Button btn2 = binding.button2;
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.removeAllViews();
                gridColumnCount = gridColumnCount == 1 ? 3 : 1;
                grid.setColumnCount(gridColumnCount);
                currentThread.interrupt();
                try {
                    currentThread.wait();
                } catch (Exception e) {
                }
                loadStoredImages();
            }
        });

        grid.setColumnCount(3);
        loadStoredImages();

        return root;
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewToDelete != 0) {
            ImageView iv = getActivity().findViewById(viewToDelete);
            ((ViewManager) iv.getParent()).removeView(iv);
            deleteImage(iv.getId());
            viewToDelete = 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomNavigationView navigation_view = getActivity().findViewById(R.id.nav_view);
        navigation_view.getMenu().findItem(R.id.navigation_dashboard).setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentThread.interrupt();
        try {
            currentThread.wait();
        } catch (Exception e) {
        }
        BottomNavigationView navigation_view = getActivity().findViewById(R.id.nav_view);
        navigation_view.getMenu().findItem(R.id.navigation_dashboard).setEnabled(true);
    }

    public void loadStoredImages() {
        // 버튼 disable 후, 끝나면 enable
        currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File filePath = getActivity().getCacheDir();
                    File[] flist = filePath.listFiles();
                    File[] sortedFlist = getSortedNameList(flist);
                    for (File i : sortedFlist) {
                        String imgpath = filePath + "/" + i.getName();

                        ImageView imageView = new ImageView(getContext());

                        maxIdx = Math.max(maxIdx, getImageIdxFromName(i.getName()));

                        Bitmap bm = getBitmapForImgView(imgpath);
                        if (Thread.interrupted()) break;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bm);
                                addImageViewToGridLayout(imageView, getImageIdxFromName(i.getName()));
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        });
        currentThread.start();
    }

//    private void loadStoredImages() {
//        try {
//            File filePath = getActivity().getCacheDir();
//            File[] flist = filePath.listFiles();
//            File[] sortedFlist = getSortedNameList(flist);
//            for (File i : sortedFlist) {
//                String imgpath = filePath + "/" + i.getName();
////                Bitmap bm = ThumbnailUtils.createImageThumbnail(imgpath,  MediaStore.Images.Thumbnails.MINI_KIND);
////                Bitmap bm = getBitmapForImage(imgpath); //BitmapFactory.decodeFile(imgpath);
//
//                ImageView imageView = new ImageView(getContext());
//                setBitmapToImgView(imgpath, imageView);
//
//                addImageViewToGridLayout(imageView, getImageIdxFromName(i.getName()));
//                maxIdx = Math.max(maxIdx, getImageIdxFromName(i.getName()));
//            }
//        } catch (Exception e) {
////            Toast.makeText(getActivity(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
//        }
//    }

    private Bitmap getBitmapForImgView(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        int imgWidth = getImageWidthAccordingToWindowSize();
        options.inSampleSize = calculateInSampleSize(options, imgWidth, imgWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    private void setBitmapToImgView(String imgPath, ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        int imgWidth = getImageWidthAccordingToWindowSize();
        options.inSampleSize = calculateInSampleSize(options, imgWidth, imgWidth);
        options.inJustDecodeBounds = false;
        imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath, options));
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private File[] getSortedNameList(File[] fList) {
        File[] ret = new File[fList.length];
        for (int i = 0; i < fList.length; i++) {
            ret[i] = fList[i];
        }
        Arrays.sort(ret, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String name1 = o1.getName(), name2 = o2.getName();
                return Integer.valueOf(name1.substring(5)).compareTo(Integer.valueOf(name2.substring(5)));
            }
        });
        return ret;
    }

    private int getImageIdxFromName(String name) {
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

        int imageWidth = getImageWidthAccordingToWindowSize();

//        GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
//                GridLayout.UNDEFINED,GridLayout.FILL,1f),
//                GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f));
//        iv.setLayoutParams(param);

        iv.setId(id);
        iv.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
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
                                // Toast.makeText(getActivity(), "삭제 취소", Toast.LENGTH_SHORT);
                            }
                        })
                        .show();
                return true;
            }
        });
        grid.addView(iv);
    }

    private int getImageWidthAccordingToWindowSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x / gridColumnCount;
    }

    private void deleteImage(int imgIdx) {
        String imgName = "image" + imgIdx;
        try {
            File file = getActivity().getCacheDir();
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++)
                if (flist[i].getName().equals(imgName)) {
                    flist[i].delete();
                }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}