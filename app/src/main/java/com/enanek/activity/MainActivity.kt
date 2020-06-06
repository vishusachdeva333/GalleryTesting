package com.enanek.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enanek.R
import com.enanek.adapter.PhotosAdapter
import com.enanek.model.response.Photo
import com.enanek.model.response.PhotosResponse
import com.enanek.retrofit.RetrofitClient
import com.enanek.utils.Constants
import com.enanek.utils.RecyclerPaginationListener
import com.techymarvel.retrofit.RetrofitInterface
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var progress_circular: ProgressBar
    private lateinit var search_view: SearchView
    private lateinit var photos_list: RecyclerView
    private var photosResponseApi: Single<Response<PhotosResponse>>? = null
    private var photoList: MutableList<Photo>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var photoAdapter: PhotosAdapter? = null
    private lateinit var recyclerPaginationListener : RecyclerPaginationListener

    private val nojsoncallback = 1

    private var per_page = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress_circular = findViewById(R.id.progress_circular)
        search_view = findViewById(R.id.search_view)
        photos_list = findViewById(R.id.photos_list)

        search_view.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { // do something on text submit
                hitPhotosResponseApi(1)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { // do something when text changes
                return false
            }
        })

        progress_circular.visibility = View.GONE
        photos_list.visibility = View.GONE

    }

    private fun hitPhotosResponseApi(page: Int) {
        if (page == 1)
        {
            photos_list.visibility = View.GONE
            progress_circular.visibility = View.VISIBLE
        }
        photosResponseApi =
            RetrofitClient.getClientRx(Constants.BASE_URL).create(RetrofitInterface::class.java)
                .getPhotosResponse(
                    Constants.method,
                    Constants.api_key,
                    Constants.gallery_id,
                    Constants.format,
                    nojsoncallback,
                    page,
                    per_page
                )
        photosResponseApi?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object :
                SingleObserver<Response<PhotosResponse>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    if (page == 1)
                        progress_circular.visibility = View.GONE
                }

                override fun onSuccess(response: Response<PhotosResponse>) {

                    when (response.code()) {
                        200 -> {
                            if (page == 1) {
                                progress_circular.visibility = View.GONE
                                setPhotoAdapter(response.body())
                            } else {
                                refreshAdapter(response.body())
                            }
                        }
                        else -> {
                        }
                    }
                }

            })
    }

    private fun refreshAdapter(body: PhotosResponse?) {
        val tempPhotoList = body?.photos?.photo
        if (!tempPhotoList.isNullOrEmpty()) {
            photoList?.addAll(tempPhotoList)
            photoAdapter?.notifyDataSetChanged()
            if (tempPhotoList.size < per_page)
                photos_list.removeOnScrollListener(recyclerPaginationListener)
        } else
            photos_list.removeOnScrollListener(recyclerPaginationListener)

    }

    private fun setPhotoAdapter(body: PhotosResponse?) {
        photos_list.visibility = View.VISIBLE
        photoList = body?.photos?.photo
        gridLayoutManager = GridLayoutManager(this, 1)
        photoAdapter = PhotosAdapter(this, photoList)
        photos_list.layoutManager = gridLayoutManager
        photos_list.adapter = photoAdapter
        recyclerPaginationListener = object : RecyclerPaginationListener(gridLayoutManager, 0) {
            override fun onLoadMore(current_page: Int) {
                hitPhotosResponseApi(current_page)
            }

            override fun onFirstItemPosition(firstVisibleItem: Int) {

            }

            override fun onLastItemPosition(lastVisibleItem: Int) {}
        }
        photos_list.addOnScrollListener(recyclerPaginationListener)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.span_1 -> {
                gridLayoutManager?.spanCount = 1
                return true
            }
            R.id.span_2 -> {
                gridLayoutManager?.spanCount = 2
                return true
            }
            R.id.span_3 -> {
                gridLayoutManager?.spanCount = 3
                return true
            }
            R.id.span_4 -> {
                gridLayoutManager?.spanCount = 4
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
