package sheridan.capstone.findmyfarmer.ImageHandler;

/**
 * @author Sohaib Hussain
 * Description: Handles Coroutine calls for the FirebaseImageHandler.
 * Date Modified: December 14th, 2020
 **/

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Optional;

public interface StorageResponse {
    void processFinish(List<StorageReference> response, Optional<Bitmap> bitmap, Optional<String> Url);
    void OnErrorListener(String error);
}
