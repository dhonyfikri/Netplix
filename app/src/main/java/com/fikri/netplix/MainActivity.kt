package com.fikri.netplix

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.fikri.netplix.core.data.source.Resource
import com.fikri.netplix.core.domain.model.Genre
import com.fikri.netplix.core.domain.model.Movie
import com.fikri.netplix.core.ui.adapter.CarouselIndicatorAdapter
import com.fikri.netplix.core.ui.adapter.FeaturedMovieListAdapter
import com.fikri.netplix.core.ui.adapter.FixedLatestMovieListAdapter
import com.fikri.netplix.core.ui.adapter.GenreDiscoverAdapter
import com.fikri.netplix.core.ui.modal.DetailMovieModal
import com.fikri.netplix.core.ui.modal.LoadingModal
import com.fikri.netplix.core.ui.modal.RefreshModal
import com.fikri.netplix.databinding.ActivityMainBinding
import com.fikri.netplix.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    companion object {
        private const val CAROUSEL_DELAY = 4000L
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val detailMovieModal = DetailMovieModal(this)
    private val loadingModal = LoadingModal(this)
    private val refreshModal = RefreshModal(this)
    private lateinit var handler: Handler
    private var featuredMovieListAdapter: FeaturedMovieListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupAction()
    }

    private fun setupData() {
        handler = Handler(Looper.myLooper()!!)
        binding.header.tvTitle.text = getString(R.string.app_name)
        binding.header.ivSearchIcon.visibility = View.VISIBLE

        binding.latestMovieList.apply {
            tvListTitle.text = getString(R.string.latest)
            btnMoreMovie.visibility = View.GONE
            rvHorizontalMovieList.setHasFixedSize(true)
            rvHorizontalMovieList.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.apply {
            rvFeaturedMovieCarouselIndicator.setHasFixedSize(true)
            rvFeaturedMovieCarouselIndicator.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rvGenreDiscoverMovieListParent.setHasFixedSize(true)
            rvGenreDiscoverMovieListParent.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.apply {
            listPopularMovie.observe(this@MainActivity) { data ->
                when (data) {
                    is Resource.Success -> {
                        binding.apply {
                            smFeaturedMovie.stopShimmer()
                            smFeaturedMovie.visibility = View.GONE
                            llFeaturedMovieList.visibility = View.VISIBLE
                            setFeaturedMovieList(data.data as ArrayList<Movie>)
                            if (data.data.isNotEmpty()) {
                                tvFeaturedMovieListMessage.visibility = View.GONE
                            } else {
                                tvFeaturedMovieListMessage.visibility = View.VISIBLE
                                tvFeaturedMovieListMessage.text =
                                    getString(R.string.no_data_available)
                            }
                        }
                    }
                    else -> {
                        binding.apply {
                            smFeaturedMovie.startShimmer()
                            smFeaturedMovie.visibility = View.VISIBLE
                            llFeaturedMovieList.visibility = View.GONE
                            setFeaturedMovieList(arrayListOf())
                        }
                    }
                }
            }

            listLatestMovie.observe(this@MainActivity) { data ->
                when (data) {
                    is Resource.Success -> {
                        binding.apply {
                            smLatestMovie.stopShimmer()
                            smLatestMovie.visibility = View.GONE
                            latestMovieList.root.visibility = View.VISIBLE
                            setLatestMovieList(data.data as ArrayList<Movie>)
                            if (data.data.isNotEmpty()) {
                                latestMovieList.rvHorizontalMovieList.visibility = View.VISIBLE
                                latestMovieList.tvHorizontalMovieListMessage.visibility = View.GONE
                            } else {
                                latestMovieList.rvHorizontalMovieList.visibility = View.GONE
                                latestMovieList.tvHorizontalMovieListMessage.visibility =
                                    View.VISIBLE
                                latestMovieList.tvHorizontalMovieListMessage.text =
                                    getString(R.string.no_data_available)
                            }
                        }
                    }
                    else -> {
                        binding.apply {
                            smLatestMovie.startShimmer()
                            smLatestMovie.visibility = View.VISIBLE
                            latestMovieList.root.visibility = View.GONE
                            setLatestMovieList(arrayListOf())
                        }
                    }
                }
            }

            listGenre.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { data ->
                    when (data) {
                        is Resource.Success -> {
                            getGenresMembers(data.data as ArrayList<Genre>)
                            binding.apply {
                                if (data.data.isNotEmpty()) {
                                    tvGenreListMessage.visibility = View.GONE
                                } else {
                                    tvGenreListMessage.visibility = View.VISIBLE
                                    tvGenreListMessage.text =
                                        getString(R.string.no_genre_data_available)
                                }
                            }
                        }
                        else -> {
                            binding.apply {
                                smGenreDiscoverMovie.startShimmer()
                                smGenreDiscoverMovie.visibility = View.VISIBLE
                                rlGenreDiscoverMovieListParentWrapper.visibility = View.GONE
                                setGenreList(arrayListOf())
                            }
                        }
                    }
                }
            }

            listGenreWithMember.observe(this@MainActivity) { data ->
                binding.apply {
                    smGenreDiscoverMovie.stopShimmer()
                    smGenreDiscoverMovie.visibility = View.GONE
                    rlGenreDiscoverMovieListParentWrapper.visibility = View.VISIBLE
                }
                setGenreList(data)
            }

            isShowingLoadingModal.observe(this@MainActivity) {
                if (it) {
                    loadingModal.showLoadingModal(message = "Loading")
                } else {
                    loadingModal.dismiss()
                }
            }

            isShowingRefreshModal.observe(this@MainActivity) {
                if (it) {
                    handler.removeCallbacks(runnable)
                    refreshModal.showRefreshModal(
                        type = RefreshModal.TYPE_FAILED,
                        message = "Failed to contact the server",
                        onRefreshClicked = {
                            dismissRefreshModal()
                            getDetailMovieTryAgain()
                        },
                        onCloseClicked = { dismissRefreshModal() }
                    )
                } else {
                    refreshModal.dismiss()
                    handler.postDelayed(runnable, CAROUSEL_DELAY)
                }
            }

            isShowingDetailMovie.observe(this@MainActivity) {
                if (it) {
                    handler.removeCallbacks(runnable)
                    detailMovieModal.showDetailMovieModal(
                        movieDetail = movieDetail,
                        movieVideo = movieVideo,
                        onCloseButtonPressed = {
                            dismissDetailMovie()
                        },
                        onWebsiteButtonPressed = { uri ->
                            startActivity(
                                Intent(Intent.ACTION_VIEW, uri)
                            )
                        })
                } else {
                    detailMovieModal.dismiss()
                    handler.postDelayed(runnable, CAROUSEL_DELAY)
                }
            }
        }

        binding.svMainHome.scrollTo(0, viewModel.scrollviewPositionY)
    }

    private fun setupAction() {
        binding.header.btnRightHeader.setOnClickListener {
            val moveToSearch = Intent(this@MainActivity, SearchMovieActivity::class.java)
            startActivity(
                moveToSearch, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity, Pair(
                        binding.header.ivSearchIcon, "search_icon"
                    )
                ).toBundle()
            )
        }
        binding.srlHomeRefresh.apply {
            setColorSchemeColors(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.secondary_color
                )
            )
            setOnRefreshListener {
                isRefreshing = false
                viewModel.getInitialData()
            }
        }
    }

    private fun setFeaturedMovieList(movieList: ArrayList<Movie>) {
        handler.removeCallbacks(runnable)
        val limitedMovieList = arrayListOf<Movie>()
        if (movieList.isNotEmpty()) {
            for (i in 0..movieList.size) {
                limitedMovieList.add(movieList[i])
                if (i >= 7) {
                    break
                }
            }
        }

        setCarouselIndicator(limitedMovieList.size)

        featuredMovieListAdapter =
            FeaturedMovieListAdapter(this, limitedMovieList)

        binding.vp2FeaturedMovieList.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            adapter = featuredMovieListAdapter

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = (0.80f + r * 0.20f)
            }
            setPageTransformer(compositePageTransformer)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCarouselIndicator(limitedMovieList.size, position)
                    this@MainActivity.handler.removeCallbacks(runnable)
                    this@MainActivity.handler.postDelayed(runnable, CAROUSEL_DELAY)
                }
            })
        }

        featuredMovieListAdapter?.setOnItemClickCallback(object :
            FeaturedMovieListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Movie) {
                viewModel.getDetailMovie(data.id ?: 0)
            }
        })
    }

    private val runnable = Runnable {
        binding.vp2FeaturedMovieList.apply {
            if (currentItem >= (featuredMovieListAdapter?.itemCount ?: 0) - 1) {
                currentItem = 0
            } else {
                currentItem++
            }
        }
    }

    private fun setCarouselIndicator(size: Int, activePosition: Int = -1) {
        val stateList = arrayListOf<Boolean>()
        for (i in 0 until size) {
            stateList.add(i == activePosition)
        }
        val carouselIndicatorAdapter = CarouselIndicatorAdapter(this, stateList)
        binding.rvFeaturedMovieCarouselIndicator.adapter = carouselIndicatorAdapter
    }

    private fun setLatestMovieList(movieList: ArrayList<Movie>) {
        val fixedLatestMovieListAdapter = FixedLatestMovieListAdapter(movieList)
        binding.latestMovieList.rvHorizontalMovieList.adapter = fixedLatestMovieListAdapter

        fixedLatestMovieListAdapter.setOnItemClickCallback(object :
            FixedLatestMovieListAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Movie) {
                viewModel.getDetailMovie(data.id ?: 0)
            }
        })
    }

    private fun setGenreList(genreList: ArrayList<Genre>) {
        val genreDiscoverAdapter = GenreDiscoverAdapter(this@MainActivity, genreList)
        binding.rvGenreDiscoverMovieListParent.adapter = genreDiscoverAdapter

        genreDiscoverAdapter.setOnSeeMoreClickCallback(object :
            GenreDiscoverAdapter.OnSeeMoreClickCallback {
            override fun onClickedSeeMore(data: Genre) {
                val moveToGenreDiscover =
                    Intent(this@MainActivity, GenreDiscoverActivity::class.java)
                moveToGenreDiscover.putExtra(GenreDiscoverActivity.EXTRA_SELECTED_GENRE, data)
                startActivity(moveToGenreDiscover)
            }
        })

        genreDiscoverAdapter.setOnMovieMemberClickCallback(object :
            GenreDiscoverAdapter.OnMovieMemberClickCallback {
            override fun onClickedMovieMember(data: Movie) {
                viewModel.getDetailMovie(data.id ?: 0)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.smFeaturedMovie.startShimmer()
        binding.smLatestMovie.startShimmer()
        binding.smGenreDiscoverMovie.startShimmer()

        handler.postDelayed(runnable, CAROUSEL_DELAY)
    }

    override fun onPause() {
        super.onPause()
        binding.smFeaturedMovie.stopShimmer()
        binding.smLatestMovie.stopShimmer()
        binding.smGenreDiscoverMovie.stopShimmer()
        viewModel.scrollviewPositionY = binding.svMainHome.scrollY

        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        detailMovieModal.dismiss()
        loadingModal.dismiss()
        refreshModal.dismiss()
    }
}