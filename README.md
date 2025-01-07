<h1 align="center">Android Movie TMDB</h1>

<p align="center">  
 Discover and explore movies effortlessly with Android Movie TMDB, a modern and intuitive app built using cutting-edge Android technologies and best practices.
</p>

## Screenshot

![screenshot](https://github.com/user-attachments/assets/861e64d9-6e7a-42b5-aec9-34e6519eb78f)

# Introduction
- Android Movie TMDB is an innovative movie discovery app powered by The Movie Database (TMDB) API. Designed with a sleek and responsive user interface using Jetpack Compose, the app ensures seamless performance and an engaging user experience. The project follows the MVVM architecture with modularized code, making it scalable and maintainable for future enhancements.

- This app is perfect for movie enthusiasts who want a fast, secure, and feature-rich platform to browse and explore the latest movies, ratings, and reviews.

## Technical in use
- App Architecture: MVVM Architecture (View - ViewModel - Model), Modularization
- UI Toolkit: Jetpack Compose
- CI - CD : Github actions
- Dependency Injection: Hilt
- Asynchronous: Kotlin Coroutines + Flow
- Network: Retrofit + OkHttp3
- Storages: ProtoDatastore + Preferences DataStore
- JSON parser: Moshi
- Paging: Paging 3
- Image loading: Coil
- Security: ProGuard
- Code check or style: Lint, Spotless, Ktlint 
- Logger: Timber, Chucker, OkHttp logging interceptor.
- App performance : Benchmark, JankStats, BaselineProfile, LeakCanary

## Modularization

**Movie TMDB** adopted modularization strategies below:

- **Reusability**: Modulizing reusable codes properly enable opportunities for code sharing and limits code accessibility in other modules at the same time.
- **Parallel Building**: Each module can be run in parallel and it reduces the build time.
- **Strict visibility control**: Modules restrict to expose dedicated components and access to other layers, so it prevents they're being used outside the module
- **Decentralized focusing**: Each developer team can assign their dedicated module and they can focus on their own modules.

For more information, check out the [Guide to Android app modularization](https://developer.android.com/topic/modularization).

# License
```xml
 Designed and developed by 2025 truongdc21 (Dang Chi Truong)
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```