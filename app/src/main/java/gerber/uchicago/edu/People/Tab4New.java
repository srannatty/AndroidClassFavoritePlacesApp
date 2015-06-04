package gerber.uchicago.edu.People;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import gerber.uchicago.edu.GoogleResultsData;
import gerber.uchicago.edu.JSONParser;
import gerber.uchicago.edu.Places.Restaurant;
import gerber.uchicago.edu.R;
import gerber.uchicago.edu.ResultsDialogActivity;
import gerber.uchicago.edu.Yelp.Yelp;
import gerber.uchicago.edu.Yelp.YelpResultsData;

/**
 * Created by jennifer1 on 5/31/15.
 */
public class Tab4New extends Fragment {


    private LinearLayout mRootViewGroup;
    private EditText mNameField, mSearchField, mAddressField, mPhoneField, mNoteField;
    private TextView mPhoneText, mAddressText, mNoteText;
    private Button mExtractButton, mSaveButton, mCancelButton;
    private CheckBox mCheckFavorite;
    private View mViewFavorite;
    private ImageView mPhotoView;

    //the restaurant passed into this activity during edit operation
    private Restaurant mRestaurant;

    private String mStrImageUrl = "";
    private GoogleResultsData mImageResult;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_layout_contact_new, container, false);


        mRootViewGroup = (LinearLayout) v.findViewById(R.id.data_root_view_group_person_new);

        //Required Field
        mNameField = (EditText) v.findViewById(R.id.person_name);
        mSearchField = (EditText) v.findViewById(R.id.person_search);
        //Photo Field
        mPhotoView = (ImageView) v.findViewById(R.id.person_image_view);


        //todo implement this later
        //each required field must monitor itself and other text field
        //mNameField.addTextChangedListener(new RequiredEditWatcher(mSearchField));
        //mSearchField.addTextChangedListener(new RequiredEditWatcher(mNameField));


        //EditText fields in detail
        mPhoneField = (EditText) v.findViewById(R.id.person_phone);
        mAddressField = (EditText) v.findViewById(R.id.person_address);
        mNoteField = (EditText) v.findViewById(R.id.person_note);
        //Favorite Check
        mViewFavorite = v.findViewById(R.id.person_view_favorite);
        mViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckFavorite.isChecked()) {
                    mCheckFavorite.setChecked(false);
                } else {
                    mCheckFavorite.setChecked(true);
                }
            }
        });
        //Favorite in detail
        mCheckFavorite = (CheckBox) v.findViewById(R.id.person_favorite);
        mCheckFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleFavoriteView(isChecked);
            }
        });

        //Buttons
        mExtractButton = (Button) v.findViewById(R.id.person_search_button);
        mExtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Search Google image and create the list
                fetchPhoto(mPhotoView);
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mNameField.getWindowToken(), 0);
            }
        });


        mSaveButton = (Button) v.findViewById(R.id.person_save_data_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save information to contacts
                String Name = mNameField.getText().toString();
                String PhoneNumber = mPhoneField.getText().toString();
                String EmailAddress = mAddressField.getText().toString();
                //Maybe notes and the picture --implemented later

                ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                //Name
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                Name).build());

                //Phone
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, PhoneNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                //Email
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, EmailAddress)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());

                //ask contactprovider to create a new contact
                try {
                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });


        return v;
    }


    private void toggleFavoriteView(boolean bFavorite) {
        if (bFavorite) {
            mViewFavorite.setBackgroundColor(getResources().getColor(R.color.orange));
        } else {
            mViewFavorite.setBackgroundColor(getResources().getColor(R.color.green));
        }

    }



    public ArrayList<String> ResultListToStringArray(List<GoogleResultsData.ResponseData.Result> list) {

        int listsize = list.size();
        ArrayList<String> converted = new ArrayList<String>(20);
        int loopCount;
        //We just need 20 at maximum
        if (listsize < 20) {
            loopCount = listsize;
        } else {
            loopCount = 20;
        }

        GoogleResultsData.ResponseData.Result resultItem;

        //conversion through loop
        for (int i = 0; i < loopCount; i++ ) {
            resultItem = list.get(i);
            converted.add(i, resultItem.unescapedUrl);
        }

        return converted;
    }





    private void fetchPhoto(ImageView imageView) {
        String strUrl = String.format("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s&imgsz=small&imgtype=photo",
                mSearchField.getText().toString());
        strUrl = strUrl.replaceAll("\\s+", "%20");
        new PersonImageTask(imageView).execute(strUrl);
    }

    private class PersonImageTask extends AsyncTask<String, Void, GoogleResultsData> {
        ImageView mImageView;

        public PersonImageTask(ImageView imageViewParam) {
            this.mImageView = imageViewParam;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mImageView.setImageResource(R.drawable.gear);
        }

        protected GoogleResultsData doInBackground(String... urls) {


            GoogleResultsData googleResultsData = null;
            //Bitmap bitmap = null;

            try {
                JSONObject jsonRaw = new JSONParser().getSecureJSONFromUrl(urls[0]);
                googleResultsData = new Gson().fromJson(jsonRaw.toString(), GoogleResultsData.class);
                //mStrImageUrl = googleResultsData.responseData.results.get(0).unescapedUrl;
                //InputStream in = new java.net.URL(mStrImageUrl).openStream();
                //bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return googleResultsData;
        }

        protected void onPostExecute(GoogleResultsData googleResult) {

            mImageResult = googleResult;

            if (mImageResult != null) {
                mImageView.setImageResource(R.drawable.gear);
                Toast.makeText(getActivity(), "No image for that search term", Toast.LENGTH_SHORT).show();
                return;
            }

            List<GoogleResultsData.ResponseData.Result> ImageList = mImageResult.responseData.results;
            ArrayList<String> stringArrayList = ResultListToStringArray(ImageList);


            Bundle bundle = new Bundle();
            bundle.putSerializable("image_data_bundle_key", stringArrayList);
            Intent intent = new Intent(getActivity(), ImageResultActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1002);


        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1002) {
            if (resultCode == getActivity().RESULT_OK) {
                //fetch the integer we passed into the dialog result which corresponds to the list position

                ArrayList<Bitmap> bitmapArrayResult = (ArrayList<Bitmap>)
                        data.getSerializableExtra(ImageResultActivity.RESULT_BITMAP_ARRAY);

                int nResult = data.getIntExtra(ImageResultActivity.RESULT_POSITION, -98);

                if (nResult != -98) {
                    try {
                        mStrImageUrl = "";
                        Bitmap resultBitmap = bitmapArrayResult.get(nResult);
                        mPhotoView.setImageBitmap(resultBitmap);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
            if (resultCode == getActivity().RESULT_CANCELED) {
                //do nothing
            }
        }
    }


}
