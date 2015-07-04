package com.windroilla.guru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.windroilla.guru.api.ApiService;
import com.windroilla.guru.api.UserProfile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnProfileFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public final static int REQUEST_DISPLAY_PICTURE = 0x1;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = ProfileFragment.class.getSimpleName();
    @Inject
    ApiService apiService;
    private ImageView profilePicture;
    private OnProfileFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuruApp.getsInstance().graph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.SettingsTheme);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        final View rootView = localInflater.inflate(R.layout.fragment_profile, container, false);
        profilePicture = (ImageView) rootView.findViewById(R.id.profile_picture);
        final ListView profileInfo = (ListView) rootView.findViewById(R.id.profile_list_info);
        apiService.getUserProfileObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserProfile>() {
                               @Override
                               public void call(final UserProfile userProfile) {
                                   if (userProfile == null) {
                                       Log.e(TAG, "No user profile returned from the server");
                                       return;
                                   }
                                   if (!TextUtils.isEmpty(userProfile.image)) {
                                       byte[] file = Base64.decode(userProfile.image, Base64.DEFAULT);
                                       profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                       profilePicture.setAdjustViewBounds(false);
                                       profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(file, 0, file.length));
                                   } else {
                                       profilePicture.setImageResource(R.mipmap.ic_launcher);
                                   }
                                   profileInfo.setAdapter(new ProfileAdapter(
                                           getActivity(),
                                           new ArrayList<String>() {
                                               {
                                                   add(getString(R.string.profile_first_name));
                                                   add(getString(R.string.profile_last_name));
                                                   add(getString(R.string.profile_mobile_number));
                                                   add(getString(R.string.profile_email));
                                                   add(getString(R.string.profile_ambition));
                                                   add(getString(R.string.profile_father_name));
                                                   add(getString(R.string.profile_address));
                                                   add(getString(R.string.profile_user_from));
                                               }
                                           },
                                           new ArrayList<String>() {
                                               {
                                                   add(userProfile.first_name);
                                                   add(TextUtils.isEmpty(userProfile.last_name) ? " " : userProfile.last_name);
                                                   add(userProfile.mobile_number);
                                                   add(userProfile.email);
                                                   add(TextUtils.isEmpty(userProfile.ambition) ? " " : userProfile.ambition);
                                                   add(TextUtils.isEmpty(userProfile.father_name) ? " " : userProfile.father_name);
                                                   add(userProfile.address);
                                                   add(userProfile.created_at);
                                               }
                                           }
                                   ));
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Could not load user data " + throwable);
                            }
                        }
                );
        profileInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), profileInfo.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.OnProfilePictureClick();
                Intent intent = new Intent(getActivity(), ProfilePictureDisplay.class);
                startActivityForResult(intent, REQUEST_DISPLAY_PICTURE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DISPLAY_PICTURE:
                if (data != null && resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), data.getStringExtra(ProfilePictureDisplay.ARG_IMAGE_PATH), Toast.LENGTH_SHORT).show();
                    String imagePath = data.getStringExtra(ProfilePictureDisplay.ARG_IMAGE_PATH);
                    if (!TextUtils.isEmpty(imagePath)) {
                        final Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                        profilePicture.setImageBitmap(myBitmap);
                        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        HashMap<String, String> updateData = new HashMap<>();
                        updateData.put("image", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                        apiService.updateUserProfile(updateData)
                                .retry(5)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<UserProfile>() {
                                               @Override
                                               public void call(UserProfile userProfile) {
                                                   Log.d(TAG, "Uploaded Profile Picture");
                                               }
                                           },
                                        new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                Log.e(TAG, "Could not upload the profile picture due to error " + throwable);
                                            }
                                        });
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProfileFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (getArguments() != null) {
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProfileFragmentInteractionListener {
        void OnProfilePictureClick();
    }

}
