# Auto Club

## Introduction

This is my Bachelor's thesis project. It represents a modern aproach to the the new Android development paradigms and practices. The goal of this project is to provide a fully functional and relevant application that make use of the new Android architecture.

Check out this demo: [youtube link](https://youtu.be/157cWJkG4JA)

Check out the thesis paper here: [click me](https://github.com/Catalin-Mihai/AutoClub/blob/master/thesis_paper.pdf)

## Description

Auto Club is a social media application that aims to create a car enthusiasts community. It has the main features of a social media application and also a few more specific ones:  

- User profiles
- Register/Login system (local accounts or google authentication)
- Meets. A panel where you can find meetings created by users or create a new one.
- My Garage. A place linked to your profile where you can show off your awesome cars.
- Advanced search for users and cars profiles

## Under the hood

This app was built with modularity and scalability in mind, even if it reached its purpose and won't be mentained anymore. The application may look pretty simple on the outside, but is has a pretty rock-solid foundation for possible future functionalities.

## Project arhitecture

The application is built within the MVVM architecture, so the project has been sliced into more components:

- Model. Here the entities and repository patterns comes in play. This layer is responsible with the database querying. The repository pattern makes an abstract way of handling data that provides the freedom of using any database or caching strategies that shouldn't bother or depend on the rest of this architecture layers.

- View. This layer of the MVVM pattern is responsible with the "render" of the visual part, where fragments, activities and layouts files are involved. The main task for this layer is to "convert" and handle the data from the ViewModel layer described below.

- ViewModel. This layer is meant to make a smooth and stable connection between View and Model (hence the name). The main goals of this layer are:
  - No references to views (So, no memory leaks)

  - Can be shared and used by more than one View.

  - Easy to maintain and test as they don't have any dependencies.

## Project file structure

The file structure is also based on the MVVM arhitecture by the fact that it is a representation of the actual layers of the arhitecture. For example the `Model` layer is represented by the `model` and `repository` packages.

The synergy between Views and ViewModels made a great point to unify them into a `ui` package where the all application features are grouped one by one. Each feature package has its own Views and ViewModels. Only the layout files of the views are in their consecrated location, the `layout` directory.

There are also another two packages: `di` for dependency injection and `util` for some helper classes.

## Tech stack

The libraries used in this project are mostly Jetpack components and also some popular libraries that managed to make the development life a little easier.

- Jetpack:
  - AndroidX - improvement to the original Android Support Library
  - Data Binding - binds UI components from layouts to data sources, using a declarative format (rather than programmatically)
  - LiveData - observable data container that respects the lifecycle of Android components (fragments, activities, services).
  - Navigation - handles application's screens logic, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
  - ViewModel - designed to store and manage UI-related data in a lifecycle conscious way. The ViewModel class allows data to survive configuration changes such as screen rotations.
  - Android KTX - provide concise, idiomatic Kotlin to Jetpack and Android platform APIs a.k.a Kotlin shortcuts.

- Coroutines - managing background threads with simplified code and reducing needs for callbacks.
- Dagger Hilt - dependency injector
- Timber - a logger with a small, extensible API which provides utility on top of Android's normal Log class.
- SafeArgs - generates simple object and builder classes for type-safe navigation and access to any associated arguments.
- Material design - awesome visual components for the UI/UX part of an application.

## Resources

- your_news (cyber-gh) - a final project for an Android course I took, made by one of the most experimented mates I had. Thank you!!

- sunflower (by android) - a gardening app illustrating Android development best practices with Android Jetpack.

- android-showcase (by igorwojda) - app following best practices: Kotlin, coroutines, Clean Architecture, feature modules, tests, MVVM, static analysis.
