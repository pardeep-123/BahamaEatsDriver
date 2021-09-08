package com.bahamaeatsdriver.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bahamaeats.network.RestObservable
import com.bahamaeats.network.Status
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.model_class.training_video_links.TrainingVideoLinksResponse
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.halilibo.bettervideoplayer.BetterVideoPlayer
import kotlinx.android.synthetic.main.activity_driver_training_video.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class DriverTrainingVideoActivity : YouTubeBaseActivity(), Observer<RestObservable> {
    private lateinit var player: BetterVideoPlayer

//    private val viewModel: BaseViewModel by lazy { ViewModelProvider(this).get(BaseViewModel::class.java) }

    private var key = "AIzaSyB3xv9GFjXugMjlR8TE1m3QfZfuP8NqkGM"
    private val TAG: String = DriverTrainingVideoActivity::class.java.getSimpleName()
    private var videoID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_training_video)
//        viewModel.getTrainingVideoLinksApi(this, true)
//        viewModel.getTrainingVideoLinksResponse().observe(this, this)
        iv_backBtn.setOnClickListener { finish() }
        if (intent.getStringExtra("videoUrl") != null && intent.getStringExtra("videoUrl")!!.isNotEmpty()) {
            videoID = getYouTubeId(intent.getStringExtra("videoUrl")!!)!!
            /*  video_thumbnail_image_view.initialize(key, object : YouTubeThumbnailView.OnInitializedListener { override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView,youTubeThumbnailLoader: YouTubeThumbnailLoader) {
                          //when initialization is sucess, set the video id to thumbnail to load
                          youTubeThumbnailLoader.setVideo(videoID)
                          youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                              YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                              override fun onThumbnailLoaded(
                                  youTubeThumbnailView: YouTubeThumbnailView,
                                  s: String
                              ) {
                                  //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                                  youTubeThumbnailLoader.release()
                              }

                              override fun onThumbnailError(
                                  youTubeThumbnailView: YouTubeThumbnailView,
                                  errorReason: YouTubeThumbnailLoader.ErrorReason
                              ) {
                                  //print or show error when thumbnail load failed
                                  //Log.e(TAG, "Youtube Thumbnail Error")
                              }
                          })
                      }

                      override fun onInitializationFailure(
                          youTubeThumbnailView: YouTubeThumbnailView,
                          youTubeInitializationResult: YouTubeInitializationResult
                      ) {
                          //print or show error when initialization failed
                          Log.e("Eeeeee", "Youtube Initialization Failure")
                      }
                  })*/
            tv_noDataFound.visibility = View.GONE
            initializeYoutubePlayer()
        }
        else
            tv_noDataFound.visibility=View.VISIBLE
    }


    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find())
            matcher.group()
        else
            "error"

    }

    /*  override fun onPause() {
          super.onPause()
          // Make sure the player stops playing if the user presses the home button.
          if (player.isPlaying) {
              player.pause()
          }
      }*/

    override fun onChanged(liveData: RestObservable?) {
        when (liveData!!.status) {
            Status.SUCCESS -> {

                if (liveData.data is TrainingVideoLinksResponse) {
//                    player.setSource(Uri.parse(liveData.data.body[0].link))
                }
            }

            Status.ERROR -> {

            }
        }
    }

    private fun initializeYoutubePlayer() {
        youtube_player_view!!.initialize(key, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer,
                wasRestored: Boolean
            ) {

                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)

                    //load the video
                    youTubePlayer.loadVideo(videoID)

                    //OR

                    //cue the video
                    //youTubePlayer.cueVideo(videoID);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //  youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    //  youTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                    /*player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/
                }
            }

            override fun onInitializationFailure(
                arg0: YouTubePlayer.Provider,
                arg1: YouTubeInitializationResult
            ) {
                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed")
            }
        })
    }
}