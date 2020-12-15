package sheridan.capstone.findmyfarmer.ImageHandler;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Sohaib Hussain
 * Description: Handles Images being stored in the firebase storage. Used to mark images as primary
 *              and also retrieving images from the storage according to the farm or customer or
 *              product. This class automatically creates a folder in Firebase for individual
 *              farm and customer.
 * Date Modified: December 14th, 2020
 **/
public class FirebaseImagehandler {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String FolderName;
    private Context context;
    private String ACTIVE_KEYWORD = "ACTIVE";

    public FirebaseImagehandler(DirectoryName directoryName, int id, Context context){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FolderName = directoryName.name() + id; //defines the folder related to the user
        generateFileName();
        this.context = context;
    }

    //generates a unique filename
    private String generateFileName(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        return FolderName + "_" + date + ".jpg";
    }
    //gets the list of images from Firebase based on directoryname and id given
    public void GetAllFirebaseImageNames(StorageResponse storageResponse) {
        StorageReference listRef = storage.getReference().child(FolderName);
        listRef.listAll().addOnSuccessListener(listResult -> {
            storageResponse.processFinish(listResult.getItems(),null,null);
        }).addOnFailureListener(e -> {
            storageResponse.OnErrorListener(e.toString());
            System.out.println(e);
        });
    }
    //downloads all images from Firebase to internal storage
    private void DownloadImagesFromFirebaseToLocalStorage(List<String> filenames, int index,StorageResponse response){
        final long TEN_MEGABYTE = 1024 * 1024 * 10;

        if(index < filenames.size()){
            String endpoint = FolderName +"/"+filenames.get(index);
            StorageReference downloadRef = storageReference.child(endpoint);

            downloadRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                saveToInternalStorage(bitmap,filenames.get(index));
                //Toast.makeText(context,"Downloaded " + filenames.get(index) ,Toast.LENGTH_SHORT).show();
                DownloadImagesFromFirebaseToLocalStorage(filenames,index+1,response);

            }).addOnFailureListener(e -> {
                System.out.println(e);
                response.OnErrorListener(e.toString());
            });
        }
        else{
            response.processFinish(null,null,null);
            return;
        }
    }
    /*
        Checks if the images in cloud have been downloaded, if there are any new files from cloud
        then they are downloaded into local storage
    */
    private void InitializeImages(StorageResponse storageResponse){
            GetAllFirebaseImageNames(new StorageResponse() {
                @Override
                public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                    List<String> localStorageImages = GetNamesOfImagesInLocalStorage();
                    List<String> ImagesToDownloadFromCloud = new ArrayList();

                    for(StorageReference ref: response){
                        if(!(localStorageImages.contains(ref.getName()))){
                            ImagesToDownloadFromCloud.add(ref.getName());
                        }
                    }

                    DownloadImagesFromFirebaseToLocalStorage(ImagesToDownloadFromCloud,0,storageResponse);
                }
                @Override
                public void OnErrorListener(String error) {
                    storageResponse.OnErrorListener(error);
                }
            });
    }
    /*
        checks if there are any extra images that have been deleted from the cloud,
        and are then removed from the local storage as well
    */
    public void RefreshLocalStorage(StorageResponse response){
        List<String> localStorageImages = GetNamesOfImagesInLocalStorage();
        if(!(localStorageImages.isEmpty())){
            GetAllFirebaseImageNames(new StorageResponse() {
                @Override
                public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                    List<String> cloudImages = new ArrayList();
                    for(StorageReference ref: response){
                        cloudImages.add(ref.getName());
                    }

                    for (String localStorageImage: localStorageImages){
                        if(!(cloudImages.contains(localStorageImage))){
                            DeleteImageFromlocalStorage(localStorageImage);
                        }
                    }
                }
                @Override
                public void OnErrorListener(String error) {

                }
            });
            InitializeImages(response);
        }
        else{
            InitializeImages(response);
        }
    }
    //uploads the image to Firebase
    public void UploadImageToFirebase(Bitmap bitmap,StorageResponse response){
        try {
            StorageReference ref = storageReference.child(FolderName + "/" + generateFileName());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = ref.putBytes(data);

            uploadTask.addOnFailureListener(e -> {
                System.out.println("Failed upload image");
                response.OnErrorListener(e.toString());
            }).addOnSuccessListener(taskSnapshot -> {
                System.out.println("Successfully upload image");
                response.processFinish(null,null,null);
            });
        }catch (Exception ex){
            System.out.println(ex);
            response.OnErrorListener(ex.toString());
        }
    }
    //uploads the image to Firebase with a custom name
    private void UploadImageToFirebasewithCustomName(Bitmap bitmap,String NewFileName,StorageResponse response){
        try {
            StorageReference ref = storageReference.child(FolderName + "/" + NewFileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = ref.putBytes(data);

            uploadTask.addOnFailureListener(e -> {
                System.out.println("Failed upload image");
                response.OnErrorListener(e.toString());
            }).addOnSuccessListener(taskSnapshot -> {
                System.out.println("Successfully upload image");
                response.processFinish(null,null,null);
            });
        }catch (Exception ex){
            System.out.println(ex);
            response.OnErrorListener(ex.toString());
        }
    }
    //Retrieves an Image from Firebase using filename
    private void GetImageFromFirebase(String fileName,StorageResponse storageResponse){
        GetAllFirebaseImageNames(new StorageResponse() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                final long TEN_MEGABYTE = 1024 * 1024 * 10;
                for(StorageReference ref : response){
                    String refname = ref.getName();
                    if(refname.compareToIgnoreCase(fileName)==0){
                        ref.getBytes(TEN_MEGABYTE).addOnSuccessListener(bytes -> {
                            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            Optional<Bitmap> optBitmap =Optional.ofNullable(bitmap1);
                            storageResponse.processFinish(null,optBitmap,null);
                        }).addOnFailureListener(e ->{
                            storageResponse.OnErrorListener(e.toString());
                        });
                    }
                }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //Deletes an image from the Firebase using filename
    public void DeleteImageFromFirebase(String fileName, StorageResponse storageResponse){
        StorageReference ref = storageReference.child(FolderName + "/" + fileName);

        ref.delete().addOnSuccessListener(aVoid -> {
            System.out.println("Deleted " + fileName + "Successfully!");
            storageResponse.processFinish(null,null,null);
        }).addOnFailureListener(e ->
                storageResponse.OnErrorListener(e.toString()));
    }
    //Renames images in Firebase
    private void RenameFileFirebase(String fileName,String newName, StorageResponse storageResponse){
        GetAllFirebaseImageNames(new StorageResponse() {
            @Override
            public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                for(StorageReference ref : response){
                    String refname = ref.getName();
                    if(refname.compareToIgnoreCase(fileName)==0){
                        GetImageFromFirebase(fileName, new StorageResponse() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                                //uploading with a new Name
                                UploadImageToFirebasewithCustomName(bitmap.get(), newName, new StorageResponse() {
                                    @Override
                                    public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                        DeleteImageFromFirebase(fileName, new StorageResponse() {
                                            @Override
                                            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                                storageResponse.processFinish(response,bitmap,Url);
                                            }
                                            @Override
                                            public void OnErrorListener(String error) {
                                                storageResponse.OnErrorListener(error);
                                            }
                                        });
                                    }
                                    @Override
                                    public void OnErrorListener(String error) {
                                        storageResponse.OnErrorListener(error);
                                    }
                                });
                            }
                            @Override
                            public void OnErrorListener(String error) {
                                storageResponse.OnErrorListener(error);
                            }
                        });
                    }
                }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //Retrieves the bitmap of the Primary Image
    public void GetPrimaryImageFromFirebase(StorageResponse storageResponse){
        GetAllFirebaseImageNames(new StorageResponse() {
            StorageReference reference = null;
            @Override
            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                for(StorageReference ref: response){
                    String refname = ref.getName();
                    if(refname.toLowerCase().contains(ACTIVE_KEYWORD.toLowerCase())){
                        reference = ref;
                    }
                }
               if(reference != null){
                   GetImageFromFirebase(reference.getName(), new StorageResponse() {
                       @Override
                       public void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url) {
                           List<StorageReference> references = new ArrayList<>();
                           references.add(reference);
                           storageResponse.processFinish(references,bitmap,null);
                       }
                       @Override
                       public void OnErrorListener(String error) {
                           storageResponse.OnErrorListener(error);
                       }
                   });
               }
               else{
                   storageResponse.processFinish(null,null,null);
               }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //Retrieves the URL of the Primary Image
    public void GetPrimaryImageFromFirebaseURL(StorageResponse storageResponse){
        GetAllFirebaseImageNames(new StorageResponse() {
            boolean NoPrimaryImageExists = true;
            StorageReference reference = null;
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                for (StorageReference ref : response){
                    String refname = ref.getName();
                    if(refname.toLowerCase().contains(ACTIVE_KEYWORD.toLowerCase())){
                        reference = ref;
                    }
                }

                if(reference != null){
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        ArrayList<StorageReference> references = new ArrayList<>();
                        references.add(reference);
                        NoPrimaryImageExists = false;
                        Optional<String> stringOptional = Optional.of(uri.toString());
                        storageResponse.processFinish(references,null,stringOptional);
                        return;
                    }).addOnFailureListener(e ->
                            storageResponse.OnErrorListener(e.toString())
                    );
                }
                else{
                    storageResponse.processFinish(null,null,null);
                }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //Retrieves the URL of any image in the Firebase
    public void GetImageURLFromFirebase(String fileName,StorageResponse storageResponse){
        GetAllFirebaseImageNames(new StorageResponse() {
            StorageReference reference = null;
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                for (StorageReference ref : response){
                    String refname = ref.getName().toLowerCase().replaceAll(".jpg","");
                    refname = refname.toLowerCase().replaceAll(".jpeg","");
                    refname = refname.toLowerCase().replaceAll(" ","");
                    String filename = fileName.replaceAll(" ","");
                    if(refname.toLowerCase().compareToIgnoreCase(filename.toLowerCase())==0){
                        reference = ref;
                    }
                }

                if(reference != null){
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        ArrayList<StorageReference> references = new ArrayList<>();
                        references.add(reference);
                        Optional<String> stringOptional = Optional.of(uri.toString());
                        storageResponse.processFinish(references,null,stringOptional);
                        return;
                    }).addOnFailureListener(e ->
                            storageResponse.OnErrorListener(e.toString())
                    );
                }
                else{
                    storageResponse.processFinish(null,null,null);
                }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //Makes an Image Primary Image in Firebase
    public void MakeImagePrimary(String fileName,StorageResponse storageResponse){
        GetPrimaryImageFromFirebase(new StorageResponse() {
            @Override
            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                //if there is a primary image
                if(bitmap != null){
                    String respname = response.get(0).getName();
                    if(respname.compareToIgnoreCase(fileName)!=0){
                        String OldName = respname;
                        String NewName = OldName.toLowerCase().replaceAll(ACTIVE_KEYWORD.toLowerCase(),"");
                        RenameFileFirebase(OldName, NewName, new StorageResponse() {
                            @Override
                            public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                String oldName = fileName;
                                String newName = fileName.replace(".jpg","");
                                newName = newName + ACTIVE_KEYWORD.toLowerCase() + ".jpg";
                                RenameFileFirebase(oldName, newName, new StorageResponse() {
                                    @Override
                                    public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                        storageResponse.processFinish(response,bitmap,Url);
                                    }
                                    @Override
                                    public void OnErrorListener(String error) {
                                        storageResponse.OnErrorListener(error);
                                    }
                                });
                            }
                            @Override
                            public void OnErrorListener(String error) { storageResponse.OnErrorListener(error); }
                        });
                    }
                    else{
                        storageResponse.processFinish(null,null,null);
                    }
                }
                //if there is no primary image
                else{
                    //If there are no primary images, make the given filename the primary image.
                    //If the filename does not exist, then check if there are any other images.
                    //If there are images in the directory then use the first image and make it primary
                    //If there are no images then return error message and null
                     GetAllFirebaseImageNames(new StorageResponse() {
                         StorageReference reference = null;
                         String OldName;
                         String NewName;
                         @Override
                         public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                             //check if the filename exists
                             for(StorageReference streference: response){
                                 String refname = streference.getName();
                                 if(refname.compareToIgnoreCase(fileName)==0){
                                    reference = streference;
                                 }
                             }
                             //if the filename exists then make that filename the primary image
                             if(reference != null){
                                 OldName = reference.getName();
                                 NewName = OldName.replaceAll(".jpg","");
                                 NewName = NewName.replaceAll(ACTIVE_KEYWORD.toLowerCase(),"");
                                 NewName = NewName + ACTIVE_KEYWORD.toLowerCase() +".jpg";
                                 if(NewName.compareToIgnoreCase(OldName)!=0){
                                     RenameFileFirebase(OldName, NewName, new StorageResponse() {
                                         @Override
                                         public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                             storageResponse.processFinish(response,bitmap,Url);
                                         }
                                         @Override
                                         public void OnErrorListener(String error) {
                                             storageResponse.OnErrorListener(error);
                                         }
                                     });
                                 }
                             }
                             //if the filename by the name does not exist then check if there are any files,
                             else{
                                 //if there are files then get the top most one and make it primary
                                 //if there are no file then return error message
                                 if(response.size() > 0){
                                     StorageReference ref1 = response.get(0);
                                     OldName = ref1.getName();
                                     NewName = OldName.replaceAll(".jpg","");
                                     NewName = NewName.replaceAll(ACTIVE_KEYWORD.toLowerCase(),"");
                                     NewName = NewName + ACTIVE_KEYWORD.toLowerCase() +".jpg";
                                     if(NewName.compareToIgnoreCase(OldName)!=0){
                                         RenameFileFirebase(OldName, NewName, new StorageResponse() {
                                             @Override
                                             public void processFinish(List<StorageReference> response,  Optional<Bitmap> bitmap, Optional<String> Url) {
                                                 storageResponse.processFinish(response,bitmap,Url);
                                             }
                                             @Override
                                             public void OnErrorListener(String error) {
                                                 storageResponse.OnErrorListener(error);
                                             }
                                         });
                                     }
                                 }
                                 else {
                                     storageResponse.OnErrorListener("Filename: " + fileName + " does not exist");
                                 }
                             }
                         }
                         @Override
                         public void OnErrorListener(String error) {
                            storageResponse.OnErrorListener(error);
                         }
                     });
                }
            }
            @Override
            public void OnErrorListener(String error) {
                storageResponse.OnErrorListener(error);
            }
        });
    }
    //saves the image to internal storage, using the given filename
    private String saveToInternalStorage(Bitmap bitmapImage,String fileName){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(FolderName, Context.MODE_PRIVATE);
        File mypath = new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();


    }
    //loads the image from local storage based on filename
    public Bitmap loadImageFromStorage(String fileName) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(FolderName, Context.MODE_PRIVATE);
            File f = new File(directory,fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }
    //Deletes the File from local Storage
    private void DeleteImageFromlocalStorage(String fileName){
        try{
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(FolderName, Context.MODE_PRIVATE);
            File f = new File(directory,fileName);
            System.out.println(f.exists());
            System.out.println(f.delete());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
    //Gets the Names of Images in Local Storage
    public List<String> GetNamesOfImagesInLocalStorage(){

        List<String> files = new ArrayList();

        try{
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(FolderName, Context.MODE_PRIVATE);

            if(directory.exists()){
                files.addAll(Arrays.asList(directory.list()));
                return files;
            }
            else{
                System.out.println("Directory " + directory.getName() + " Doesnt exist");
                return null;
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

        return files;
    }
}

