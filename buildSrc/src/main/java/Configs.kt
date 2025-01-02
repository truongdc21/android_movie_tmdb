object Configs {

    const val APP_ID = "com.truongdc.movie"
    const val NAMESPACE = "com.truongdc.movie"

    // Core namespaces
    const val NAMESPACE_CORE = "$NAMESPACE.core"
    const val NAMESPACE_CORE_DATA = "$NAMESPACE_CORE.data"
    const val NAMESPACE_CORE_MODEL = "$NAMESPACE_CORE.model"
    const val NAMESPACE_CORE_NETWORK = "$NAMESPACE_CORE.network"
    const val NAMESPACE_CORE_UI = "$NAMESPACE_CORE.ui"
    const val NAMESPACE_CORE_DATASTORE = "$NAMESPACE_CORE.datastore"
    const val NAMESPACE_CORE_DATASTORE_PROTO = "$NAMESPACE_CORE_DATASTORE.proto"
    const val NAMESPACE_CORE_COMMON = "$NAMESPACE_CORE.common"
    const val NAMESPACE_CORE_NAVIGATION = "$NAMESPACE_CORE.navigation"
    const val NAMESPACE_CORE_DESIGNSYSTEM = "$NAMESPACE_CORE.designsystem"
    const val NAMESPACE_CORE_VIEWMODEL = "$NAMESPACE_CORE.viewmodel"
    const val NAMESPACE_CORE_STATE = "$NAMESPACE_CORE.state"
    const val NAMESPACE_CORE_TESTING = "$NAMESPACE_CORE.testing"

    // Feature namespaces
    const val NAMESPACE_FEATURE = "$NAMESPACE.feature"
    const val NAMESPACE_FEATURE_LOGIN = "$NAMESPACE_FEATURE.login"
    const val NAMESPACE_FEATURE_REGISTER = "$NAMESPACE_FEATURE.register"
    const val NAMESPACE_FEATURE_MOVIE_LIST = "$NAMESPACE_FEATURE.movieList"
    const val NAMESPACE_FEATURE_MOVIE_DETAIL = "$NAMESPACE_FEATURE.movieDetail"
    const val NAMESPACE_FEATURE_SETTINGS = "$NAMESPACE_FEATURE.settings"


    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"

    const val COMPLIED_SDK = 34
    const val MIN_SDK = 24
    const val TARGET_SDK = 34

    const val ANDROID_JUNIT_RUNNER = "com.truongdc.movie.core.testing.MovieTestRunner"

    const val PROGUARD_FILE = "proguard-android-optimize.txt"
    const val PROGUARD_RULES = "proguard-rules.pro"
}
