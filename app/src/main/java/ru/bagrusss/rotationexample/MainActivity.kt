package ru.bagrusss.rotationexample

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkontakte.core.platform.orientation.RotatableContainer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import ru.bagrusss.list.Adapter
import ru.bagrusss.list.items.ActionItem
import ru.bagrusss.list.items.SquareItem
import ru.bagrusss.multiwindow.MultiWindowDelegateImpl
import ru.bagrusss.pip.PictureInPictureActivityLauncher
import ru.bagrusss.rotation.LockedOrientationDelegateImpl
import ru.bagrusss.rotationexample.containers.ViewsContainer
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var pipLauncher: PictureInPictureActivityLauncher
    private lateinit var delegate: LockedOrientationDelegateImpl
    private lateinit var multiWindowDelegate: MultiWindowDelegateImpl

    private lateinit var container: ViewsContainer

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delegate = LockedOrientationDelegateImpl(applicationContext)

        val ref = WeakReference(this)
        multiWindowDelegate = MultiWindowDelegateImpl(ref::get)

        container = ViewsContainer(
            topView = findViewById(R.id.top_text_view),
            bottomView = findViewById(R.id.bottom_text_view),
            image = findViewById(R.id.image_with_bg),
            bottomId = R.id.recycler,
            orientationDelegate = delegate
        )

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = Adapter(delegate).apply {
            updateItems(createItems())
        }

        pipLauncher = PictureInPictureActivityLauncher(this)
    }

    override fun onStart() {
        super.onStart()

        delegate.enable(multiWindowDelegate)
    }

    override fun onStop() {
        super.onStop()

        delegate.disable()
    }

    override fun onDestroy() {
        super.onDestroy()

        container.destroy()
        pipLauncher.destroy()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        pipLauncher.onPictureInPictureModeChanged(isInPictureInPictureMode)
    }

    private fun createItems() = listOf(
        SquareItem("\uD83D\uDE3B"), // 😻
        ActionItem("\uD83D\uDC08", true), // 🐈
        SquareItem("\uD83D\uDE3C"), // 😼
        SquareItem("\uD83D\uDE3A"), // 😺
        SquareItem("\uD83D\uDC36"), // 🐶
        SquareItem("\uD83D\uDC15"), // 🐕
        SquareItem("\uD83D\uDC07"), // 🐇
        SquareItem("\uD83D\uDC3F️"), // 🐿️
        SquareItem("\uD83D\uDC3C"), // 🐼
        SquareItem("\uD83D\uDC0E"), // 🐎
        SquareItem("\uD83D\uDC1F"), // 🐟
        SquareItem("\uD83D\uDC33"), // 🐳
        SquareItem("\uD83E\uDD95"), // 🦕
        SquareItem("\uD83D\uDC18"), // 🐘
        SquareItem("\uD83E\uDDA1"), // 🦡
        SquareItem("\uD83E\uDDAD"), // 🦭
        SquareItem("\uD83E\uDD93"), // 🦓
        SquareItem("\uD83D\uDC02"), // 🐂
        SquareItem("\uD83E\uDD8A"), // 🦊
        SquareItem("\uD83D\uDC3A"), // 🐺
        SquareItem("\uD83E\uDD81"), // 🦁
        SquareItem("\uD83D\uDC38"), // 🐸
        SquareItem("\uD83D\uDC0A"), // 🐊
        SquareItem("\uD83D\uDC22"), // 🐢
        SquareItem("\uD83E\uDD8E"), // 🦎
        SquareItem("\uD83D\uDC2B"), // 🐫
    )

}