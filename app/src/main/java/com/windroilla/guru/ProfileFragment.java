package com.windroilla.guru;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.windroilla.guru.authenticator.ApiService;
import com.windroilla.guru.authenticator.UserProfile;

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

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = ProfileFragment.class.getSimpleName();
    @Inject
    ApiService apiService;
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

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        final ImageView profilePicture = (ImageView) rootView.findViewById(R.id.profile_picture);
        apiService.getUserProfileObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserProfile>() {
                               @Override
                               public void call(UserProfile userProfile) {
                                   if (userProfile == null) {
                                       Log.e(TAG, "No user profile returned from the server");
                                       return;
                                   }
                                   if (userProfile.image != null) {
                                       byte[] file = Base64.decode(userProfile.image, Base64.DEFAULT);
                                       profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                       profilePicture.setAdjustViewBounds(false);
                                       profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(file, 0, file.length));

                                   } else {
                                       profilePicture.setImageResource(R.mipmap.ic_launcher);
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Could not load user data " + throwable);
                                Toast.makeText(getActivity(), "Could not load user data. Please try again!" + throwable, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //    mListener.onProfileFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        //public void onProfileFragmentInteraction(Uri uri);
    }

}
