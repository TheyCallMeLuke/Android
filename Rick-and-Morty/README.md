![](./images/hero.png)

# Android app - Rick and Morty Characters

## Description

A pet project to learn android development.

## API

The [API](https://rickandmortyapi.com/documentation) is accessed via REST.

## Features

* Display a list of characters
* Filter the list of a characters
* List character details
* Save characters
* Display a list of favorite characters
* Support for dark mode
* Support for tablets

## Tech Stack

* Support for Android 21+
* Fully Kotlin
* [Architecture components](https://developer.android.com/topic/libraries/architecture) (LiveData, ViewModels, Navigation, Room, Paging)
* [Koin](https://insert-koin.io/) for dependency injection
* [Retrofit](https://github.com/square/retrofit) to aid with API communicaton
* [Moshi](https://github.com/square/moshi) for parsing JSON
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) to perform async tasks
* Asynchronous Flow to perform Paging
* [Coil](https://github.com/coil-kt/coil) for async image loading

## Patterns
* Clean Architecture
* MVVM
* Repository pattern
